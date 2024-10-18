package frontend.Symbol;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Symbol> symbolMap;
    private ArrayList<String> symbolNameList;
    private int tableNum;
    // private HashSet<Symbol> symbols;

    public SymbolTable(int tableNum) {
        this.symbolMap = new HashMap<>();
        this.symbolNameList = new ArrayList<>();
        this.tableNum = tableNum;
    }

    public boolean addSymbol(Symbol symbol) {
        if (symbolMap.containsKey(symbol.getSymbolName())) {
            return false;
        }
        symbolMap.put(symbol.getSymbolName(), symbol);
        symbolNameList.add(symbol.getSymbolName());
        return true;
    }

    public Symbol getSymbol(String identName) {
        return symbolMap.getOrDefault(identName, null);
    }

    public ArrayList<String> getsymbolNameList() {
        return symbolNameList;
    }

    public int getTableNum() {
        return tableNum;
    }

    public ArrayList<String> toStrings() {
        ArrayList<String> strings = new ArrayList<>();
        for (String name : symbolNameList) {
            strings.add(tableNum + " " + symbolMap.get(name).toString());
        }
        return strings;
    }
}
