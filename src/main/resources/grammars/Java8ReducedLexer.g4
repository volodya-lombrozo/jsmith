/*
 * [The "BSD license"]
 *  Copyright (c) 2014 Terence Parr
 *  Copyright (c) 2014 Sam Harwell
 *  Copyright (c) 2019 Student Main (Make it universal)
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * A Java 8 grammar for ANTLR 4 derived from the Java Language Specification
 * chapter 19.
 *
 * NOTE: This grammar results in a generated parser that is much slower
 *       than the Java 7 grammar in the grammars-v4/java directory. This
 *     one is, however, extremely close to the spec.
 *
 * You can test with
 *
 *  $ antlr4 Java8.g4
 *  $ javac *.java
 *  $ grun Java8 compilationUnit *.java
 *
 * Or,
~/antlr/code/grammars-v4/java8 $ java Test .
/Users/parrt/antlr/code/grammars-v4/java8/./Java8BaseListener.java
/Users/parrt/antlr/code/grammars-v4/java8/./Java8Lexer.java
/Users/parrt/antlr/code/grammars-v4/java8/./Java8Listener.java
/Users/parrt/antlr/code/grammars-v4/java8/./Java8Parser.java
/Users/parrt/antlr/code/grammars-v4/java8/./Test.java
Total lexer+parser time 30844ms.
 */

// ATTENTION! This is the significantly modified copy of the Java8Lexer.g4 file from the ANTLRv4 grammar distribution.
// The original file is located in the ANTLRv4 grammar distribution at the following path:
// https://github.com/antlr/grammars-v4/blob/master/java/java8/Java8Lexer.g4

// $antlr-format alignTrailingComments true, columnLimit 150, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine true, allowShortBlocksOnASingleLine true, minEmptyLines 0, alignSemicolons ownLine
// $antlr-format alignColons trailing, singleLineOverrulesHangingColon true, alignLexerCommands true, alignLabels true, alignTrailers true

lexer grammar Java8ReducedLexer;

// LEXER

// §3.9 Keywords

ABSTRACT     : 'abstract';
ASSERT       : 'assert';
BOOLEAN      : 'boolean';
BREAK        : 'break';
BYTE         : 'byte';
CASE         : 'case';
CATCH        : 'catch';
CHAR         : 'char';
CLASS        : 'class';
CONST        : 'const';
CONTINUE     : 'continue';
DEFAULT      : 'default';
DO           : 'do';
DOUBLE       : 'double';
ELSE         : 'else';
ENUM         : 'enum';
EXTENDS      : 'extends';
FINAL        : 'final';
FINALLY      : 'finally';
FLOAT        : 'float';
FOR          : 'for';
IF           : 'if';
GOTO         : 'goto';
IMPLEMENTS   : 'implements';
IMPORT       : 'import';
INSTANCEOF   : 'instanceof';
INT          : 'int';
INTERFACE    : 'interface';
LONG         : 'long';
NATIVE       : 'native';
NEW          : 'new';
PACKAGE      : 'package';
PRIVATE      : 'private';
PROTECTED    : 'protected';
PUBLIC       : 'public';
RETURN       : 'return';
SHORT        : 'short';
STATIC       : 'static';
STRICTFP     : 'strictfp';
SUPER        : 'super';
SWITCH       : 'switch';
SYNCHRONIZED : 'synchronized';
THIS         : 'this';
THROW        : 'throw';
THROWS       : 'throws';
TRANSIENT    : 'transient';
TRY          : 'try';
VOID         : 'void';
VOLATILE     : 'volatile';
WHILE        : 'while';

// §3.10.1 Integer Literals

IntegerLiteral:
    DecimalIntegerLiteral
    | HexIntegerLiteral
    | OctalIntegerLiteral
    | BinaryIntegerLiteral
;

fragment DecimalIntegerLiteral: DecimalNumeral IntegerTypeSuffix?;

fragment HexIntegerLiteral: HexNumeral IntegerTypeSuffix?;

fragment OctalIntegerLiteral: OctalNumeral IntegerTypeSuffix?;

fragment BinaryIntegerLiteral: BinaryNumeral IntegerTypeSuffix?;

fragment IntegerTypeSuffix: [lL];

fragment DecimalNumeral: '0' | NonZeroDigit (Digits? | Underscores Digits);

fragment Digits: Digit (DigitsAndUnderscores? Digit)?;

fragment Digit: '0' | NonZeroDigit;

fragment NonZeroDigit: [1-9];

fragment DigitsAndUnderscores: DigitOrUnderscore+;

fragment DigitOrUnderscore: Digit | '_';

fragment Underscores: '_'+;

fragment HexNumeral: '0' [xX] HexDigits;

fragment HexDigits: HexDigit (HexDigitsAndUnderscores? HexDigit)?;

fragment HexDigit: [0-9a-fA-F];

fragment HexDigitsAndUnderscores: HexDigitOrUnderscore+;

fragment HexDigitOrUnderscore: HexDigit | '_';

fragment OctalNumeral: '0' Underscores? OctalDigits;

fragment OctalDigits: OctalDigit (OctalDigitsAndUnderscores? OctalDigit)?;

fragment OctalDigit: [0-7];

fragment OctalDigitsAndUnderscores: OctalDigitOrUnderscore+;

fragment OctalDigitOrUnderscore: OctalDigit | '_';

fragment BinaryNumeral: '0' [bB] BinaryDigits;

fragment BinaryDigits: BinaryDigit (BinaryDigitsAndUnderscores? BinaryDigit)?;

fragment BinaryDigit: [01];

fragment BinaryDigitsAndUnderscores: BinaryDigitOrUnderscore+;

fragment BinaryDigitOrUnderscore: BinaryDigit | '_';

// §3.10.2 Floating-Point Literals

FloatingPointLiteral: DecimalFloatingPointLiteral | HexadecimalFloatingPointLiteral;

fragment DecimalFloatingPointLiteral:
    Digits '.' Digits? ExponentPart? FloatTypeSuffix?
    | '.' Digits ExponentPart? FloatTypeSuffix?
    | Digits ExponentPart FloatTypeSuffix?
    | Digits FloatTypeSuffix
;

fragment ExponentPart: ExponentIndicator SignedInteger;

fragment ExponentIndicator: [eE];

fragment SignedInteger: Sign? Digits;

fragment Sign: [+-];

fragment FloatTypeSuffix: [fFdD];

fragment HexadecimalFloatingPointLiteral: HexSignificand BinaryExponent FloatTypeSuffix?;

fragment HexSignificand: HexNumeral '.'? | '0' [xX] HexDigits? '.' HexDigits;

fragment BinaryExponent: BinaryExponentIndicator SignedInteger;

fragment BinaryExponentIndicator: [pP];

// §3.10.3 Boolean Literals

BooleanLiteral: 'true' | 'false';

// §3.10.4 Character Literals

CharacterLiteral: '\'' SingleCharacter '\'';

fragment SingleCharacter: ~['\\\r\n];

// §3.10.5 String Literals

StringLiteral: '"' StringCharacters? '"';

fragment StringCharacters: StringCharacter+;

fragment StringCharacter: ~["\\\r\n];

// §3.10.7 The Null Literal

NullLiteral: 'null';

// §3.11 Separators

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI   : ';';
COMMA  : ',';
DOT    : '.';

// §3.12 Operators

ASSIGN     : '=';
GT         : '>';
LT         : '<';
BANG       : '!';
TILDE      : '~';
QUESTION   : '?';
COLON      : ':';
EQUAL      : '==';
LE         : '<=';
GE         : '>=';
NOTEQUAL   : '!=';
AND        : '&&';
OR         : '||';
INC        : '++';
DEC        : '--';
ADD        : '+';
SUB        : '-';
MUL        : '*';
DIV        : '/';
BITAND     : '&';
BITOR      : '|';
CARET      : '^';
MOD        : '%';
ARROW      : '->';
COLONCOLON : '::';

ADD_ASSIGN     : '+=';
SUB_ASSIGN     : '-=';
MUL_ASSIGN     : '*=';
DIV_ASSIGN     : '/=';
AND_ASSIGN     : '&=';
OR_ASSIGN      : '|=';
XOR_ASSIGN     : '^=';
MOD_ASSIGN     : '%=';
LSHIFT_ASSIGN  : '<<=';
RSHIFT_ASSIGN  : '>>=';
URSHIFT_ASSIGN : '>>>=';

// §3.8 Identifiers (must appear after all keywords in the grammar)

Identifier: IdentifierStart IdentifierPart*;

// Dropped SMP support as ANTLR has no native support for it
fragment IdentifierStart: [a-zA-Z$];

fragment IdentifierPart: IdentifierStart;

//
// Additional symbols not defined in the lexical specification
//

AT       : '@';
ELLIPSIS : '...';

//
// Whitespace and comments
//

// Original: '\r'? '\n' | '\r'
NL: '\n' ;

SPACE: ' ';

WS: [ \t\r\n\u000C]+;

COMMENT: '/*' .*? '*/';

LINE_COMMENT: '//' ~[\r\n]*;