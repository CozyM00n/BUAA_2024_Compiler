package llvm_IR.Instr;

import BackEnd.Mips.ASM.AluAsm;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.ASM.laAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.GlobalVar;
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
        if (refType instanceof ArrayType) { // inbound
            return name + " = getelementptr " + refType + ", " + pointerType + " "
                    + pointer.getName() + ", i32 0, i32 " + offset.getName();
        } else if (refType instanceof IntType){
            return name + " = getelementptr " + refType + ", " + pointerType + " "
                    + pointer.getName() + ", i32 " + offset.getName();
        }
        else {
            System.out.println("GEPInstr: err");
            return name + " = getelementptr " + refType + ", " + pointerType + " "
                    + pointer.getName() + ", i32 " + offset.getName();
        }
//        else if (refType instanceof PointerType){
//            refType = ((PointerType) refType).getReferencedType();
//            pointerType = ((PointerType) pointerType).getReferencedType();
//            return name + " = getelementptr " + refType + ", " + pointerType + " "
//                    + pointer.getName() + ", i32 " + offset.getName();
//        }
    }

    public void loadPointerToReg(Value pointer, Register reg) {
        if (pointer instanceof GlobalVar) {
            // 把标签pointer的地址加载到寄存器中
            new laAsm(reg, pointer.getName().substring(1));
        } else {
            int offset = MipsManager.getInstance().getOffsetOfValue(pointer);
            new MemoryAsm(MemoryAsm.memOp.LW, reg, offset, Register.SP);
        }
    }

    @Override
    public void genAsm() {
        super.genAsm();
        Register pointerReg = Register.K0;
        Register offsetReg = Register.K1;
        loadPointerToReg(pointer, pointerReg);
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
