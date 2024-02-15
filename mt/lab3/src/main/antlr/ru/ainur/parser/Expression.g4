grammar Expression;

@header {
package ru.ainur.parser;
}
// parser

line : expression EOF;

expression: term expression1;
expression1: PLUS term expression1 | MINUS term expression1 | ;
term: factor term1;
term1: MULTIPLY factor term1 | ;
factor: LPAREN expression RPAREN | MINUS factor  | FUNC factor | NUMBER;

// lexer

PLUS: '+';
MINUS: '-';
MULTIPLY: '*';
LPAREN: '(';
RPAREN: ')';
FUNC: ([a-z]|[A-Z])+;
NUMBER: ([0-9])+;
WS : [ \r\n\t] + -> skip;
