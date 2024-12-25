package frontend.Symbol;

import java.util.HashMap;
import java.util.Stack;

public class SymbolManager {
    private static final SymbolManager MANAGER = new SymbolManager();
    public static SymbolManager getInstance() {
        return MANAGER;
    }

    private final Stack<SymbolTable> symbolTableStack;
    private final HashMap<Integer, SymbolTable> symbolMap;
    public static int curSymbolId;
    private static int curTableId = -1; // 全局Block的父亲是-1

    private FuncSymbol curFuncSymbol;
    private boolean isGlobal;
    private TypeInfo.typeInfo declaredType;
    private int loopDepth;

    private SymbolManager() {
        this.symbolTableStack = new Stack<>();
        this.curFuncSymbol = null;
        this.isGlobal = true;
        this.loopDepth = 0;
        this.symbolMap = new HashMap<>();
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
        // genIR阶段用于LVal 需要满足查表查到的符号id <= curSymbolId 否则查表查到的就是还未定义的名为name的符号
        int id = curTableId;
        while (id != -1) {
            SymbolTable table = symbolMap.get(id);
            if (table.getSymbol(name) != null && table.getSymbol(name).getSymbolId() <= curSymbolId) {
                return table.getSymbol(name);
            }
            id = table.getFatherId();
        }
        System.out.println("getSymbolForIR : symbol :【" + name + "】not found!");
        return null;
    }

    public static void setCurSymbolId(int id) {
        curSymbolId = id;
    }
    public static int getCurSymbolId() {
        return curSymbolId;
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

    /*** Symbol Table ***/
    public void pushTable() {
        // for checkError 建表阶段,新建一个符号表
        SymbolTable symbolTable = new SymbolTable(curTableId); // 传入参数是：父亲符号表的id
        curTableId = symbolTable.getTableId();
        symbolMap.put(curTableId, symbolTable);
        symbolTableStack.push(symbolTable);
    }

    public void popTable() {
        SymbolTable topTable = symbolTableStack.pop();
        curTableId = topTable.getFatherId();
    }

    public static void setCurTableId(int id) {
        curTableId = id;
    }
    public static int getCurTableId() {
        return curTableId;
    }

    public int getFatherTableId(int id) {
        // genIR阶段：用于退出当前符号表，设置返回上一级符号表
        return symbolMap.get(id).getFatherId();
    }

    public HashMap<Integer, SymbolTable> getSymbolMap() {
        // for printer 语义分析阶段
        return symbolMap;
    }

    /*** variable info***/
    public boolean isConst(String identName) { // 在checkError使用
        // for error h,不能修改常量的值  LVal '=' xxx
        Symbol symbol = getSymbol(identName);
        return  (symbol instanceof ConstSymbol); // 若为null也返回false
    }

    public TypeInfo.typeInfo getDeclaredType() {
        // 语义分析阶段：定义变量时记录declare的类型，以便createSymbol时使用
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
    // for checkErr && genIR 每到函数定义都要在此更新FuncSymbol，以便之后获得返回值类型
    public void setCurFuncSymbol(FuncSymbol curFuncSymbol) {
        this.curFuncSymbol = curFuncSymbol;
    }
    // for ReturnStmt in ce and gc
    public FuncSymbol getCurFuncSymbol() {
        return curFuncSymbol;
    }
}
