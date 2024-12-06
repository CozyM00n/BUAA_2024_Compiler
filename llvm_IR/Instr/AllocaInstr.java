package llvm_IR.Instr;

import BackEnd.Mips.ASM.AluAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.llvm_Types.ArrayType;
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

    @Override
    public void genAsm() {
        super.genAsm();
        // 在栈上分配出空间 指针类型为4Bytes
        if (refType instanceof ArrayType) {
            MipsManager.getInstance().pushStackFrame(4 * ((ArrayType) refType).getLength());
        } else {
            MipsManager.getInstance().pushStackFrame(4);
        }
        // 接下来需要保存指针本身的值，没有寄存器只能存入栈中
        // 将指针的值（地址）存入栈中
        int spPos = MipsManager.getInstance().getCurStackPos();
        new AluAsm(AluAsm.aluOp.ADDI, Register.K0, Register.SP, spPos); // k0保存新分配内存的首地址（指针的值）
        // 为指针本身分配内存
        MipsManager.getInstance().pushStackFrame(4);
        spPos = MipsManager.getInstance().getCurStackPos();
        MipsManager.getInstance().addValueToStack(this, spPos);
        new MemoryAsm(MemoryAsm.memOp.SW, Register.K0, spPos, Register.SP);
    }
}
