package BackEnd.Mips.ASM;

import BackEnd.Mips.Register;

public class HiLoAsm extends Asm {
    public enum HiLoOp {
        MFHI, MFLO,
        MTHI, MTLO,
    }

    private HiLoOp op;
    private Register register;

    public HiLoAsm(HiLoOp op, Register register) {
        this.op = op;
        this.register = register;
    }

    @Override
    public String toString() {
        return op.toString().toLowerCase() + " " + register;
    }
}
