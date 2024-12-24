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
    private boolean isRemoved;

    public BasicBlock(String name) {
        super(name, OtherType.BASIC_BLOCK);
        this.instrs = new LinkedList<>();
        this.isFirstBlock = false;
        this.parentFunc = null;
    }

    /** Instr **/
    public void addInstr(Instr instr) {
        instrs.add(instr);
        instr.setParentBlock(this);
    }

    public Instr getBBLastInstr() {
        if (instrs.isEmpty()) return null;
        return instrs.getLast();
    }

    public LinkedList<Instr> getInstrs() {
        return instrs;
    }

    /** Block **/
    public void setFirstBlock(boolean firstBlock) {
        isFirstBlock = firstBlock;
    }

    /** function***/
    public void setParentFunc(Function parentFunc) {
        this.parentFunc = parentFunc;
    }

    public Function getParentFunc() {
        return parentFunc;
    }


    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }
    public boolean isRemoved() {
        return isRemoved;
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
