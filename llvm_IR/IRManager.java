package llvm_IR;

import llvm_IR.Instr.Instr;
import llvm_IR.llvm_Values.*;

import java.util.HashMap;
import java.util.Stack;

public class IRManager {
    private Function curFunc;
    private BasicBlock curBlock;
    private HashMap<Function, Integer> localVarMap;
    private int strLiteralNum;
    private Stack<ForLoop> forLoops;
    private int BasicBlockNum;
    public static boolean AUTO_ADD_INSTR = true;

    private IRManager() {
        this.localVarMap = new HashMap<>();
        this.curFunc = null;
        this.curBlock = null;
        this.strLiteralNum = 0;
        this.forLoops = new Stack<>();
        this.BasicBlockNum = 0;
    }

    private static final IRManager irManager = new IRManager();
    public static IRManager getInstance() {
        return irManager;
    }

    /*** Function ***/
    public void addAndSetCurFunc(Function func) {
        this.curFunc = func;
        localVarMap.put(func, 0);
        addFunction(func);
    }

    public void addParam(Param param) {
        curFunc.addParam(param);
    }

    public Value getLastInstr() {
        return curFunc.getFuncLastInstr();
    }

    /**block**/
    public void addBlock(BasicBlock block) { // 同时设置parent
        curFunc.addBlock(block);
    }

    public void addAndSetCurBlock(BasicBlock block) {
        this.curBlock = block;
        addBlock(block);
    }

    /** Instr**/
    public void addInstr(Instr instr) { // 同时设置parent
        curBlock.addInstr(instr);
    }

    /*** add item for Module ***/
    public void addStringLiteral(StringLiteral stringLiteral) {
        Module.getInstance().addStringLiteral(stringLiteral);
    }

    public void addGlobalVar(GlobalVar globalVar) {
        Module.getInstance().addGlobalVar(globalVar);
    }

    public void addFunction(Function func) {
        Module.getInstance().addFunction(func);
    }

    /*** Generate Name ***/
    public String genStrName() {
        String name;
        if (strLiteralNum == 0) name = "@.str";
        else name = "@.str." + strLiteralNum;
        strLiteralNum++;
        return name;
    }

    public String genVRName() {
        int num = localVarMap.get(curFunc);
        localVarMap.put(curFunc, num + 1);
        return "%v" + num;
    }

    public String genBlockName() {
//        int num = localVarMap.get(curFunc);
//        localVarMap.put(curFunc, num + 1);
        return "L" + (++BasicBlockNum);
    }

    /***loop***/
    public void pushLoop(ForLoop loop) {
        forLoops.push(loop);
    }

    public ForLoop getCurLoop() {
        return forLoops.peek();
    }

    public void popLoop() {
        forLoops.pop();
    }
}
