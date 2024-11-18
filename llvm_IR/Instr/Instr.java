package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.IRManager;
import llvm_IR.User;
import llvm_IR.llvm_Types.LLVMType;

public class Instr extends User {
    private InstrType instrType;
    // public static boolean addThisInstr = true;

    public Instr(String name, LLVMType llvmType, InstrType instrType) {
        super(name, llvmType);
        this.instrType = instrType;
        IRManager.getInstance().addInstr(this);
    }
}
