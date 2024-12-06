package llvm_IR.Instr;

import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.ASM.laAsm;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.IRManager;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.GlobalVar;
import llvm_IR.llvm_Values.Value;


public class StoreInstr extends Instr {
    private Value from;
    private Value to;

    // store i32 %1, i32* %3
    // to <= from;
    public static StoreInstr checkAndGenStoreInstr(Value from, Value to) {
        assert from.getLlvmType() instanceof IntType;
        assert to.getLlvmType() instanceof PointerType;
        LLVMType toType = ((PointerType) to.getLlvmType()).getReferencedType();
        if (from.getLlvmType() != toType) {
            if (from instanceof Constant) {
                ((Constant) from).switchType(toType);
            }
            else {
                if (((IntType) from.getLlvmType()).getLength() < ((IntType) toType).getLength()) {
                    from = new ZextInstr(IRManager.getInstance().genVRName(), from, toType);
                }
                else {
                    from = new TruncInstr(IRManager.getInstance().genVRName(), from, toType);
                }
            }
        }
        return new StoreInstr(from, to);
    }

    public StoreInstr(Value from, Value to) {
        super("store", VoidType.VOID, InstrType.STORE_INSTR);
        addOperand(from);  addOperand(to);
        this.from = from;  this.to = to;
    }

    public Value getFrom() {
        return from;
    }

    public Value getTo() {
        return to;
    }

    @Override
    public void genAsm() {
        super.genAsm();
        Register toReg = Register.K1; // 保存目标存入地址
        if (to instanceof GlobalVar) { // 获得全局变量的地址
            new laAsm(toReg, to.getName().substring(1));
        } else { // to是局部变量所在的地址
            // 将to中存储的值（目标存入地址）加载到k1寄存器中
            new MemoryAsm(MemoryAsm.memOp.LW, toReg,
                    MipsManager.getInstance().getOffsetOfValue(to), Register.SP);
        }
        Register fromReg = Register.K0;
        if (from instanceof Constant) {
            new LiAsm(fromReg, ((Constant) from).getValue());
        } else {
            // 尝试获取from在当前栈中的位置
            Integer offset = MipsManager.getInstance().getOffsetOfValue(from);
            if (offset == null) {
                System.out.println("todo");
            }
            new MemoryAsm(MemoryAsm.memOp.LW, fromReg, offset, Register.SP);
        }
        // sw $t0, 8($t1)
        new MemoryAsm(MemoryAsm.memOp.SW, fromReg, 0, toReg);
    }

    @Override
    public String toString() {
        return "store " + from.getLlvmType() + " " + from.getName()
                + ", " + to.getLlvmType() + " " + to.getName();
    }
}
