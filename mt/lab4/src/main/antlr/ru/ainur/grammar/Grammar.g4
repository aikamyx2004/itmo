grammar Grammar;

@header{
package ru.ainur.grammar;
}

startRule
    :   grammarName
        package?
        rule*
        ALL_CODE?
        EOF
    ;

package
    :   '@package'
        '='
        packageName
        SEMICOLON
    ;
packageName
    :   LOWER_START_IDENTIFIER
        (DOT (LOWER_START_IDENTIFIER|CAPITAL_START_IDENTIFIER))*
    ;

grammarName
    :   'grammar'
        CAPITAL_START_IDENTIFIER
        SEMICOLON
    ;


rule: nonTerm | term;

nonTerm
    :   LOWER_START_IDENTIFIER
        inh?
        synt?
        COLON
        nonTermRule
        BLOCKED_CODE?
        SEMICOLON
    ;

nonTermRule
    :   (LOWER_START_IDENTIFIER | CAPITAL_START_IDENTIFIER)*
    ;

inh
    :   INHERITED
        attributes
    ;

synt
    :
        SYNTHESIZED
        attributes
    ;

attributes
    :   LPAREN
        attribute
        (COMMA attribute)*
        RPAREN
    ;

attribute
    :   (attributeType = BASE_TYPE | CAPITAL_START_IDENTIFIER)
        LOWER_START_IDENTIFIER
    ;

term
    :   CAPITAL_START_IDENTIFIER
        COLON
        REGEX
        SEMICOLON
    ;
BASE_TYPE
    :   'byte'
    |   'short'
    |   'char'
    |   'int'
    |   'long'
    |   'float'
    |   'double'
    |   'boolean'
    ;

ALL_CODE
    :   '"""'
        .*?
        '"""'
    ;

BLOCKED_CODE
    :   START_BLOCK
        .*?
        END_BLOCK
    ;

LOWER_START_IDENTIFIER
    :   LETTER
        (CAPITAL_LETTER | LETTER | DIGIT)*
    ;

CAPITAL_START_IDENTIFIER
    :   CAPITAL_LETTER
        (CAPITAL_LETTER | LETTER | '_' | DIGIT)*
    ;

FUNCTION: '#' LOWER_START_IDENTIFIER;

REGEX : '"' .*? '"';


fragment CAPITAL_LETTER: [A-Z];
fragment LETTER: [a-z];
fragment DIGIT: [0-9];

INHERITED : '@inh';
SYNTHESIZED : '@synt';

START_BLOCK : '{';
END_BLOCK : '}';
LPAREN : '(';
RPAREN : ')';

COLON: ':';
SEMICOLON : ';';
COMMA: ',';
DOT: '.';

WS : [ \t\r\n]+ -> skip;
