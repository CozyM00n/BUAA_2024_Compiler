package frontend.Symbol;


import Enums.ReturnType;
import Enums.SymbolType;

import java.util.ArrayList;

public class FuncSymbol extends Symbol{
    private ReturnType returnType;
    private ArrayList<TypeInfo> typeList;

    public FuncSymbol(String symbolName, ReturnType returnType) {
        super(symbolName, SymbolType.SYMBOL_FUNC);
        this.returnType = returnType;
        this.typeList = null;
    }

    public void setTypeList(ArrayList<TypeInfo> typeList) {
        this.typeList = typeList;
    }

    public ArrayList<TypeInfo> getTypeList() {
        return typeList;
    }

    @Override
    public String toString() {
        return symbolName + " " + returnType.toString() + "Func";
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public TypeInfo getTypeInfo() {
        if (returnType == ReturnType.RETURN_VOID) return new TypeInfo(false, "Void");
        if (returnType == ReturnType.RETURN_INT) return new TypeInfo(false, "Int");
        return new TypeInfo(false, "Char");
    }
}
