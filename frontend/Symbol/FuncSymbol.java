package frontend.Symbol;


import Enums.ReturnType;
import Enums.SymbolType;
import llvm_IR.Function;

import java.util.ArrayList;

public class FuncSymbol extends Symbol{
    private ReturnType returnType;
    private ArrayList<TypeInfo> typeList;
    private Function llvmValue;

    public FuncSymbol(String symbolName, ReturnType returnType) {
        super(symbolName, SymbolType.SYMBOL_FUNC, true);
        this.returnType = returnType;
        this.typeList = null;
        this.llvmValue = null;
    }

    public void setTypeList(ArrayList<TypeInfo> typeList) {
        this.typeList = typeList;
    }

    public ArrayList<TypeInfo> getTypeList() {
        return typeList;
    }

    public void setLlvmValue(Function llvmValue) {
        this.llvmValue = llvmValue;
    }

    public Function getLlvmValue() {
        return llvmValue;
    }

    @Override
    public String toString() {
        return symbolName + " " + returnType.toString() + "Func";
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public TypeInfo getTypeInfo() {
        if (returnType == ReturnType.RETURN_VOID) return new TypeInfo(false, TypeInfo.typeInfo.VOID_TYPE);
        if (returnType == ReturnType.RETURN_INT) return new TypeInfo(false, TypeInfo.typeInfo.INT_TYPE);
        return new TypeInfo(false, TypeInfo.typeInfo.CHAR_TYPE);
    }
}
