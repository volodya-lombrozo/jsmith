grammar Arithmetic;

prog: stat+ ;

stat: expr NEWLINE
    | /* $jsmith-variableDeclaration */ ID '=' expr NEWLINE
    | NEWLINE
    ;

expr: expr ('*' | '/' ) expr
    | expr ('+' | '-' ) expr
    | INT
    | /* $jsmith-variableUsage */ ID
    | '(' expr ')'
    ;

ID  : [a-zA-Z]+ ;
INT : [0-9]+ ;
NEWLINE: '\r'? '\n' ;
WS  : [ \t]+ -> skip ;