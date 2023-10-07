package queue;

import java.util.LinkedList;

public class Test {
    public static void main(String[] args) {
        testQueueModule();
        System.out.println("tested QueueModule");
        testQueueADT();
        System.out.println("tested QueueADT");
        testQueue();
        System.out.println("tested Queue");
        testLinkedQueue();
        System.out.println("tested LinkedQueue");
    }

    public static void testQueueModule() {
        LinkedList<Object> queue = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            if (ArrayQueueModule.size() != queue.size()) {
                throw new AssertionError("wrong size");
            }
            if (ArrayQueueModule.isEmpty() != queue.isEmpty()) {
                throw new AssertionError("wrong size");
            }
            ArrayQueueModule.enqueue(i);
            queue.add(i);
        }
        for (int i = 0; i < 10; i++) {
            if (!ArrayQueueModule.element().equals(queue.getFirst())) {
                throw new AssertionError("top of my ArrayQueueModule is bad");
            }
            if (!ArrayQueueModule.dequeue().equals(queue.pop())) {
                throw new AssertionError("top of my ArrayQueueModule is bad");
            }
        }
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.enqueue(i);
            queue.add(i);
        }
        ArrayQueueModule.clear();
        queue.clear();
        if (ArrayQueueModule.size() != queue.size()) {
            throw new AssertionError("clear is wrong");
        }
        for (int i = 0; i < 10; i++) {
            if (ArrayQueueModule.size() != queue.size()) {
                throw new AssertionError("wrong size");
            }
            if (ArrayQueueModule.isEmpty() != queue.isEmpty()) {
                throw new AssertionError("wrong size");
            }
            ArrayQueueModule.push(i);
            queue.addFirst(i);
        }
        for (int i = 0; i < 10; i++) {
            if (!ArrayQueueModule.peek().equals(queue.getLast())) {
                throw new AssertionError("front of my ArrayQueueModule is bad");
            }
            if (!ArrayQueueModule.remove().equals(queue.removeLast())) {
                throw new AssertionError("front of my ArrayQueueModule is bad");
            }
        }
    }

    public static void testQueueADT() {
        ArrayQueueADT arrayQueue = new ArrayQueueADT();
        LinkedList<Object> queue = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            if (ArrayQueueADT.size(arrayQueue) != queue.size()) {
                throw new AssertionError("wrong size");
            }
            if (ArrayQueueADT.isEmpty(arrayQueue) != queue.isEmpty()) {
                throw new AssertionError("wrong size");
            }
            ArrayQueueADT.enqueue(arrayQueue, i);
            queue.add(i);
        }
        for (int i = 0; i < 10; i++) {
            if (!ArrayQueueADT.element(arrayQueue).equals(queue.getFirst())) {
                throw new AssertionError("top of my ArrayQueueADT is bad");
            }
            if (!ArrayQueueADT.dequeue(arrayQueue).equals(queue.pop())) {
                throw new AssertionError("top of my ArrayQueueADT is bad");
            }
        }
        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.enqueue(arrayQueue, i);
            queue.add(i);
        }
        ArrayQueueADT.clear(arrayQueue);
        queue.clear();
        if (ArrayQueueADT.size(arrayQueue) != queue.size()) {
            throw new AssertionError("clear is wrong");
        }
        for (int i = 0; i < 10; i++) {
            if (ArrayQueueADT.size(arrayQueue) != queue.size()) {
                throw new AssertionError("wrong size");
            }
            if (ArrayQueueADT.isEmpty(arrayQueue) != queue.isEmpty()) {
                throw new AssertionError("wrong size");
            }
            ArrayQueueADT.push(arrayQueue, i);
            queue.addFirst(i);
        }
        for (int i = 0; i < 10; i++) {
            if (!ArrayQueueADT.peek(arrayQueue).equals(queue.getLast())) {
                throw new AssertionError("front of my ArrayQueueADT is bad");
            }
            if (!ArrayQueueADT.remove(arrayQueue).equals(queue.removeLast())) {
                throw new AssertionError("front of my ArrayQueueADT is bad");
            }
        }
    }

    public static void testQueue() {
        ArrayQueue arrayQueue = new ArrayQueue();
        testQueues(arrayQueue);
    }

    public static void testLinkedQueue() {
        LinkedQueue queue1 = new LinkedQueue();
        testQueues(queue1);
    }

    public static void testQueues(Queue queue1) {
        LinkedList<Object> queue2 = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            if (queue1.size() != queue2.size()) {
                throw new AssertionError("wrong size");
            }
            if (queue1.isEmpty() != queue2.isEmpty()) {
                throw new AssertionError("wrong size");
            }
            queue1.enqueue(i);
            queue2.add(i);
        }
        for (int i = 0; i < 10; i++) {
            if (!queue1.element().equals(queue2.getFirst())) {
                throw new AssertionError("top of my  is bad");
            }
            if (!queue1.dequeue().equals(queue2.pop())) {
                throw new AssertionError("top of my  is bad");
            }
        }
        for (int i = 0; i < 10; i++) {
            queue1.enqueue(i);
            queue2.add(i);
        }
        queue1.clear();
        queue2.clear();
        if (queue1.size() != queue2.size()) {
            throw new AssertionError("clear is wrong");
        }
        for (int i = 0; i < 10; i++) {
            if (queue1.size() != queue2.size()) {
                throw new AssertionError("wrong size");
            }
            if (queue1.isEmpty() != queue2.isEmpty()) {
                throw new AssertionError("wrong size");
            }
            queue1.push(i);
            queue2.addFirst(i);
        }
        for (int i = 0; i < 10; i++) {
            if (!queue1.peek().equals(queue2.getLast())) {
                throw new AssertionError("front of my  is bad");
            }
            if (!queue1.remove().equals(queue2.removeLast())) {
                throw new AssertionError("front of my  is bad");
            }
        }
    }

}
