package llvm_IR.llvm_Values;

import BackEnd.Mips.ASM.DataAsm.Asciiz;
import llvm_IR.IRManager;
import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.PointerType;

public class StringLiteral extends Value {
    // 用于printf中的字符串输出
    // @.str.2 = private unnamed_addr constant [8 x i8] c"Hello: \00", align 1

    private String string;

    public StringLiteral(String name, String string, int len) {
        super(name, new PointerType(new ArrayType(len + 1, IntType.INT8)));
        this.string = string;
        IRManager.getInstance().addStringLiteral(this);
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        // 不会有除了\n之外的转义字符
        return name + " = private unnamed_addr constant " + ((PointerType) llvmType).getReferencedType()
                + " c\"" + string.replace("\n", "\\0A") + "\\00\", align 1";
    }

    @Override
    public void genAsm() {
        new Asciiz(name.substring(2).replace(".", "_"), string);
    }
}
