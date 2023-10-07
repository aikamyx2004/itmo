package queue;


import java.util.ArrayList;
import java.util.List;

public class LinkedQueue extends AbstractQueue {
    private Node head, tail;

    public LinkedQueue() {
        head = new Node(null);
        tail = new Node(null, head, null);
        head.next = tail;
    }

    @Override
    protected void enqueueImpl(Object element) {
        tail.value = element;
        tail.next = new Node(null, tail, null);
        tail = tail.next;
    }


    @Override
    protected void dequeueImpl() {
        head = head.next;
        head.value = null;
        head.prev = null;
    }

    @Override
    public Object element() {
        return head.next.value;
    }

    @Override
    protected void removeImpl() {
        tail = tail.prev;
        tail.value = null;
        tail.next = null;
    }

    @Override
    protected List<Object> toList() {
        List<Object> list = new ArrayList<>();
        for (Node iter = head.next; iter.next != null; iter = iter.next) {
            list.add(iter.value);
        }
        return list;
    }

    @Override
    protected void pushImpl(Object element) {
        head.value = element;
        head.prev = new Node(null, null, head);
        head = head.prev;
    }

    @Override
    public Object peek() {
        return tail.prev.value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node iter = head.next; iter.next != null; iter = iter.next) {
            sb.append(iter.value).append(' ');
        }
        return sb.toString();
    }

    @Override
    protected Queue createQueue() {
        return new LinkedQueue();
    }

    private static class Node {
        private Object value;
        private Node next;
        private Node prev;

        private Node(Object value) {
            this.value = value;
        }

        private Node(Object value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }
}
