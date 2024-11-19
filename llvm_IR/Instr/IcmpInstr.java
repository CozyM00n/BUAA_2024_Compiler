package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Value;

public class IcmpInstr extends Instr {
    // %10 = icmp sgt i32 %9, 9

    public enum cmpOp {
        EQ, NE,
        SGT, SGE, SLT, SLE,
        UGT, UGE, ULT, ULE,
    }

    private cmpOp cmpop;

//    public static void checkAndGenIcmp(String name, cmpOp cmpop, Value operand1, Value operand2) {
//        if ()
//    }

    public IcmpInstr(String name, cmpOp cmpop, Value op1, Value op2) {
        super(name, IntType.INT1, InstrType.ICMP_INSTR);
        this.cmpop = cmpop;
        addOperand(op1); addOperand(op2);
    }

    public Value getOp1() {
        return operands.get(0);
    }

    public Value getOp2() {
        return operands.get(1);
    }

    @Override
    public String toString() {
        return name + " = icmp " + cmpop.toString().toLowerCase() + " "
                + operands.get(0).getLlvmType() + " " + operands.get(0).getName()
                + ", " + operands.get(1).getName();
    }
}
