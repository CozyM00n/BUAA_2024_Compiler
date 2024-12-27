package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.CondExp;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;
import llvm_IR.ForLoop;
import llvm_IR.IRManager;
import llvm_IR.Instr.JumpInstr;
import llvm_IR.BasicBlock;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// ForStmt =>
// 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt // h

public class ForBodyStmt extends Stmt {

    private ForStmt forStmt1 = null;
    private ForStmt forStmt2 = null;
    private CondExp cond = null;
    private Stmt forBodyStmt;
    private int symbolTableId;

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
//        for(int i = 0; i < children.size(); ++i) {
//            if (i == 1) {
//                SymbolManager.getInstance().pushTable();
//            }
//            else {
//                children.get(i).checkError();
//            }
//        }
//        this.symbolTableId = SymbolManager.getCurTableId();
//        SymbolManager.getInstance().popTable();
        super.checkError();
        SymbolManager.getInstance().leaveLoop();
    }

    @Override
    public Value generateIR() {
        SymbolManager.getInstance().enterLoop();
        // 进入for的符号表
        //SymbolManager.setCurTableId(symbolTableId);
        BasicBlock condBlock = new BasicBlock(IRManager.getInstance().genBlockName());
        BasicBlock forBodyBlock = new BasicBlock(IRManager.getInstance().genBlockName());
        BasicBlock followBlock = new BasicBlock(IRManager.getInstance().genBlockName());
        BasicBlock forStmt2Block = new BasicBlock(IRManager.getInstance().genBlockName());
        IRManager.getInstance().pushLoop(new ForLoop(forStmt2Block, followBlock));
        // 生成ForStmt1的IR
        if (forStmt1 != null) forStmt1.generateIR();
        // 跳转到condBlock
        new JumpInstr(condBlock);
        // 生成cond的IR指令
        IRManager.getInstance().addAndSetCurBlock(condBlock);
        if (cond != null) {
            cond.genConditionIR(forBodyBlock, followBlock); // cond 成立跳转到body，不成立跳转到follow
        } else { // 没有cond 直接跳转到forBody
            new JumpInstr(forBodyBlock);
        }
        // 生成forBody的IR指令
        IRManager.getInstance().addAndSetCurBlock(forBodyBlock);
        forBodyStmt.generateIR();
        // 跳转到forStmt2
        new JumpInstr(forStmt2Block);
        IRManager.getInstance().addAndSetCurBlock(forStmt2Block);
        // 生成forStmt2的IR指令
        if (forStmt2 != null) forStmt2.generateIR();
        new JumpInstr(condBlock);
        // 设置当前块为follow
        IRManager.getInstance().addAndSetCurBlock(followBlock);
        IRManager.getInstance().popLoop();
        SymbolManager.getInstance().leaveLoop();
        // SymbolManager.setCurTableId(SymbolManager.getInstance().getFatherTableId(symbolTableId));
        return null;
    }
}
