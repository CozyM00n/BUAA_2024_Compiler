package llvm_IR;

import llvm_IR.llvm_Types.OtherType;
import llvm_IR.llvm_Values.GlobalVar;
import llvm_IR.llvm_Values.StringLiteral;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class Module extends Value {
    private ArrayList<String> declareFuncList;
    private final ArrayList<StringLiteral> stringLiteralList;
    private ArrayList<GlobalVar> globalVarList;
    private ArrayList<Function> funcList;

    private Module() {
        super("module", OtherType.MODULE);
        this.declareFuncList = new ArrayList<>();
        declareFuncList.add("declare i32 @getint()\n" +
                "declare i32 @getchar()\n" +
                "declare void @putint(i32)\n" +
                "declare void @putch(i32)\n" +
                "declare void @putstr(i8*)\n");
        this.stringLiteralList = new ArrayList<>();
        this.globalVarList = new ArrayList<>();
        this.funcList = new ArrayList<>();
    }

    private static final Module MODULE = new Module();

    public static Module getInstance() {
        return MODULE;
    }

    public void addStringLiteral(StringLiteral stringLiteral) {
        stringLiteralList.add(stringLiteral);
    }

    public void addGlobalVar(GlobalVar globalVar) {
        globalVarList.add(globalVar);
    }

    public void addFunction(Function func) {
        funcList.add(func);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String dec : declareFuncList) {
            sb.append(dec);
        }
        if (!declareFuncList.isEmpty()) sb.append("\n");
        for (StringLiteral str : stringLiteralList)
            sb.append(str).append("\n");
        if (!stringLiteralList.isEmpty()) sb.append("\n");

        for (GlobalVar gv : globalVarList) {
            sb.append(gv.toString()).append("\n");
        }
        if (!globalVarList.isEmpty()) sb.append("\n");

        for (Function func : funcList)
            sb.append(func.toString()).append("\n");
        return sb.toString();
    }
}
