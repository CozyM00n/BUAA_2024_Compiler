package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Exp.LValExp;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.SymbolManager;
import llvm_IR.IRManager;
import llvm_IR.Instr.InOut.GetIntInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.StoreInstr;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class GetIntStmt extends Stmt{
    //  LVal '=' 'getint''('')'';' // h 不能改变常量的值 行号为 LVal 所在行号
    public GetIntStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        TokenNode identToken = ((LValExp)children.get(0)).getIdentToken(); // 获取identName
        String identName = identToken.getTokenValue();
        if (SymbolManager.getInstance().isConst(identName)) {
            Error error = new Error(identToken.getLino(), 'h');
            Printer.addError(error);
        }
        super.checkError();
    }

    @Override
    public Value generateIR() {
        Value pointer = ((LValExp) children.get(0)).genIRForAssign();
        Instr getIntInstr = new GetIntInstr(IRManager.getInstance().genVRName());
        return StoreInstr.checkAndGenStoreInstr(getIntInstr, pointer);
    }
}
