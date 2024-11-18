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
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

//  AddExp → MulExp { ('+' | '−') MulExp }
public class AddExp extends Node {
    public AddExp(SyntaxVarType type, ArrayList<Node> children) {
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
            if (!(children.get(i) instanceof TokenNode))
                System.out.println("CG Error: Calculate at AddExp");
            if (((TokenNode)children.get(i)).getTokenType() == TokenType.PLUS) {
                i++;
                res += children.get(i).calculate();
            } else { // minus
                i++;
                res -= children.get(i).calculate();
            }
        }
        return res;
    }

    @Override
    public Value generateIR() {
        // 如果涉及binInstr返回的一定是i32，如果只有单个就返回原类型(i32/8)
        Value op1 = children.get(0).generateIR();
        Value op2;
        Instr instr;
        for (int i = 1; i < children.size(); i++) {
            if (((TokenNode)children.get(i)).getTokenType() == TokenType.PLUS) {
                op2 = children.get(++i).generateIR();
                // String name = IRManager.getInstance().genVirRegName();
                instr = BinaryInstr.checkAndGenBinInstr(IntType.INT32, BinaryInstr.op.ADD, op1, op2);
                op1 = instr;
            } else {
                op2 = children.get(++i).generateIR();
                // String name = IRManager.getInstance().genVirRegName();
                instr = BinaryInstr.checkAndGenBinInstr(IntType.INT32, BinaryInstr.op.SUB, op1, op2);
                op1 = instr;
            }
        }
        return op1;
    }
}
