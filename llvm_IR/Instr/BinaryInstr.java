package llvm_IR.Instr;

import BackEnd.Mips.ASM.AluAsm;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.HiLoAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.ASM.mdAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
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
    private Value op1;
    private Value op2;

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
        this.op1 = operand1;
        this.op2 = operand2;
        addOperand(operand1);
        addOperand(operand2);
    }

    @Override
    public String toString() {
        return name + " = " + binOp.toString().toLowerCase() + " "
                + operands.get(0).toTypeAndName() + ", " + operands.get(1).getName();
    }

    public void value2Reg(Value value, Register register) {

    }

    @Override
    public void genAsm() {
        super.genAsm();
        Register register1 = Register.K0;
        Register register2 = Register.K1;
        Register toReg = Register.K0;
        if (op1 instanceof Constant) {
            new LiAsm(register1, ((Constant) op1).getValue());
        } else {
            // 从内存中load得到op1的值并存入reg1
            Integer offset = MipsManager.getInstance().getOffsetOfValue(op1);
            assert offset != null;
            new MemoryAsm(MemoryAsm.memOp.LW, register1, offset, Register.SP);
        }
        if (op2 instanceof Constant) {
            new LiAsm(register2, ((Constant) op2).getValue());
        } else {
            // 从内存中load得到op2的值并存入reg2
            Integer offset = MipsManager.getInstance().getOffsetOfValue(op2);
            assert offset != null;
            new MemoryAsm(MemoryAsm.memOp.LW, register2, offset, Register.SP);
        }
        switch (binOp) {
            case ADD: new AluAsm(AluAsm.aluOp.ADDU, toReg, register1, register2); break;
            case SUB: new AluAsm(AluAsm.aluOp.SUBU, toReg, register1, register2); break;
            case AND: new AluAsm(AluAsm.aluOp.AND, toReg, register1, register2); break;
            case OR: new AluAsm(AluAsm.aluOp.OR, toReg, register1, register2); break;
            case MUL:
                new mdAsm(mdAsm.mdOp.MULT, register1, register2);
                new HiLoAsm(HiLoAsm.HiLoOp.MFLO, toReg); break;
            case SDIV:
                new mdAsm(mdAsm.mdOp.DIV, register1, register2);
                new HiLoAsm(HiLoAsm.HiLoOp.MFLO, toReg); break;
            case SREM:
                new mdAsm(mdAsm.mdOp.DIV, register1, register2);
                new HiLoAsm(HiLoAsm.HiLoOp.MFHI, toReg); break;
        }
        // 将toReg的值存回栈中
        int offset = MipsManager.getInstance().pushAndRetStackFrame(4);
        MipsManager.getInstance().addValueToStack(this, offset);
        new MemoryAsm(MemoryAsm.memOp.SW, toReg, offset, Register.SP);
    }
}
