package frontend.Nodes;

import Enums.SyntaxVarType;
import frontend.Symbol.SymbolManager;
import llvm_IR.IRManager;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

import static frontend.Symbol.SymbolManager.curTableId;
// Block → '{' { BlockItem } '}'
// BlockItem → ConstDecl | VarDecl | Stmt

public class Block extends Node {

    public Block(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    private int symbolTableId;

    @Override
    public void checkError() {
        this.symbolTableId = curTableId;
        super.checkError();
    }

    @Override
    public Value generateIR() {
        curTableId = symbolTableId;
        super.generateIR();
        // 退出block 更新curTableId
        curTableId = SymbolManager.getInstance().getFatherTableId(curTableId);
        return null;
    }
}
