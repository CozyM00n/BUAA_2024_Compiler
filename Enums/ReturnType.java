package Enums;

public enum ReturnType {
    RETURN_INT("Int"),
    RETURN_CHAR("Char"),
    RETURN_VOID("Void"),
    ;

    private final String ReturnTypeName;
    ReturnType(String returnTypeName) {
        this.ReturnTypeName = returnTypeName;
    }

    @Override
    public String toString() {
        return ReturnTypeName;
    }
}
