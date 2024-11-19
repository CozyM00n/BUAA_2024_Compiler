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
        ch = ch.substring(1, ch.length() - 1);
        if (ch.length() == 2) {
            switch (ch.charAt(1)) {
                case 'a': value = 7; break;   // \a
                case 'b': value = 8; break;   // \b
                case 't': value = 9; break;   // \t
                case 'n': value = 10; break;  // \n
                case 'v': value = 11; break;  // \v
                case 'f': value = 12; break;  // \f
                case 'r': value = 13; break;  // \r
                case '0': value = 0; break;   // \0
                case '\\': value = 92; break; // \\
                case '\'': value = 39; break; // \'
                case '"': value = 34; break;  // \"
            }
        } else {
            value = ch.charAt(0);
        }
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
