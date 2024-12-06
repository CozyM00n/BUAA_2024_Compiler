package llvm_IR.Instr;

import BackEnd.Mips.ASM.CmpAsm;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

public class IcmpInstr extends Instr {
    // %10 = icmp sgt i32 %9, 9

    public enum cmpOp {
        EQ, NE,
        SGT, SGE, SLT, SLE,
        //UGT, UGE, ULT, ULE,
    }
    private Value op1;
    private Value op2;
    private cmpOp cmpop;

    public IcmpInstr(String name, cmpOp cmpop, Value op1, Value op2) {
        super(name, IntType.INT1, InstrType.ICMP_INSTR);
        assert op1.getLlvmType() == IntType.INT32;
        assert op2.getLlvmType() == IntType.INT32;
        this.cmpop = cmpop;
        this.op1 = op1;
        this.op2 = op2;
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
        Register register1 = Register.K0;
        Register register2 = Register.K1;
        Register toReg = Register.K0;
        loadValueToReg(op1, register1);
        loadValueToReg(op2, register2);
        switch (cmpop) {
            case EQ: new CmpAsm(CmpAsm.cmpOp.SEQ, toReg, register1, register2); break;
            case NE: new CmpAsm(CmpAsm.cmpOp.SNE, toReg, register1, register2); break;
            case SGT: new CmpAsm(CmpAsm.cmpOp.SGT, toReg, register1, register2); break;
            case SGE: new CmpAsm(CmpAsm.cmpOp.SGE, toReg, register1, register2); break;
            case SLT: new CmpAsm(CmpAsm.cmpOp.SLT, toReg, register1, register2); break;
            case SLE: new CmpAsm(CmpAsm.cmpOp.SLE, toReg, register1, register2); break;
        }
        // 把结果从k0压入内存
        int offset = MipsManager.getInstance().pushAndRetStackFrame(4);
        MipsManager.getInstance().addValueToStack(this, offset);
        new MemoryAsm(MemoryAsm.memOp.SW, toReg, offset, Register.SP);
    }
}
