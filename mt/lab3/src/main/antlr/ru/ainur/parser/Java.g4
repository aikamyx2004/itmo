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
    :   'package' WS+
        qualifiedIdentifier WS*
        ';' WS*
    ;


importDeclaration
    :   'import' WS*
        ('static' WS*)?
        Identifier WS*
        (WS* '.' WS* Identifier)* WS*
        ('.' '*')? WS*
    ;


classDeclaration
    :   modifier* WS*
        'class' WS*
        Identifier WS*
        classBody
    ;


classBody
    :   '{' WS*
         classBodyDeclaration* WS*
        '}' WS*
    ;


classBodyDeclaration
    :   ';' WS*
    |   (modifier WS*)* memberDecl WS*
    ;


memberDecl // public static ...
    :   methodDeclaration WS*
    |   fieldDeclaration WS*
    ;


methodDeclaration // public static ...
    :   typeOrVoid WS*
        Identifier WS*
        parametersInBrackets WS*
        throwsList? WS*
        methodBody WS*
    ;


parametersInBrackets
    :   '(' WS*
        (parameters WS*)?
        ')' WS*
    ;

parameters
    :   parameter WS*
        (WS* ',' WS* parameters WS*)*
    ;

parameter
    :   type WS*
        variableDeclaratorId WS*
    ;

fieldDeclaration // public static ...
    :   type WS*
        variableDeclarators WS*
        ';' WS*
    ;

methodBody
    :   block
    |   ';'
    ;

throwsList
    :   'throws' WS*
        qualifiedIdentifierList WS*
    ;


typeOrVoid
    :   type WS*
    |   'void' WS*
    ;

variableDeclarators
     : variableDeclarator WS*
       (WS* ',' WS* variableDeclarator WS*)* WS*
     ;

variableDeclaratorId
    : Identifier WS*
    ;

variableDeclarator
    :   variableDeclaratorId WS*
        ('=' WS* variableInitializer WS*)?
    ;

variableInitializer
    :   arrayInitializer WS*
    |   expression WS*
    |   'new' WS*
        qualifiedIdentifier WS*
        ('<' WS* types? WS* '>' WS*)?
        arguments WS*
    ;

arrayInitializer
    :   '{' WS*
        (variableInitializer WS*
            (WS* ',' WS* variableInitializer WS*)* WS*
            ',' WS*?
        )? WS*
        '}' WS*
    ;

block
    :   '{' WS*
        blockStatement* WS*
        '}' WS*
    ;

blockStatement
    :   localVariableDeclaration WS* ';' WS*
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
    |   'return' WS* expression? WS* ';' WS*
    |   'break' WS* ';' WS*
    |   'continue' WS* ';' WS*
    |   expression WS* ';' WS*
    ;

whileStatement
    :   'while' WS*
        parExpression WS* // (i<0)
        statement WS*     // {...}
    ;

forStatement
    :   'for' WS*
        '(' WS*
            localVariableDeclaration? WS* ';' WS*   // int i = 0;
            expression? WS* ';' WS*                 // i < 0;
            expressionList? WS* ';' WS*             // i++;
        ')' WS*
        statement WS*
    ;

ifStatement
    :   'if' WS*
        parExpression WS*           // (i < 0)
        statement WS*               // {...}
        ('else' WS* statement)? WS* // else
    ;


parExpression
    : '(' WS* expression WS* ')' WS*;


expressionList
    : expression (WS* ',' WS*  expression)* WS*;

expression
    :   primary WS*
    |   expression WS*
        '.' WS*
        (   Identifier
        |   methodCall
        ) WS*
    |   methodCall WS*
    |   expression WS* IncDec WS*
    |   IncDec WS* expression WS*

    |   expression WS* multiplicative WS* expression WS*
    |   expression WS* additive       WS* expression WS*
    |   expression WS* relational     WS* expression WS*
    |   expression WS* equality       WS* expression WS*
    |   expression WS* and            WS* expression WS*
    |   expression WS* or             WS* expression WS*
    |   expression WS* andAnd         WS* expression WS*
    |   expression WS* orOr           WS* expression WS*

    |   <assoc = right> expression WS* assignment WS* expression WS*
    ;
primary
    : '(' expression ')'
    | literal
    | Identifier
    ;

literal
    :   integerLiteral
    |   floatLiteral
    ;

integerLiteral: digits;

floatLiteral
    :   digits '.' digits?
    |   '.' digits
    ;

methodCall
    :   Identifier WS*
        arguments  WS*
    ;

arguments
    :   '(' WS*
        expressionList? WS*
        ')' WS*
    ;

type
    :   BasicType (WS* '[' WS* ']' WS*)* WS*
    |   Identifier (WS* '[' WS* ']' WS*)* WS*
    |   type WS*
        '<'  WS*
        types  WS*
        '>' WS*
    ;

types: type (WS* ',' WS* type)*;

qualifiedIdentifierList
    : qualifiedIdentifier
      (WS* ',' WS* qualifiedIdentifier WS*)* WS*
    ;

qualifiedIdentifier
    :   Identifier
        (WS* '.' WS* Identifier)*
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
    :    '<'
    |   '>'
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

Digit : [0-9];
digits: Digit+;

Letter: [a-zA-Z_];
LetterOrDigit: Letter | Digit;
WS: [ \r\t\n];
