package frontend.Nodes.Func;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import frontend.Symbol.VarSymbol;
import llvm_IR.IRManager;
import llvm_IR.Instr.AllocaInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.StoreInstr;
import llvm_IR.llvm_Values.Param;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

import static frontend.Symbol.SymbolManager.curSymbolId;

public class FuncFParam extends Node {
    //  FuncFParam → BType Ident ['[' ']'] // b k
    private VarSymbol varSymbol;

    public FuncFParam(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public boolean judgeIsArray() {
        return children.size() == 4;
    }

    public TypeInfo.typeInfo judgeType() {
        if (((TokenNode)children.get(0)).getTokenType() == TokenType.INTTK) {
            return TypeInfo.typeInfo.INT_TYPE;
        } else {
            return TypeInfo.typeInfo.CHAR_TYPE;
        }
    }
    public VarSymbol createSymbol() {
        TypeInfo typeInfo = new TypeInfo(judgeIsArray(), judgeType());
        return new VarSymbol(typeInfo, ((TokenNode) children.get(1)).getTokenValue());
    }
    public VarSymbol getVarSymbol() {
        return varSymbol;
    }
    @Override
    public void checkError() {
        // b 名字重定义 报错Ident
        this.varSymbol = createSymbol();
        boolean res = SymbolManager.getInstance().addSymbol(varSymbol);
        if (!res) {
            Error error = new Error(children.get(1).getEndLine(), 'b');
            Printer.addError(error);
        }
        super.checkError();
    }



    public void updateCurSymbolId() {
        assert curSymbolId == varSymbol.getSymbolId() - 1;
        curSymbolId = varSymbol.getSymbolId();
    }

    public void setParamLLVM() {
        // 获取当前参数的llvmType
        IntType intType;
        if (varSymbol.getTypeInfo().getType() == TypeInfo.typeInfo.INT_TYPE)
            intType = IntType.INT32;
        else intType = IntType.INT8;
        // 如果是数组，该参数类型符号的llvmType是一个指向数组元素类型的指针,llvmValue就是这个param
        LLVMType llvmType = varSymbol.getTypeInfo().getIsArray() ? new PointerType(intType) : intType;
        Param param = new Param(IRManager.getInstance().genVRName(), llvmType);
        varSymbol.setLlvmType(llvmType);
        varSymbol.setLlvmValue(param);
    }

    @Override
    public Value generateIR() {
        Param param = (Param) varSymbol.getLlvmValue();
        if (!varSymbol.getTypeInfo().getIsArray()) {
            // 复制形参信息
            Instr alloca = new AllocaInstr(IRManager.getInstance().genVRName(), param.getLlvmType());
            varSymbol.setLlvmValue(alloca);
            // 把形参的值存入
            StoreInstr.checkAndGenStoreInstr(param, alloca);
        }
//        else {
//            // 为数组参数创建新的指针类型，并将首地址存入
//            //LLVMType eleType = ((PointerType)param.getLlvmType()).getReferencedType();
//            Instr alloca = new AllocaInstr(IRManager.getInstance().genVRName(), param.getLlvmType());
//            varSymbol.setLlvmValue(alloca);
//            StoreInstr.checkAndGenStoreInstr(param, alloca);
//        }
        return null;
    }
}
