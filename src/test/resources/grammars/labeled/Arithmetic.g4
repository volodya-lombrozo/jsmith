grammar Arithmetic;

prog: stat+ ;

stat: expr NEWLINE
    | /* $jsmith-variable-declaration */ ID '=' expr NEWLINE
    | NEWLINE
    ;

expr: expr ('*' | '/' ) expr
    | expr ('+' | '-' ) expr
    | INT
    | /* $jsmith-variable-usage */ ID
    | '(' expr ')'
    ;

ID  : [a-zA-Z] ;
INT : [0-9] ;
NEWLINE: '\r'? '\n' ;
WS  : [ \t] -> skip ;