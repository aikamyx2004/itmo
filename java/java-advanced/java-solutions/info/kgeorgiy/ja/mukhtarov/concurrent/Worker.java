package info.kgeorgiy.ja.mukhtarov.concurrent;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Worker is a class that implements {@link Runnable}
 * and creates for paralleling iterative operations on list
 *
 * @param <T> Type of objects inside list
 * @param <R> Resulting type
 */
public class Worker<T, R> implements Runnable {
    private final Stream<? extends T> values;
    private final Function<Stream<? extends T>, R> function;
    private R result;

    /**
     * Constructor that creates {@link Worker} from {@link List}
     * and {@link Function}
     *
     * @param values   list on which should be found result
     * @param function function to find result on values
     */
    public Worker(Stream<? extends T> values, Function<Stream<? extends T>, R> function) {
        this.values = values;
        this.function = function;
    }

    @Override
    public void run() {
        result = function.apply(values);
    }

    /**
     * Returns result after {@link #run()} worked.
     * Returns null, if {@link #run()} was not called.
     *
     * @return result after {@link #run()} worked
     */
    public R getResult() {
        return result;
    }
}