lexer grammar LettersLexer;

LETTERS : [a-zA-Z]+;
SPACE : ' ';

WS : [\t\r\n]+ -> skip;