package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Func.FuncRParams;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.FuncSymbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class UnaryExp extends Node {
    // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // c d e
    public UnaryExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public ArrayList<TypeInfo> getRParamsList() {
        if (children.size() >= 3 && children.get(2) instanceof FuncRParams) {
            return ((FuncRParams) children.get(2)).getRParamsType();
        }
        return new ArrayList<>(); // 没有参数
    }

    @Override
    public void checkError() {
        if (children.get(0) instanceof TokenNode) { // function Call
            // c 使用了未定义的标识符报错行号为Ident 所在行数
            TokenNode identToken = ((TokenNode) children.get(0));
            FuncSymbol symbol = (FuncSymbol) SymbolManager.getInstance().getSymbol(identToken.getTokenName());
            if (symbol == null) {
                Error error = new Error(((TokenNode) children.get(0)).getLino(), 'c');
                Printer.addError(error);
            } else {
                // d 参数个数不匹配，函数名所在行数
                ArrayList<TypeInfo> fParamsType = symbol.getTypeList();
                ArrayList<TypeInfo> rParamsType = getRParamsList();
                if (fParamsType.size() != rParamsType.size()) {
                    Error error = new Error(identToken.getLino(), 'd');
                    Printer.addError(error);
                }
                // e 函数参数类型不匹配 函数名所在行数
                for (int i = 0; i < rParamsType.size() && i < fParamsType.size(); i++) {
                    if (! rParamsType.get(i).equals(fParamsType.get(i))) {
                        Error error = new Error(identToken.getLino(), 'e');
                        Printer.addError(error);
                        return; // 只需打印一次
                    }
                }
            }
        }
        super.checkError();
    }

    @Override
    public TypeInfo getTypeInfo() {
        if (children.get(0) instanceof TokenNode) {
            String identName = ((TokenNode) children.get(0)).getTokenName();
            return (SymbolManager.getInstance().getSymbol(identName)).getTypeInfo();
        }
        for (Node child : children) {
            if (child.getTypeInfo() != null) {
                return child.getTypeInfo();
            }
        }
        return null;
    }
}
