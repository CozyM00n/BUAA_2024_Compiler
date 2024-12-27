package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.LValExp;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.SymbolManager;
import llvm_IR.Instr.StoreInstr;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

// AssignStmt → LVal '=' Exp ';'
// LVal → Ident ['[' Exp ']']
public class AssignStmt extends Stmt{

    public AssignStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        // h  LVal 为常量时，不能对其修改。报错行号为 LVal 所在行号。
        super.checkError();
        TokenNode identToken = ((LValExp)children.get(0)).getIdentToken();
        String identName = identToken.getTokenValue();
        if (SymbolManager.getInstance().isConst(identName)) {
            Error error = new Error(identToken.getLino(), 'h');
            Printer.addError(error);
        }
    }

    @Override
    public Value generateIR() {
        Value expVal = children.get(2).generateIR();
        Value lVal = ((LValExp) children.get(0)).genIRForAssign();
        return StoreInstr.checkAndGenStoreInstr(expVal, lVal);
    }
}
