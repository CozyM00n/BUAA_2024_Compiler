package llvm_IR;

import BackEnd.Mips.ASM.LabelAsm;
import llvm_IR.Instr.Instr;
import llvm_IR.User;
import llvm_IR.llvm_Types.OtherType;
import llvm_IR.llvm_Values.Value;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class BasicBlock extends User {
    private LinkedList<Instr> instrs;
    private boolean isFirstBlock;
    private Function parentFunc;

    public BasicBlock(String name) {
        super(name, OtherType.BASIC_BLOCK);
        this.instrs = new LinkedList<>();
        this.isFirstBlock = false;
        this.parentFunc = null;
    }

    public void addInstr(Instr instr) {
        instrs.add(instr);
        instr.setParentBlock(this);
    }

    public void setFirstBlock(boolean firstBlock) {
        isFirstBlock = firstBlock;
    }

    public Value getBBLastInstr() {
        if (instrs.isEmpty()) return null;
        return instrs.getLast();
    }

    /** function***/
    public void setParentFunc(Function parentFunc) {
        this.parentFunc = parentFunc;
    }

    public Function getParentFunc() {
        return parentFunc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isFirstBlock)  sb.append("\t");
        else  sb.append(name + ":\n\t");
        sb.append(instrs.stream()
                .map(Instr::toString)
                .collect(Collectors.joining("\n\t")));
        return sb.toString();
    }

    @Override
    public void genAsm() {
        new LabelAsm(name); // name=L+序号
        for (Instr instr : instrs) instr.genAsm();
    }
}
