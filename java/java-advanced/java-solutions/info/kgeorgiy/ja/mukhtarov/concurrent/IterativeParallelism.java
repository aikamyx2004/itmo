package info.kgeorgiy.ja.mukhtarov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class for paralleling iterative counting
 */
public class IterativeParallelism implements ListIP {
    private final ParallelMapper mapper;

    /**
     * Default constructor
     */
    public IterativeParallelism() {
        this.mapper = null;
    }

    /**
     * Constructor from {@link ParallelMapper}
     */
    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        Function<Stream<? extends T>, T> max = stream -> stream.max(comparator).orElse(null);
        return compute(threads, values, max, max);
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return compute(threads, values,
                stream -> stream.allMatch(predicate),
                stream -> stream.allMatch(x -> x)
        );
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }

    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return compute(threads, values,
                stream -> stream.filter(predicate).count(),
                stream -> stream.mapToLong(x -> x).sum())
                .intValue();
    }

    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return compute(threads, values,
                stream -> stream.filter(predicate).collect(Collectors.toList()),
                stream -> stream.flatMap(Collection::stream).toList());
    }

    @Override
    public <T, U> List<U> map(int threads, List<? extends T> values, Function<? super T, ? extends U> f) throws InterruptedException {
        return compute(threads, values,
                stream -> stream.map(f).collect(Collectors.toList()),
                stream -> stream.flatMap(Collection::stream).toList());
    }

    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        return compute(threads, values,
                stream -> stream.map(Object::toString).collect(Collectors.joining()),
                stream -> stream.collect(Collectors.joining()));
    }

    private <T, R> R compute(int threads, List<? extends T> values,
                             Function<Stream<? extends T>, R> function,
                             Function<Stream<? extends R>, R> union) throws InterruptedException {
        if (threads == 0) {
            throw new IllegalArgumentException("at least 1 thread required");
        }
        threads = Math.min(threads, values.size());
        List<Stream<? extends T>> splittedValues = split(values, threads);
        if (mapper != null) {
            return union.apply(mapper.map(function, splittedValues).stream());
        } else {
            List<Worker<T, R>> workers = new ArrayList<>();
            List<Thread> threadList = new ArrayList<>();
            splittedValues.forEach(l -> {
                Worker<T, R> worker = new Worker<>(l, function);
                workers.add(worker);
                threadList.add(new Thread(worker));
            });

            threadList.forEach(Thread::start);

            InterruptedException exceptions = new InterruptedException();
            for (Thread thread : threadList) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    thread.interrupt();
                    exceptions.addSuppressed(e);
                }
            }
            if (exceptions.getSuppressed().length > 0) {
                throw new InterruptedException("Error in joining threads: " + exceptions);
            }
            return union.apply(workers.stream().map(Worker::getResult));
        }
    }

    private <T> List<Stream<? extends T>> split(List<? extends T> values, int parts) {
        List<Stream<? extends T>> result = new ArrayList<>();
        int threadSize = values.size() / parts;
        int cntBigger = values.size() % parts;
        for (int i = 0, begin = 0; i < parts; i++) {
            int size = i < cntBigger ? 1 + threadSize : threadSize;
            result.add(values.subList(begin, begin + size).stream());
            begin += size;
        }
        return result;
    }
}
