package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.LValExp;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.SymbolManager;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

// Stmt → LVal '=' Exp ';'
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
        String identName = identToken.getTokenName();
        if (SymbolManager.getInstance().isConst(identName)) {
            Error error = new Error(identToken.getLino(), 'h');
            Printer.addError(error);
        }
    }
}
