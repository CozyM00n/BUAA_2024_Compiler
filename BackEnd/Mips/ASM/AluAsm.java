package BackEnd.Mips.ASM;

import BackEnd.Mips.Register;

public class AluAsm extends Asm{
    public enum aluOp {
        // R型指令
        ADD, SUB,
        ADDU, SUBU,
        AND, OR, ORI,
        // I型指令
        ADDI, SUBI,
        // 移位运算
        SLL, SRL, SLLV, SRLV
    }
    private aluOp aluOp;
    private Register rd;
    private Register rs;
    private Register rt;
    private int imme;

    public AluAsm(aluOp aluOp, Register rd, Register rs, Register rt) {
        this.aluOp = aluOp;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    public AluAsm(aluOp aluOp, Register rd, Register rs, int imme) {
        this.aluOp = aluOp;
        this.rd = rd;
        this.rs = rs;
        this.rt = null;
        this.imme = imme;
    }

    @Override
    public String toString() {
        // addu $k0, $k0, $k1
        if (this.rt != null) {
            return aluOp.toString().toLowerCase() + " " + rd + ", " + rs + ", " + rt;
        }
        else {
            return aluOp.toString().toLowerCase() + " " + rd + ", " + rs + ", " + imme;
        }
    }
}
