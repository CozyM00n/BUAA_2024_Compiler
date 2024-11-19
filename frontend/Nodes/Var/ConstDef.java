package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Symbol.ConstSymbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import llvm_IR.IRManager;
import llvm_IR.InitInfo;
import llvm_IR.Instr.AllocaInstr;
import llvm_IR.Instr.GEPInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.StoreInstr;
import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.PointerType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.GlobalVar;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

import static frontend.Symbol.SymbolManager.curSymbolId;

//  ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal
public class ConstDef extends Node {
    private ConstSymbol constSymbol;
    // private LLVMType llvmType;

    public ConstDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public boolean judgeIsArray() {
        return children.size() >= 2 && ((TokenNode)children.get(1)).getTokenType() == TokenType.LBRACK;
    }

    // 认为可以直接计算出值 在这一步直接解析ConstInitVal 生成initInfo
    public ConstSymbol createSymbol() {
        String symbolName = ((TokenNode) children.get(0)).getTokenValue();
        TypeInfo typeInfo = new TypeInfo(judgeIsArray(), SymbolManager.getInstance().getDeclaredType());
        return new ConstSymbol(typeInfo, symbolName);
    }

    @Override
    public void checkError() { // b 重复定义
        super.checkError();
        this.constSymbol = createSymbol();
        if (!SymbolManager.getInstance().addSymbol(constSymbol)) { // b error
            Printer.addError(new Error(((TokenNode) children.get(0)).getLino(), 'b'));
        }
    }

    public void setConstSymLlvmType() {
        // 由constSymbol中的TypeInfo信息设置其llvmType
        TypeInfo typeInfo = constSymbol.getTypeInfo();
        LLVMType eleType;
        if (typeInfo.getType() == TypeInfo.typeInfo.INT_TYPE) {
            eleType = IntType.INT32;
        } else { // char
            eleType = IntType.INT8;
        }
        int len;
        if (typeInfo.getIsArray()) { // 数组
            // constExp可以直接算出具体值
            len = children.get(2).calculate();
            constSymbol.setLlvmType(new ArrayType(len, eleType));
        } else {
            constSymbol.setLlvmType(eleType);
        }
    }

    public void setConstInitInfo() {
        // 计算数组长度
        LLVMType type = constSymbol.getLlvmType();
        int len;
        if (type instanceof ArrayType) len = ((ArrayType)type).getLength();
        else len = 0;
        // 常量一定有初始值
        ConstInitVal initVal = (ConstInitVal) children.get(children.size() - 1);
        String str = null;
        if (initVal.isStringConst()) str = initVal.getInitString(len);
        constSymbol.setInitInfo(new InitInfo(constSymbol.getLlvmType(), initVal.getIntList(len), str));
    }

    @Override
    public Value generateIR() {
        // genIR阶段 每当遍历到变量定义的时候 实时更新curSymbolId
        curSymbolId = constSymbol.getSymbolId();
        setConstSymLlvmType();
        setConstInitInfo();
        LLVMType llvmType = constSymbol.getLlvmType();
        InitInfo initInfo = constSymbol.getInitInfo();
        if (constSymbol.isGlobal()) { // 全局常量
            String name = "@" + constSymbol.getSymbolName();
            GlobalVar globalVar = new GlobalVar(name, llvmType, initInfo);
            constSymbol.setLlvmValue(globalVar);
            return null;
        }
        // 非数组局部常量
        if (!constSymbol.getTypeInfo().getIsArray()) {
            Instr alloc = new AllocaInstr(IRManager.getInstance().genVRName(), llvmType);
            constSymbol.setLlvmValue(alloc);
            // 常量一定要store初始值
            Value from = new Constant(initInfo.getInitValues().get(0), llvmType);
            StoreInstr.checkAndGenStoreInstr(from, alloc);
        }
        else {
            Instr alloc = new AllocaInstr(IRManager.getInstance().genVRName(), llvmType);
            constSymbol.setLlvmValue(alloc);
            LLVMType eleType = ((ArrayType) llvmType).getEleType();
            ArrayList<Integer> valueList = initInfo.getInitValues();
            for (int i = 0; i < valueList.size(); i++) {
                Instr arrEleAddr = new GEPInstr(IRManager.getInstance().genVRName(), alloc,
                        new Constant(i, IntType.INT32), eleType);
                Value from = new Constant(valueList.get(i), eleType);
                StoreInstr.checkAndGenStoreInstr(from, arrEleAddr);
            }
        }
        return null;
    }
}
