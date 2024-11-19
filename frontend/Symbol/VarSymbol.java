package frontend.Symbol;

import Enums.SymbolType;
import llvm_IR.InitInfo;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Param;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class VarSymbol extends Symbol{
    private TypeInfo typeInfo;
    private InitInfo initInfo; // 全局变量才有的初始值
    private LLVMType llvmType;
    private Value llvmValue;
    // private Param param;

    public VarSymbol(TypeInfo typeInfo, String symbolName) {
        super(symbolName, SymbolType.SYMBOL_VAR, SymbolManager.getInstance().isGlobal());
        this.typeInfo = typeInfo;
        this.initInfo = null;
        this.llvmValue = null;
        // this.param = null;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }


    // 全局变量的初始值
    public void setInitInfo(InitInfo initInfo) {
        this.initInfo = initInfo;
    }
    public InitInfo getInitInfo() {
        return initInfo;
    }


    /*** for lValExp.calculate ***/
    public int getVarVal() {
        if (initInfo.getInitValues() == null) {
            if (!isGlobal)
                System.out.println("Err:Var is not initialized");
            return 0;
        } else {
            return initInfo.getInitValues().get(0);
        }
    }

    public int getVarArrVal(int offset) {
        if (initInfo.getInitValues() == null) {
            if (!isGlobal)
                System.out.println("Err:Var is not initialized");
            return 0;
        } else {
            if (offset >= initInfo.getInitValues().size()) {
                System.out.println("getVarArrVal Err: index out of bound!");
                return 0;
            }
            return initInfo.getInitValues().get(offset);
        }
    }


    public void setLlvmValue(Value llvmValue) {
        this.llvmValue = llvmValue;
    }
    public Value getLlvmValue() {
        return llvmValue;
    }

    public void setLlvmType(LLVMType llvmType) {
        this.llvmType = llvmType;
    }
    public LLVMType getLlvmType() {
        return llvmType;
    }


//    public void setParam(Param param) {
//        this.param = param;
//    }
//
//    public Param getParam() {
//        return param;
//    }

    /*** 符号表输出 ***/
    @Override
    public String toString() {
        return this.symbolName + " " + typeInfo.toString() + " id - " + this.symbolId; // for debug
    }
}
