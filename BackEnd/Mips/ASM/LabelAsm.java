package BackEnd.Mips.ASM;

public class LabelAsm extends Asm {
    private String labelName;

    public LabelAsm(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String toString() {
        return "\n"+labelName + ":";
    }
}
