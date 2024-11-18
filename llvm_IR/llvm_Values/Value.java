package llvm_IR.llvm_Values;

import llvm_IR.IRManager;
import llvm_IR.Instr.ZextInstr;
import llvm_IR.Use;
import llvm_IR.User;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;

import java.util.ArrayList;

public class Value {
    protected String name;
    protected LLVMType llvmType;
    protected ArrayList<Use> useList; // 关系索引列表

    public Value(String name, LLVMType llvmType) {
        this.name = name;
        this.llvmType = llvmType;
        this.useList = new ArrayList<>();
    }

    public void addUse(User user) {
        useList.add(new Use(user, this));
    }

    public String getName() {
        return name;
    }

    public LLVMType getLlvmType() {
        return llvmType;
    }

    public String toTypeAndName() { // i32 %5
        return llvmType + " " + name;
    }
}
