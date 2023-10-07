package info.kgeorgiy.ja.mukhtarov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ParallelMapperImpl implements ParallelMapper {
    private final Queue<Runnable> queue = new ArrayDeque<>();
    private final List<Thread> threads = new ArrayList<>();

    public ParallelMapperImpl(int maxThreads) {
        for (int i = 0; i < maxThreads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        getTask().run();
                    }
                } catch (InterruptedException ignored) {
                }
            });
            thread.start();
            threads.add(thread);
        }
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        List<R> result = new ArrayList<>(Collections.nCopies(args.size(), null));
        TaskCounter counter = new TaskCounter(args.size());
        InterruptedException exception = new InterruptedException();
        IntStream.range(0, args.size()).forEach(i -> {
            synchronized (queue) {
                queue.add(() -> {
                    try {
                        result.set(i, f.apply(args.get(i)));
                        counter.decrease();
                    } catch (Throwable e) {
                        synchronized (exception) {
                            exception.addSuppressed(e);
                        }
                    }
                });
                queue.notify();
            }
        });
        counter.waitCompletion();
        if (exception.getSuppressed().length > 0) {
            throw exception;
        }
        return result;
    }

    @Override
    public void close() {
        threads.forEach(thread -> {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                thread.interrupt();
                System.err.println(e.getMessage());
            }
        });
    }

    private Runnable getTask() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            return queue.remove();
        }
    }

    private static class TaskCounter {
        private int count;

        public TaskCounter(int count) {
            this.count = count;
        }

        public synchronized void decrease() {
            count--;
            if (count == 0) {
                notify();
            }
        }

        public synchronized void waitCompletion() throws InterruptedException {
            while (count != 0) {
                wait();
            }
        }
    }
}
