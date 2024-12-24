package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.llvm_Types.LLVMType;

public class phiInstr extends Instr{
    public phiInstr(String name, LLVMType llvmType, InstrType instrType) {
        super(name, llvmType, instrType);
    }
}
