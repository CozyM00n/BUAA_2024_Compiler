package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.*;
import llvm_IR.IRManager;
import llvm_IR.Instr.GEPInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.LoadInstr;
import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

// 左值表达式 LVal → Ident ['[' Exp ']'] // c k
public class LValExp extends Node {
    public LValExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public TokenNode getIdentToken() {
        return (TokenNode) children.get(0);
    }

    @Override
    public void checkError() {
        // c 使用了未定义的标识符 报错行号为 Ident 所在行数
        String identName = ((TokenNode)children.get(0)).getTokenValue();
        if (SymbolManager.getInstance().getSymbol(identName) == null) {
            Error error = new Error(((TokenNode) children.get(0)).getLino(), 'c');
            Printer.addError(error);
        }
        super.checkError();
    }

    @Override
    public TypeInfo getTypeInfo() {
        String name = ((TokenNode)children.get(0)).getTokenValue();
        Symbol symbol = SymbolManager.getInstance().getSymbol(name);
        if (symbol == null) {
            System.out.println("LvalExp getTypeInfo :cannot find funcRealSymbol:" + name);
            return null;
        }
        TypeInfo typeInfo = symbol.getTypeInfo();
        if (children.size() >= 2) {
            if (!typeInfo.getIsArray()) {
                System.out.println("err: LvalExp getTypeInfo");
            }
            return new TypeInfo(false, typeInfo.getType());
        } else {
            return typeInfo; // 实际调用的是子类的方法
        }
    }

    @Override
    public int calculate() {
        String name = ((TokenNode)children.get(0)).getTokenValue();
        // 必须保证使用的Symbol是到目前为止定义过的
        Symbol symbol = SymbolManager.getInstance().getSymbolForIR(name);
        if (symbol instanceof ConstSymbol) {
            if (children.size() >= 2) { // arr[1]
                int offset = children.get(2).calculate();
                return ((ConstSymbol) symbol).getConstArrVal(offset);
            } else {
                return ((ConstSymbol) symbol).getConstVal();
            }
        } else { // VarSymbol
            if (children.size() >= 2) {
                int offset = children.get(2).calculate();
                return ((VarSymbol) symbol).getVarArrVal(offset);
            } else {
                return ((VarSymbol) symbol).getVarVal();
            }
        }
    }

    public Value generateIR() { // todo 优化结构
        String name = ((TokenNode)children.get(0)).getTokenValue();
        // 必须保证使用的Symbol是到目前为止定义过的
        Symbol symbol = SymbolManager.getInstance().getSymbolForIR(name);
        if (symbol instanceof VarSymbol) {
            VarSymbol varSymbol = (VarSymbol) symbol;
            if (varSymbol.getTypeInfo().getIsArray()) {
                LLVMType eleType;
                if (varSymbol.getLlvmType() instanceof PointerType) {
                    eleType = ((PointerType) varSymbol.getLlvmType()).getReferencedType();
                } else {
                     eleType = ((ArrayType)varSymbol.getLlvmType()).getEleType();
                }
                if (children.size() >= 2) { // 取数组的某个index的值
                    Value offset = children.get(2).generateIR();
                    // 这里varSymbol的LlvmValue是一个指向数组/INT的指针
                    Instr gepInstr = new GEPInstr(IRManager.getInstance().genVRName(),
                            varSymbol.getLlvmValue(), offset, eleType);
                    return new LoadInstr(IRManager.getInstance().genVRName(), gepInstr);
                }
                else { // 返回数组首地址
                    return new GEPInstr(IRManager.getInstance().genVRName(),
                            varSymbol.getLlvmValue(), new Constant(0, IntType.INT32), eleType);
                }
            }
            else {
                return new LoadInstr(IRManager.getInstance().genVRName(), varSymbol.getLlvmValue());
            }
        }
        else if (symbol instanceof ConstSymbol) {
            ConstSymbol constSymbol = (ConstSymbol) symbol;
            if (constSymbol.getTypeInfo().getIsArray()) {
                LLVMType eleType;
                if (constSymbol.getLlvmType() instanceof PointerType) {
                    eleType = ((PointerType) constSymbol.getLlvmType()).getReferencedType();
                } else {
                    eleType = ((ArrayType)constSymbol.getLlvmType()).getEleType();
                }
                if (children.size() >= 2) {
                    Value offset = children.get(2).generateIR();
                    Instr gepInstr = new GEPInstr(IRManager.getInstance().genVRName(),
                            constSymbol.getLlvmValue(), offset, eleType);
                    return new LoadInstr(IRManager.getInstance().genVRName(), gepInstr);
                }
                else {
                    return new GEPInstr(IRManager.getInstance().genVRName(),
                            constSymbol.getLlvmValue(), new Constant(0, IntType.INT32), eleType);
                }
            }
            else {
                return new LoadInstr(IRManager.getInstance().genVRName(), constSymbol.getLlvmValue());
            }
        }
        else {
            System.out.println("LvalExp: generateIR sym = null");
            return null;
        }
    }


    public Value genIRForAssign() {
        String name = ((TokenNode)children.get(0)).getTokenValue();
        // 必须保证使用的Symbol是到目前为止定义过的
        VarSymbol varSymbol = (VarSymbol) SymbolManager.getInstance().getSymbolForIR(name); // 一定是varSymbol
        if (children.size() == 1) {
            return varSymbol.getLlvmValue();
        } else { // 数组
            LLVMType eleType;
            if (varSymbol.getLlvmType() instanceof PointerType) {
                eleType = ((PointerType) varSymbol.getLlvmType()).getReferencedType();
            } else {
                eleType = ((ArrayType)varSymbol.getLlvmType()).getEleType();
            }
            Value offset = children.get(2).generateIR();
            return new GEPInstr(IRManager.getInstance().genVRName(), varSymbol.getLlvmValue(), offset, eleType);
        }
    }
}
