package utils;

public class Error {
    private int lineno;
    private char errType;

    public Error(int lineno, char errType) {
        this.lineno = lineno;
        this.errType = errType;
    }

    public int getLineno() {
        return lineno;
    }

    public char getErrType() {
        return errType;
    }
}
