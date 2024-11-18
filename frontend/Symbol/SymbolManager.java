package frontend.Symbol;

import java.util.HashMap;
import java.util.Stack;

public class SymbolManager {
    private static final SymbolManager MANAGER = new SymbolManager();
    private Stack<SymbolTable> symbolTableStack;
    private FuncSymbol curFuncSymbol;
    private boolean isGlobal;
    private TypeInfo.typeInfo declaredType;
    private int loopDepth;
    private HashMap<Integer, SymbolTable> symbolMap;
    public static int curSymbolId;
    public static int curTableId = -1; // 全局Block的父亲是-1

    private SymbolManager() {
        this.symbolTableStack = new Stack<>();
        this.curFuncSymbol = null;
        this.isGlobal = true;
        this.loopDepth = 0;
        this.symbolMap = new HashMap<>();
    }
    public static SymbolManager getInstance() {
        return MANAGER;
    }

    /*** symbol ***/
    public boolean addSymbol(Symbol symbol) {
        SymbolTable curTable = this.symbolTableStack.peek();
        return curTable.addSymbol(symbol); // 如果成功定义变量，当前表的curSymbolId++
    }

    public Symbol getSymbol (String identName) {
        for (int i = symbolTableStack.size() - 1; i >= 0; i--) {
            if (symbolTableStack.get(i).getSymbol(identName) != null) {
                return symbolTableStack.get(i).getSymbol(identName);
            }
        }
        return null;
    }

    public Symbol getSymbolForIR(String name) {
        // genIR阶段用于LVal 需要满足查表查到的符号id < curSymbolId 否则查表查到的就是还未定义的名为name的符号
        int id = curTableId;
        while (id != -1) {
            SymbolTable table = symbolMap.get(id);
            if (table.getSymbol(name) != null && table.getSymbol(name).getSymbolId() <= curSymbolId) {
                return table.getSymbol(name);
            }
            id = table.getFatherId();
        }
        System.out.println("getSymbolForIR : symbol : " + name + "not found!");
        return null;
    }

    /*** Block ***/
    public void pushBlock() {
        SymbolTable symbolTable = new SymbolTable(curTableId);
        curTableId = symbolTable.getTableId();
        symbolMap.put(curTableId, symbolTable);
        symbolTableStack.push(symbolTable);
    }

    public void popBlock() {
        SymbolTable topTable = symbolTableStack.pop();
        curTableId = topTable.getFatherId();
    }

    /*** loop ***/
    public void enterLoop() {
        loopDepth++;
    }

    public void leaveLoop() {
        loopDepth--;
    }

    public int getLoopDepth() {
        return loopDepth;
    }

    /***Symbol Map ***/
    public int getFatherTableId(int id) {
        return symbolMap.get(id).getFatherId();
    }

    public HashMap<Integer, SymbolTable> getSymbolMap() {
        return symbolMap;
    }

    /*** variable info***/
    public boolean isConst(String identName) { // 只能在checkError使用
        Symbol symbol = getSymbol(identName);
        return  (symbol instanceof ConstSymbol); // 若为null也返回false
    }

    public TypeInfo.typeInfo getDeclaredType() {
        // 定义变量时记录declare的类型
        return declaredType;
    }

    public void setDeclaredType(TypeInfo.typeInfo declaredType) {
        this.declaredType = declaredType;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    /*** function ***/
    public void setCurFuncSymbol(FuncSymbol curFuncSymbol) {
        this.curFuncSymbol = curFuncSymbol;
    }

    public FuncSymbol getCurFuncSymbol() {
        return curFuncSymbol;
    }
}
