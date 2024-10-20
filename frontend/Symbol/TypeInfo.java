package frontend.Symbol;

import Enums.ReturnType;

public class TypeInfo {
    private boolean isArray;
    private String type;

    public TypeInfo(boolean isArray, String type) {
        this.isArray = isArray;
        this.type = type;
    }

    public void setIsArray(boolean flg) {
        isArray = flg;
    }

    public String getType() {
        return type;
    }

    public boolean getIsArray() {
        return isArray;
    }

    @Override
    public String toString() {
        return (type.equals("Int") ? "Int" : type.equals("Char") ? "Char" : "Void") + ((isArray) ? "Array": "");
    }

    public static boolean match(TypeInfo formal, TypeInfo real) {
        if (formal.isArray) {
            return real.isArray && formal.type.equals(real.type);
        } else {
            return ! real.isArray;
        }
    }
}
