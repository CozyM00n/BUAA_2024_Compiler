package frontend.Nodes.Func;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.TypeInfo;

import java.util.ArrayList;

public class FuncFParams extends Node {
    // 函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
    public FuncFParams(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public ArrayList<TypeInfo> getFParamsType() { // for checkErr
        ArrayList<TypeInfo> typeList = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof FuncFParam) {
                typeList.add(((FuncFParam) child).getVarSymbol().getTypeInfo());
            }
        }
        return typeList;
    }
}
