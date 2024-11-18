package llvm_IR;

import llvm_IR.llvm_Values.BasicBlock;

public class ForLoop {
    private BasicBlock condBlock;
    private BasicBlock followBlock;

    public ForLoop(BasicBlock condBlock, BasicBlock followBlock) {
        this.condBlock = condBlock;
        this.followBlock = followBlock;
    }

    public BasicBlock getCondBlock() {
        return condBlock;
    }

    public BasicBlock getFollowBlock() {
        return followBlock;
    }
}
