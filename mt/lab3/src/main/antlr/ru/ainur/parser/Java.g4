grammar Java;

@header {
package ru.ainur.parser;
}

file
    :   WS*
        package? WS*
        importDeclaration* WS*
        classDeclaration* WS*
        EOF
    ;

package
    :   PACKAGE WS+
        qualifiedIdentifier WS*
        SEMICOLON WS*
    ;



PACKAGE : 'package' ;


importDeclaration
    :   IMPORT WS*
        (STATIC WS*)?
        Identifier WS*
        (WS* DOT WS* Identifier)* WS*
        (DOT MULTIPLY)? WS*
        SEMICOLON WS*
    ;

MULTIPLY : '*' ;

DOT : '.' ;

STATIC : 'static' ;

IMPORT : 'import' ;


classDeclaration
    :   modifier* WS*
        CLASS WS*
        stackIdentifier WS*
        (EXTENDS WS* qualifiedIdentifier WS*)?
        (IMPLEMENTS WS* types WS*)?

        classBody WS*
    ;

IMPLEMENTS : 'implements' ;

EXTENDS : 'extends' ;

CLASS : 'class' ;


classBody
    :   START_BLOCK WS*
        classBodyDeclaration* WS*
        END_BLOCK WS*
    ;

END_BLOCK : '}' ;

START_BLOCK : '{' ;


classBodyDeclaration
    :   SEMICOLON WS*
    |   (modifier WS*)* memberDecl WS*
    ;


memberDecl // public static ...
    :    methodDeclaration WS*
    |    fieldDeclaration WS*
    |    classDeclaration WS*
    ;


methodDeclaration // public static ...
    :   typeOrVoid WS*
        stackIdentifier WS*
        parametersInBrackets WS*
        throwsList? WS*
        methodBody WS*
    ;


parametersInBrackets
    :   LPAREN WS*
        (parameters WS*)?
        RPAREN WS*
    ;

parameters
    :   parameter WS*
        (WS* COMMA WS* parameters WS*)*
    ;

parameter
    :   type WS*
        variableDeclaratorId WS*
    ;

fieldDeclaration // public static ...
    :   type WS*
        variableDeclarators WS*
        SEMICOLON WS*
    ;

methodBody
    :   block
    |   SEMICOLON
    ;

throwsList
    :   THROWS WS*
        qualifiedIdentifierList WS*
    ;

THROWS : 'throws' ;


typeOrVoid
    :   type WS*
    |   VOID WS*
    ;

VOID : 'void' ;

variableDeclarators
     : variableDeclarator WS*
       (WS* COMMA WS* variableDeclarator WS*)* WS*
     ;

variableDeclaratorId
    : Identifier
    ;

variableDeclarator
    :   variableDeclaratorId WS*
        (EQUALS WS* variableInitializer WS*)?
    ;

EQUALS : '=' ;

variableInitializer
    :   arrayInitializer WS*
    |   expression WS*
    ;

NEW : 'new' ;

arrayInitializer
    :   NEW WS*
        type WS*
        (ARRAY_LPAREN WS* expression? WS* ARRAY_RPAREN WS*)+
        (START_BLOCK WS*
        variableInitializer WS*
            (WS* COMMA WS* variableInitializer WS*)* WS*
            COMMA WS*?
         WS*
        END_BLOCK WS*)?
    ;

block
    :   START_BLOCK WS*
        blockStatement* WS*
        END_BLOCK WS*
    ;

blockStatement
    :   localVariableDeclaration WS* SEMICOLON WS*
    |   statement WS*
    ;

localVariableDeclaration
    :   type WS*
        variableDeclarators WS*
    ;

statement
    :   block WS*
    |   ifStatement WS*
    |   forStatement WS*
    |   whileStatement WS*
    |   breakStatement WS*
    |   expression WS* SEMICOLON WS*
    ;

breakStatement
    :   RETURN WS* expression? WS* SEMICOLON WS*
    |   BREAK WS* SEMICOLON WS*
    |   CONTINUE WS* SEMICOLON WS*;

CONTINUE : 'continue' ;

BREAK : 'break' ;

RETURN : 'return' ;

whileStatement
    :   WHILE WS*
        parExpression WS* // (i<0)
        statement WS*     // {...}
    ;

WHILE : 'while' ;

forStatement
    :   FOR WS*
        LPAREN WS*
            localVariableDeclaration? WS* SEMICOLON WS*   // int i = 0;
            expression? WS* SEMICOLON WS*                 // i < 0;
            expressionList? WS* WS*             // i++;
        RPAREN WS*
        statement WS*
    ;

FOR : 'for' ;

ifStatement
    :   IF WS*
        parExpression WS*           // (i < 0)
        statement WS*               // {...}
        (ELSE WS* statement)? WS* // else
    ;

ELSE : 'else' ;

IF : 'if' ;


parExpression
    : LPAREN WS* expression WS* RPAREN WS*;


expressionList
    : expression (WS* COMMA WS*  expression WS*)*;

expression
    :   primary WS*
    |   expression WS* ARRAY_LPAREN WS* expression WS* ARRAY_RPAREN WS*
    |   expression WS*
        DOT WS*
        (   Identifier
        |   methodCall
        ) WS*
    |   methodCall WS*
    |   expression WS* IncDec WS*
    |   IncDec WS* expression WS*
    |   NOT WS* expression WS*
    |   LPAREN WS* type WS* RPAREN WS* expression WS*
    |   expression WS* multiplicative WS* expression WS*
    |   expression WS* additive       WS* expression WS*
    |   expression WS* relational     WS* expression WS*
    |   expression WS* equality       WS* expression WS*
    |   expression WS* and            WS* expression WS*
    |   expression WS* or             WS* expression WS*
    |   expression WS* andAnd         WS* expression WS*
    |   expression WS* orOr           WS* expression WS*

    |   expression WS* assignment WS* expression WS*
    ;

NOT : '!' ;
primary
    : LPAREN expression RPAREN
    | literal
    | Identifier
    |   NEW WS*
            qualifiedIdentifier WS*
            (LESS_SIGN WS* types? WS* MORE_SIGN WS*)?
            arguments WS*
    ;

literal
    :   IntegerLiteral
    |   FloatLiteral
    |   StringLiteral
    |   CharLiteral
    ;

CharLiteral
    :   APOSTROPH ~['] APOSTROPH
    ;

StringLiteral
    : QUOT ~["]* QUOT
    ;


IntegerLiteral: Digits;

FloatLiteral
    :   Digits DOT Digits?
    |   DOT Digits
    ;

methodCall
    :   Identifier WS*
        arguments  WS*
    ;

arguments
    :   LPAREN WS*
        expressionList? WS*
        RPAREN WS*
    ;

type
    :   BasicType (WS* ARRAY_LPAREN WS* ARRAY_RPAREN WS*)* WS*
    |   qualifiedIdentifier (WS* ARRAY_LPAREN WS* ARRAY_RPAREN WS*)* WS*
    |   type WS*
        LESS_SIGN  WS*
        types  WS*
        MORE_SIGN WS*
    ;


LESS_SIGN: '<';
MORE_SIGN: '>';


types: type (WS* COMMA WS* type)*;

qualifiedIdentifierList
    : qualifiedIdentifier
      (WS* COMMA WS* qualifiedIdentifier WS*)* WS*
    ;

qualifiedIdentifier
    :   Identifier
        (WS* DOT WS* Identifier)*
    ;
stackIdentifier
    :   Identifier
    ;

Identifier
    :   Letter (LetterOrDigit)*
    ;


BasicType
    :   'byte'
    |   'short'
    |   'char'
    |   'int'
    |   'long'
    |   'float'
    |   'double'
    |   'boolean'
    ;

IncDec
    : '++'
    | '--'
    ;

modifier:
    'public'       |
    'protected'    |
    'private'      |
    'static'       |
    'abstract'     |
    'final'        |
    'native'       |
    'synchronized' |
    'transient'    |
    'volatile'     |
    'strictfp'
    ;

multiplicative
    :   '*'
    |   '/'
    |   '%'
    ;

additive
    :   '+'
    |   '-'
    ;

relational
    :    LESS_SIGN
    |   MORE_SIGN
    |   '<='
    |    '>='
    ;

equality
    :   '=='
    |   '!='
    ;

and
    :   '&'
    ;

andAnd
    :   '&&'
    ;

or
    :   '|'
    ;
orOr
    :   '||'
    ;
assignment
    :   '='
    |   '+='
    |   '-='
    |   '*='
    |   '/='
    |   '&='
    |   '|='
    |   '^='
    |   '%='
    ;

Digits: DIGIT+;



DIGIT: [0-9];
Letter: [a-zA-Z_];
LetterOrDigit: Letter | [0-9];
WS: [ \r\t\n];
LPAREN : '(';
RPAREN : ')';
ARRAY_LPAREN : '[';
ARRAY_RPAREN : ']';
SEMICOLON : ';' ;
COMMA: ',';
QUOT : '"';
APOSTROPH : '\'';
