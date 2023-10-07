package queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArrayQueueADT {
    // Model: a[0,1,...n) - sequence
    // Invariant: n >= 0 && for all i (0 <= i < n): a[i] != null
    private Object[] array = new Object[2];
    private Map<Object, Integer> counter = new HashMap<>();
    private int head = 0, tail = 0;

    // Let immutable(n):
    // for all i (0 <= i < n): a'[i] == a[i]


    // Pred: queue != null
    // Post: n' = n + 1 && a'[n] = element && immutable(n)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        ensureCapacity(queue);
        addElement(queue, queue.tail, element);
        queue.tail = add(queue, queue.tail);
    }

    // Pred: n > 0 && queue != null
    // Post: R = a[0] && n' = n && immutable(n)
    public static Object element(ArrayQueueADT queue) {
        return queue.array[queue.head];
    }

    // Pred: n > 0 && queue != null
    // Post: R = a[0] && n' = n - 1 &&
    // for all i (0 <= i < n'): a'[i] = a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        Object result = element(queue);
        queue.counter.put(result, queue.counter.getOrDefault(result, 0) - 1);
        queue.array[queue.head] = null;
        queue.head = add(queue, queue.head);
        return result;
    }

    // Pred: queue != null
    // Post: R = n && n' == n && immutable(n)
    public static int size(ArrayQueueADT queue) {
        if (queue.head >= queue.tail) {
            return queue.array[queue.head] == null ? 0 : queue.array.length - queue.head + queue.tail;
        }
        return queue.tail - queue.head;
    }

    // Pred: queue != null
    // Post: R = (n == 0) && n' == n && immutable(n)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }


    // Pred: queue != null
    // Post: n = 0
    public static void clear(ArrayQueueADT queue) {
        queue.array = new Object[2];
        queue.counter = new HashMap<>();
        queue.head = queue.tail = 0;
    }

    // Pred: n > 0 && queue != null
    // Post: R = a'[n'] && n' = n - 1 && immutable(n')
    public static Object remove(ArrayQueueADT queue) {
        queue.tail = subtract(queue, queue.tail);
        Object result = queue.array[queue.tail];
        queue.counter.put(result, queue.counter.getOrDefault(result, 0) - 1);
        queue.array[queue.tail] = null;
        return result;
    }

    // Pred: queue != null
    // Post: a'[0] = element && n' = n + 1 &&
    // for all i (1 <= i < n'): a'[i] = a[i - 1]
    public static void push(ArrayQueueADT queue, Object element) {
        ensureCapacity(queue);
        queue.head = subtract(queue, queue.head);
        addElement(queue, queue.head, element);
    }

    // Pred: queue != null
    // Post: R = a[n - 1] && n' = n && immutable(n)
    public static Object peek(ArrayQueueADT queue) {
        return queue.array[subtract(queue, queue.tail)];
    }

    // Pred: queue != null
    // Post: R = |{all i(0 <= i < n) && a[i] == element| i}|
    public static Integer count(ArrayQueueADT queue, Object element) {
        return queue.counter.getOrDefault(element, 0);
    }


    private static void addElement(ArrayQueueADT queue, int index, Object element) {
        Objects.requireNonNull(element);
        queue.array[index] = element;
        queue.counter.put(element, queue.counter.getOrDefault(element, 0) + 1);
    }

    private static void ensureCapacity(ArrayQueueADT queue) {
        if (size(queue) == queue.array.length) {
            Object[] temp = new Object[queue.array.length];
            for (int i = 0, pos = queue.head; i < size(queue); i++) {
                temp[i] = queue.array[pos];
                pos = add(queue, pos);
            }
            queue.head = 0;
            queue.tail = queue.array.length;
            queue.array = Arrays.copyOf(temp, 2 * queue.array.length);
        }
    }

    private static int add(ArrayQueueADT queue, int index) {
        return (index + 1) % queue.array.length;
    }

    private static int subtract(ArrayQueueADT queue, int index) {
        return (index - 1 + queue.array.length) % queue.array.length;
    }
}