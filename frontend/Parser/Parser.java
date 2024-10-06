package frontend.Parser;

import Enums.SyntaxVarType;
import Enums.tokenType;
import frontend.Lexer.Token;
import frontend.Lexer.TokenStream;
import frontend.Nodes.Node;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class Parser {
    private TokenStream tokenStream;
    private Token curToken;

    public Parser(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
        read();
    }

    public void read() {
        curToken = tokenStream.read();
    }

    public boolean isBtype() {
        return curToken.getTokenType() == tokenType.INTTK || curToken.getTokenType() == tokenType.CHARTK;
    }

    public Node parseCompUnit() {
        // CompUnit => {ConstDecl | VarDecl} {FuncDef} MainFuncDef
        ArrayList<Node> children = new ArrayList<>();
        while (true) {
            if (curToken == null) break;
            if (tokenStream.look(1).getTokenType() == tokenType.MAINTK) {
                children.add(parseMainFuncDef());
                break;
            }
            else if (curToken.getTokenType() == tokenType.CONSTTK) { // ConstDecl
                children.add(parseConstDecl());
            }
            else if (tokenStream.look(2).getTokenType() == tokenType.LPARENT) { // '('
                children.add(parseFuncDef());
            }
            else if (isBtype()) { // varDecl
                children.add(parseVarDecl());
            }
            else { System.out.println("Err: parseCompUnit"); break; }
        }
        return NodeCreator.createNode(SyntaxVarType.COMP_UNIT, children);
    }

    public Node parseConstDecl() {
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' // i
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read(); // read 'const'
        children.add(NodeCreator.createNode(curToken)); read(); // read Btype
        children.add(parseConstDef()); // ConstDef
        while (curToken.getTokenType() == tokenType.COMMA) {
            children.add(NodeCreator.createNode(curToken)); read(); // read ','
            children.add(parseConstDef());
        }
        if (curToken.getTokenType() == tokenType.SEMICN) { // i
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i')); }
        return NodeCreator.createNode(SyntaxVarType.CONST_DECL, children);
    }

    public Node parseConstDef() {
        // ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal // k
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read(); // parse Ident
        if (curToken.getTokenType() == tokenType.LBRACK) {
            children.add(NodeCreator.createNode(curToken)); read(); // read [
            children.add(parseConstExp());
            if (curToken.getTokenType() == tokenType.RBRACK) { // k 缺中括号
                children.add(NodeCreator.createNode(curToken)); read();
            } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'k')); }
        }
        children.add(NodeCreator.createNode(curToken)); read(); // read '='
        children.add(parseConstInitVal());
        return NodeCreator.createNode(SyntaxVarType.CONST_DEF, children);
    }

    public Node parseConstExp() {
        // ConstExp → AddExp 注：使用的 Ident 必须是常量
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        return NodeCreator.createNode(SyntaxVarType.CONST_EXP, children);
    }

    public Node parseConstInitVal() {
        //  ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getTokenType() == tokenType.LBRACE) { // '{'
            children.add(NodeCreator.createNode(curToken)); read(); // read {
            if (isExpFirstSet()) { // ConstExp 的 FIRST
                // parse ConstExp { ',' ConstExp }
                children.add(parseConstExp());
                while (curToken.getTokenType() == tokenType.COMMA) {
                    children.add(NodeCreator.createNode(curToken)); read(); // read ','
                    children.add(parseConstExp());
                }
            }
            children.add(NodeCreator.createNode(curToken)); read(); // read }
        }
        else if (curToken.getTokenType() == tokenType.STRCON) {
            children.add(NodeCreator.createNode(curToken)); read();
        }
        else { // ConstExp
            children.add(parseConstExp());
        }
        return NodeCreator.createNode(SyntaxVarType.CONST_INIT_VAL, children);
    }

    public Node parseVarDecl() {
        // err:i 缺分号
        // VarDecl → BType VarDef { ',' VarDef } ';'
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read(); // parse Btype
        children.add(parseVarDef());
        while (curToken.getTokenType() == tokenType.COMMA) {
            children.add(NodeCreator.createNode(curToken)); read();// read ','
            children.add(parseVarDef());
        }
        // parse ';'
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { // i
            Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i'));
        }
        return NodeCreator.createNode(SyntaxVarType.VAR_DECL, children);
    }

    public Node parseVarDef() {
        // VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal // k 缺]
        // VarDef → Ident [ '[' ConstExp ']' ]  [ '=' InitVal ]
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read(); // read Ident
        if (curToken.getTokenType() == tokenType.LBRACK) { // 如果有 '[' ConstExp ']'
            children.add(NodeCreator.createNode(curToken)); read(); // read [
            children.add(parseConstExp());
            if (curToken.getTokenType() == tokenType.RBRACK) {
                children.add(NodeCreator.createNode(curToken)); read(); // read ]
            } else {
                Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'k'));
            }
        }
        if (curToken.getTokenType() == tokenType.ASSIGN) {
            children.add(NodeCreator.createNode(curToken)); read(); // read =
            children.add(parseInitVal());
        }
        return NodeCreator.createNode(SyntaxVarType.VAR_DEF, children);
    }

    public Node parseInitVal() {
        // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getTokenType() == tokenType.LBRACE) { // '{'
            children.add(NodeCreator.createNode(curToken)); read(); // read {
            if (isExpFirstSet()) { // Exp 的 FIRST
                children.add(parseExp());
                while (curToken.getTokenType() == tokenType.COMMA) {
                    children.add(NodeCreator.createNode(curToken)); read(); // read ','
                    children.add(parseExp());
                }
            }
            children.add(NodeCreator.createNode(curToken)); read(); // read }
        }
        else if (curToken.getTokenType() == tokenType.STRCON) { // StringConst
            children.add(NodeCreator.createNode(curToken)); read();
        }
        else { // Exp
            children.add(parseExp());
        }
        return NodeCreator.createNode(SyntaxVarType.INIT_VAL, children);
    }

    public Node parseAddExp() {
        //  AddExp → MulExp { ('+' | '−') MulExp }
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseMulExp());
        while (curToken.getTokenType() == tokenType.PLUS || curToken.getTokenType() == tokenType.MINU) {
            Printer.printSynVarType(SyntaxVarType.ADD_EXP);
            children.add(NodeCreator.createNode(curToken)); read(); // + / -
            children.add(parseMulExp());
        }
        return NodeCreator.createNode(SyntaxVarType.ADD_EXP, children);
    }

    public Node parseMulExp() {
        // MulExp → UnaryExp { ('*' | '/' | '%') UnaryExp }
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseUnaryExp());
        while (curToken.getTokenType() == tokenType.MULT ||
                curToken.getTokenType() == tokenType.DIV || curToken.getTokenType() == tokenType.MOD) {
            Printer.printSynVarType(SyntaxVarType.MUL_EXP);
            children.add(NodeCreator.createNode(curToken)); read(); // '*' | '/' | '%'
            children.add(parseUnaryExp());
        }
        return NodeCreator.createNode(SyntaxVarType.MUL_EXP, children);
    }

    public Node parseUnaryExp() {
        // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getTokenType() == tokenType.IDENFR &&
                tokenStream.look(1).getTokenType() == tokenType.LPARENT) { // Funccall
            children.add(NodeCreator.createNode(curToken)); read(); // read Ident
            children.add(NodeCreator.createNode(curToken)); read(); // read (
            if (isExpFirstSet()) { // FuncRParams 的 FIRST set
                children.add(parseFuncRParams());
            }
            if (curToken.getTokenType() == tokenType.RPARENT) {
                children.add(NodeCreator.createNode(curToken)); read();
            } else { // j 缺)
                Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'j'));
            }
        }
        else if (isUnaryOp()) {
            children.add(parseUnaryOp());
            children.add(parseUnaryExp());
        }
        else { // PrimaryExp
            children.add(parsePrimaryExp());
        }
        return NodeCreator.createNode(SyntaxVarType.UNARY_EXP, children);
    }

    public Node parsePrimaryExp() {
        // PrimaryExp → '(' Exp ')' | LVal | Number | Character// j 缺)
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getTokenType() == tokenType.IDENFR) { // LVal
            children.add(parseLValExp());
        }
        else if(curToken.getTokenType() == tokenType.LPARENT) { // '(' Exp ')'
            children.add(NodeCreator.createNode(curToken)); read(); // read (
            children.add(parseExp());
            if (curToken.getTokenType() == tokenType.RPARENT) {
                children.add(NodeCreator.createNode(curToken)); read();
            } else { // j 缺)
                Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'j'));
            }
        }
        else if (curToken.getTokenType() == tokenType.INTCON) {
            children.add(parseNumber());
        }
        else if (curToken.getTokenType() == tokenType.CHRCON) {
            children.add(parseCharacter());
        }
        return NodeCreator.createNode(SyntaxVarType.PRIM_EXP, children);
    }

    public Node parseExp() {
        // 表达式 Exp → AddExp
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        return NodeCreator.createNode(SyntaxVarType.EXP, children);
    }

    public Node parseLValExp() {
        //  LVal → Ident ['[' Exp ']'] // k 缺]
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();  // read Ident
        if (curToken.getTokenType() == tokenType.LBRACK) {
            children.add(NodeCreator.createNode(curToken)); read(); // read [
            Node node = parseExp();
            children.add(node);
            if (curToken.getTokenType() == tokenType.RBRACK) {
                children.add(NodeCreator.createNode(curToken)); read();
            } else { // k 缺中括号
                Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'k'));
            }
        }
        return NodeCreator.createNode(SyntaxVarType.LVAL_EXP, children);
    }

    public Node parseFuncDef() {
        //  FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseFuncType());
        // parse Ident
        children.add(NodeCreator.createNode(curToken)); read();
        // parse '('
        children.add(NodeCreator.createNode(curToken)); read();
        // parse FuncFParams 开头为 Btype:int / char
        if (isBtype()) {
            children.add(parseFuncFParams());
        }
        // parse ')'
        if (curToken.getTokenType() == tokenType.RPARENT) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { // err j
            Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'j'));
        }
        children.add(parseBlock());
        return NodeCreator.createNode(SyntaxVarType.FUNC_DEF, children);
    }

    public Node parseMainFuncDef() {
        // MainFuncDef → 'int' 'main' '(' ')' Block // j
        ArrayList<Node> children = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            children.add(NodeCreator.createNode(curToken)); read(); // int main (
        }
        if (curToken.getTokenType() == tokenType.RPARENT) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { // j 缺)
            Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'j'));
        }
        children.add(parseBlock());
        return NodeCreator.createNode(SyntaxVarType.MAIN_FUNC_DEF, children);
    }

    public Node parseFuncType() {
        //  FuncType → 'void' | 'int' | 'char'
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();
        return NodeCreator.createNode(SyntaxVarType.FUNC_TYPE, children);
    }

    public Node parseFuncFParam() {
        // 函数形参 FuncFParam → BType Ident ['[' ']'] // k
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();
        children.add(NodeCreator.createNode(curToken)); read();
        if (curToken.getTokenType() == tokenType.LBRACK) {
            children.add(NodeCreator.createNode(curToken)); read(); // [
            if (curToken.getTokenType() == tokenType.RBRACK) {
                children.add(NodeCreator.createNode(curToken)); read();
            } else { // k 缺中括号
                Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'k'));
            }
        }
        return NodeCreator.createNode(SyntaxVarType.FUNC_FPARA, children);

    }

    public Node parseFuncFParams() {
        // 函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseFuncFParam());
        while (curToken.getTokenType() == tokenType.COMMA) {
            children.add(NodeCreator.createNode(curToken)); read();  // read ','
            children.add(parseFuncFParam());
        }
        return NodeCreator.createNode(SyntaxVarType.FUNC_FPARAS, children);
    }

    public Node parseFuncRParams() {
        // 函数实参表 FuncRParams → Exp { ',' Exp }
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseExp());
        while (curToken.getTokenType() == tokenType.COMMA) {
            children.add(NodeCreator.createNode(curToken)); read();  // read ','
            children.add(parseExp());
        }
        return NodeCreator.createNode(SyntaxVarType.FUNC_RPARAS, children);
    }

    public Node parseBlock(){
        //  Block → '{' {   ConstDecl | VarDecl | Stmt } '}'
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read(); // {
        while (curToken.getTokenType() !=tokenType.RBRACE) { // Decl => const / int / char
            if (curToken.getTokenType() == tokenType.CONSTTK) {
                children.add(parseConstDecl());
            } else if (isBtype()) {
                children.add(parseVarDecl());
            } else { // stmt
                children.add(parseStmt());
            }
        }
        children.add(NodeCreator.createNode(curToken)); read(); // read the token after }
        return NodeCreator.createNode(SyntaxVarType.BLOCK, children);
    }

    public Node parseStmt() {
        if (curToken.getTokenType() == tokenType.LBRACE) { // {
            return parseBlockStmt();
        } else if (curToken.getTokenType() == tokenType.IFTK) {
            return parseIfStmt();
        } else if (curToken.getTokenType() == tokenType.FORTK) {
            return parseForBodyStmt();
        } else if (curToken.getTokenType() == tokenType.BREAKTK) {
            return parseBreakStmt();
        } else if (curToken.getTokenType() == tokenType.CONTINUETK) {
            return parseContinueStmt();
        } else if (curToken.getTokenType() == tokenType.RETURNTK) {
            return parseReturnStmt();
        } else if (curToken.getTokenType() == tokenType.PRINTFTK) {
            return parsePrintfStmt();
        } else if (curToken.getTokenType() == tokenType.SEMICN) { // ;
            return parseExpStmt();
        } else {
            Printer.enable = false;
            tokenStream.logPosition();
            parseExp();
            Printer.enable = true;
            if (curToken.getTokenType() == tokenType.ASSIGN) {
                if (tokenStream.look(1).getTokenType() == tokenType.GETINTTK) {
                    curToken = tokenStream.gotoLogPosition();
                    return parseGetIntStmt();
                } else if (tokenStream.look(1).getTokenType() == tokenType.GETCHARTK) {
                    curToken = tokenStream.gotoLogPosition();
                    return parseGetCharStmt();
                } else {
                    curToken = tokenStream.gotoLogPosition();
                    return parseAssignStmt();
                }
            } else {
                curToken = tokenStream.gotoLogPosition();
                return parseExpStmt();
            }
        }
    }

    public Node parseExpStmt() {
        // [Exp] ';' // i
        ArrayList<Node> children = new ArrayList<>();
        if (isExpFirstSet()) {
            children.add(parseExp());
        }
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i')); }
        return NodeCreator.createNode(SyntaxVarType.EXP_STMT, children);
    }

    public Node parseAssignStmt() {
        // LVal '=' Exp ';' // i
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLValExp());
        children.add(NodeCreator.createNode(curToken)); read(); // =
        children.add(parseExp());
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i')); }
        return NodeCreator.createNode(SyntaxVarType.ASSIGN_STMT, children);
    }

    public Node parseGetIntStmt() {
        // LVal '=' 'getint''('')'';' // // h i j
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLValExp());
        for (int i = 0; i < 3; i++) {
            children.add(NodeCreator.createNode(curToken)); read(); // =,getint,(
        }
        if (curToken.getTokenType() == tokenType.RPARENT) { // j
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'j')); }
        // i
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i')); }
        return NodeCreator.createNode(SyntaxVarType.GET_INT_STMT, children);
    }

    public Node parseGetCharStmt() {
        //  LVal '=' 'getchar''('')'';' // h i j
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLValExp());
        for (int i = 0; i < 3; i++) {
            children.add(NodeCreator.createNode(curToken)); read(); // =,getchar,(
        }
        if (curToken.getTokenType() == tokenType.RPARENT) { // j
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'j')); }
        // i
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i')); }
        return NodeCreator.createNode(SyntaxVarType.GET_CHAR_STMT, children);
    }

    public Node parseBlockStmt() {
        // 语句块 Block → '{' { BlockItem } '}'
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseBlock());
        return NodeCreator.createNode(SyntaxVarType.BLOCK_STMT, children);
    }

    public Node parseIfStmt() {
        //  'if' '(' Cond ')' Stmt [ 'else' Stmt ] // j
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();
        children.add(NodeCreator.createNode(curToken)); read();
        children.add(parseCondExp());
        if (curToken.getTokenType() == tokenType.RPARENT) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { // j 缺)
            Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'j'));
        }
        children.add(parseStmt());
        if (curToken.getTokenType() == tokenType.ELSETK) {
            children.add(NodeCreator.createNode(curToken)); read();
            children.add(parseStmt());
        }
        return NodeCreator.createNode(SyntaxVarType.IF_STMT, children);
    }

    public Node parseForBodyStmt() {
        // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();
        children.add(NodeCreator.createNode(curToken)); read();
        if (curToken.getTokenType() == tokenType.IDENFR) { // ForStmt 的 FIRST
            children.add(parseForStmt());
        }
        children.add(NodeCreator.createNode(curToken)); read(); // read ';'
        if (isExpFirstSet()) { // CondExp 的 FIRST
            children.add(parseCondExp());
        }
        children.add(NodeCreator.createNode(curToken)); read(); // read ';'
        if (curToken.getTokenType() == tokenType.IDENFR) { // ForStmt 的 FIRST
            children.add(parseForStmt());
        }
        children.add(NodeCreator.createNode(curToken)); read(); // read ')'
        children.add(parseStmt());
        return NodeCreator.createNode(SyntaxVarType.FOR_BODY_STMT, children);
    }

    public Node parseForStmt() {
        // ForStmt → LVal '=' Exp
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLValExp());
        children.add(NodeCreator.createNode(curToken)); read();
        children.add(parseExp());
        return NodeCreator.createNode(SyntaxVarType.FOR_STMT, children);
    }

    public Node parseCondExp() {
        //  Cond → LOrExp
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLorExp());
        return NodeCreator.createNode(SyntaxVarType.COND_EXP, children);
    }

    public Node parseBreakStmt() {
        //  'break' ';'
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { // err i
            Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i'));
        }
        return NodeCreator.createNode(SyntaxVarType.BREAK_STMT, children);
    }

    public Node parseContinueStmt() {
        //  'continue' ';'
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { // err i
            Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i'));
        }
        return NodeCreator.createNode(SyntaxVarType.CONTINUE_STMT, children);
    }

    public Node parseReturnStmt() {
        //  'return' [Exp] ';' // f i
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();
        if (isExpFirstSet()) {
            children.add(parseExp());
        }
        // parse ';'
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { // err i
            Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i'));
        }
        return NodeCreator.createNode(SyntaxVarType.RETURN_STMT, children);
    }

    public Node parsePrintfStmt() {
        //  'printf''('StringConst {','Exp} ')'';' // i j
        ArrayList<Node> children = new ArrayList<>();
        for (int i = 0 ; i < 3; i++) // 'printf'  '('   StringConst
            { children.add(NodeCreator.createNode(curToken)); read(); }
        while (curToken.getTokenType() == tokenType.COMMA) {
            children.add(NodeCreator.createNode(curToken)); read();
            children.add(parseExp());
        }
        // j
        if (curToken.getTokenType() == tokenType.RPARENT) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'j')); }
        // i
        if (curToken.getTokenType() == tokenType.SEMICN) {
            children.add(NodeCreator.createNode(curToken)); read();
        } else { Printer.addError(new Error(tokenStream.look(-1).getLineno(), 'i')); }
        return NodeCreator.createNode(SyntaxVarType.PRINTF_STMT, children);
    }

    public Node parseLorExp() {
        // LOrExp → LAndExp | LOrExp '||' LAndExp
        // LOrExp → LAndExp { '||' LAndExp }
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLAndExp());
        while (curToken.getTokenType() == tokenType.OR) {
            Printer.printSynVarType(SyntaxVarType.LOR_EXP);
            children.add(NodeCreator.createNode(curToken)); read(); // read ||
            children.add(parseLAndExp());
        }
        return NodeCreator.createNode(SyntaxVarType.LOR_EXP, children);
    }

    public Node parseLAndExp() {
        //  LAndExp → EqExp | LAndExp '&&' EqExp
        //  LAndExp → EqExp { '&&' EqExp }
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseEqExp());
        while (curToken.getTokenType() == tokenType.AND) {
            Printer.printSynVarType(SyntaxVarType.LAND_EXP);
            children.add(NodeCreator.createNode(curToken)); read(); // read &&
            children.add(parseEqExp());
        }
        return NodeCreator.createNode(SyntaxVarType.LAND_EXP, children);
    }

    public Node parseEqExp() {
        // 相等性表达式 EqExp → RelExp { '==' | '!=') RelExp }
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseRelExp());
        while (curToken.getTokenType() == tokenType.EQL || curToken.getTokenType() == tokenType.NEQ) {
            Printer.printSynVarType(SyntaxVarType.EQ_EXP);
            children.add(NodeCreator.createNode(curToken)); read(); // read
            children.add(parseRelExp());
        }
        return NodeCreator.createNode(SyntaxVarType.EQ_EXP, children);
    }

    public Node parseRelExp() {
        // 关系表达式 RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp }
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        while (curToken.getTokenType() == tokenType.LSS || curToken.getTokenType() == tokenType.GRE ||
                curToken.getTokenType() == tokenType.LEQ || curToken.getTokenType() == tokenType.GEQ) {
            Printer.printSynVarType(SyntaxVarType.REL_EXP);
            children.add(NodeCreator.createNode(curToken)); read(); // read
            children.add(parseAddExp());
        }
        return NodeCreator.createNode(SyntaxVarType.REL_EXP, children);
    }

    public Node parseUnaryOp() {
        // UnaryOp → '+' | '−' | '!'
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read();
        return NodeCreator.createNode(SyntaxVarType.UNARY_OP, children);
    }

    public Node parseNumber() {
        //  Number → IntConst
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read(); // num readed
        return NodeCreator.createNode(SyntaxVarType.NUMBER, children);
    }

    public Node parseCharacter() {
        // 字符 Character → CharConst
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreator.createNode(curToken)); read(); // read char Tk
        return NodeCreator.createNode(SyntaxVarType.CHAR, children);
    }

    public boolean isUnaryOp() {
        return curToken.getTokenType() == tokenType.PLUS ||
                curToken.getTokenType() == tokenType.MINU || curToken.getTokenType() == tokenType.NOT;
    }

    public boolean isExpFirstSet() {
        return isUnaryOp() || curToken.getTokenType() == tokenType.IDENFR ||
                curToken.getTokenType() == tokenType.LPARENT ||
                curToken.getTokenType() == tokenType.INTCON || curToken.getTokenType() == tokenType.CHRCON;
    }
}
