package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Exp.LValExp;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import llvm_IR.Instr.StoreInstr;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

// ForStmt => LVal '=' Exp // h
// BType VarDef
public class ForStmt extends Node {
    public ForStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public TypeInfo.typeInfo judgeType() {
        if (((TokenNode)children.get(0)).getTokenType() == TokenType.INTTK) {
            return TypeInfo.typeInfo.INT_TYPE;
        } else {
            return TypeInfo.typeInfo.CHAR_TYPE;
        }
    }

    public void checkError() {
        if (children.get(0) instanceof LValExp) {
            super.checkError();
            TokenNode identToken = ((LValExp)children.get(0)).getIdentToken(); // 获取identName
            String identName = identToken.getTokenValue();
            if (SymbolManager.getInstance().isConst(identName)) {
                Error error = new Error(identToken.getLino(), 'h');
                Printer.addError(error);
            }
        } else {
            SymbolManager.getInstance().setDeclaredType(judgeType());
            super.checkError();
        }
    }

    @Override
    public Value generateIR() {
        if (children.get(0) instanceof LValExp) {
            Value lVal = ((LValExp) children.get(0)).genIRForAssign();
            Value expVal = children.get(2).generateIR();
            StoreInstr.checkAndGenStoreInstr(expVal, lVal);
        } else {
            // BType VarDef
            super.generateIR();
        }
        return null;
    }
}
