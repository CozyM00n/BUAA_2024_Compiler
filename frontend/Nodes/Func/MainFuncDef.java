package frontend.Nodes.Func;

import Enums.ReturnType;
import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Nodes.Stmt.ReturnStmt;
import frontend.Symbol.FuncSymbol;
import frontend.Symbol.SymbolManager;
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
        this.funcSymbol = createSymbol();
        SymbolManager.getInstance().pushBlock();
        super.checkError();
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
}
