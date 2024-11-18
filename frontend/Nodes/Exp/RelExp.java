package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import llvm_IR.IRManager;
import llvm_IR.Instr.IcmpInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.ZextInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class RelExp extends Node {
    // RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp }

    public RelExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public Value generateIR() {
        // 如果只有一个addExp，将其转为i32返回
        Value op1 = children.get(0).generateIR();
        if (op1.getLlvmType() != IntType.INT32)
            op1 = new ZextInstr(IRManager.getInstance().genVRName(), op1, IntType.INT32);
        if (children.size() == 1) {
            return op1;
        }
        // 如果有多个返回肯定是i1
        Value res = null;
        for (int i = 1; i < children.size(); i += 2) {
            // 上一次循环得到的res是i1类型，同样需要转为i32
            if (op1.getLlvmType() != IntType.INT32)
                op1 = new ZextInstr(IRManager.getInstance().genVRName(), op1, IntType.INT32);
            // 将后一个addExp先转为i32再大小比较
            Value op2 = children.get(i+1).generateIR();
            if (op2.getLlvmType() != IntType.INT32)
                op2 = new ZextInstr(IRManager.getInstance().genVRName(), op2, IntType.INT32);
            switch (((TokenNode) children.get(i)).getTokenType()) {
                case GRE: res = new IcmpInstr(IRManager.getInstance().genVRName(), IcmpInstr.cmpOp.SGT, op1, op2); break;
                case GEQ: res = new IcmpInstr(IRManager.getInstance().genVRName(), IcmpInstr.cmpOp.SGE, op1, op2); break;
                case LSS: res = new IcmpInstr(IRManager.getInstance().genVRName(), IcmpInstr.cmpOp.SLT, op1, op2); break;
                case LEQ: res = new IcmpInstr(IRManager.getInstance().genVRName(), IcmpInstr.cmpOp.SLE, op1, op2); break;
            }
            op1 = res;
        }
        return op1;
    }
}
