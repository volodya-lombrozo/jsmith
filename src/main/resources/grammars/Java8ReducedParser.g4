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

typeParameter
    : Identifier typeBound?
    ;

typeBound
    : 'extends' typeVariable
    | 'extends' classOrInterfaceType additionalBound*
    ;

additionalBound
    : '&' interfaceType
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
    | ambiguousName '.' Identifier
    ;

methodName
    : Identifier
    ;

ambiguousName
    : Identifier
    | ambiguousName '.' Identifier
    ;

/*
 * Productions from §7 (Packages)
 */

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
    : 'import' SPACE typeName ';' NL
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

/*
 * Productions from §8 (Classes)
 */

classDeclaration
    : normalClassDeclaration
    ;

normalClassDeclaration
    : (classModifier SPACE)* 'class' SPACE Identifier classBody
    ;

classModifier
    : 'public'
    | 'protected'
    | 'private'
    | 'abstract'
    | 'static'
    | 'final'
    | 'strictfp'
    ;

typeParameters
    : '<' typeParameterList '>'
    ;

typeParameterList
    : typeParameter (',' typeParameter)*
    ;

superinterfaces
    : 'implements' interfaceTypeList
    ;

interfaceTypeList
    : interfaceType (',' interfaceType)*
    ;

classBody
    : '{' classBodyDeclaration* '}' NL
    ;

classBodyDeclaration
    : classMemberDeclaration
    ;

classMemberDeclaration
    :  ';'
    ;


variableDeclaratorList
    : variableDeclarator (',' variableDeclarator)*
    ;

variableDeclarator
    : variableDeclaratorId ('=' variableInitializer)?
    ;

variableDeclaratorId
    : Identifier dims?
    ;

variableInitializer
    : expression
    | arrayInitializer
    ;

unannType
    : unannPrimitiveType
    | unannReferenceType
    ;

unannPrimitiveType
    : numericType
    | 'boolean'
    ;

unannReferenceType
    : unannClassOrInterfaceType
    | unannTypeVariable
    | unannArrayType
    ;

unannClassOrInterfaceType
    : (
        unannClassType_lfno_unannClassOrInterfaceType
        | unannInterfaceType_lfno_unannClassOrInterfaceType
    ) (
        unannClassType_lf_unannClassOrInterfaceType
        | unannInterfaceType_lf_unannClassOrInterfaceType
    )*
    ;

unannClassType
    : Identifier typeArguments?
    | unannClassOrInterfaceType '.' Identifier typeArguments?
    ;

unannClassType_lf_unannClassOrInterfaceType
    : '.' Identifier typeArguments?
    ;

unannClassType_lfno_unannClassOrInterfaceType
    : Identifier typeArguments?
    ;

unannInterfaceType_lf_unannClassOrInterfaceType
    : unannClassType_lf_unannClassOrInterfaceType
    ;

unannInterfaceType_lfno_unannClassOrInterfaceType
    : unannClassType_lfno_unannClassOrInterfaceType
    ;

unannTypeVariable
    : Identifier
    ;

unannArrayType
    : unannPrimitiveType dims
    | unannClassOrInterfaceType dims
    | unannTypeVariable dims
    ;

methodHeader
    : result methodDeclarator throws_?
    | typeParameters result methodDeclarator throws_?
    ;

result
    : unannType
    | 'void'
    ;

methodDeclarator
    : Identifier '(' formalParameterList? ')' dims?
    ;

formalParameterList
    : receiverParameter
    | formalParameters ',' lastFormalParameter
    | lastFormalParameter
    ;

formalParameters
    : formalParameter (',' formalParameter)*
    | receiverParameter (',' formalParameter)*
    ;

formalParameter
    : variableModifier* unannType variableDeclaratorId
    ;

variableModifier
    : 'final'
    ;

lastFormalParameter
    : variableModifier* unannType '...' variableDeclaratorId
    | formalParameter
    ;

receiverParameter
    : unannType (Identifier '.')? 'this'
    ;

throws_
    : 'throws' exceptionTypeList
    ;

exceptionTypeList
    : exceptionType (',' exceptionType)*
    ;

exceptionType
    : classType
    | typeVariable
    ;

methodBody
    : block
    | ';'
    ;

instanceInitializer
    : block
    ;

staticInitializer
    : 'static' block
    ;

/*
 * Productions from §9 (Interfaces)
 */

interfaceDeclaration
    : normalInterfaceDeclaration
    ;

normalInterfaceDeclaration
    : interfaceModifier* 'interface' Identifier typeParameters? extendsInterfaces? interfaceBody
    ;

interfaceModifier
    : 'public'
    | 'protected'
    | 'private'
    | 'abstract'
    | 'static'
    | 'strictfp'
    ;

extendsInterfaces
    : 'extends' interfaceTypeList
    ;

interfaceBody
    : '{' interfaceMemberDeclaration* '}'
    ;

interfaceMemberDeclaration
    : constantDeclaration
    | interfaceMethodDeclaration
    | classDeclaration
    | interfaceDeclaration
    | ';'
    ;

constantDeclaration
    : constantModifier* unannType variableDeclaratorList ';'
    ;

constantModifier
    : 'public'
    | 'static'
    | 'final'
    ;

interfaceMethodDeclaration
    : interfaceMethodModifier* methodHeader methodBody
    ;

interfaceMethodModifier
    : 'public'
    | 'abstract'
    | 'default'
    | 'static'
    | 'strictfp'
    ;

elementValue
    : conditionalExpression
    | elementValueArrayInitializer
    ;

elementValueArrayInitializer
    : '{' elementValueList? ','? '}'
    ;

elementValueList
    : elementValue (',' elementValue)*
    ;

/*
 * Productions from §10 (Arrays)
 */

arrayInitializer
    : '{' variableInitializerList? ','? '}'
    ;

variableInitializerList
    : variableInitializer (',' variableInitializer)*
    ;

/*
 * Productions from §14 (Blocks and Statements)
 */

block
    : '{' blockStatements? '}'
    ;

blockStatements
    : blockStatement+
    ;

blockStatement
    : localVariableDeclarationStatement
    | classDeclaration
    | statement
    ;

localVariableDeclarationStatement
    : localVariableDeclaration ';'
    ;

localVariableDeclaration
    : variableModifier* unannType variableDeclaratorList
    ;

statement
    : statementWithoutTrailingSubstatement
    | labeledStatement
    | ifThenStatement
    | ifThenElseStatement
    | whileStatement
    | forStatement
    ;

statementNoShortIf
    : statementWithoutTrailingSubstatement
    | labeledStatementNoShortIf
    | ifThenElseStatementNoShortIf
    | whileStatementNoShortIf
    | forStatementNoShortIf
    ;

statementWithoutTrailingSubstatement
    : block
    | emptyStatement_
    | expressionStatement
    | assertStatement
    | switchStatement
    | doStatement
    | breakStatement
    | continueStatement
    | returnStatement
    | synchronizedStatement
    | throwStatement
    | tryStatement
    ;

emptyStatement_
    : ';'
    ;

labeledStatement
    : Identifier ':' statement
    ;

labeledStatementNoShortIf
    : Identifier ':' statementNoShortIf
    ;

expressionStatement
    : statementExpression ';'
    ;

statementExpression
    : assignment
    | preIncrementExpression
    | preDecrementExpression
    | postIncrementExpression
    | postDecrementExpression
    | methodInvocation
    | classInstanceCreationExpression
    ;

ifThenStatement
    : 'if' '(' expression ')' statement
    ;

ifThenElseStatement
    : 'if' '(' expression ')' statementNoShortIf 'else' statement
    ;

ifThenElseStatementNoShortIf
    : 'if' '(' expression ')' statementNoShortIf 'else' statementNoShortIf
    ;

assertStatement
    : 'assert' expression ';'
    | 'assert' expression ':' expression ';'
    ;

switchStatement
    : 'switch' '(' expression ')' switchBlock
    ;

switchBlock
    : '{' switchBlockStatementGroup* switchLabel* '}'
    ;

switchBlockStatementGroup
    : switchLabels blockStatements
    ;

switchLabels
    : switchLabel switchLabel*
    ;

switchLabel
    : 'case' constantExpression ':'
    | 'case' enumConstantName ':'
    | 'default' ':'
    ;

enumConstantName
    : Identifier
    ;

whileStatement
    : 'while' '(' expression ')' statement
    ;

whileStatementNoShortIf
    : 'while' '(' expression ')' statementNoShortIf
    ;

doStatement
    : 'do' statement 'while' '(' expression ')' ';'
    ;

forStatement
    : basicForStatement
    | enhancedForStatement
    ;

forStatementNoShortIf
    : basicForStatementNoShortIf
    | enhancedForStatementNoShortIf
    ;

basicForStatement
    : 'for' '(' forInit? ';' expression? ';' forUpdate? ')' statement
    ;

basicForStatementNoShortIf
    : 'for' '(' forInit? ';' expression? ';' forUpdate? ')' statementNoShortIf
    ;

forInit
    : statementExpressionList
    | localVariableDeclaration
    ;

forUpdate
    : statementExpressionList
    ;

statementExpressionList
    : statementExpression (',' statementExpression)*
    ;

enhancedForStatement
    : 'for' '(' variableModifier* unannType variableDeclaratorId ':' expression ')' statement
    ;

enhancedForStatementNoShortIf
    : 'for' '(' variableModifier* unannType variableDeclaratorId ':' expression ')' statementNoShortIf
    ;

breakStatement
    : 'break' Identifier? ';'
    ;

continueStatement
    : 'continue' Identifier? ';'
    ;

returnStatement
    : 'return' expression? ';'
    ;

throwStatement
    : 'throw' expression ';'
    ;

synchronizedStatement
    : 'synchronized' '(' expression ')' block
    ;

tryStatement
    : 'try' block catches
    | 'try' block catches? finally_
    | tryWithResourcesStatement
    ;

catches
    : catchClause catchClause*
    ;

catchClause
    : 'catch' '(' catchFormalParameter ')' block
    ;

catchFormalParameter
    : variableModifier* catchType variableDeclaratorId
    ;

catchType
    : unannClassType ('|' classType)*
    ;

finally_
    : 'finally' block
    ;

tryWithResourcesStatement
    : 'try' resourceSpecification block catches? finally_?
    ;

resourceSpecification
    : '(' resourceList ';'? ')'
    ;

resourceList
    : resource (';' resource)*
    ;

resource
    : variableModifier* unannType variableDeclaratorId '=' expression
    ;

/*
 * Productions from §15 (Expressions)
 */

primary
    : (primaryNoNewArray_lfno_primary | arrayCreationExpression) primaryNoNewArray_lf_primary*
    ;

primaryNoNewArray
    : literal
    | typeName ('[' ']')* '.' 'class'
    | 'void' '.' 'class'
    | 'this'
    | typeName '.' 'this'
    | '(' expression ')'
    | classInstanceCreationExpression
    | fieldAccess
    | arrayAccess
    | methodInvocation
    | methodReference
    ;

primaryNoNewArray_lf_arrayAccess
    :
    ;

primaryNoNewArray_lfno_arrayAccess
    : literal
    | typeName ('[' ']')* '.' 'class'
    | 'void' '.' 'class'
    | 'this'
    | typeName '.' 'this'
    | '(' expression ')'
    | classInstanceCreationExpression
    | fieldAccess
    | methodInvocation
    | methodReference
    ;

primaryNoNewArray_lf_primary
    : classInstanceCreationExpression_lf_primary
    | fieldAccess_lf_primary
    | arrayAccess_lf_primary
    | methodInvocation_lf_primary
    | methodReference_lf_primary
    ;

primaryNoNewArray_lf_primary_lf_arrayAccess_lf_primary
    :
    ;

primaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary
    : classInstanceCreationExpression_lf_primary
    | fieldAccess_lf_primary
    | methodInvocation_lf_primary
    | methodReference_lf_primary
    ;

primaryNoNewArray_lfno_primary
    : literal
    | typeName ('[' ']')* '.' 'class'
    | unannPrimitiveType ('[' ']')* '.' 'class'
    | 'void' '.' 'class'
    | 'this'
    | typeName '.' 'this'
    | '(' expression ')'
    | classInstanceCreationExpression_lfno_primary
    | fieldAccess_lfno_primary
    | arrayAccess_lfno_primary
    | methodInvocation_lfno_primary
    | methodReference_lfno_primary
    ;

primaryNoNewArray_lfno_primary_lf_arrayAccess_lfno_primary
    :
    ;

primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary
    : literal
    | typeName ('[' ']')* '.' 'class'
    | unannPrimitiveType ('[' ']')* '.' 'class'
    | 'void' '.' 'class'
    | 'this'
    | typeName '.' 'this'
    | '(' expression ')'
    | classInstanceCreationExpression_lfno_primary
    | fieldAccess_lfno_primary
    | methodInvocation_lfno_primary
    | methodReference_lfno_primary
    ;

classInstanceCreationExpression
    : 'new' typeArguments? Identifier ('.' Identifier)* typeArgumentsOrDiamond? '(' argumentList? ')' classBody?
    | expressionName '.' 'new' typeArguments? Identifier typeArgumentsOrDiamond? '(' argumentList? ')' classBody?
    | primary '.' 'new' typeArguments? Identifier typeArgumentsOrDiamond? '(' argumentList? ')' classBody?
    ;

classInstanceCreationExpression_lf_primary
    : '.' 'new' typeArguments?  Identifier typeArgumentsOrDiamond? '(' argumentList? ')' classBody?
    ;

classInstanceCreationExpression_lfno_primary
    : 'new' typeArguments?  Identifier ('.'  Identifier)* typeArgumentsOrDiamond? '(' argumentList? ')' classBody?
    | expressionName '.' 'new' typeArguments?  Identifier typeArgumentsOrDiamond? '(' argumentList? ')' classBody?
    ;

typeArgumentsOrDiamond
    : typeArguments
    | '<' '>'
    ;

fieldAccess
    : primary '.' Identifier
    | 'super' '.' Identifier
    | typeName '.' 'super' '.' Identifier
    ;

fieldAccess_lf_primary
    : '.' Identifier
    ;

fieldAccess_lfno_primary
    : 'super' '.' Identifier
    | typeName '.' 'super' '.' Identifier
    ;

arrayAccess
    : (expressionName '[' expression ']' | primaryNoNewArray_lfno_arrayAccess '[' expression ']') (
        primaryNoNewArray_lf_arrayAccess '[' expression ']'
    )*
    ;

arrayAccess_lf_primary
    : primaryNoNewArray_lf_primary_lfno_arrayAccess_lf_primary '[' expression ']' (
        primaryNoNewArray_lf_primary_lf_arrayAccess_lf_primary '[' expression ']'
    )*
    ;

arrayAccess_lfno_primary
    : (
        expressionName '[' expression ']'
        | primaryNoNewArray_lfno_primary_lfno_arrayAccess_lfno_primary '[' expression ']'
    ) (primaryNoNewArray_lfno_primary_lf_arrayAccess_lfno_primary '[' expression ']')*
    ;

methodInvocation
    : methodName '(' argumentList? ')'
    | typeName '.' typeArguments? Identifier '(' argumentList? ')'
    | expressionName '.' typeArguments? Identifier '(' argumentList? ')'
    | primary '.' typeArguments? Identifier '(' argumentList? ')'
    | 'super' '.' typeArguments? Identifier '(' argumentList? ')'
    | typeName '.' 'super' '.' typeArguments? Identifier '(' argumentList? ')'
    ;

methodInvocation_lf_primary
    : '.' typeArguments? Identifier '(' argumentList? ')'
    ;

methodInvocation_lfno_primary
    : methodName '(' argumentList? ')'
    | typeName '.' typeArguments? Identifier '(' argumentList? ')'
    | expressionName '.' typeArguments? Identifier '(' argumentList? ')'
    | 'super' '.' typeArguments? Identifier '(' argumentList? ')'
    | typeName '.' 'super' '.' typeArguments? Identifier '(' argumentList? ')'
    ;

argumentList
    : expression (',' expression)*
    ;

methodReference
    : expressionName '::' typeArguments? Identifier
    | referenceType '::' typeArguments? Identifier
    | primary '::' typeArguments? Identifier
    | 'super' '::' typeArguments? Identifier
    | typeName '.' 'super' '::' typeArguments? Identifier
    | classType '::' typeArguments? 'new'
    | arrayType '::' 'new'
    ;

methodReference_lf_primary
    : '::' typeArguments? Identifier
    ;

methodReference_lfno_primary
    : expressionName '::' typeArguments? Identifier
    | referenceType '::' typeArguments? Identifier
    | 'super' '::' typeArguments? Identifier
    | typeName '.' 'super' '::' typeArguments? Identifier
    | classType '::' typeArguments? 'new'
    | arrayType '::' 'new'
    ;

arrayCreationExpression
    : 'new' primitiveType dimExprs dims?
    | 'new' classOrInterfaceType dimExprs dims?
    | 'new' primitiveType dims arrayInitializer
    | 'new' classOrInterfaceType dims arrayInitializer
    ;

dimExprs
    : dimExpr dimExpr*
    ;

dimExpr
    : '[' expression ']'
    ;

constantExpression
    : expression
    ;

expression
    : lambdaExpression
    | assignmentExpression
    ;

lambdaExpression
    : lambdaParameters '->' lambdaBody
    ;

lambdaParameters
    : Identifier
    | '(' formalParameterList? ')'
    | '(' inferredFormalParameterList ')'
    ;

inferredFormalParameterList
    : Identifier (',' Identifier)*
    ;

lambdaBody
    : expression
    | block
    ;

assignmentExpression
    : conditionalExpression
    | assignment
    ;

assignment
    : leftHandSide assignmentOperator expression
    ;

leftHandSide
    : expressionName
    | fieldAccess
    | arrayAccess
    ;

assignmentOperator
    : '='
    | '*='
    | '/='
    | '%='
    | '+='
    | '-='
    | '<<='
    | '>>='
    | '>>>='
    | '&='
    | '^='
    | '|='
    ;

conditionalExpression
    : conditionalOrExpression
    | conditionalOrExpression '?' expression ':' conditionalExpression
    ;

conditionalOrExpression
    : conditionalAndExpression
    | conditionalOrExpression '||' conditionalAndExpression
    ;

conditionalAndExpression
    : inclusiveOrExpression
    | conditionalAndExpression '&&' inclusiveOrExpression
    ;

inclusiveOrExpression
    : exclusiveOrExpression
    | inclusiveOrExpression '|' exclusiveOrExpression
    ;

exclusiveOrExpression
    : andExpression
    | exclusiveOrExpression '^' andExpression
    ;

andExpression
    : equalityExpression
    | andExpression '&' equalityExpression
    ;

equalityExpression
    : relationalExpression
    | equalityExpression '==' relationalExpression
    | equalityExpression '!=' relationalExpression
    ;

relationalExpression
    : shiftExpression
    | relationalExpression '<' shiftExpression
    | relationalExpression '>' shiftExpression
    | relationalExpression '<=' shiftExpression
    | relationalExpression '>=' shiftExpression
    | relationalExpression 'instanceof' referenceType
    ;

shiftExpression
    : additiveExpression
    | shiftExpression '<' '<' additiveExpression
    | shiftExpression '>' '>' additiveExpression
    | shiftExpression '>' '>' '>' additiveExpression
    ;

additiveExpression
    : multiplicativeExpression
    | additiveExpression '+' multiplicativeExpression
    | additiveExpression '-' multiplicativeExpression
    ;

multiplicativeExpression
    : unaryExpression
    | multiplicativeExpression '*' unaryExpression
    | multiplicativeExpression '/' unaryExpression
    | multiplicativeExpression '%' unaryExpression
    ;

unaryExpression
    : preIncrementExpression
    | preDecrementExpression
    | '+' unaryExpression
    | '-' unaryExpression
    | unaryExpressionNotPlusMinus
    ;

preIncrementExpression
    : '++' unaryExpression
    ;

preDecrementExpression
    : '--' unaryExpression
    ;

unaryExpressionNotPlusMinus
    : postfixExpression
    | '~' unaryExpression
    | '!' unaryExpression
    | castExpression
    ;

postfixExpression
    : (primary | expressionName) (
        postIncrementExpression_lf_postfixExpression
        | postDecrementExpression_lf_postfixExpression
    )*
    ;

postIncrementExpression
    : postfixExpression '++'
    ;

postIncrementExpression_lf_postfixExpression
    : '++'
    ;

postDecrementExpression
    : postfixExpression '--'
    ;

postDecrementExpression_lf_postfixExpression
    : '--'
    ;

castExpression
    : '(' primitiveType ')' unaryExpression
    | '(' referenceType additionalBound* ')' unaryExpressionNotPlusMinus
    | '(' referenceType additionalBound* ')' lambdaExpression
    ;