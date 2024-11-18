package frontend.Symbol;


import Enums.SymbolType;
import llvm_IR.llvm_Values.Value;

public class Symbol {
    protected String symbolName;
    protected SymbolType symbolType;
    protected boolean isGlobal;
    protected static int SYMBOL_ID = 1;
    protected int symbolId = 0; // 语义分析建表阶段为每个变/常量分配全局唯一ID 是递增的

    public Symbol(String symbolName, SymbolType symbolType, boolean isGlobal) {
        this.symbolName = symbolName;
        this.symbolType = symbolType;
        this.isGlobal = isGlobal;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public TypeInfo getTypeInfo() {
        return null;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public Value getLlvmValue() {
        return null;
    }
}
