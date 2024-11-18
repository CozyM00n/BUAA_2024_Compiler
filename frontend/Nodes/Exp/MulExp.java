package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.TypeInfo;
import llvm_IR.IRManager;
import llvm_IR.Instr.BinaryInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
// 实际上语法结构是：UnaryExp ('*' | '/' | '%') UnaryExp
public class MulExp extends Node {

    public MulExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public TypeInfo getTypeInfo() {
        for (Node child : children) {
            if (child.getTypeInfo() != null) {
                return child.getTypeInfo();
            }
        }
        return null;
    }

    @Override
    public int calculate() {
        int res = children.get(0).calculate();
        for (int i = 1; i < children.size(); i++) {
            System.out.println(((TokenNode)children.get(i)).getLino());
            switch (((TokenNode)children.get(i)).getTokenType()) {
                case MULT: res *= children.get(++i).calculate(); break;
                case DIV:  res /= children.get(++i).calculate(); break;
                case MOD:  res %= children.get(++i).calculate(); break;
            }
        }
        return res;
    }

    @Override
    public Value generateIR() {
        Value op1 = children.get(0).generateIR();
        Value op2;
        Instr instr;
        for (int i = 1; i < children.size(); i++) {
            BinaryInstr.op op = null;
            switch (((TokenNode)children.get(i)).getTokenType()) {
                case MULT: op = BinaryInstr.op.MUL; break;
                case DIV:  op = BinaryInstr.op.SDIV; break;
                case MOD:  op = BinaryInstr.op.SREM; break;
            }
            op2 = children.get(++i).generateIR();
            instr = BinaryInstr.checkAndGenBinInstr(IntType.INT32, op, op1, op2);
            op1 = instr;
        }
        return op1;
    }
}
