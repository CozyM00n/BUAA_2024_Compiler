package llvm_IR.Instr;

import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.ASM.laAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.GlobalVar;
import llvm_IR.llvm_Values.Value;

public class LoadInstr extends Instr {
    private Value pointer;

    // %v3 = load i32, i32* %v0
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

    @Override
    public void genAsm() {
        // lw $t0, -4($sp)
        // lw $k0, 0(Reg_base)
        // %v3 = load i32, i32* %v0
        super.genAsm();
        Register toReg = Register.K0; // load访存得到的值存入toReg
        Register pointerReg = Register.K0; // pointerReg中存的是要从哪个地址进行load
        if (pointer instanceof GlobalVar) { // k0保存该全局变量的地址
            new laAsm(pointerReg, pointer.getName().substring(1));
        } else {
            // 获取指针的地址
            Integer offset = MipsManager.getInstance().getOffsetOfValue(pointer);
            assert offset != null;
            // load指针的值，即为该局部变量的地址，存入pointerReg
            new MemoryAsm(MemoryAsm.memOp.LW, pointerReg, offset, Register.SP);
        }
        // load得到的值存入toReg
        new MemoryAsm(MemoryAsm.memOp.LW, toReg, 0, pointerReg);
        //// 为普通变量%v3（load的返回值）在内存中分配空间
        MipsManager.getInstance().pushStackFrame(4);
        int offset = MipsManager.getInstance().getCurStackPos();
        // 记录load返回值的位置
        MipsManager.getInstance().addValueToStack(this, offset);
        // 将load得到的结果(toReg中的值)存入刚刚分配的位置
        new MemoryAsm(MemoryAsm.memOp.SW, toReg, offset, Register.SP);
    }
}
