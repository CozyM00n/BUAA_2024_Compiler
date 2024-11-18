package llvm_IR.Instr.InOut;

import llvm_IR.Function;
import llvm_IR.IRManager;
import llvm_IR.Instr.CallInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.ZextInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class PutIntInstr extends CallInstr {
    // call void @putint(i32 %7)

    private Value intValue;
    private static final Function putIntFunc = new Function("@putint", VoidType.VOID);

    public static PutIntInstr checkAndGenPutInt(Value intValue) {
        if (intValue.getLlvmType() != IntType.INT32) {
            intValue = new ZextInstr(IRManager.getInstance().genVRName(), intValue, IntType.INT32);
        }
        return new PutIntInstr(intValue);
    }

    public PutIntInstr(Value intValue) {
        super("%putint", putIntFunc, new ArrayList<>());
        rParams.add(intValue);
    }
}
