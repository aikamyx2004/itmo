init(MAX_N) :- sieve(2, MAX_N).

sieve(I, MAX_N) :- I * I > MAX_N, !.
sieve(I, MAX_N) :- composite(I), !,
                   next_I(I, MAX_N).
sieve(I, MAX_N) :- J is I * I, fill_sieve(J, I, MAX_N),
                   next_I(I, MAX_N).
next_I(I, MAX_N) :- I1 is I + 1, sieve(I1, MAX_N).

fill_sieve(J, I, MAX_N) :- J > MAX_N, !.
fill_sieve(J, I, MAX_N) :- assert(composite(J)),
	                       J1 is J + I,
	                       fill_sieve(J1, I, MAX_N).

prime(N) :- N >= 2, \+ composite(N).

% :NOTE: [3, X, 11] fixed
prime_divisors(N, L) :- number(N), !, fill_divisors(N, L, 2).
prime_divisors(N, L) :- list(L), list_of_number(L), !, product(N, L).

fill_divisors(1, [], _) :- !.
fill_divisors(N, [H | T], I) :-
        next_divisor(N, I, H),
        N1 is N // H,
        fill_divisors(N1, T, H).

next_divisor(N, I, N) :- I * I > N, !.
next_divisor(N, I, I) :- 0 is mod(N, I), !.
next_divisor(N, I, R) :- I1 is I + 1,
                         next_divisor(N, I1, R).

product(1, []).
product(H, [H]) :- prime(H), !.
product(N, [H1, H2 | T]) :-
        H1 =< H2, prime(H1),
        product(R, [H2 | T]),
        N is H1 * R.

list_of_number([]) :- !.
list_of_number([H | T]) :- number(H), list_of_number(T).

prime_palindrome(N, K) :- number(N), number(K),
                          prime(N), palindrome(N, K).

palindrome(N, K) :- radix_conversion(N, K, L), reverse(L, L).

radix_conversion(0, _, []) :- !.
radix_conversion(N, K, [H | T]) :-
    H is mod(N, K),
    N1 is N // K,
    radix_conversion(N1, K, T).
