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

    public void setParamForSymbol() {
        // 获取当前参数的llvmType
        IntType type = varSymbol.getTypeInfo().getType() == TypeInfo.typeInfo.INT_TYPE ? IntType.INT32 : IntType.INT8;
        LLVMType llvmType = varSymbol.getTypeInfo().getIsArray() ? new PointerType(type) : type;
        Param param = new Param(IRManager.getInstance().genVRName(), llvmType);
        varSymbol.setParam(param);
        assert curSymbolId == varSymbol.getSymbolId() - 1;
        curSymbolId = varSymbol.getSymbolId();
    }

    @Override
    public Value generateIR() {
        Param param = varSymbol.getParam();
        if (varSymbol.getTypeInfo().getIsArray()) {
            varSymbol.setLlvmValue(param);
        }
        else {
            // 复制形参信息
            Instr alloca = new AllocaInstr(IRManager.getInstance().genVRName(), param.getLlvmType());
            varSymbol.setLlvmValue(alloca);
            // 把形参的值存入
            alloca = StoreInstr.checkAndGenStoreInstr(param, alloca);
        }
        return null;
    }
}
