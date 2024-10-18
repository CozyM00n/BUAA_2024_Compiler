package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Symbol.ConstSymbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import frontend.Symbol.VarSymbol;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

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

    public VarSymbol createSymbol() {
        String symbolName = ((TokenNode) children.get(0)).getTokenName();
        TypeInfo typeInfo = new TypeInfo(judgeIsArray(), SymbolManager.getInstance().isInt());
//        if (((TokenNode)children.get(0)).getTokenName().equals("a5")) {
//            System.out.println(judgeIsArray());
//        }
        return new VarSymbol(typeInfo, symbolName);
    }

    @Override
    public void checkError() { // b
        super.checkError();
        this.varSymbol = createSymbol();
        if (!SymbolManager.getInstance().addSymbol(varSymbol)) {
            Printer.addError(new Error(((TokenNode) children.get(0)).getLino(), 'b'));
        }
    }
}
