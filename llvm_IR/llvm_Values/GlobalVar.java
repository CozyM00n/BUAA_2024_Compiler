package llvm_IR.llvm_Values;

import BackEnd.Mips.ASM.DataAsm.Word;
import llvm_IR.IRManager;
import llvm_IR.InitInfo;
import llvm_IR.User;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;

public class GlobalVar extends User {
    private InitInfo initInfo;

    public GlobalVar(String name, LLVMType refType, InitInfo initInfo) {
        super(name, new PointerType(refType));
        this.initInfo = initInfo;
        IRManager.getInstance().addGlobalVar(this);
    }

    @Override
    public String toString() {
        return this.name + " = dso_local global " + initInfo;
    }

    @Override
    public void genAsm() {
        new Word(name.substring(1), initInfo);
    }
}
