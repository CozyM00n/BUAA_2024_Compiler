package BackEnd.Mips.ASM;

import BackEnd.Mips.Register;

public class laAsm extends Asm {
    // la $a0, label 把label的地址存到寄存器a0中
    private Register toReg;
    private String fromLabel;

    public laAsm(Register toReg, String fromLabel) {
        this.toReg = toReg;
        this.fromLabel = fromLabel;
    }

    @Override
    public String toString() {
        return "la" + toReg + ", " + fromLabel;
    }
}
