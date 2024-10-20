package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Lexer.Token;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
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
        String identName = ((TokenNode)children.get(0)).getTokenName();
        if (SymbolManager.getInstance().getSymbol(identName) == null) {
            Error error = new Error(((TokenNode) children.get(0)).getLino(), 'c');
            Printer.addError(error);
        }
        super.checkError();
    }

    @Override
    public TypeInfo getTypeInfo() {
        String name = ((TokenNode)children.get(0)).getTokenName();
        Symbol symbol = SymbolManager.getInstance().getSymbol(name);
        if (symbol == null) return null;
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
}
