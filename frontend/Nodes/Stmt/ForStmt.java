package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.LValExp;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.SymbolManager;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

// ForStmt => LVal '=' Exp // h
public class ForStmt extends Node {
    public ForStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void checkError() {
        super.checkError();
        TokenNode identToken = ((LValExp)children.get(0)).getIdentToken(); // 获取identName
        String identName = identToken.getTokenName();
        if (SymbolManager.getInstance().isConst(identName)) {
            Error error = new Error(identToken.getLino(), 'h');
            Printer.addError(error);
        }
    }
}
