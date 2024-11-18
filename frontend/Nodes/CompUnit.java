package frontend.Nodes;

import Enums.SyntaxVarType;
import frontend.Symbol.SymbolManager;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

//  CompUnit → { ConstDecl | VarDecl } {FuncDef} MainFuncDef
public class CompUnit extends Node {
    public CompUnit(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        SymbolManager.getInstance().pushBlock();
        SymbolManager.getInstance().setGlobal(true);
        super.checkError();
        SymbolManager.getInstance().popBlock();
    }

    @Override
    public Value generateIR() {
        SymbolManager.getInstance().setGlobal(true); // 前面checkError结束后Global为false
        super.generateIR();
        return null;
    }
}
