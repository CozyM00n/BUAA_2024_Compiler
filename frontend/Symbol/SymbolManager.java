package frontend.Symbol;

import utils.Printer;

import java.util.Stack;

public class SymbolManager {
    private static SymbolManager MANAGER = new SymbolManager();
    private Stack<SymbolTable> symbolTableStack;
    private FuncSymbol curFuncSymbol;
    private boolean isGlobal;
    private boolean isInt;
    private int nextBlockNum;
    private int loopDepth;

    private SymbolManager() {
        this.symbolTableStack = new Stack<>();
        this.curFuncSymbol = null;
        this.isGlobal = true;
        this.nextBlockNum = 1;
        this.loopDepth = 0;
    }

    public static SymbolManager getInstance() {
        return MANAGER;
    }

    public void pushBlock() {
        SymbolTable symbolTable = new SymbolTable(nextBlockNum);
        nextBlockNum++;
        symbolTableStack.push(symbolTable);
    }

    public void popBlock() {
        SymbolTable topTable = symbolTableStack.pop();
        Printer.addOutFileInfo(topTable.getTableNum(), topTable.toStrings());
    }

    public void enterLoop() {
        loopDepth++;
    }

    public void leaveLoop() {
        loopDepth--;
    }

    public boolean addSymbol(Symbol symbol) {
        SymbolTable curTable = this.symbolTableStack.peek();
        return curTable.addSymbol(symbol);
    }

    public Symbol getSymbol (String identName) {
        for (int i = symbolTableStack.size() - 1; i >= 0; i--) {
            if (symbolTableStack.get(i).getSymbol(identName) != null) {
                return symbolTableStack.get(i).getSymbol(identName);
            }
        }
        return null;
    }

    public boolean isConst(String identName) {
        Symbol symbol = getSymbol(identName);
        return  (symbol instanceof ConstSymbol); // 若为null也返回false
    }

    public void setIsInt(boolean isInt) {
        this.isInt = isInt;
    }

    public boolean isInt() {
        return isInt;
    }

    public int getLoopDepth() {
        return loopDepth;
    }

    public void setCurFuncSymbol(FuncSymbol curFuncSymbol) {
        this.curFuncSymbol = curFuncSymbol;
    }

    public FuncSymbol getCurFuncSymbol() {
        return curFuncSymbol;
    }
}
