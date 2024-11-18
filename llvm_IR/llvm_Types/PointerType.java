package llvm_IR.llvm_Types;

public class PointerType extends LLVMType{
    private LLVMType referencedType;

    public PointerType(LLVMType referencedType) {
        this.referencedType = referencedType;
    }

    public LLVMType getReferencedType() {
        return referencedType;
    }

    @Override
    public String toString() {
        return referencedType.toString() + "*";
    }
}
