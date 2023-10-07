package queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArrayQueueModule {
    // Model: a[0,1,...n) - sequence
    // Invariant: n >= 0 && for all i (0 <= i < n): a[i] != null
    private static Object[] array = new Object[2];
    private static Map<Object, Integer> counter = new HashMap<>();
    private static int head = 0, tail = 0;

    // Let immutable(n):
    // for all i (0 <= i < n): a'[i] == a[i]


    // Pred: true
    // Post: n' = n + 1 && a'[n] = element && immutable(n)
    public static void enqueue(Object element) {
        ensureCapacity();
        addElement(tail, element);
        tail = add(tail);
    }

    // Pred: n > 0
    // Post: R = a[0] && n' = n && immutable(n)
    public static Object element() {
        return array[head];
    }

    // Pred: n > 0
    // Post: R = a[0] && n' = n - 1 &&
    // for all i (0 <= i < n'): a'[i] = a[i + 1]
    public static Object dequeue() {
        Object result = element();
        array[head] = null;
        counter.put(result, counter.getOrDefault(result, 0) - 1);
        head = add(head);
        return result;
    }

    // Pred: true
    // Post: R = n && n' == n && immutable(n)
    public static int size() {
        if (head >= tail) {
            return array[head] == null ? 0 : array.length - head + tail;
        }
        return tail - head;
    }

    // Pred: true
    // Post: R = (n == 0) && n' == n && immutable(n)
    public static boolean isEmpty() {
        return size() == 0;
    }


    // Pred: true
    // Post: n = 0
    public static void clear() {
        array = new Object[2];
        counter = new HashMap<>();
        head = tail = 0;
    }

    // Pred: n > 0
    // Post: R = a'[n'] && n' = n - 1 && immutable(n')
    public static Object remove() {
        tail = subtract(tail);
        Object result = array[tail];
        counter.put(result, counter.getOrDefault(result, 0) - 1);
        array[tail] = null;
        return result;
    }

    // Pred: true
    // Post: a'[0] = element && n' = n + 1 &&
    // for all i (1 <= i < n'): a'[i] = a[i - 1]
    public static void push(Object element) {
        ensureCapacity();
        head = subtract(head);
        addElement(head, element);
    }

    // Pred: true
    // Post: R = a[n - 1] && n' = n && immutable(n)
    public static Object peek() {
        return array[subtract(tail)];
    }

    // Pred: true
    // Post: R = |{all i(0 <= i < n) && a[i] == element| i}|
    public static Integer count(Object element) {
        return counter.getOrDefault(element, 0);
    }

    private static void addElement(int index, Object element) {
        Objects.requireNonNull(element);
        array[index] = element;
        counter.put(element, counter.getOrDefault(element, 0) + 1);
    }

    private static void ensureCapacity() {
        if (size() == array.length) {
            Object[] temp = new Object[array.length];
            for (int i = 0, pos = head; i < size(); i++) {
                temp[i] = array[pos];
                pos = add(pos);
            }
            head = 0;
            tail = array.length;
            array = Arrays.copyOf(temp, 2 * array.length);
        }
    }


    private static int add(int index) {
        return (index + 1) % array.length;
    }

    private static int subtract(int index) {
        return (index - 1 + array.length) % array.length;
    }
}