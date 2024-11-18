package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import frontend.Symbol.VarSymbol;
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

// VarDef → Ident [ '[' ConstExp ']' ] |
//          Ident [ '[' ConstExp ']' ] '=' InitVal // b
public class VarDef extends Node {
    private VarSymbol varSymbol;

    public VarDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public boolean judgeIsArray() {
        return children.size() >= 2 && ((TokenNode)children.get(1)).getTokenType() == TokenType.LBRACK;
    }

    // 语义分析阶段调用 只有最基本的：name + typeInfo
    public VarSymbol createSymbol() {
        String symbolName = ((TokenNode) children.get(0)).getTokenValue();
        TypeInfo typeInfo = new TypeInfo(judgeIsArray(), SymbolManager.getInstance().getDeclaredType());
        return new VarSymbol(typeInfo, symbolName);
    }

    // 中间代码生成调用
    public void setSymLlvmType() {
        TypeInfo typeInfo = varSymbol.getTypeInfo();
        LLVMType eleType; // 判断元素类型
        if (typeInfo.getType() == TypeInfo.typeInfo.INT_TYPE) eleType = IntType.INT32;
        else eleType = IntType.INT8; // char
        int len;
        if (typeInfo.getIsArray()) { // 数组
            len = children.get(2).calculate();
            varSymbol.setLlvmType(new ArrayType(len, eleType));
        } else {
            varSymbol.setLlvmType(eleType);
        }
    }

    // 中间代码生成 只有全局变量调用
    public void setSymbolInitInfo() {
        ArrayList<Integer> initValues = null; String str = null;
        // 仅当var有初始值时，initValues 不为 null
        int len;
        LLVMType type = varSymbol.getLlvmType();
        if (type instanceof ArrayType) len = ((ArrayType) type).getLength();
        else len = 0;
        if (children.get(children.size() - 1) instanceof InitVal) {
            InitVal varInitVal = (InitVal) children.get(children.size() - 1);
            initValues = varInitVal.getIntList(len);
            if (varInitVal.isStringConst()) str = varInitVal.getVarInitString(len);
        }
        varSymbol.setInitInfo(new InitInfo(type, initValues, str));
    }

    @Override
    public void checkError() { // b
        super.checkError();
        this.varSymbol = createSymbol();
        if (!SymbolManager.getInstance().addSymbol(varSymbol)) {
            Printer.addError(new Error(((TokenNode) children.get(0)).getLino(), 'b'));
        }
    }

    @Override
    public Value generateIR() {
        // genIR阶段 每当遍历到变量定义的时候 实时更新curSymbolId
        curSymbolId = varSymbol.getSymbolId();
        setSymLlvmType();
        LLVMType llvmType = varSymbol.getLlvmType();
        if (varSymbol.isGlobal()) { // 全局变量
            setSymbolInitInfo();
            String name = "@" + varSymbol.getSymbolName();
            GlobalVar globalVar = new GlobalVar(name, new PointerType(llvmType), varSymbol.getInitInfo());
            varSymbol.setLlvmValue(globalVar);
        }
        else {
            if (!varSymbol.getTypeInfo().getIsArray()) {
                Instr instr = new AllocaInstr(IRManager.getInstance().genVRName(), llvmType);
                varSymbol.setLlvmValue(instr);
                if (children.get(children.size() - 1) instanceof InitVal) {
                    Value from = ((InitVal) children.get(children.size() - 1)).generateIRList(0, llvmType).get(0);
                    instr = StoreInstr.checkAndGenStoreInstr(from, instr);
                }
            }
            else { // 数组类型
                Instr alloc = new AllocaInstr(IRManager.getInstance().genVRName(), llvmType); // 指向数组的指针
                varSymbol.setLlvmValue(alloc);
                if (children.get(children.size() - 1) instanceof InitVal) {
                    ArrayList<Value> arrInits = ((InitVal) children.get(children.size() - 1))
                            .generateIRList(((ArrayType)llvmType).getLength(), llvmType);
                    for (int i = 0; i < arrInits.size(); i++) {
                        // 这里向GEP传入的是指向整个数组的指针
                        Instr arrEleAddr = new GEPInstr(IRManager.getInstance().genVRName(), alloc,
                                new Constant(i, IntType.INT32), ((ArrayType) llvmType).getEleType());
                        StoreInstr.checkAndGenStoreInstr(arrInits.get(i), arrEleAddr);
                    }
                }
            }
        }
        return null;
    }
}
