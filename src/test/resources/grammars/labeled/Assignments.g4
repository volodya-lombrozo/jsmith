grammar Assignments;

prog: statement+;

statement: /* $jsmith-var-decl */ ID ';' NEWLINE
         | /* $jsmith-var-init */ /* $jsmith-variable-assignment */ ID ' = ' expr ';' NEWLINE
         ;

expr: expr (' + ' | ' - ') expr
    | INT
    | /* $jsmith-variable-usage */ ID
    ;

ID: [a-zA-Z]+;
INT: [0-9]+;
COMMENT: '//' ~[\r\n]* -> skip;
NEWLINE: '\r'? '\n';
