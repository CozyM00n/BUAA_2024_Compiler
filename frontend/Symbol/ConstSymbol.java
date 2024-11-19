package frontend.Symbol;

import Enums.SymbolType;
import llvm_IR.InitInfo;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Value;

public class ConstSymbol extends Symbol {
    private TypeInfo typeInfo;
    private InitInfo initInfo; // 保存
    private LLVMType llvmType;
    private Value llvmValue;

    public ConstSymbol (TypeInfo typeInfo, String symbolName) {
        super(symbolName, SymbolType.SYMBOL_CONST, SymbolManager.getInstance().isGlobal());
        this.typeInfo = typeInfo;
        this.initInfo = null;
        this.llvmType = null;
        this.llvmValue = null;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }


    public void setInitInfo(InitInfo initInfo) {
        this.initInfo = initInfo;
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


    /*** for lValExp.calculate ***/
    public int getConstVal() {
        return initInfo.getInitValues().get(0);
    }

    public int getConstArrVal(int offset) {
        if (offset >= initInfo.getInitValues().size()) {
            System.out.println("getVarArrVal Err: index out of bound!");
            return 0;
        }
        return initInfo.getInitValues().get(offset);
    }

    /*** 符号表输出 ***/
    @Override
    public String toString() {
        return this.symbolName + " " + "Const" + typeInfo.toString();
    }
}
