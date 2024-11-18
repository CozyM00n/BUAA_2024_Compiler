package llvm_IR.Instr.InOut;

import llvm_IR.Function;
import llvm_IR.Instr.CallInstr;
import llvm_IR.llvm_Types.IntType;

import java.util.ArrayList;

public class GetIntInstr extends CallInstr {
    // %3 = call i32 @getint()

    private static Function getIntFunc = new Function("@getint", IntType.INT32);

    public GetIntInstr(String name) {
        super(name, getIntFunc, new ArrayList<>());
    }
}
