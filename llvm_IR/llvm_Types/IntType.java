package llvm_IR.llvm_Types;

public class IntType extends LLVMType {
    private int length;

    public IntType(int length) {
        this.length = length;
    }

    public static IntType INT1 = new IntType(1);
    public static IntType INT8 = new IntType(8);
    public static IntType INT32 = new IntType(32);

    @Override
    public String toString() {
        switch (length) {
            case 1: return "i1";
            case 8: return "i8";
            case 32: return "i32";
            default: return null;
        }
    }
}
