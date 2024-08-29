// Highly recursive grammar that can
// produce a stack overflow in the program generator.
grammar Recursive;

expr: recur
    | '0'
    ;

recur: '1' expr expr expr expr expr expr expr expr
    ;

WS: [ \t\n\r]+ -> skip;