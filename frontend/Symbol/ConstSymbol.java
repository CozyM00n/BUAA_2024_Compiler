package frontend.Symbol;

import Enums.SymbolType;

public class ConstSymbol extends Symbol {
    private TypeInfo typeInfo;

    public ConstSymbol (TypeInfo typeInfo, String symbolName) {
        super(symbolName, SymbolType.SYMBOL_CONST);
        this.typeInfo = typeInfo;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    @Override
    public String toString() {
        return this.symbolName + " " + "Const" + typeInfo.toString();
    }
}
