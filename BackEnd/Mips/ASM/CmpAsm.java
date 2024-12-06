package BackEnd.Mips.ASM;

import BackEnd.Mips.Register;

public class CmpAsm extends Asm {
    public enum cmpOp {
        SLT, SLE,
        SGT, SGE,
        SEQ, SNE,
    }
    private cmpOp op;
    private Register rd;
    private Register rs;
    private Register rt;

    public CmpAsm(cmpOp op, Register rd, Register rs, Register rt) {
        this.op = op;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return op.toString().toLowerCase() + " "
                + rd + ", "
                + rs + ", "
                + rt + ", ";
    }
}
