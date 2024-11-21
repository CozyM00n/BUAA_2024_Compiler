package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.Exp;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import llvm_IR.IRManager;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.InOut.PutChInstr;
import llvm_IR.Instr.InOut.PutIntInstr;
import llvm_IR.Instr.InOut.PutStrInstr;
import llvm_IR.Instr.ZextInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.StringLiteral;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class PrintfStmt extends Stmt {
    //  'printf''('StringConst {','Exp}')'';' // l
    // 格式字符与表达式个数不匹配 行号为‘printf’所在行号

    private final String stringConst;
    private final ArrayList<Exp> expList;

    public PrintfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        this.stringConst = ((TokenNode)children.get(2)).getToken().getValue();
        this.expList = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof Exp) {
                expList.add((Exp) child);
            }
        }
    }

    @Override
    public void checkError() {
        // % 数量和 , 数量
        int cnt = 0;
        for (int i = 0; i < stringConst.length(); i++) {
            if (stringConst.charAt(i) == '%' && i+1 < stringConst.length() &&
                    (stringConst.charAt(i+1) == 'd' || stringConst.charAt(i+1) == 'c') ) {
                cnt++;
            }
        }
        if (cnt != expList.size()) {
            Error error = new Error(((TokenNode)children.get(0)).getLino(), 'l');
            Printer.addError(error);
        }
        super.checkError();
    }

    @Override
    public Value generateIR() {
//        ArrayList<Value> expValues = new ArrayList<>();
//        for (Exp exp : expList) {
//            expValues.add(exp.generateIR());
//        }
        String string = stringConst.substring(1, stringConst.length() - 1);
        StringBuilder sb = new StringBuilder();
        int cnt = 0, newLineNum = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '%' && i != string.length() - 1
                    && (string.charAt(i+1) == 'd' || string.charAt(i+1) == 'c')) {
                if (sb.length() != 0) {
                    StringLiteral stringLiteral = new StringLiteral(
                            IRManager.getInstance().genStrName(), sb.toString(), sb.length() - 2*newLineNum);
                    new PutStrInstr(stringLiteral);
                    sb.setLength(0); newLineNum = 0;
                }
                if (string.charAt(i+1) == 'd') {
                    PutIntInstr.checkAndGenPutInt(expList.get(cnt++).generateIR());
                } else {
                    Value value = expList.get(cnt++).generateIR();
                    PutChInstr.checkAndGenPutCh(value);
                }
                i++;
            }
            else if (string.charAt(i) == '\\') {
                sb.append("\\0A"); i++;
                newLineNum++;
            }
            else {
                sb.append(string.charAt(i));
            }
        }
        if (sb.length() != 0) {
            StringLiteral stringLiteral = new StringLiteral(
                    IRManager.getInstance().genStrName(), sb.toString(), sb.length() - 2*newLineNum);
            Instr putstrInstr = new PutStrInstr(stringLiteral);
        }
        return null;
    }
}
