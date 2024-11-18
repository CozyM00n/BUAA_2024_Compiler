package frontend.Symbol;

import Enums.SymbolType;
import llvm_IR.InitInfo;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class ConstSymbol extends Symbol {
    private TypeInfo typeInfo;
    private InitInfo initInfo;
    private ArrayList<Integer> initValues;
    private LLVMType llvmType;
    private Value llvmValue;

    public ConstSymbol (TypeInfo typeInfo, String symbolName) {
        super(symbolName, SymbolType.SYMBOL_CONST, SymbolManager.getInstance().isGlobal());
        this.typeInfo = typeInfo;
        this.initInfo = null;
        this.llvmType = null;
        this.llvmValue = null;
    }

    public void setInitInfo(InitInfo initInfo) {
        this.initInfo = initInfo;
        this.initValues = initInfo.getInitValue();
    }

    public int getConstVal() {
        return initValues.get(0);
    }

    public int getConstArrVal(int offset) {
        if (offset >= initValues.size()) return 0;
        return initValues.get(offset);
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public InitInfo getInitInfo() {
        return initInfo;
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

    @Override
    public String toString() {
        return this.symbolName + " " + "Const" + typeInfo.toString();
    }
}
