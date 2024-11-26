/*
 * [The "BSD license"]
 *  Copyright (c) 2014 Terence Parr
 *  Copyright (c) 2014 Sam Harwell
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

// ATTENTION! This is the significantly modifed copy of the Java8Parser.g4 file from the ANTLRv4 grammar distribution.
// The original file is located in the ANTLRv4 grammar distribution at the following path:
// https://github.com/antlr/grammars-v4/blob/master/java/java8/Java8Parser.g4

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

parser grammar Java8ReducedParser;

options {
    tokenVocab = Java8ReducedLexer;
}

/*
 * Productions from §3 (Lexical Structure)
 */

literal
    : IntegerLiteral
    | FloatingPointLiteral
    | BooleanLiteral
    | CharacterLiteral
    | StringLiteral
    | NullLiteral
    ;

/*
 * Productions from §4 (Types, Values, and Variables)
 */

primitiveType
    : numericType
    | 'boolean'
    ;

numericType
    : integralType
    | floatingPointType
    ;

integralType
    : 'byte'
    | 'short'
    | 'int'
    | 'long'
    | 'char'
    ;

floatingPointType
    : 'float'
    | 'double'
    ;

referenceType
    : classOrInterfaceType
    | typeVariable
    | arrayType
    ;

classOrInterfaceType
    : (classType_lfno_classOrInterfaceType | interfaceType_lfno_classOrInterfaceType) (
        classType_lf_classOrInterfaceType
        | interfaceType_lf_classOrInterfaceType
    )*
    ;

classType
    : Identifier typeArguments?
    | classOrInterfaceType '.' Identifier typeArguments?
    ;

classType_lf_classOrInterfaceType
    : '.' Identifier typeArguments?
    ;

classType_lfno_classOrInterfaceType
    : Identifier typeArguments?
    ;

interfaceType
    : classType
    ;

interfaceType_lf_classOrInterfaceType
    : classType_lf_classOrInterfaceType
    ;

interfaceType_lfno_classOrInterfaceType
    : classType_lfno_classOrInterfaceType
    ;

typeVariable
    : Identifier
    ;

arrayType
    : primitiveType dims
    | classOrInterfaceType dims
    | typeVariable dims
    ;

dims
    : '[' ']' ('[' ']')*
    ;

typeArguments
    : '<' typeArgumentList '>'
    ;

typeArgumentList
    : typeArgument (',' typeArgument)*
    ;

typeArgument
    : referenceType
    | wildcard
    ;

wildcard
    : '?' wildcardBounds?
    ;

wildcardBounds
    : 'extends' referenceType
    | 'super' referenceType
    ;

/*
 * Productions from §6 (Names)
 */

packageName
    : Identifier
    | packageName '.' Identifier
    ;

typeName
    : Identifier
    | packageOrTypeName '.' Identifier
    ;

packageOrTypeName
    : Identifier
    | packageOrTypeName '.' Identifier
    ;

expressionName
    : Identifier
//    | ambiguousName '.' Identifier
    ;

compilationUnit
    : packageDeclaration? importDeclaration* typeDeclaration EOF
    ;

packageDeclaration
    : 'package' SPACE packageName ';' NL
    ;

importDeclaration
    : singleTypeImportDeclaration
    | typeImportOnDemandDeclaration
    | singleStaticImportDeclaration
    | staticImportOnDemandDeclaration
    ;

singleTypeImportDeclaration
    : 'import' SPACE typeName '.' typeName ';' NL
    ;

typeImportOnDemandDeclaration
    : 'import' SPACE packageOrTypeName '.' '*' ';' NL
    ;

singleStaticImportDeclaration
    : 'import' SPACE 'static' SPACE typeName '.' Identifier ';' NL
    ;

staticImportOnDemandDeclaration
    : 'import' SPACE 'static' SPACE typeName '.' '*' ';' NL
    ;

typeDeclaration
    : classDeclaration
    ;

classDeclaration
    : normalClassDeclaration
    ;

normalClassDeclaration
    : (inheritanceModifier SPACE)? ('strictfp' SPACE)?  'class' SPACE Identifier classBody
    ;

inheritanceModifier
    : 'final'
    | 'abstract'
    ;

classBody
    : '{' classBodyDeclaration+ '}' NL
    ;

classBodyDeclaration
    : classMemberDeclaration
    ;

classMemberDeclaration
    :  ';'
    | methodDeclaration
    ;

methodDeclaration
    : NL 'public' SPACE 'void' SPACE Identifier '(' ')' methodBody NL
    ;

methodBody
    : methodBlock
    ;

methodBlock
    : '{' expressionStatement+ '}'
    ;

expressionStatement
    : NL statementExpression ';' NL
    ;

vardef
    : 'long' SPACE /* $jsmith-variable-declaration */ Identifier
    ;

statementExpression
    : vardef
    | /* $jsmith-variable-initialization */ assignment
    ;

assignment
    : /* $jsmith-variable-assignment */ leftHandSide '=' simplifiedExpression
    ;

leftHandSide
    : expressionName
    ;

simplifiedExpression
    : simplifiedExpression SPACE '+' SPACE IntegerLiteral
    | /* $jsmith-variable-usage */ Identifier
    | IntegerLiteral
    ;
