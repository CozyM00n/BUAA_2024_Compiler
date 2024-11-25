package BackEnd.Mips.ASM.DataAsm;

import BackEnd.Mips.ASM.Asm;

public class Asciiz extends DataAsm {
    // 来自：printf的字符串
    // str_0:	.asciiz   "This is a string\n"
    private String name;
    private String asciiz;

    public Asciiz(String name, String asciiz) {
        this.name = name;
        this.asciiz = asciiz;
    }

    @Override
    public String toString() {
        return name + ": .asciiz \"" + asciiz.replace("\n", "\\n") + "\"";
    }
}
