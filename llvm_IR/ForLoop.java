package llvm_IR;

import llvm_IR.llvm_Values.BasicBlock;

public class ForLoop {
    private BasicBlock forStmt2Block;
    private BasicBlock followBlock;

    public ForLoop(BasicBlock forStmt2Block, BasicBlock followBlock) {
        this.forStmt2Block = forStmt2Block;
        this.followBlock = followBlock;
    }

    public BasicBlock getForStmt2Block() {
        return forStmt2Block;
    }

    public BasicBlock getFollowBlock() {
        return followBlock;
    }
}
