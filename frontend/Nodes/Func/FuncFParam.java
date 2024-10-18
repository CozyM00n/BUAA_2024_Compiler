package frontend.Nodes.Func;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.FuncSymbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import frontend.Symbol.VarSymbol;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class FuncFParam extends Node {
    //  FuncFParam → BType Ident ['[' ']'] // b k
    private VarSymbol varSymbol;

    public FuncFParam(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public boolean judgeIsArray() {
        return children.size() >= 3;
    }

    public boolean judgeIsInt() {
        return ((TokenNode)children.get(0)).getTokenType() == TokenType.INTTK;
    }

    public VarSymbol createSymbol() {
        TypeInfo typeInfo = new TypeInfo(judgeIsArray(), judgeIsInt());
        return new VarSymbol(typeInfo, ((TokenNode) children.get(1)).getTokenName());
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
}
