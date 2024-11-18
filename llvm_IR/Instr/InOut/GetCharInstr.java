package llvm_IR.Instr.InOut;

import llvm_IR.Function;
import llvm_IR.Instr.CallInstr;
import llvm_IR.llvm_Types.IntType;

import java.util.ArrayList;

public class GetCharInstr extends CallInstr {
    // %3 = call i32 @getchar()

    private static Function getCharFunc = new Function("@getchar", IntType.INT32);

    public GetCharInstr(String name) {
        super(name, getCharFunc, new ArrayList<>());
    }
}
