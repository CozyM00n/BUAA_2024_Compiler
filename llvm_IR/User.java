package llvm_IR;

import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class User extends Value {
    protected ArrayList<Value> operands;

    public User(String name, LLVMType llvmType) {
        super(name, llvmType);
        this.operands = new ArrayList<>();
    }

    public void addOperand(Value value) {
        operands.add(value);
    }
}
