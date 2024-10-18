package frontend.Nodes.Func;

import Enums.ReturnType;
import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Block;
import frontend.Nodes.Node;
import frontend.Nodes.Stmt.ReturnStmt;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.FuncSymbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;
import java.util.SortedMap;

//  FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // b g j
public class FuncDef extends Node {
    private FuncSymbol funcSymbol;

    public FuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public FuncSymbol createSymbol() {
        ReturnType returnType = ((FuncType)children.get(0)).getFuncRetType();
        return new FuncSymbol(((TokenNode)children.get(1)).getTokenName(), returnType);
    }

    public void setParaInfo() {
        ArrayList<TypeInfo> typeList = new ArrayList<>();
        if (children.get(3) instanceof  FuncFParams) {
            typeList.addAll(((FuncFParams) children.get(3)).getFParamsType());
        }
        funcSymbol.setTypeList(typeList);
    }

    @Override
    public void checkError() {
        this.funcSymbol = createSymbol();
        // b : 名字重定义 报错行号 Ident所在行数
        boolean res = SymbolManager.getInstance().addSymbol(funcSymbol);
        if (!res) {
            Error error = new Error(children.get(1).getEndLine(), 'b');
            Printer.addError(error);
        }
        SymbolManager.getInstance().pushBlock();
        SymbolManager.getInstance().setCurFuncSymbol(funcSymbol);
        for (Node child : children) {
            if (child instanceof Block) {
                setParaInfo(); // 此时函数参数信息已存入FuncSymbol
            }
            child.checkError(); // 在进入函数体之前将函数参数信息加入FuncSymbol
        }
        SymbolManager.getInstance().popBlock();

        // g
        Node BlockNode = children.get(children.size() - 1);
        int BlockChildNum = BlockNode.getChildren().size();
        Node returnStmt = BlockNode.getChildren().get(BlockChildNum - 2); // 倒数第2
        if (funcSymbol.getReturnType() != ReturnType.RETURN_VOID &&
                ! (returnStmt instanceof ReturnStmt)) { // 只查返回值不是void的
            Error error = new Error(BlockNode.getChildren().get(BlockChildNum - 1).getEndLine(), 'g');
            Printer.addError(error);
        }
    }
}
