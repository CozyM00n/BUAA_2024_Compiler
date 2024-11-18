package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.Function;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CallInstr extends Instr {
    protected Function function;
    protected ArrayList<Value> rParams;

    public CallInstr(String name, Function function, ArrayList<Value> rParams) {
        super(name, function.getRetType(), InstrType.CALL_INSTR);
        addOperand(function);
        this.function = function;
        this.rParams = rParams;
    }

    public Function getFunction() {
        return function;
    }

    public ArrayList<Value> getRParams() {
        return rParams;
    }

    @Override
    public String toString() { // %6 = call i32 @foo(i32 %5, i32 %4)
        StringBuilder sb = new StringBuilder();
        if (!(this.llvmType instanceof VoidType)) {
            sb.append(this.name + " = ");
        }
        sb.append("call " + function.getRetType() + " " + function.getName() + "(");
        sb.append(rParams.stream()
                .map(Value::toTypeAndName)
                .collect(Collectors.joining(", ")));
        sb.append(")");
        return sb.toString();
    }
}
