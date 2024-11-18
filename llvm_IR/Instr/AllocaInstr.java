package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.IRManager;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;

// %3 = alloca i32
public class AllocaInstr extends Instr {
    private LLVMType refType;

    public AllocaInstr(String name, LLVMType refType) { // 接受要分配的对象类型，返回的是地址，即该对象对应指针类型
        super(name, new PointerType(refType), InstrType.ALLOCA_INSTR);
        this.refType = refType;
    }

    @Override
    public String toString() {
        return name + " = alloca " + refType;
    }
}
