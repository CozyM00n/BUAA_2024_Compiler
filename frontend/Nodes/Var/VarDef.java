package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import frontend.Symbol.VarSymbol;
import llvm_IR.IRManager;
import llvm_IR.InitInfo;
import llvm_IR.Instr.AllocaInstr;
import llvm_IR.Instr.GEPInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.StoreInstr;
import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.GlobalVar;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

import static frontend.Symbol.SymbolManager.curSymbolId;

// VarDef → Ident [ '[' ConstExp ']' ] |
//          Ident [ '[' ConstExp ']' ] '=' InitVal // b
public class VarDef extends Node {
    private VarSymbol varSymbol;

    public VarDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }


    public boolean judgeIsArray() {
        return children.size() >= 2 && ((TokenNode)children.get(1)).getTokenType() == TokenType.LBRACK;
    }
    public VarSymbol createSymbol() { // 语义分析阶段调用 只有最基本的：name + typeInfo
        String symbolName = ((TokenNode) children.get(0)).getTokenValue();
        TypeInfo typeInfo = new TypeInfo(judgeIsArray(), SymbolManager.getInstance().getDeclaredType());
        return new VarSymbol(typeInfo, symbolName);
    }
    @Override
    public void checkError() { // b 变量名字重定义
        super.checkError();
        this.varSymbol = createSymbol();
        if (!SymbolManager.getInstance().addSymbol(varSymbol)) {
            Printer.addError(new Error(((TokenNode) children.get(0)).getLino(), 'b'));
        }
    }


    /****** for llvm ********/
    //中间代码生成调用,设置symbol的llvmType属性
    public void setSymLlvmType() {
        TypeInfo typeInfo = varSymbol.getTypeInfo();
        LLVMType eleType; // 判断元素类型
        if (typeInfo.getType() == TypeInfo.typeInfo.INT_TYPE) eleType = IntType.INT32;
        else eleType = IntType.INT8; // char
        int len;
        if (typeInfo.getIsArray()) { // 数组
            len = children.get(2).calculate();
            varSymbol.setLlvmType(new ArrayType(len, eleType));
        } else {
            varSymbol.setLlvmType(eleType);
        }
    }

    // 中间代码生成 只有全局变量调用
    public void setSymbolInitInfo() {
        ArrayList<Integer> initValues = null; String str = null;
        int len;
        LLVMType type = varSymbol.getLlvmType();
        if (type instanceof ArrayType) len = ((ArrayType) type).getLength(); // 数组[constexp] evaluate得到
        else len = 0;
        // 仅当var有初始值时，initValues 不为 null
        if (children.get(children.size() - 1) instanceof InitVal) {
            InitVal varInitVal = (InitVal) children.get(children.size() - 1);
            initValues = varInitVal.getIntList(len);
            if (varInitVal.isStringConst()) str = varInitVal.getVarInitString(len);
        }
        // 否则initValues 为 null
        varSymbol.setInitInfo(new InitInfo(type, initValues, str, len));
    }

    @Override
    public Value generateIR() {
        // genIR阶段 每当遍历到变量定义的时候 实时更新curSymbolId
        curSymbolId = varSymbol.getSymbolId();
        this.setSymLlvmType();
        LLVMType llvmType = varSymbol.getLlvmType();

        if (varSymbol.isGlobal()) { // 全局变量
            this.setSymbolInitInfo();
            InitInfo initInfo = varSymbol.getInitInfo();
            String name = "@" + varSymbol.getSymbolName();
            GlobalVar globalVar = new GlobalVar(name, llvmType, initInfo);
            varSymbol.setLlvmValue(globalVar);
            return null;
        }

        // 非数组局部变量
        if (!varSymbol.getTypeInfo().getIsArray()) {
            Instr alloc = new AllocaInstr(IRManager.getInstance().genVRName(), llvmType);
            varSymbol.setLlvmValue(alloc);
            // 如果有初始值，先求出其Value，再用store指令存入
            if (children.get(children.size() - 1) instanceof InitVal) {
                Value from = ((InitVal) children.get(children.size() - 1))
                        .generateIRList(0, llvmType).get(0);
                StoreInstr.checkAndGenStoreInstr(from, alloc);
            }
        }

        else { // 数组类型
            // 新建指向数组的指针
            Instr alloc = new AllocaInstr(IRManager.getInstance().genVRName(), llvmType);
            varSymbol.setLlvmValue(alloc);
            // 如果有初始值，得到ValueList，再用store指令存入
            if (children.get(children.size() - 1) instanceof InitVal) {
                int len = ((ArrayType) llvmType).getLength();
                LLVMType eleType = ((ArrayType) llvmType).getEleType();
                ArrayList<Value> valueList = ((InitVal) children.get(children.size() - 1)).generateIRList(len, llvmType);
                for (int i = 0; i < valueList.size(); i++) {
                    // 这里向GEP传入的是指向整个数组的指针
                    Instr arrEleAddr = new GEPInstr(IRManager.getInstance().genVRName(), alloc,
                            new Constant(i, IntType.INT32), eleType);
                    StoreInstr.checkAndGenStoreInstr(valueList.get(i), arrEleAddr);
                }
            }
        }
        return null;
    }
}
