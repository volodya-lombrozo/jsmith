parser grammar WordsAndNumbersParser;

options {
    tokenVocab = WordsAndNumbersLexer;
}

words : line+;

line  : sentence newline;
sentence: sentence space word
        | sentence space number
        | word
        | number;

newline : CR? LF;
number : NUMBER;
space : SPACE;
word : WORD;