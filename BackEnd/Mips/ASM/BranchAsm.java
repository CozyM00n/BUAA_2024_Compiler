package BackEnd.Mips.ASM;

import BackEnd.Mips.Register;

public class BranchAsm extends Asm {
    public enum brOp {
        BEQ, BNE,
        BGTZ, BLTZ,
        BGEZ, BLEZ,
    }

    private brOp op;
    private Register rs;
    private Register rt;
    private String label;

    // BEQ rs, rt, lable
    public BranchAsm(brOp op, Register rs, Register rt, String label) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.label = label;
    }

    // BGTZ rs, lable
    public BranchAsm(brOp op, Register rs, String label) {
        this.op = op;
        this.rs = rs;
        this.rt = null;
        this.label = label;
    }

    @Override
    public String toString() {
        if (rt == null) {
            return op.toString().toLowerCase() + " " + rs + ", " + label;
        } else {
            return op.toString().toLowerCase() + " " + rs + ", " + rt + ", " + label;
        }
    }
}
