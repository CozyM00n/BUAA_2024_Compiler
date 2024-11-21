package llvm_IR;

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
