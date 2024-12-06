package llvm_IR.Instr.InOut;

import BackEnd.Mips.ASM.Comment;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.ASM.syscallAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
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

    @Override
    public void genAsm() {
        new Comment(this.toString());
        new LiAsm(Register.V0, 12);
        new syscallAsm();
        // 为this在栈上开辟空间，把v0的值存入（函数返回值依然是int）
        int offset = MipsManager.getInstance().pushAndRetStackFrame(4);
        MipsManager.getInstance().addValueToStack(this, offset);
        new MemoryAsm(MemoryAsm.memOp.SW, Register.V0, offset, Register.SP);
    }
}
