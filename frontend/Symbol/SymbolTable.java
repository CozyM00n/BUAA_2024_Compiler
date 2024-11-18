package frontend.Symbol;

import java.util.ArrayList;
import java.util.HashMap;

import static frontend.Symbol.Symbol.SYMBOL_ID;

public class SymbolTable {
    static int TABLE_ID = 1; // 每个符号表对应一个唯一id 按照出现顺序递增
    private int tableId;
    private HashMap<String, Symbol> symbolMap;
    private ArrayList<String> symbolNameList; // 记录每个symbol在该表中的出场顺序
    private int fatherId;

    public SymbolTable(int fatherId) {
        this.symbolMap = new HashMap<>();
        this.symbolNameList = new ArrayList<>();
        this.tableId = TABLE_ID++;
        this.fatherId = fatherId;
    }

    public boolean addSymbol(Symbol symbol) {
        if (symbolMap.containsKey(symbol.getSymbolName())) {
            return false;
        }
        if (!(symbol instanceof FuncSymbol)) symbol.setSymbolId(SYMBOL_ID++);
        symbolMap.put(symbol.getSymbolName(), symbol);
        symbolNameList.add(symbol.getSymbolName());
        return true;
    }

    public Symbol getSymbol(String identName) {
        return symbolMap.getOrDefault(identName, null);
    }

    public int getTableId() {
        return tableId;
    }

    public int getFatherId() {
        return fatherId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String name : symbolNameList) {
            sb.append(tableId + " " + symbolMap.get(name).toString() + "\n");
        }
        return sb.toString();
    }
}
