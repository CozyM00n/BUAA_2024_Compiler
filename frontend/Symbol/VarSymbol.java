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
    private ArrayList<Integer> initValues;
    private LLVMType llvmType;
    private Value llvmValue;
    private Param param;

    public VarSymbol(TypeInfo typeInfo, String symbolName) {
        super(symbolName, SymbolType.SYMBOL_VAR, SymbolManager.getInstance().isGlobal());
        this.typeInfo = typeInfo;
        this.initInfo = null;
        this.llvmValue = null;
        this.param = null;
    }

//    public VarSymbol(TypeInfo typeInfo, String symbolName, InitInfo initInfo) {
//        super(symbolName, SymbolType.SYMBOL_VAR, SymbolManager.getInstance().isGlobal());
//        this.typeInfo = typeInfo;
//        this.initInfo = initInfo;
//        this.initValues = initInfo.getInitValue();
//        this.llvmValue = null;
//        this.param = null;
//    }

    public void setInitInfo(InitInfo initInfo) {
        this.initInfo = initInfo;
        this.initValues = initInfo.getInitValue();
    }

    public InitInfo getInitInfo() {
        return initInfo;
    }

    public int getVarVal() {
        if (initValues == null) {
            if (isGlobal) return 0;
            else {
                System.out.println("Var is not initialized");
                return 0xff;
            }
        } else {
            return initValues.get(0);
        }
    }

    public int getVarArrVal(int offset) {
        if (initValues == null) {
            if (isGlobal) return 0;
            else {
                System.out.println("Var is not initialized");
                return 0xff;
            }
        } else {
            if (offset >= initValues.size()) return 0xf;
            return initValues.get(offset);
        }
    }

    public void setLlvmValue(Value llvmValue) {
        this.llvmValue = llvmValue;
    }

    public Value getLlvmValue() {
        return llvmValue;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public Param getParam() {
        return param;
    }

    public void setLlvmType(LLVMType llvmType) {
        this.llvmType = llvmType;
    }

    public LLVMType getLlvmType() {
        return llvmType;
    }

    @Override
    public String toString() {
        return this.symbolName + " " + typeInfo.toString() + " id - " + this.symbolId; // for debug
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }
}
