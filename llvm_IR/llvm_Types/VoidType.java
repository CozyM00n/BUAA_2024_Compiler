package llvm_IR.llvm_Types;

public class VoidType extends LLVMType {
    public static final VoidType VOID = new VoidType();

    @Override
    public String toString() {
        return "void";
    }
}
