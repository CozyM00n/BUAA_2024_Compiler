package BackEnd.Mips.ASM.DataAsm;

public class Space extends DataAsm {
    // global_c:  .space 40
    private String name;
    private int byteNum;

    public Space(String name, int byteNum) {
        this.name = name;
        this.byteNum = byteNum;
    }

    @Override
    public String toString() {
        return name + ": .space " + byteNum;
    }
}
