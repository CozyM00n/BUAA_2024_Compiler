package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.LValExp;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.SymbolManager;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class GetIntStmt extends Stmt{
    //  LVal '=' 'getint''('')'';' // h 不能改变常量的值 行号为 LVal 所在行号
    public GetIntStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        TokenNode identToken = ((LValExp)children.get(0)).getIdentToken(); // 获取identName
        String identName = identToken.getTokenName();
        if (SymbolManager.getInstance().isConst(identName)) {
            Error error = new Error(identToken.getLino(), 'h');
            Printer.addError(error);
        }
        super.checkError();
    }
}
