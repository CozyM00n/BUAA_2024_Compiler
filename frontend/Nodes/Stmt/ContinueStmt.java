package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class ContinueStmt extends Stmt{

    public ContinueStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        // m 报错行号为 ‘break’ 与 ’continue’ 所在行号
        if (SymbolManager.getInstance().getLoopDepth() <= 0) {
            Error error = new Error(children.get(0).getEndLine(), 'm');
            Printer.addError(error);
        }
        super.checkError();
    }
}
