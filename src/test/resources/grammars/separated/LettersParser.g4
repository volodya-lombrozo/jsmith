parser grammar LettersParser;

options {
    tokenVocab = LettersLexer;
}

sentences : sentence+;

sentence : word+;

word : letters+
    | SPACE
    ;

space : SPACE;
letters : LETTERS;