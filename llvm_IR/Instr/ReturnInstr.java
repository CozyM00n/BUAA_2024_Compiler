package llvm_IR.Instr;

import Enums.InstrType;
import Enums.ReturnType;
import llvm_IR.IRManager;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

public class ReturnInstr extends Instr {
    // ret i32 %7
    private Value retValue;

    public static ReturnInstr checkAndGenRet(Value retValue, LLVMType funRetType) {
        if (retValue != null && retValue.getLlvmType() != funRetType) {
            if (retValue instanceof Constant) {
                ((Constant) retValue).switchType(funRetType);
            } else {
                if (funRetType == IntType.INT32) {
                    retValue = new ZextInstr(IRManager.getInstance().genVRName(), retValue, IntType.INT32);
                } else if (funRetType == IntType.INT8) {
                    retValue = new TruncInstr(IRManager.getInstance().genVRName(), retValue, IntType.INT8);
                }
            }
        }
        return new ReturnInstr(retValue);
    }
    public ReturnInstr(Value retValue) { // retValue在返回void时为null
        super("return", VoidType.VOID, InstrType.RETURN_INSTR);
        this.retValue = retValue;
        addOperand(retValue);
    }

    public Value getRetValue() {
        return retValue;
    }

    @Override
    public String toString() {
        if (retValue == null) return "ret void";
        return "ret " + retValue.getLlvmType() + " " + retValue.getName();
    }
}
