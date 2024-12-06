package BackEnd.Mips;

import BackEnd.Mips.ASM.Asm;
import BackEnd.Mips.ASM.LabelAsm;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MipsManager {
    private static MipsManager mipsManager = new MipsManager();
    public static MipsManager getInstance() {
        return mipsManager;
    }

    private ArrayList<Asm> dataSeg;
    private ArrayList<Asm> textSeg;
    private MipsManager() {
        this.dataSeg = new ArrayList<>();
        this.textSeg = new ArrayList<>();
    }

    public void addToData(Asm asm) {
        dataSeg.add(asm);
    }

    public void addToText(Asm asm) {
        textSeg.add(asm);
    }

    /** stack **/
    private int curStackPos;
    private HashMap<Value, Integer> stackOffsetMap;
    private HashMap<Value, Register> var2Reg;

    public void enterFunction() {
        this.curStackPos = 0;
        this.stackOffsetMap = new HashMap<>();
    }

    public int getCurStackPos() {
        return curStackPos;
    }

    public void pushStackFrame(int offset) {
        curStackPos -= offset;
    }

    public int pushAndRetStackFrame(int size) {
        curStackPos -= size;
        return curStackPos;
    }

    public void addValueToStack(Value value, int offset) {
        // <变量, 变量的地址>
        stackOffsetMap.put(value, offset);
    }

    public Integer getOffsetOfValue(Value value) {
        return stackOffsetMap.get(value);
    }

    public String genMipsCode() {
        StringBuilder sb = new StringBuilder(".data\n\t");
        sb.append(dataSeg.stream().map(Asm::toString).
                collect(Collectors.joining("\n\t")));
        sb.append("\n");
        sb.append(".text\n");
//        for (Asm asm : textSeg) {
//            if (asm instanceof LabelAsm) {
//                sb.append(asm);
//            } else {
//                sb.append(asm+"\n");
//            }
//        }
        sb.append(textSeg.stream().map(Asm::toString)
                .collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
