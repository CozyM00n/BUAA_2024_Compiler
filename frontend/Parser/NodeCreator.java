package frontend.Parser;

import Enums.SyntaxVarType;
import frontend.Lexer.Token;
import frontend.Nodes.Block;
import frontend.Nodes.CompUnit;
import frontend.Nodes.Exp.*;
import frontend.Nodes.Exp.Number;
import frontend.Nodes.Func.*;
import frontend.Nodes.Node;
import frontend.Nodes.Stmt.*;
import frontend.Nodes.Var.*;
import utils.Printer;

import java.util.ArrayList;

public class NodeCreator {
    public static Node createNode(SyntaxVarType type, ArrayList<Node> children){
        //Printer.printSynVarType(type);
        Printer.addOutFileInfo(type.toString());
        switch (type) {
            case COMP_UNIT: return new CompUnit(type, children);
            // Func
            case MAIN_FUNC_DEF: return new MainFuncDef(type, children);
            case FUNC_DEF: return new FuncDef(type, children);
            case FUNC_TYPE: return new FuncType(type, children);
            case FUNC_FPARAS: return new FuncFParams(type, children);
            case FUNC_FPARA: return new FuncFParam(type, children);
            case FUNC_RPARAS: return new FuncRParams(type, children);
            // Var
            case CONST_DECL: return new ConstDecl(type, children);
            case CONST_DEF: return new ConstDef(type, children);
            case CONST_INIT_VAL: return new ConstInitVal(type, children);
            case VAR_DECL: return new VarDecl(type, children);
            case VAR_DEF: return new VarDef(type, children);
            case INIT_VAL: return new InitVal(type, children);
            // Stmt
            // case STMT: return new Stmt(type, children);
            case ASSIGN_STMT: return new AssignStmt(type, children);
            case BLOCK_STMT: return new BlockStmt(type, children);
            case BREAK_STMT: return new BreakStmt(type, children);
            case CONTINUE_STMT: return new ContinueStmt(type, children);
            case EXP_STMT: return new ExpStmt(type, children);
            case FOR_STMT: return new ForStmt(type, children);
            case FOR_BODY_STMT: return new ForBodyStmt(type, children);
            case GET_CHAR_STMT: return new GetCharStmt(type, children);
            case GET_INT_STMT: return new GetIntStmt(type, children);
            case IF_STMT: return new IfStmt(type, children);
            case PRINTF_STMT: return new PrintfStmt(type, children);
            case RETURN_STMT: return new ReturnStmt(type, children);
            // Exp
            case ADD_EXP: return new AddExp(type, children);
            case CHAR: return new Char(type, children);
            case COND_EXP: return new CondExp(type, children);
            case CONST_EXP: return new ConstExp(type, children);
            case EQ_EXP: return new EqExp(type, children);
            case EXP: return new Exp(type, children);
            case LAND_EXP: return new LAndExp(type, children);
            case LOR_EXP: return new LOrExp(type, children);
            case LVAL_EXP: return new LValExp(type, children);
            case MUL_EXP: return new MulExp(type, children);
            case NUMBER: return new Number(type, children);
            case PRIM_EXP: return new PrimExp(type, children);
            case REL_EXP: return new RelExp(type, children);
            case UNARY_EXP: return new UnaryExp(type, children);
            case UNARY_OP: return new UnaryOp(type, children);

            // case TOKEN: return new TokenNode(type, children);
            case BLOCK: return new Block(type, children);
            default: return null;
        }
    }

    public static Node createNode(Token token) {
        //Printer.printToken(token);
        Printer.addOutFileInfo(token.toString());
        return new TokenNode(SyntaxVarType.TOKEN, null, token);
    }
}
