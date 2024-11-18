package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.IRManager;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

public class BinaryInstr extends Instr {
    // %2 = add i32 1, 2

    public enum op {
        ADD, SUB, MUL, AND, OR,
        SDIV, SREM, // 有符号除法 有符号取余
    }
    private op binOp;

    public static BinaryInstr checkAndGenBinInstr(LLVMType llvmType, op binOp, Value operand1, Value operand2) {
        if (operand1.getLlvmType() != IntType.INT32) {
            if (operand1 instanceof Constant) {
                ((Constant) operand1).switchToi32();
            } else {
                operand1 = new ZextInstr(IRManager.getInstance().genVRName(), operand1, IntType.INT32);
            }
        }
        if (operand2.getLlvmType() != IntType.INT32) {
            if (operand2 instanceof Constant) {
                ((Constant) operand2).switchToi32();
            } else {
                operand2 = new ZextInstr(IRManager.getInstance().genVRName(), operand2, IntType.INT32);            }
        }
        return new BinaryInstr(IRManager.getInstance().genVRName(), llvmType, binOp, operand1, operand2);
    }

    public BinaryInstr(String name, LLVMType llvmType, op binOp, Value operand1, Value operand2) {
        super(name, llvmType, InstrType.BINARY_INSTR);
        this.binOp = binOp;
        addOperand(operand1);
        addOperand(operand2);
    }

    @Override
    public String toString() {
        return name + " = " + binOp.toString().toLowerCase() + " "
                + operands.get(0).toTypeAndName() + ", " + operands.get(1).getName();
    }
}
