package llvm_IR;

import llvm_IR.llvm_Types.ArrayType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class InitInfo {
    // 这里的特指编译时就能计算出结果（Int）的初始值
    // 包括全局变量 全局常量
    private LLVMType llvmType;
    private ArrayList<Integer> initValues; // 通过InitVal中调用calculate得到
    private String initString;

    public InitInfo(LLVMType llvmType, ArrayList<Integer> initValues, String initString) {
        this.llvmType = llvmType;
        this.initValues = initValues;
        this.initString = initString;
    }

    public LLVMType getLlvmType() {
        return llvmType;
    }

    public ArrayList<Integer> getInitValues() {
        return initValues;
    }

    public boolean isInitialized() {
        return initValues != null;
    }

    @Override
    public String toString() { // i32 2333
        if (!isInitialized()) { // 全局变/常量 应初始化为0
            if (llvmType instanceof IntType)
                return llvmType + " 0";
            else
                return llvmType + " zeroinitializer";
        }
        if (this.initString == null) {
            if (llvmType instanceof IntType) return llvmType + " " + initValues.get(0).toString();
            else { // 数组
                StringBuilder sb = new StringBuilder(llvmType.toString());
                sb.append(" [");
                LLVMType eleType = ((ArrayType) llvmType).getEleType();
                String str = initValues.stream()
                        .map(i -> eleType + " " + i)
                        .collect(Collectors.joining(", ")); // i 是流中的每个元素
                sb.append(str).append("]");
                return sb.toString();
            }
        }
        else { // 字符串常量
            return llvmType.toString() + " c\"" +
                    initString.replace("\0", "\\00") + "\"";
        }
    }
}
