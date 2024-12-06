package BackEnd.Mips.ASM;

import BackEnd.Mips.Register;

public class mdAsm extends Asm {
    // div rs, rt # 余数在HI，商在LO
    public enum mdOp {
        MULT, DIV,
    }

    private mdOp op;
    private Register rs;
    private Register rt;

    public mdAsm(mdOp op, Register rs, Register rt) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return op.toString().toLowerCase() + " " + rs + ", " + rt;
    }
}
