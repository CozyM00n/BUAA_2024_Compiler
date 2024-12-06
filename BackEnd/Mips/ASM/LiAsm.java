package BackEnd.Mips.ASM;

import BackEnd.Mips.ASM.Asm;
import BackEnd.Mips.Register;

public class LiAsm extends Asm {
    // li $rd, immediate
    private Register toReg;
    private int imme;

    public LiAsm(Register toReg, int imme) {
        this.toReg = toReg;
        this.imme = imme;
    }

    @Override
    public String toString() {
        return "li " + toReg + ", " + imme;
    }
}
