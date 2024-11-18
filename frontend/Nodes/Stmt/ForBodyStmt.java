package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.CondExp;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;
import llvm_IR.ForLoop;
import llvm_IR.IRManager;
import llvm_IR.Instr.JumpInstr;
import llvm_IR.llvm_Values.BasicBlock;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// ForStmt =>
// 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt // h

public class ForBodyStmt extends Stmt {

    private ForStmt forStmt1 = null;
    private ForStmt forStmt2 = null;
    private CondExp cond = null;
    private Stmt forBodyStmt;

    public ForBodyStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof ForStmt && i == 2) {
                forStmt1 = (ForStmt) children.get(i);
            } else if (children.get(i) instanceof CondExp) {
                cond = (CondExp) children.get(i);
            } else if (children.get(i) instanceof ForStmt) {
                forStmt2 = (ForStmt) children.get(i);
            } else if (children.get(i) instanceof Stmt) {
                forBodyStmt = (Stmt) children.get(i);
            }
        }
        SymbolManager.getInstance().enterLoop();
        super.checkError();
        SymbolManager.getInstance().leaveLoop();
    }

    @Override
    public Value generateIR() {
        SymbolManager.getInstance().enterLoop();
        BasicBlock condBlock = new BasicBlock("");
        BasicBlock forBodyBlock = new BasicBlock("");
        BasicBlock followBlock = new BasicBlock("");
        BasicBlock forStmt2Block = new BasicBlock("");
        IRManager.getInstance().pushLoop(new ForLoop(condBlock, followBlock));
        // 生成ForStmt1的IR
        if (forStmt1 != null) forStmt1.generateIR();
        // 跳转到condBlock
        new JumpInstr(condBlock);
        // 生成cond的IR指令
        IRManager.getInstance().addAndSetCurBlock(condBlock);
        IRManager.getInstance().resetBlockName(condBlock);
        if (cond != null) {
            cond.genConditionIR(forBodyBlock, followBlock); // cond 成立跳转到body，不成立跳转到follow
        } else { // 没有cond 直接跳转到forBody
            new JumpInstr(forBodyBlock);
        }
        // 生成forBody的IR指令
        IRManager.getInstance().addAndSetCurBlock(forBodyBlock);
        IRManager.getInstance().resetBlockName(forBodyBlock);
        forBodyStmt.generateIR();
        // 跳转到forStmt2
        new JumpInstr(forStmt2Block);
        IRManager.getInstance().addAndSetCurBlock(forStmt2Block);
        IRManager.getInstance().resetBlockName(forStmt2Block);
        // 生成forStmt2的IR指令
        if (forStmt2 != null) forStmt2.generateIR();
        new JumpInstr(condBlock);
        // 设置当前块为follow
        IRManager.getInstance().addAndSetCurBlock(followBlock);
        IRManager.getInstance().resetBlockName(followBlock);
        IRManager.getInstance().popLoop();
        SymbolManager.getInstance().leaveLoop();
        return null;
    }
}
