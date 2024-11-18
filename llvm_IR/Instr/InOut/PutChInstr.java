package llvm_IR.Instr.InOut;

import llvm_IR.Function;
import llvm_IR.Instr.CallInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class PutChInstr extends CallInstr {
    // call void @putch(i32 %3)

    private static final Function putChFunc = new Function("@putch", VoidType.VOID);

    public PutChInstr(Value intValue) {
        super("%putch", putChFunc, new ArrayList<>());
        assert intValue.getLlvmType() instanceof IntType;
        rParams.add(intValue);
    }
}
