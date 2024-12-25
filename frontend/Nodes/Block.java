package frontend.Nodes;

import Enums.SyntaxVarType;
import frontend.Symbol.SymbolManager;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

// Block → '{' { BlockItem } '}'
// BlockItem → ConstDecl | VarDecl | Stmt

public class Block extends Node {

    public Block(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    private int symbolTableId;

    @Override
    public void checkError() {
        // 此前已经完成了pushBlock的工作
        this.symbolTableId = SymbolManager.getCurTableId();
        super.checkError();
    }

    @Override
    public Value generateIR() {
        SymbolManager.setCurTableId(symbolTableId);
        super.generateIR();
        // 退出block 更新curTableId
        SymbolManager.setCurTableId(SymbolManager.getInstance().getFatherTableId(symbolTableId));
        return null;
    }
}
