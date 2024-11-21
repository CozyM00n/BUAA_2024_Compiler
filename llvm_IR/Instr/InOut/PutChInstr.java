package llvm_IR.Instr.InOut;

import llvm_IR.Function;
import llvm_IR.IRManager;
import llvm_IR.Instr.CallInstr;
import llvm_IR.Instr.ZextInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class PutChInstr extends CallInstr {
    // call void @putch(i32 %3)

    private static final Function putChFunc = new Function("@putch", VoidType.VOID);

    public static PutChInstr checkAndGenPutCh(Value chValue) {
        if (chValue.getLlvmType() != IntType.INT32) {
            if (chValue instanceof Constant) {
                ((Constant) chValue).switchToi32();
            } else {
                chValue = new ZextInstr(IRManager.getInstance().genVRName(), chValue, IntType.INT32);
            }
        }
        return new PutChInstr(chValue);
    }

    public PutChInstr(Value intValue) {
        super("%putch", putChFunc, new ArrayList<>());
        rParams.add(intValue);
    }
}
