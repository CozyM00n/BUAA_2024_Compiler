package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.CondExp;
import frontend.Nodes.Node;
import llvm_IR.IRManager;
import llvm_IR.Instr.JumpInstr;
import llvm_IR.llvm_Values.BasicBlock;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
public class IfStmt extends Stmt{

    public IfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public Value generateIR() {
        BasicBlock trueBlock = new BasicBlock("");
        BasicBlock followBlock = new BasicBlock("");
        if (children.size() > 5) { // 有else
            BasicBlock falseBlock = new BasicBlock("");
            ((CondExp) children.get(2)).genConditionIR(trueBlock, falseBlock);
            // 解析trueBlock内容,解析前为trueBlock分配名字
            IRManager.getInstance().addAndSetCurBlock(trueBlock);
            IRManager.getInstance().resetBlockName(trueBlock);
            children.get(4).generateIR();
            new JumpInstr(followBlock);
            // 解析falseBlock内容
            IRManager.getInstance().addAndSetCurBlock(falseBlock);
            IRManager.getInstance().resetBlockName(falseBlock);
            children.get(6).generateIR();
            new JumpInstr(followBlock);
            IRManager.getInstance().addAndSetCurBlock(followBlock);
            IRManager.getInstance().resetBlockName(followBlock);
        }
        else {
            ((CondExp) children.get(2)).genConditionIR(trueBlock, followBlock);
            // 解析trueBlock内容
            IRManager.getInstance().addAndSetCurBlock(trueBlock);
            IRManager.getInstance().resetBlockName(trueBlock);
            children.get(4).generateIR();
            new JumpInstr(followBlock);
            IRManager.getInstance().addAndSetCurBlock(followBlock);
            IRManager.getInstance().resetBlockName(followBlock);
        }
        return null;
    }
}
