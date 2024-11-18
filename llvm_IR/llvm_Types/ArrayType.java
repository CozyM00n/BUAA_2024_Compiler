package llvm_IR.llvm_Types;

public class ArrayType extends LLVMType {
    private int length;
    private LLVMType eleType;

    public ArrayType(int length, LLVMType eleType) {
        this.length = length;
        this.eleType = eleType;
    }

    public LLVMType getEleType() {
        return eleType;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "[" + length + " x " + eleType.toString() + "]";
    }
}
