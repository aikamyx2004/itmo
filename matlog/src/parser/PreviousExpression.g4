grammar PreviousExpression;
//
//@header {
//package parser;
//import expression.*;
//}
//
//lineRecursive returns [LineRecursive expr] : cont1=contextRecursive TOURNIQUET exp=expression {$expr=new LineRecursive($cont1.cont, $exp.expr);}
//                               | TOURNIQUET exp1=expression {$expr = new LineRecursive(ContextRecursive.emptyContext(), $exp1.expr);};
//
//contextRecursive returns [ContextRecursive cont] : exp=expression {$cont=new ContextRecursive($exp.expr, null);}
//                               | cont1=contextRecursive COMMA exp1=expression {$cont=new ContextRecursive($exp1.expr, $cont1.cont);};
//
//
//expression returns [Expression expr] : disj=disjunction {$expr = $disj.expr;}
//                                     | disj1=disjunction IMPLIES exp1=expression {$expr = new Implication($disj1.expr, $exp1.expr);};
//
//disjunction returns [Expression expr] : conj=conjunction {$expr = $conj.expr;}
//                                      | disj1=disjunction OR conj1=conjunction {$expr = new Disjunction($disj1.expr, $conj1.expr);};
//
//conjunction returns [Expression expr] : neg=negation {$expr = $neg.expr;}
//                                      | conj1=conjunction AND neg1=negation{$expr = new Conjunction($conj1.expr, $neg1.expr);};
//
//negation returns [Expression expr] : var=variable {$expr = $var.expr;}
//                                   | FALSE {$expr = new False();}
//                                   | NOT neg=negation {$expr = new Negation($neg.expr);}
//                                   | OB exp=expression CB {$expr = $exp.expr;};
//
//variable returns [Expression expr] : VAR {$expr = new Variable($VAR.text);};
//WS : [ \t\r]+ -> skip ;
//IMPLIES : '->';
//OR : '|';
//AND : '&';
//NOT : '!';
//OB : '(';
//CB : ')';
//COMMA : ',';
//TOURNIQUET: '|-';
//FALSE: '_|_';
//VAR : [A-Z]([A-Z0-9'])*;
