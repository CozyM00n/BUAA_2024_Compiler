package frontend.Nodes.Func;

import Enums.ReturnType;
import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Nodes.Stmt.ReturnStmt;
import frontend.Symbol.FuncSymbol;
import frontend.Symbol.SymbolManager;
import llvm_IR.Function;
import llvm_IR.IRManager;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.BasicBlock;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

// 主函数定义 MainFuncDef → 'int' 'main' '(' ')' Block // g j
//  倒2: Stmt->'return' [Exp] ';' 其中exp只能为0
//  '}'
public class MainFuncDef extends Node {
    private FuncSymbol funcSymbol;
    public MainFuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public FuncSymbol createSymbol() {
        return new FuncSymbol("main", ReturnType.RETURN_INT);
    }

    @Override
    public void checkError() {
        SymbolManager.getInstance().setGlobal(false);
        this.funcSymbol = createSymbol();
        SymbolManager.getInstance().setCurFuncSymbol(funcSymbol);
        SymbolManager.getInstance().pushBlock();
        super.checkError(); // 就是Block的checkError
        SymbolManager.getInstance().popBlock();
        // g  考虑函数末尾是否存在return语句  函数结尾的’}’所在行号
        Node BlockNode = children.get(children.size() - 1);
        int BlockChildNum = BlockNode.getChildren().size();
        Node returnStmt = BlockNode.getChildren().get(BlockChildNum - 2); // 获取Block倒数第2的child
        if (! (returnStmt instanceof ReturnStmt)) {
            Error error = new Error(BlockNode.getChildren().get(BlockChildNum - 1).getEndLine(), 'g');
            Printer.addError(error);
        }
    }

    @Override
    public Value generateIR() {
        SymbolManager.getInstance().setGlobal(false);
        SymbolManager.getInstance().setCurFuncSymbol(funcSymbol);
        Function mainFunc = new Function("@main", IntType.INT32);
        funcSymbol.setLlvmValue(mainFunc);
        IRManager.getInstance().addAndSetCurFunc(mainFunc);
        // 创建新的Block
        BasicBlock block = new BasicBlock(IRManager.getInstance().genBlockName());
        IRManager.getInstance().addAndSetCurBlock(block);
        super.generateIR();
        return null;
    }
}
