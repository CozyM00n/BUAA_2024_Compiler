package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.TypeInfo;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// 字符 Character → CharConst
public class Char extends Node {
    private int value;

    public Char(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        String ch = ((TokenNode) children.get(0)).getToken().getValue();
        value = ch.charAt(1); // 跳过"
    }

    @Override
    public TypeInfo getTypeInfo() {
        return new TypeInfo(false, TypeInfo.typeInfo.CHAR_TYPE);
    }

    @Override
    public int calculate() {
        return value;
    }

    @Override
    public Value generateIR() {
        return new Constant(value, IntType.INT8);
    }
}
