package BackEnd.Mips.ASM;

import BackEnd.Mips.ASM.DataAsm.DataAsm;
import BackEnd.Mips.MipsManager;

public class Asm {
    public Asm() {
        if (this instanceof DataAsm)
            MipsManager.getInstance().addToData(this);
        else
            MipsManager.getInstance().addToText(this);
    }
}
