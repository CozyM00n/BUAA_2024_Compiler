package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.Value;

public class GEPInstr extends Instr{
    // %1 = getelementptr [5 x i32], [5 x i32]* @a, i32 0, i32 3
    // %4 = getelementptr i32, i32* %3, i32 1 函数参数类型为数组
    private Value pointer;
    private Value offset;

    public GEPInstr(String name, Value pointer, Value offset, LLVMType eleType) {
        super(name, new PointerType(eleType), InstrType.GEP_INSTR);
        this.pointer = pointer; addOperand(pointer);
        this.offset = offset;  addOperand(offset);
    }

    public Value getPointer() {
        return pointer;
    }

    public Value getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        LLVMType pointerType = pointer.getLlvmType();
        LLVMType refType = ((PointerType) pointer.getLlvmType()).getReferencedType();
        if (refType instanceof ArrayType) {
            return name + " = getelementptr " + refType + ", " + pointerType + " "
                    + pointer.getName() + ", i32 0, i32 " + offset.getName();
        } else {
            return name + " = getelementptr " + refType + ", " + pointerType + " "
                    + pointer.getName() + ", i32 " + offset.getName();
        }
    }
}
