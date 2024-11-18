package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import llvm_IR.IRManager;
import llvm_IR.Instr.IcmpInstr;
import llvm_IR.Instr.ZextInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class EqExp extends Node {
    // 相等性表达式 EqExp → RelExp { '==' | '!=' RelExp }

    public EqExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public Value generateIR() { // RelExp可能是i1 / i32
        Value op1 = children.get(0).generateIR();
        // 如果只有一个RelExp 依旧按照i32并返回
        if (children.size() == 1) {
//            if (op1.getLlvmType() != IntType.INT1) {
//                op1 = new IcmpInstr(IRManager.getInstance().genVRName(), IcmpInstr.cmpOp.NE,
//                        op1, new Constant(0, op1.getLlvmType()));
//            }
            return op1;
        }
        Value res = null;
        for (int i = 1; i < children.size(); i += 2) {
            if (op1.getLlvmType() != IntType.INT32)
                op1 = new ZextInstr(IRManager.getInstance().genVRName(), op1, IntType.INT32);
            // 获取op2 并转为i32格式
            Value op2 = children.get(i + 1).generateIR();
            if (op2.getLlvmType() != IntType.INT32)
                op2 = new ZextInstr(IRManager.getInstance().genVRName(), op2, IntType.INT32);
            // 计算op1 == op2 的结果 并存入op1(一定是i1)
            if (((TokenNode) children.get(i)).getTokenType() == TokenType.EQL)
                res = new IcmpInstr(IRManager.getInstance().genVRName(), IcmpInstr.cmpOp.EQ, op1, op2);
            else
                res = new IcmpInstr(IRManager.getInstance().genVRName(), IcmpInstr.cmpOp.NE, op1, op2);
            op1 = res;
        }
        return op1;
    }
}
