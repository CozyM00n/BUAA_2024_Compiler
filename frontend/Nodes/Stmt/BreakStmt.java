package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;
import llvm_IR.IRManager;
import llvm_IR.Instr.JumpInstr;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class BreakStmt extends Stmt{
    // 'break' ';'
    public BreakStmt(SyntaxVarType type, ArrayList<Node> children) {
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

    @Override
    public Value generateIR() {
        new JumpInstr(IRManager.getInstance().getCurLoop().getFollowBlock());
        return null;
    }
}
