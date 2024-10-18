package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Symbol.ConstSymbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

//  ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal
public class ConstDef extends Node {
    private ConstSymbol constSymbol;

    public ConstDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public boolean judgeIsArray() {
        return children.size() >= 2 && ((TokenNode)children.get(1)).getTokenType() == TokenType.LBRACK;
    }

    public ConstSymbol createSymbol() {
        String symbolName = ((TokenNode) children.get(0)).getTokenName();
        TypeInfo typeInfo = new TypeInfo(judgeIsArray(), SymbolManager.getInstance().isInt());
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
}
