package frontend.Nodes.Stmt;

import Enums.ReturnType;
import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.FuncSymbol;
import frontend.Symbol.SymbolManager;
import llvm_IR.Instr.ReturnInstr;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;
// Stmt → 'return' [Exp] ';'

public class ReturnStmt extends Stmt {

    public ReturnStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        // f  无返回值的函数存在不匹配的return语句 报错行号为‘return’所在行号
        FuncSymbol funcSymbol = SymbolManager.getInstance().getCurFuncSymbol();
        if (funcSymbol.getReturnType() == ReturnType.RETURN_VOID) {
            if (children.size() >= 3) {
                Printer.addError(new Error(((TokenNode) children.get(0)).getLino(), 'f'));
            }
        }
        super.checkError();
    }

    @Override
    public Value generateIR() {
        FuncSymbol funcSymbol = SymbolManager.getInstance().getCurFuncSymbol();
        Value retValue;
        if (children.size() >= 3)
            retValue = children.get(1).generateIR();
        else
            retValue = null;
        ReturnInstr.checkAndGenRet(retValue, funcSymbol.getLlvmValue().getRetType());
        return null;
    }
}
