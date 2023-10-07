nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).
:- load_library('alice.tuprolog.lib.DCGLibrary').

expr_p(variable(Name)) --> [Name], { member(Name, [x, y, z]) }.

expr_p(const(Value)) -->
  { nonvar(Value, number_chars(Value, Chars)) },
  digits_p(Chars),
  { Chars = [_ | _], number_chars(Value, Chars) }.

digits_p([]) --> [].
digits_p([H | T]) -->
  { member(H, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'])},
  [H],
  digits_p(T).

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_negate) --> ['/'].

expr_p(bin(Op, A, B)) --> op_p(Op), ['('], expr_p(A), [','], expr_p(B), [')'].
