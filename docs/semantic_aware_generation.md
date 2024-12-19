# Semantic Aware Generation

The library can generate the simple Java programs that syntactically correct.
To guide the generation of semantically correct programs, we use special
annotations in the grammar. They are not a part of the ANTLR grammar, but they
might be used to generate generation.

Here is the example of such annotations:

```antlr
grammar Arithmetic;

prog: stat+ ;

stat: expr NEWLINE
    | /* $jsmith-var-init */ /* $jsmith-var-target */ /* $jsmith-var-decl */ ID '=' expr NEWLINE
    ;

expr: expr ('*' | '/' ) expr
    | expr ('+' | '-' ) expr
    | INT
    | /* $jsmith-var-use */ ID
    | '(' expr ')'
    ;

ID  : [a-zA-Z] ;
INT : [0-9] ;
NEWLINE: '\r'? '\n' ;
WS  : [ \t] -> skip ;
```

Pay attention to the `$jsmith-var-init`, `$jsmith-var-target`
and `$jsmith-var-use` annotations in comments.
They help the generator understand the context of the program and generate
semantically correct programs, such as the following (real generated strings):

```txt
O=(8+4)
O=(O+O)/2*6

4
((5))/0+8*5
T=6
2
T=0*T-3/(T)

I=0*1-((7))*((0))-((8))
Q=5-6+6*I
(I+I)
I=3
C=Q/Q

K=8
K=K
2/9
```

Now expressions use only the initialized variables, and the variables are
initialized before they are used.

Here is a full list of supported annotations:

* `$jsmith-var-decl` - variable declaration, in some language it might be
  required to declare a variable before using it like `int x;`. This
  annotation should be attached to a terminal usage in parser rules.
* `$jsmith-var-init` - variable initialization, like `x = 5;` this annotation
  is applicable only to the parser option rule that
  contains `$jsmith-var-target`
* `$jsmith-var-target` - variable initialization target `(target->)x = 5;`.
  This annotation is attached to the token usage in a parser rule.
* `$jsmith-var-use` - variable usage, like `x` in `x * 2;`
* `$jsmith-unique` - unique value, if it generates already used value, it
  should be regenerated.
* `$jsmith-scope` - scope, like `{}` in `if (x > 5) { x = 5; }`, it helps to
  separate identifiers in different scopes.
* `$jsmith-type`
* `$jsmith-predicate(long)` or `$jsmith-predicate(boolean)`