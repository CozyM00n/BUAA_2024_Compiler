package llvm_IR.Instr.InOut;

import BackEnd.Mips.ASM.Comment;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.ASM.syscallAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import llvm_IR.Function;
import llvm_IR.IRManager;
import llvm_IR.Instr.CallInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.ZextInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class PutIntInstr extends CallInstr {
    // call void @putint(i32 %7)

    private Value intValue;
    private static final Function putIntFunc = new Function("@putint", VoidType.VOID);

    public static PutIntInstr checkAndGenPutInt(Value intValue) {
        if (intValue.getLlvmType() != IntType.INT32) {
            if (intValue instanceof Constant) {
                ((Constant) intValue).switchToi32();
            } else {
                intValue = new ZextInstr(IRManager.getInstance().genVRName(), intValue, IntType.INT32);
            }
        }
        return new PutIntInstr(intValue);
    }

    public PutIntInstr(Value intValue) {
        super("%putint", putIntFunc, new ArrayList<>());
        this.intValue = intValue;
        rParams.add(intValue);
    }

    @Override
    public void genAsm() {
        new Comment(this.toString());
        // 将整数加载到$a0寄存器
        loadValueToReg(intValue, Register.A0);
        new LiAsm(Register.A0, 1);
        new syscallAsm();
    }
}
