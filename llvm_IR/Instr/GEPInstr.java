package llvm_IR.Instr;

import BackEnd.Mips.ASM.AluAsm;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.Constant;
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

    public void loadValueToReg(Value value, Register register) {
        if (value instanceof Constant) {
            new LiAsm(register, ((Constant) value).getValue());
        } else {
            // 从内存中将值加载到寄存器
            Integer offset = MipsManager.getInstance().getOffsetOfValue(value);
            assert offset != null;
            new MemoryAsm(MemoryAsm.memOp.LW, register, offset, Register.SP);
        }
    }

    @Override
    public void genAsm() {
        super.genAsm();
        Register pointerReg = Register.K0;
        Register offsetReg = Register.K1;
        loadValueToReg(pointer, pointerReg);
        loadValueToReg(offset, offsetReg);
        // 计算偏移量
        // todo 如果offset为Constant
        new AluAsm(AluAsm.aluOp.SLL, offsetReg, offsetReg, 2);
        new AluAsm(AluAsm.aluOp.ADDU, pointerReg, pointerReg, offsetReg);
        // 为返回值开辟空间并压入栈中
        int offset = MipsManager.getInstance().pushAndRetStackFrame(4);
        MipsManager.getInstance().addValueToStack(this, offset);
        new MemoryAsm(MemoryAsm.memOp.SW, pointerReg, offset, Register.SP);
    }
}
