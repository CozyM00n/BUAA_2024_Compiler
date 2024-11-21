package frontend.Nodes.Exp;

import Enums.ReturnType;
import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Func.FuncRParams;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;
import frontend.Symbol.FuncSymbol;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.TypeInfo;
import llvm_IR.Function;
import llvm_IR.IRManager;
import llvm_IR.Instr.*;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;
import utils.Error;
import utils.Printer;

import java.util.ArrayList;

public class UnaryExp extends Node {
    // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // c d e
    public UnaryExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public ArrayList<TypeInfo> getRParamsList() {
        if (children.size() >= 3 && children.get(2) instanceof FuncRParams) {
            return ((FuncRParams) children.get(2)).getRParamsType();
        }
        return new ArrayList<>(); // 没有参数
    }

    @Override
    public void checkError() {
        if (children.size() >= 3 && ((TokenNode)children.get(1)).getTokenType() == TokenType.LPARENT) { // function Call
            // c 使用了未定义的标识符报错行号为Ident 所在行数
            TokenNode identToken = ((TokenNode) children.get(0));
            Symbol symbol = SymbolManager.getInstance().getSymbol(identToken.getTokenValue());
            if (!(symbol instanceof FuncSymbol)) { // 包含null的情况
                Error error = new Error(((TokenNode) children.get(0)).getLino(), 'c');
                Printer.addError(error);
            }
            else {
                // d 参数个数不匹配，函数名所在行数
                FuncSymbol funcSymbol = (FuncSymbol) symbol;
                ArrayList<TypeInfo> fParamsType = funcSymbol.getTypeList();
                ArrayList<TypeInfo> rParamsType = getRParamsList();
                if (fParamsType.size() != rParamsType.size()) {
                    Error error = new Error(identToken.getLino(), 'd');
                    Printer.addError(error);
                }
                // e 函数参数类型不匹配 函数名所在行数
                for (int i = 0; i < rParamsType.size() && i < fParamsType.size(); i++) { // 如果实参未定义则为null
                    if (! TypeInfo.match(fParamsType.get(i), rParamsType.get(i))) {
                        Error error = new Error(identToken.getLino(), 'e');
                        Printer.addError(error);
                        return; // 只需打印一次
                    }
                }
            }
        }
        super.checkError();
    }

    @Override
    public TypeInfo getTypeInfo() {
        if (children.get(0) instanceof TokenNode) {
            String identName = ((TokenNode) children.get(0)).getTokenValue();
            return (SymbolManager.getInstance().getSymbol(identName)).getTypeInfo();
        }
        for (Node child : children) {
            if (child.getTypeInfo() != null) {
                return child.getTypeInfo();
            }
        }
        return null;
    }

    @Override
    public int calculate() {
        if (children.size() == 2 && children.get(0) instanceof UnaryOp) {
            int res = children.get(1).calculate();
            TokenNode uOp = (TokenNode)((children.get(0)).getChildren()).get(0);
            switch (uOp.getTokenType()) {
                case PLUS: return res;
                case MINU: return -res;
                case NOT: return res == 0 ? 1 : 0;
            }
        }
        else if (children.get(0) instanceof PrimExp) {
            return children.get(0).calculate();
        }
        System.out.println("Error CG : UnaryExp in calculate!");
        return 0;
    }

    @Override
    public Value generateIR() {
        if (children.get(0) instanceof PrimExp) {
            return children.get(0).generateIR();
        }
        else if (children.get(0) instanceof UnaryOp) {
            TokenNode uOp = (TokenNode)((children.get(0)).getChildren()).get(0);
            Value op2 = children.get(1).generateIR();
            if (uOp.getTokenType() == TokenType.PLUS) {
                return op2;
            } else if (uOp.getTokenType() == TokenType.MINU) {
                return BinaryInstr.checkAndGenBinInstr(IntType.INT32, BinaryInstr.op.SUB, new Constant(0, IntType.INT32), op2);
            } else { // NOT
                return new IcmpInstr(IRManager.getInstance().genVRName(),
                        IcmpInstr.cmpOp.EQ, new Constant(0, op2.getLlvmType()), op2);
            }
        }
        else { // Function Call : Ident '(' [FuncRParams] ')'
            TokenNode identToken = ((TokenNode) children.get(0));
            FuncSymbol funcSymbol = (FuncSymbol) SymbolManager.getInstance().getSymbolForIR(identToken.getTokenValue());
            Function function = funcSymbol.getLlvmValue();
            ArrayList<Value> rParams = new ArrayList<>();
            if (children.get(2) instanceof FuncRParams) {
                rParams = ((FuncRParams) children.get(2)).genIRList();
            }
            if (funcSymbol.getReturnType() == ReturnType.RETURN_VOID)
                return new CallInstr("%call", function, rParams);
            else
                return new CallInstr(IRManager.getInstance().genVRName(), function, rParams);
        }
    }
}
