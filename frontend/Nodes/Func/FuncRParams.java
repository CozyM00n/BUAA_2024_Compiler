package frontend.Nodes.Func;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.Exp;
import frontend.Nodes.Node;
import frontend.Symbol.TypeInfo;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class FuncRParams extends Node {
    // FuncRParams → Exp { ',' Exp }
    public FuncRParams(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public ArrayList<TypeInfo> getRParamsType() {
        ArrayList<TypeInfo> typeList = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof Exp) {
                typeList.add(child.getTypeInfo());
            }
        }
        return typeList;
    }

    public ArrayList<Value> genIRList() {
        ArrayList<Value> rParams = new ArrayList<>();
        for (Node child: children) {
            if (child instanceof Exp)
                rParams.add(child.generateIR());
        }
        return rParams;
    }
}
