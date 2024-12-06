package BackEnd.Mips.ASM;

import BackEnd.Mips.ASM.Asm;
import BackEnd.Mips.Register;

public class JumpAsm extends Asm {
    public enum JumpOp {
        J, JAL, JR;
    }

    private JumpOp op;
    private String label;
    private Register ra;

    public JumpAsm(JumpOp op, String label) {
        this.op = op;
        this.label = label;
        this.ra = null;
    }

    public JumpAsm(JumpOp op, Register ra) {
        this.op = op;
        this.ra = ra;
        this.label = null;
    }

    @Override
    public String toString() {
        if (this.ra == null) {
            return op.toString().toLowerCase() + " " + label;
        } else {
            return op.toString().toLowerCase() + " " + ra;
        }
    }
}
