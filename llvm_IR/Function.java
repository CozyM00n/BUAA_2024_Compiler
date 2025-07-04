package llvm_IR;

import BackEnd.Mips.ASM.LabelAsm;
import BackEnd.Mips.MipsManager;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Types.OtherType;
import llvm_IR.llvm_Values.Param;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Function extends User {
    private LLVMType retType;
    private ArrayList<Param> params;
    private LinkedList<BasicBlock> blocks;

    public Function(String name, LLVMType retType) {
        super(name, OtherType.FUNCTION);
        this.retType = retType;
        this.params = new ArrayList<>();
        this.blocks = new LinkedList<>();
    }

    public void addParam(Param param) {
        params.add(param);
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public LLVMType getRetType() {
        return retType;
    }

    public Value getFuncLastInstr() {
        if (blocks.isEmpty()) return null;
        return blocks.get(blocks.size() - 1).getBBLastInstr();
    }

    /** Block **/
    public void addBlock(BasicBlock block) {
        if (blocks.isEmpty()) block.setFirstBlock(true);
        blocks.add(block);
        block.setParentFunc(this);
    }

    public LinkedList<BasicBlock> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() { // define dso_local i32 @sum(i32 %0, i32 %1)
        StringBuilder sb = new StringBuilder("define dso_local ");
        sb.append(retType).append(" ").append(name);
        String paraStr = params.stream()
                .map(Param::toString)
                .collect(Collectors.joining(", "));
        sb.append("(").append(paraStr).append(")").append(" {\n");
        String blockStr = blocks.stream()
                .map(BasicBlock::toString)
                .collect(Collectors.joining("\n"));
        sb.append(blockStr).append("\n}\n");
        return sb.toString();
    }

    @Override
    public void genAsm() {
        // name=@f_+函数名/@main/@putchar
        new LabelAsm(name.substring(1));
        MipsManager.getInstance().enterFunction();
        for (int i = 0; i < params.size(); i++) {
            int offset = MipsManager.getInstance().pushAndRetStackFrame(4);
            // 映射到的这块内存是caller已经复制好参数的那块内存
            MipsManager.getInstance().addValueToStack(params.get(i), offset);
        }
        for (BasicBlock block : blocks) {
            block.genAsm();
        }
    }
}
