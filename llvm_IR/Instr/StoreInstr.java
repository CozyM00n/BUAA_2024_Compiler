package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.IRManager;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;


public class StoreInstr extends Instr {
    private Value from;
    private Value to;

    // store i32 %1, i32* %3
    // int to = from;
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
    public String toString() {
        return "store " + from.getLlvmType() + " " + from.getName()
                + ", " + to.getLlvmType() + " " + to.getName();
    }
}
