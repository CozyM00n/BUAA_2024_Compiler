package Optimizer;

import llvm_IR.Module;

public class Optimizer {
    public static void optimize(Module module) {
        DeadCode.deleteDeadCode(module);
    }
}
