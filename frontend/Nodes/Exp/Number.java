package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.TypeInfo;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// 数值 Number → IntConst
public class Number extends Node {

    public Number(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public TypeInfo getTypeInfo() {
        return new TypeInfo(false, TypeInfo.typeInfo.INT_TYPE);
    }

    @Override
    public int calculate() {
        return Integer.parseInt(((TokenNode) children.get(0)).getToken().getValue());
    }

    @Override
    public Value generateIR() {
        int value = Integer.parseInt( ((TokenNode) children.get(0)).getToken().getValue() );
        return new Constant(value, IntType.INT32);
    }
}
