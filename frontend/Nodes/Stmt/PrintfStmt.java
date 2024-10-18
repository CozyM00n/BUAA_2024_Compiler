package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Exp.Exp;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class PrintfStmt extends Stmt{
    private String stringCosnt;
    private ArrayList<Exp> expList;
    //  'printf''('StringConst {','Exp}')'';' // l
    // 格式字符与表达式个数不匹配 行号为‘printf’所在行号
    public PrintfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
        this.stringCosnt = ((TokenNode)children.get(2)).getToken().getValue();
        this.expList = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof Exp) {
                expList.add((Exp) child);
            }
        }
    }

    @Override
    public void checkError() {
        // % 数量和 , 数量
        int cnt = 0;
        for (int i = 0; i < stringCosnt.length(); i++) {
            if (stringCosnt.charAt(i) == '%' && i+1 < stringCosnt.length() &&
                    (stringCosnt.charAt(i+1) == 'd' || stringCosnt.charAt(i+1) == 'c') ) {
                cnt++;
            }
        }
//        System.out.println("cnt = " + cnt);
//        System.out.println("size = " + expList.size());
        if (cnt != expList.size()) {
            Error error = new Error(((TokenNode)children.get(0)).getLino(), 'l');
            Printer.addError(error);
        }
        super.checkError();
    }
}
