package Enums;

public enum SyntaxVarType {
    COMP_UNIT("CompUnit"),
    // Func
    MAIN_FUNC_DEF("MainFuncDef"),
    FUNC_DEF("FuncDef"),
    FUNC_TYPE("FuncType"),
    FUNC_FPARAS("FuncFParams"),
    FUNC_FPARA("FuncFParam"),
    FUNC_RPARAS("FuncRParams"),

    // Var
    CONST_DECL("ConstDecl"),
    CONST_DEF("ConstDef"),
    CONST_INIT_VAL("ConstInitVal"),
    VAR_DECL("VarDecl"),
    VAR_DEF("VarDef"),
    INIT_VAL("InitVal"),

    //Stmt
    STMT("Stmt"), // 不需要
    ASSIGN_STMT("Stmt"),
    BLOCK_STMT("Stmt"),
    BREAK_STMT("Stmt"),
    CONTINUE_STMT("Stmt"),
    EXP_STMT("Stmt"),
    FOR_BODY_STMT("Stmt"),
    GET_CHAR_STMT("Stmt"),
    GET_INT_STMT("Stmt"),
    IF_STMT("Stmt"),
    PRINTF_STMT("Stmt"),
    RETURN_STMT("Stmt"),

    FOR_STMT("ForStmt"),

    //Exp
    ADD_EXP("AddExp"),
    CHAR("Character"),
    COND_EXP("Cond"),
    CONST_EXP("ConstExp"),
    EQ_EXP("EqExp"),
    EXP("Exp"),
    LAND_EXP("LAndExp"),
    LOR_EXP("LOrExp"),
    LVAL_EXP("LVal"),
    MUL_EXP("MulExp"),
    NUMBER("Number"),
    PRIM_EXP("PrimaryExp"),
    REL_EXP("RelExp"),
    UNARY_EXP("UnaryExp"),
    UNARY_OP("UnaryOp"),


    BLOCK("Block"),
    TOKEN("Token"),
    ;

    private final String typeName;

    SyntaxVarType(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "<" + typeName + ">";
    }
}
