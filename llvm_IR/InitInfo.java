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
            StringBuilder sb = getSb();
            return llvmType.toString() + " c\"" + sb + "\"";
        }
    }

    private StringBuilder getSb() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < initString.length(); i++) {
            // 注意： 10个转义符中'按照原样输出
            switch (initString.charAt(i)) {
                case 7 : sb.append("\\07"); break;
                case 8 : sb.append("\\08"); break;
                case 9 : sb.append("\\09"); break;
                case 10 : sb.append("\\0A"); break;
                case 11 : sb.append("\\0B"); break;
                case 12 : sb.append("\\0C"); break;
                case '\"' : sb.append("\\22"); break;
                case '\\' : sb.append("\\\\"); break; // 输出2个反斜杠\\
                case '\0' : sb.append("\\00"); break;
                default: sb.append(initString.charAt(i));
            }
        }
        return sb;
    }
}
