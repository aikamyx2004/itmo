build_node(Key, Value, node(Key, Value, Prior, null, null)) :-
    rand_float(Prior).

map_build([], null).
map_build([(Key, Value) | T], TreeMap) :-
    map_build(T, R),
    map_put(R, Key, Value, TreeMap).

map_put(TreeMap, Key, Value, Result) :-
    build_node(Key, Value, Node),
    split(TreeMap, Key, L, R),
    remove_max_key(L, Key, NL),
    merge(NL, Node, M), merge(M, R, Result).

map_remove(TreeMap, Key, Result) :-
    split(TreeMap, Key, L, R),
    remove_max_key(L, Key, NL),
    merge(NL, R, Result).

map_floorKey(TreeMap, Key, FloorKey) :-
    split(TreeMap, Key, L, R),
    right_element(L, FloorKey).

map_get(node(Key, Value, _, _, _), Key, Value) :- !.

map_get(node(X, _, _, Left, _), Key, Value) :-
    Key < X, !,
    map_get(Left, Key, Value).

map_get(node(_, _, _, _, Right), Key, Value) :-
    map_get(Right, Key, Value).

% right_element(TreeMap, MaxKey)
right_element(node(Key, _, _, _, null), Key).
right_element(node(_, _, _, _, R), Key) :- right_element(R, Key).

% remove_max_key(TreeMap, Key, Result)
remove_max_key(null, _, null) :- !.
remove_max_key(node(Key, _, _, Left, _), Key, Left) :- !.

remove_max_key(node(K, V, P, L, R),
               Key,
               node(K, V, P, L, NewR)) :-
    remove_max_key(R, Key, NewR).

%split(T, Key, L, R)
split(null, _, null, null).

split(node(X, V, Y, L, R), Key,
      Left, node(X, V, Y, NewL, R)) :-
    Key < X, !,
    split(L, Key, Left, NewL).

split(node(X, V, Y, L, R), Key,
      node(X, V, Y, L, NewR), Right) :-
    split(R, Key, NewR, Right).

%merge(L, R, Result)
merge(null, T, T) :- !.
merge(T, null, T) :- !.

merge(node(K1, V1, P1, L1, R1),
      node(K2, V2, P2, L2, R2),
      node(K1, V1, P1, L1, NewR)) :-
    P1 > P2, !,
    merge(R1, node(K2, V2, P2, L2, R2), NewR).

merge(node(K1, V1, P1, L1, R1),
      node(K2, V2, P2, L2, R2),
      node(K2, V2, P2, NewL, R2)) :-
    merge(node(K1, V1, P1, L1, R1), L2, NewL).
