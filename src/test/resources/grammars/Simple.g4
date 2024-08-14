grammar Simple;

expr
    : expr PLUS number
    | number
    ;

number: NUMBER;


PLUS: '+';
NUMBER: [0-9]+;
WS: [ \t\r\n]+ -> skip;