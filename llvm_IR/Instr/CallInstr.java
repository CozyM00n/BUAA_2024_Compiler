package llvm_IR.Instr;

import BackEnd.Mips.ASM.AluAsm;
import BackEnd.Mips.ASM.JumpAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.Function;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CallInstr extends Instr {
    protected Function function;
    protected ArrayList<Value> rParams;

    public CallInstr(String name, Function function, ArrayList<Value> rParams) {
        super(name, function.getRetType(), InstrType.CALL_INSTR);
        addOperand(function);
        this.function = function;
        this.rParams = rParams;
    }

    public Function getFunction() {
        return function;
    }

    public ArrayList<Value> getRParams() {
        return rParams;
    }

    @Override
    public String toString() { // %6 = call i32 @foo(i32 %5, i32 %4)
        StringBuilder sb = new StringBuilder();
        if (!(this.llvmType instanceof VoidType)) {
            sb.append(this.name + " = ");
        }
        sb.append("call " + function.getRetType() + " " + function.getName() + "(");
        sb.append(rParams.stream()
                .map(Value::toTypeAndName)
                .collect(Collectors.joining(", ")));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void genAsm() {
        super.genAsm();
        // 保存当前函数的栈指针
        int curOffset = MipsManager.getInstance().getCurStackPos();
        new MemoryAsm(MemoryAsm.memOp.SW, Register.SP, curOffset-4, Register.SP);
        new MemoryAsm(MemoryAsm.memOp.SW, Register.RA, curOffset-8, Register.SP);
        // 子函数的栈（底）开始于curOffset-12~curOffset-8
        // 将形参的值复制给子函数栈底
        int newStackOffset = curOffset-8;
        Register tmpReg = Register.K0;
        for (int i = 0; i < rParams.size(); ++i) {
            // 将参数的值加载到$k0
            loadValueToReg(rParams.get(i), tmpReg);
            new MemoryAsm(MemoryAsm.memOp.SW, tmpReg, newStackOffset - 4*(i+1), Register.SP);
        }
        // 栈指针更新为子函数栈的起始位置
        new AluAsm(AluAsm.aluOp.ADDI, Register.SP, Register.SP, newStackOffset);
        new JumpAsm(JumpAsm.JumpOp.JAL, function.getName().substring(1));
        // 还原$sp和$ra，此时sp还是子函数的栈指针位置
        new MemoryAsm(MemoryAsm.memOp.LW, Register.RA, 0, Register.SP);
        new MemoryAsm(MemoryAsm.memOp.LW, Register.SP, 4, Register.SP);
        // $sp已更新为调用者的栈帧，curOffset依然不变
        // 储存返回值
        if (!(function.getRetType() instanceof VoidType)) {
            int offset = MipsManager.getInstance().pushAndRetStackFrame(4);
            MipsManager.getInstance().addValueToStack(this, offset);
            new MemoryAsm(MemoryAsm.memOp.SW, Register.V0, offset, Register.SP);
        }
    }
}
