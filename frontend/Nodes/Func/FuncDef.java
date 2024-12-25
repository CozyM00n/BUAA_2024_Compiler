package frontend.Nodes.Func;

import Enums.ReturnType;
import Enums.SyntaxVarType;
import frontend.Nodes.Block;
import frontend.Nodes.Node;
import frontend.Nodes.Stmt.ReturnStmt;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.FuncSymbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import llvm_IR.Function;
import llvm_IR.IRManager;
import llvm_IR.Instr.ReturnInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.BasicBlock;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

//  FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // b g j
public class FuncDef extends Node {
    private FuncSymbol funcSymbol;

    public FuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public FuncSymbol createSymbol() {
        ReturnType returnType = ((FuncType)children.get(0)).getFuncRetType();
        return new FuncSymbol(((TokenNode)children.get(1)).getTokenValue(), returnType);
    }

    public void setParaInfo() {
        // 将参数信息存入funcSymbol，以便调用函数时进行checkError检查参数类型是否匹配
        ArrayList<TypeInfo> typeList = new ArrayList<>();
        if (children.get(3) instanceof FuncFParams) {
            typeList.addAll(((FuncFParams) children.get(3)).getFParamsType());
        }
        funcSymbol.setTypeList(typeList);
    }

    @Override
    public void checkError() {
        this.funcSymbol = createSymbol();
        SymbolManager.getInstance().setGlobal(false); // 接下来无全局变量定义
        // b : 名字重定义 报错行号 Ident所在行数
        boolean res = SymbolManager.getInstance().addSymbol(funcSymbol);
        if (!res) {
            Error error = new Error(children.get(1).getEndLine(), 'b');
            Printer.addError(error);
        }
        SymbolManager.getInstance().pushTable();
        SymbolManager.getInstance().setCurFuncSymbol(funcSymbol);
        for (Node child : children) {
            if (child instanceof Block) {
                this.setParaInfo(); // 此时函数参数信息已存入FuncSymbol
            }
            child.checkError(); // 在进入函数体之前将函数参数信息加入FuncSymbol
        }
        SymbolManager.getInstance().popTable();

        // g
        Node BlockNode = children.get(children.size() - 1);
        int BlockChildNum = BlockNode.getChildren().size();
        Node returnStmt = BlockNode.getChildren().get(BlockChildNum - 2); // Block → '{' {   ConstDecl | VarDecl | Stmt } '}'
        if (funcSymbol.getReturnType() != ReturnType.RETURN_VOID &&
                ! (returnStmt instanceof ReturnStmt)) { // 只查返回值不是void的
            Error error = new Error(BlockNode.getChildren().get(BlockChildNum - 1).getEndLine(), 'g'); // }所在行号
            Printer.addError(error);
        }
    }

    public void setFuncSymLLVMValue() {
        LLVMType retType;
        switch (funcSymbol.getReturnType()) {
            case RETURN_INT: retType = IntType.INT32; break;
            case RETURN_CHAR: retType = IntType.INT8; break;
            case RETURN_VOID: retType = VoidType.VOID; break;
            default: retType = null; break;
        }
        Function function = new Function("@f_" + funcSymbol.getSymbolName(), retType);
        funcSymbol.setLlvmValue(function);
    }

    @Override
    public Value generateIR() {
        SymbolManager.getInstance().setCurFuncSymbol(funcSymbol);
        setFuncSymLLVMValue();
        IRManager.getInstance().addAndSetCurFunc(funcSymbol.getLlvmValue());
        // 遍历所有FuncFParam，设置参数的llvmType和llvmValue
        if (children.get(3) instanceof FuncFParams) {
            for (Node node : children.get(3).getChildren()) {
                if (node instanceof FuncFParam) {
                    ((FuncFParam) node).setParamLLVM();
                    ((FuncFParam) node).updateCurSymbolId(); // 形参相当于定义了变量，更新全局符号id
                }
            }
        }
        // 为函数新建基本块
        BasicBlock block = new BasicBlock(IRManager.getInstance().genBlockName());
        IRManager.getInstance().addAndSetCurBlock(block);
        if (children.get(3) instanceof FuncFParams) {
            (children.get(3)).generateIR();
        }
        children.get(children.size() - 1).generateIR(); // Block
        // 如果void没有return，需要补全返回语句
        if (!(IRManager.getInstance().getLastInstr() instanceof ReturnInstr)
                && funcSymbol.getReturnType() == ReturnType.RETURN_VOID) {
            ReturnInstr.checkAndGenRet(null, null);
        }
        // todo 建立新的Block？
        return null;
    }
}
