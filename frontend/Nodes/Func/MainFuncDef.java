package frontend.Nodes.Func;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class MainFuncDef extends Node {


    public MainFuncDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {

    }
}
