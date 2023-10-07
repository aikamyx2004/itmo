package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    // Model: a[0,1,...n) - sequence
    // Invariant: n >= 0 && for all i (0 <= i < n): a[i] != null

    // Let immutable(n):
    // for all i (0 <= i < n): a'[i] == a[i]

    // Pred: true
    // Post: n' = n + 1 && a'[n] = element && immutable(n)
    void enqueue(Object element);

    // Pred: n > 0
    // Post: R = a[0] && n' = n && immutable(n)
    Object element();

    // Pred: n > 0
    // Post: R = a[0] && n' = n - 1 &&
    // for all i (0 <= i < n'): a'[i] = a[i + 1]

    Object dequeue();
    // Pred: true
    // Post: R = n && n' == n && immutable(n)
    int size();

    // Pred: true
    // Post: R = (n == 0) && n' == n && immutable(n)
    boolean isEmpty();


    // Pred: true
    // Post: n = 0
    void clear();

    // Pred: n > 0
    // Post: R = a'[n'] && n' = n - 1 && immutable(n')
    Object remove();

    // Pred: true
    // Post: a'[0] = element && n' = n + 1 &&
    // for all i (1 <= i < n'): a'[i] = a[i - 1]
    void push(Object element);

    // Pred: true
    // Post: R = a[n - 1] && n' = n && immutable(n)
    Object peek();

    // Pred: true
    // Post: R = |{all i(0 <= i < n) && a[i] == element | i}|
    Integer count(Object element);

    // Pred: true
    // Post: R = b[0,..k) = (a[i_0], a[i_1], ..., a[i_{k-1}]):
    // (for all j(0 <= j < k): 0 <= i_j < i_{j + 1} < n && predicate.test(b[j]) == true) &&
    // not exists l ((0 <= l < n): predicate(a[l]) == true && not exists j(0 <= j < k): (i_j == l))
    Queue filter(Predicate<Object> predicate);

    // Pred: true
    // Post: R = b[0,..n): for all i (0 <= i < n): b[i] = function(a[i])
    Queue map(Function<Object, Object> function);
}