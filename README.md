# BUAA_2024_Compiler

### TODO

- 缺的; ] } 没补全，也就是children可能是不符合文法的，
- 监视点的正确性验证
- 未考虑Globol
- 检查每次作业是否都能运行
- 调用super.checkE的顺序

### 词法分析



### 语法分析

我的`parser`采用递归下降的思路构建抽象语法树，为每一种语法成分写一个`parseXXX`函数，从顶层`parseCompUnit`开始，判断前方为该语法成分时调用，返回该语法节点。

#### 改写部分文法

##### 精简文法

由于不要求输出`BlockItem,Decl,BType`这三种语法成分，且此三类的产生式也很简单，因此将这三种语法成分先与其他合并，例如：

```
CompUnit → {Decl} {FuncDef} MainFuncDef
改为：CompUnit → {ConstDecl | VarDecl} {FuncDef} MainFuncDef

语句块 Block → '{' { BlockItem } '}
改为：Block → '{' { ConstDecl | VarDecl | Stmt } '}'
```

##### 消除左递归

由于左递归会导致程序进入死循环，需要改写含有左递归的文法。使用扩展巴科斯范式（EBNF）进行如下改写即可：

```
AddExp → MulExp | AddExp ('+' | '−') MulExp
改为：AddExp → MulExp { ('+' | '−') MulExp }
```

不过这里需要注意：由于我使用while循环解析第二个MulExp及之后的MulExp，但原来的文法中只有最右边的`MulExp`被认为是`MulExp`，其他的实际上套了一层`AddExp`的壳，所以在while循环需要加上一个AddExp语法成分的输出。大概如下：

```java
children.add(parseMulExp());
while (curToken.getTokenType() == tokenType.PLUS || curToken.getTokenType() == tokenType.MINU) {
    Printer.printSynVarType(SyntaxVarType.ADD_EXP);
    children.add(NodeCreator.createNode(curToken)); read(); // + / -
    children.add(parseMulExp());
}
```

<img src="README/image-20241007095803775.png" alt="image-20241007095803775" style="zoom:43%;" />

##### `Stmt`的处理

由于语句类有太多种情况，因此为每一种语句单独设置一个类继承`Stmt`，再由`Stmt`继承`Node`。

#### 提前读

在提完公因子之后还有FIRST集合的冲突的情况，使用**提前读**若干`token`的策略，直到遇到可以判定的词法成分。使用的是`tokenStream`的`look`函数：

```java
public Token look(int n) {
    if (pos + n < tokenList.size())
        return tokenList.get(pos + n);
    else return null;
}
```

例如，一元表达式 UnaryExp → PrimaryExp | **Ident** '(' [FuncRParams] ')' | UnaryOp UnaryExp，其中PrimaryExp->LVal的FIRST集也包含**Ident**，此时就采用提前读的策略，具体如下：

```java
if (curToken.getTokenType() == tokenType.IDENFR &&
        tokenStream.look(1).getTokenType() == tokenType.LPARENT) {
    // parse Funccall
}
else if (isUnaryOp()) {
    children.add(parseUnaryOp());
    children.add(parseUnaryExp());
}
else { // PrimaryExp
    children.add(parsePrimaryExp());
}
```

对于常用的FIRST集合整理成函数，例如`Exp`的FIRST集：`+, -, !, 标识符, (, int, char`。

#### 回溯

但是，仍有一种情况无法使用提前读来解决，需要读完下图红色部分之后才能进一步判断。
<img src="README/image-20241007102850708.png" alt="image-20241007102850708" style="zoom:43%;" />

#### 抽象语法树（AST）结构

新建语法成分枚举类`SyntaxVarType`，对应给出文法的每一种非终结符，对于所有终结符创建一个新的`TOKEN`枚举类型。

根据题目给出的文法，为每个产生式左侧的语法成分各建立一个类，均继承自基类`Node`，记录的是语法成分的种类和其孩子节点。主要分为以下两种：

- **词法**成分节点：其成分是词法分析给出的`tokenStream`中的单个`token`，`children`为空，等价为终结符，是语法树中的叶子节点。
- **语法**分析节点：文法中给出的所有非终结符，是语法树中的非叶子节点。

每当递归下降子程序解析完某一语法成分，即可调用相应的`Node`的子类新创建语法树节点。在实现上使用了工厂模式生成两种`Node`。同时工厂模式产生新节点这一步进行输出，即可保证输出顺序和解析顺序的一致性。

##### 与lexer的配合

从lexer传来的是token流，parser使用curToken记录当前单个词法成分。采取的读取规范是预先读好一个token，即在解析下一个语法成分前，其第一个词法成分已经存储在curToken里了。

### 语义分析

from教程：

#### 类型系统

新建符号类`Symbol`作为所有类型的父类，使用继承区分不同的符号。

新建枚举类`SymbolType`用于区分该符号是变量/常量/函数类型。

新建枚举类`ReturnType`用于函数的返回值（`int/char/void`）

#### 创建符号表

符号表类 `SymbolTable` 对应某一作用域的符号表。

##### 符号表的组织

进入新作用域时，调用 `pushScope` 函数，该函数主要负责：新建一个符号表并将其入栈。



离开作用域时，我们调用 `popScope` 函数，返回上一层作用域的符号表，即 `_father` 字段取值。

前序遍历

如何打印