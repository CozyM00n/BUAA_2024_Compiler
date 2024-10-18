package frontend.Symbol;

public class TypeInfo {
    private boolean isArray;
    private boolean isInt;

    public TypeInfo(boolean isArray, boolean isInt) {
        this.isArray = isArray;
        this.isInt = isInt;
    }

    public void setIsArray(boolean flg) {
        isArray = flg;
    }

    public void setIsInt(boolean flg) {
        isInt = flg;
    }

    public boolean getIsInt() {
        return isInt;
    }

    public boolean getIsArray() {
        return isArray;
    }

    @Override
    public String toString() {
        return (isInt ? "Int" : "Char") + ((isArray) ? "Array": "");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TypeInfo other = (TypeInfo) obj;
        return this.isArray == other.isArray && this.isInt == other.isInt;
    }
}
