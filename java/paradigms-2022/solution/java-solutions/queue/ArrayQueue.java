package queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayQueue extends AbstractQueue {
    // Model: a[0,1,...n) - sequence
    // Invariant: n >= 0 && for all i (0 <= i < n): a[i] != null
    private Object[] array = new Object[2];
    private int head = 0, tail = 0;

    // Let immutable(n):
    // for all i (0 <= i < n): a'[i] == a[i]


    // Pred: true
    // Post: n' = n + 1 && a'[n] = element && immutable(n)
    @Override
    protected void enqueueImpl(Object element) {
        ensureCapacity();
        array[tail] = element;
        tail = add(tail);
    }

    // Pred: n > 0
    // Post: R = a[0] && n' = n && immutable(n)
    @Override
    public Object element() {
        return array[head];
    }

    // Pred: n > 0
    // Post: R = a[0] && n' = n - 1 &&
    // for all i (0 <= i < n'): a'[i] = a[i + 1]
    @Override
    protected void dequeueImpl() {
        array[head] = null;
        head = add(head);
    }


    // Pred: n > 0
    // Post: R = a'[n'] && n' = n - 1 && immutable(n')
    @Override
    protected void removeImpl() {
        tail = subtract(tail);
        array[tail] = null;
    }

    // Pred: true
    // Post: a'[0] = element && n' = n + 1 &&
    // for all i (1 <= i < n'): a'[i] = a[i - 1]
    @Override
    protected void pushImpl(Object element) {
        ensureCapacity();
        head = subtract(head);
        array[head] = element;
    }

    // Pred: true
    // Post: R = a[n - 1] && n' = n && immutable(n)
    public Object peek() {
        return array[subtract(tail)];
    }

    @Override
    protected List<Object> toList() {
        List<Object> list = new ArrayList<>();
        for (int i = 0, pos = head; i < size(); i++) {
            list.add(array[pos]);
            pos = add(pos);
        }
        return list;
    }

    @Override
    protected Queue createQueue() {
        return new ArrayQueue();
    }

    private void ensureCapacity() {
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

    private int add(int index) {
        return (index + 1) % array.length;
    }

    private int subtract(int index) {
        return (index - 1 + array.length) % array.length;
    }
}