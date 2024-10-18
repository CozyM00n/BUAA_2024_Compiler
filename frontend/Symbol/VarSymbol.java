package frontend.Symbol;

import Enums.SymbolType;

public class VarSymbol extends Symbol{
    private TypeInfo typeInfo;

    public VarSymbol(TypeInfo typeInfo, String symbolName) {
        super(symbolName, SymbolType.SYMBOL_VAR);
        this.typeInfo = typeInfo;
    }

    @Override
    public String toString() {
        return this.symbolName + " " + typeInfo.toString();
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }
}
