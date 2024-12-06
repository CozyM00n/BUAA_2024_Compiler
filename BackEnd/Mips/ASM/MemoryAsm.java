package BackEnd.Mips.ASM;

import BackEnd.Mips.Register;

public class MemoryAsm extends Asm {
    // LW rt, offset(base)
    public enum memOp {
        LW, LH, LB, LBU,
        SW, SH, SB,
    }

    private memOp op;
    private Register rt;
    private int offset;
    private Register base;

    public MemoryAsm(memOp op, Register rt, int offset, Register base) {
        this.op = op;
        this.rt = rt;
        this.offset = offset;
        this.base = base;
    }

    @Override
    public String toString() {
        return op.toString().toLowerCase() + " " + rt + ", "
                + offset + "(" + base + ")";
    }
}
