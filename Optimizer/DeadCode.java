package Optimizer;

import llvm_IR.BasicBlock;
import llvm_IR.Function;
import llvm_IR.Instr.BranchInstr;
import llvm_IR.Instr.Instr;
import llvm_IR.Instr.JumpInstr;
import llvm_IR.Instr.ReturnInstr;
import llvm_IR.Module;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class DeadCode {
    public static void deleteDeadCode(Module module) {
        // 删除多余的jump语句
        for (Function func : module.getFuncList()) {
            for (BasicBlock block : func.getBlocks()) {
                deleteDeadInstr(block);
            }
        }
        for (Function func : module.getFuncList()) {
            deleteDeadBlock(func);
        }
    }

    private static void deleteDeadInstr(BasicBlock block) {
        boolean isAfterBr = false;
        Iterator<Instr> iter = block.getInstrs().iterator();
        while (iter.hasNext()) {
            Instr instr = iter.next();
            if (isAfterBr) iter.remove();
            else if (instr instanceof JumpInstr || instr instanceof BranchInstr
                    || instr instanceof ReturnInstr) {
                isAfterBr = true;
            }
        }
    }

    private static void deleteDeadBlock(Function func) {
        BasicBlock entry = func.getBlocks().get(0);
        HashSet<BasicBlock> vis = new HashSet<>();
        dfs(entry, vis);
        Iterator<BasicBlock> iter = func.getBlocks().iterator();
        while (iter.hasNext()) {
            BasicBlock block = iter.next();
            if (!vis.contains(block)) {
                iter.remove();
                block.setRemoved(true);
            }
        }
    }

    private static void dfs(BasicBlock entry, HashSet<BasicBlock> vis) {
        vis.add(entry);
        Instr instr = entry.getBBLastInstr();
        // 如果是retInstr，则该函数结束，无法再往前visit了
        if (instr instanceof  JumpInstr) {
            BasicBlock toBlock = ((JumpInstr) instr).getToBlock();
            if (!vis.contains(toBlock))
                dfs(toBlock, vis);
        } else if (instr instanceof BranchInstr) {
            BasicBlock block1 = ((BranchInstr) instr).getTrueBlock();
            BasicBlock block2 = ((BranchInstr) instr).getFalseBlock();
            if (!vis.contains(block1)) dfs(block1, vis);
            if (!vis.contains(block2)) dfs(block2, vis);
        }
    }
}
