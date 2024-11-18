package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.Value;

public class LoadInstr extends Instr {
    private Value pointer;

    // %6 = load i32, i32* %4
    public LoadInstr(String name, Value pointer) {
        // 传入的pointer是symbol的LLVMValue
        // 对于局部变量，传入的pointer就是AllocaInstr,获取LLVMType即为该变量对应的指针类型
        // 对于全局变量，传入的pointer就是GlobalVar；再取LLVMType就是对应的指针
        super(name, ((PointerType) pointer.getLlvmType()).getReferencedType(), InstrType.LOAD_INSTR);
        addOperand(pointer);
        this.pointer = pointer;
    }

    @Override
    public String toString() {
        return name + " = load " + this.llvmType + ", " +
                pointer.getLlvmType() + " " + pointer.getName();
    }
}
