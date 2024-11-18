package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Exp.ConstExp;
import frontend.Nodes.Node;

import java.util.ArrayList;

//  ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst

public class ConstInitVal extends Node {
    public ConstInitVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public boolean isStringConst() {
        return children.get(0) instanceof TokenNode &&
                ((TokenNode)children.get(0)).getTokenType() == TokenType.STRCON;
    }

    public String getInitString(int len) { // 不足补\0
        String str = ((TokenNode) children.get(0)).getToken().getValue();
        str = str.substring(1, str.length() - 1);
        StringBuilder sb = new StringBuilder(str);
        for (int i = str.length(); i < len; i++) {
            sb.append('\0');  // Append the null character '\0'
        }
        return sb.toString();
    }

    public ArrayList<Integer> getIntList(int len) {
        // 对于Const，不足len的部分一定要初始化为0
        ArrayList<Integer> res = new ArrayList<>();
        if (isStringConst()) {
            String str = ((TokenNode) children.get(0)).getToken().getValue();
            str = str.substring(1, str.length() - 1);
            int i = 0;
            while (i < str.length()) {
                res.add((int) str.charAt(i++));
            }
            while (i++ < len) {
                res.add((int) '\0');
            }
            return res;
        }
        else if (children.get(0) instanceof ConstExp) { // len == 0
            res.add(children.get(0).calculate());
            return res;
        }
        else {
            int cnt = 0;
            for (int i = 1; i < children.size(); i += 2) {
                res.add(children.get(i).calculate()); // ConstExp
                cnt++;
            }
            while (cnt < len) { // 剩下的也都初始化为0
                res.add(0);
                cnt++;
            }
        }
        return res;
    }
}
