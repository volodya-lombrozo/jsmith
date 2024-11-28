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