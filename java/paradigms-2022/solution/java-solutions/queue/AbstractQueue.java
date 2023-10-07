package queue;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    private int size;
    private Map<Object, Integer> counter = new HashMap<>();

    protected abstract void enqueueImpl(Object element);

    @Override
    public void enqueue(Object element) {
        enqueueImpl(Objects.requireNonNull(element));
        counter.put(element, counter.getOrDefault(element, 0) + 1);
        size++;
    }

    protected abstract void dequeueImpl();

    @Override
    public Object dequeue() {
        Object result = element();
        counter.put(result, counter.getOrDefault(result, 0) - 1);
        dequeueImpl();
        size--;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        while (size != 0) {
            remove();
        }
    }

    protected abstract void removeImpl();

    @Override
    public Object remove() {
        Object result = peek();
        removeImpl();
        counter.put(result, counter.getOrDefault(result, 0) - 1);
        size--;
        return result;
    }

    protected abstract void pushImpl(Object element);

    @Override
    public void push(Object element) {
        pushImpl(Objects.requireNonNull(element));
        counter.put(element, counter.getOrDefault(element, 0) + 1);
        size++;
    }


    @Override
    public Queue filter(Predicate<Object> predicate) {
        Queue queue = createQueue();
        for (Object element : toList()) {
            if (predicate.test(element)) {
                queue.enqueue(element);
            }
        }
        return queue;
    }

    @Override
    public Queue map(Function<Object, Object> function) {
        Queue queue = createQueue();
        for (Object element : toList()) {
            queue.enqueue(function.apply(element));
        }
        return queue;
    }

    @Override
    public Integer count(Object element) {
        return counter.getOrDefault(element, 0);
    }

    protected abstract List<Object> toList();

    protected abstract Queue createQueue();

}
