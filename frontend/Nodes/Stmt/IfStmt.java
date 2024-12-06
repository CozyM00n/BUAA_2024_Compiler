package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.CondExp;
import frontend.Nodes.Node;
import llvm_IR.IRManager;
import llvm_IR.Instr.JumpInstr;
import llvm_IR.BasicBlock;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
public class IfStmt extends Stmt{

    public IfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public Value generateIR() {
        BasicBlock trueBlock = new BasicBlock(IRManager.getInstance().genBlockName());
        BasicBlock followBlock = new BasicBlock(IRManager.getInstance().genBlockName());
        if (children.size() > 5) { // 有else
            BasicBlock falseBlock = new BasicBlock(IRManager.getInstance().genBlockName());
            ((CondExp) children.get(2)).genConditionIR(trueBlock, falseBlock);
            // 解析trueBlock内容,解析前为trueBlock分配名字
            IRManager.getInstance().addAndSetCurBlock(trueBlock);
            children.get(4).generateIR();
            new JumpInstr(followBlock);
            // 解析falseBlock内容
            IRManager.getInstance().addAndSetCurBlock(falseBlock);
            children.get(6).generateIR();
            new JumpInstr(followBlock);
            IRManager.getInstance().addAndSetCurBlock(followBlock);
        }
        else {
            ((CondExp) children.get(2)).genConditionIR(trueBlock, followBlock);
            // 解析trueBlock内容
            IRManager.getInstance().addAndSetCurBlock(trueBlock);
            children.get(4).generateIR();
            new JumpInstr(followBlock);
            IRManager.getInstance().addAndSetCurBlock(followBlock);
        }
        return null;
    }
}
