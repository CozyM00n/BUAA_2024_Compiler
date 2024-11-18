package frontend.Symbol;

public class TypeInfo {

    public enum typeInfo {
        INT_TYPE("Int"), CHAR_TYPE("Char"), VOID_TYPE("Void"),
        ;

        private final String typeName;
        typeInfo(String str) {
            this.typeName = str;
        }

        @Override
        public String toString() {
            return this.typeName;
        }
    }

    private boolean isArray;
    private typeInfo type;

    public TypeInfo(boolean isArray, typeInfo type) {
        this.isArray = isArray;
        this.type = type;
    }

    public void setIsArray(boolean flg) {
        isArray = flg;
    }

    public typeInfo getType() {
        return type;
    }

    public boolean getIsArray() {
        return isArray;
    }

    @Override
    public String toString() {
        return type.toString() + ((isArray) ? "Array": "");
    }

    public static boolean match(TypeInfo formal, TypeInfo real) {
        if (real == null) return false;
        if (formal.isArray) {
            return real.isArray && formal.type.equals(real.type);
        } else {
            return ! real.isArray;
        }
    }
}
