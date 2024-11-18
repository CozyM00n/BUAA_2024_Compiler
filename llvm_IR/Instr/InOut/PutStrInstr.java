package llvm_IR.Instr.InOut;

import llvm_IR.Function;
import llvm_IR.IRManager;
import llvm_IR.Instr.CallInstr;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.StringLiteral;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class PutStrInstr extends CallInstr {
    // call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.2, i64 0, i64 0)

    private StringLiteral stringLiteral;
    private static final Function putstrFunc = new Function("@putstr", VoidType.VOID);

    public PutStrInstr(StringLiteral stringLiteral) {
        super("%putstr", putstrFunc, new ArrayList<>()); // 没有返回值 name
        this.stringLiteral = stringLiteral;
    }

    @Override
    public String toString() {
        PointerType pointerType = (PointerType) stringLiteral.getLlvmType();
        return "call void @putstr(i8* getelementptr inbounds (" +
                pointerType.getReferencedType() + ", " +
                pointerType + " " + stringLiteral.getName() +
                ", i64 0, i64 0))";
    }
}
