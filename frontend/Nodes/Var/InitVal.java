package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Exp.Exp;
import frontend.Nodes.Node;
import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// 变量初值
//  InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
public class InitVal extends Node {
    public InitVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public boolean isStringConst() {
        return children.get(0) instanceof TokenNode &&
                ((TokenNode)children.get(0)).getTokenType() == TokenType.STRCON;
    }

    // 仅限全局变量
    public ArrayList<Integer> getIntList(int len) {
        ArrayList<Integer> res = new ArrayList<>();
        if (isStringConst()) {
            String str = ((TokenNode) children.get(0)).getToken().getValue();
            str = str.substring(1, str.length() - 1);
            int i = 0;
            while (i < str.length()) {
                res.add((int) str.charAt(i++));
            }
            while (i++ < len) {
                res.add((int) '\0');
            }
            return res;
        }
        else if (children.get(0) instanceof Exp) { // len == 0
            res.add(children.get(0).calculate());
            return res;
        }
        else { // 数组
            int cnt = 0;
            for (int i = 1; i < children.size(); i += 2) {
                res.add(children.get(i).calculate());
                cnt++;
            }
            while (cnt < len) { // 剩下的也都初始化为0
                res.add(0);
                cnt++;
            }
        }
        return res;
    }

    // 仅限全局变量，且为字符串初始化的char数组
    public String getVarInitString(int len) { // 不足补\0
        String str = ((TokenNode) children.get(0)).getToken().getValue();
        str = str.substring(1, str.length() - 1);
        StringBuilder sb = new StringBuilder(str);
        for (int i = str.length(); i < len; i++) {
            sb.append('\0');
        }
        return sb.toString();
    }

    public ArrayList<Value> generateIRList(int len, LLVMType llvmType) {
        // 统一返回i32, store发生类型转换
        // 只有局部变量会调用该函数。若llvmType为int，剩下的就不需要初始化
        ArrayList<Value> res = new ArrayList<>();
        if (isStringConst()) {
            String str = ((TokenNode) children.get(0)).getToken().getValue();
            str = str.substring(1, str.length() - 1);
            int i = 0;
            while (i < str.length()) {
                res.add(new Constant(str.charAt(i++), IntType.INT8));
            }
            while (i++ < len) {
                res.add(new Constant('\0', IntType.INT8));
            }
        }
        else if (children.get(0) instanceof Exp) { // len == 0
            res.add(children.get(0).generateIR());
        }
        else { // 数组
            int cnt = 0;
            LLVMType eleType = ((ArrayType)llvmType).getEleType();
            for (int i = 1; i < children.size(); i += 2) {
                res.add(children.get(i).generateIR());
                cnt++;
            }
            if (eleType != IntType.INT32) {
                while (cnt < len) { // 剩下的也都初始化为0
                    res.add(new Constant(0, eleType));
                    cnt++;
                }
            }
        }
        return res;
    }
}
