package BackEnd.Mips.ASM.DataAsm;

import llvm_IR.InitInfo;
import llvm_IR.llvm_Types.ArrayType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Word extends DataAsm {
    // global_a: .word 1, 2, 3, 4, 5,
    private String name;
    private InitInfo initInfo;

    public Word(String name, InitInfo initInfo) {
        // 第一行实际隐式插入调用了父类无参构造方法 super()
        this.name = name;
        this.initInfo = initInfo;
    }

    @Override
    public String toString() {
        String str;
        if (initInfo.isInitialized()) {
            str = initInfo.getInitValues().stream().map(String::valueOf) // 将每个Integer转换为String
                    .collect(Collectors.joining(", "));
        } else { // 未初始化的都默认是0
            if (initInfo.getLlvmType() instanceof ArrayType) {
                str = ((ArrayType) initInfo.getLlvmType()).getLength() + ": 0";
            } else {
                str = "0";
            }
        }
        return name + ": .word " + str;
    }
}
