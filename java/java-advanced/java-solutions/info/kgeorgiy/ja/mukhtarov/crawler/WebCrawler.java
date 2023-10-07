package info.kgeorgiy.ja.mukhtarov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;
    private final int perHost;

    /**
     * Creates WebCrawler and runs {@link #download(String, int)} by given arguments.
     * If arguments weren't given, they set by default
     *
     * <p> url [depth [downloads [extractors [perHost]]]]</p>
     * From url starts recursive downloading site till {@code depth} depth,
     * maximum sites from which it can download simultaneously - {@code downloaders},
     * maximum sites from which links can be extracted - simultaneously {@code extractors},
     * maximum sites that can be downloaded from one host - simultaneously {@code perHost}
     * <p>
     * default values:
     * <ol>
     *     <li>depth = 1</li>
     *     <li>downloaders = 10</li>
     *     <li>extractors = 10</li>
     *     <li>perHost = 5</li>
     * </ol>
     *
     * @param args command line arguments. It can be {@code  url [depth [downloads [extractors [perHost]]]]}
     * @see #download(String, int)
     */
    public static void main(String[] args) {
        if (badArguments(args)) {
            return;
        }

        try (WebCrawler crawler = new WebCrawler(new CachingDownloader(1),
                getIntegerOrDefault(args, 2, 10, "downloaders"),
                getIntegerOrDefault(args, 3, 10, "extractors"),
                getIntegerOrDefault(args, 4, 5, "perHost"))
        ) {
            Result result = crawler.download(args[0], getIntegerOrDefault(args, 1, 1, "depth"));
            System.out.println("Downloaded:");
            result.getDownloaded().forEach(System.out::println);
            if (!result.getErrors().isEmpty()) {
                System.out.println();
                System.out.println("Error occurred in:");
                result.getErrors().forEach((s, e) -> System.out.printf("During processing %s occurred:%n%s%n%n", s, e.getMessage()));
            }
        } catch (IOException e) {
            System.err.println("Couldn't create CachingDownloader" + e.getMessage());
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Creates WebCrawler by given arguments.
     *
     * @param downloader  class that implement interface {@link Downloader}. With it WebCrawler will download sites
     * @param downloaders maximum sites from which it can download simultaneously
     * @param extractors  maximum sites from which links can be extracted - simultaneously
     * @param perHost     maximum sites that can be downloaded from one host - simultaneously
     */
    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        validate(downloaders, extractors, perHost);
        this.downloader = downloader;
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors = Executors.newFixedThreadPool(extractors);
        this.perHost = perHost;
    }

    @Override
    public Result download(String url, int depth) {
        validate(depth);
        return new Crawler().download(url, depth);
    }

    @Override
    public void close() {
        shutdownAndAwaitTermination(downloaders);
        shutdownAndAwaitTermination(extractors);
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private final class Crawler {
        private final Queue<String> downloaded = new LinkedBlockingQueue<>();
        private final Set<String> usedUrls = ConcurrentHashMap.newKeySet();
        private final Map<String, IOException> errors = new ConcurrentHashMap<>();
        private final Map<String, HostBlocker> hosts = new ConcurrentHashMap<>();
        private final Queue<String> thisLevel = new LinkedBlockingQueue<>();
        private final Queue<String> nextLevel = new LinkedBlockingQueue<>();

        private Result download(String url, int depth) {
            thisLevel.add(url);
            IntStream.rangeClosed(1, depth).forEach(i -> {
                Phaser phaser = new Phaser(1);
                for (String curUrl : thisLevel) {
                    addUrl(curUrl, phaser, i != depth);
                }
                phaser.arriveAndAwaitAdvance();
                thisLevel.clear();
                thisLevel.addAll(nextLevel);
                nextLevel.clear();
            });
            return new Result(downloaded.stream().toList(), errors);
        }

        private void addUrl(String url, Phaser phaser, boolean needContinue) {
            if (!usedUrls.add(url)) {
                return;
            }
            try {
                String host = URLUtils.getHost(url);
                HostBlocker hostBlocker = hosts.computeIfAbsent(host, h -> new HostBlocker());
                phaser.register();
                hostBlocker.addTask(() -> {
                    try {
                        Document document = downloader.download(url);
                        downloaded.add(url);
                        if (needContinue) {
                            phaser.register();
                            extractors.submit(() -> {
                                try {
                                    List<String> links = document.extractLinks();
                                    nextLevel.addAll(links);
                                } catch (Exception e) {
                                    errors.put(url, (IOException) e.getCause());
                                } finally {
                                    phaser.arrive();
                                }
                            });
                        }
                    } catch (IOException e) {
                        errors.put(url, e);
                    } finally {
                        phaser.arrive();
                        hostBlocker.run();
                    }
                });
            } catch (IOException e) {
                errors.put(url, e);
            }
        }


        private class HostBlocker {
            private final Queue<Runnable> queue = new ArrayDeque<>();
            private int downloading;

            public synchronized void addTask(Runnable task) {
                if (downloading < perHost) {
                    downloaders.submit(task);
                    downloading++;
                } else {
                    queue.add(task);
                }
            }

            public synchronized void run() {
                if (queue.isEmpty()) {
                    downloading--;
                } else {
                    downloaders.submit(queue.poll());
                }
            }
        }

    }

    private static boolean badArguments(String[] args) {
        return checkMainArguments(args == null || args.length == 0, "No arguments") ||
                checkMainArguments(args.length > 5, "Too many arguments") ||
                checkMainArguments(Arrays.stream(args).anyMatch(Objects::isNull), "Arguments can't be null");
    }

    private static boolean checkMainArguments(boolean isBad, String message) {
        return checkArguments(isBad,
                String.format("%s%n%s", message, "Usage: WebCrawler url [depth [downloads [extractors [perHost]]]]"));
    }

    private static boolean checkArguments(boolean isBad, String message) {
        if (isBad) {
            error(message);
        }
        return isBad;
    }

    private static int getIntegerOrDefault(String[] args, int index, int defaultValue, String type) {
        if (index >= args.length) {
            return defaultValue;
        }
        int result = 0;
        try {
            result = Integer.parseInt(args[index]);
        } catch (NumberFormatException e) {
            errorIllegalArgument(type + " is not an integer");
        }
        if (result <= 0) {
            errorIllegalArgument(type + " is less or equal than zero");
        }
        return result;
    }

    private static void validate(int downloaders, int extractors, int perHost) {
        validateImpl(downloaders, "downloaders");
        validateImpl(extractors, "extractors");
        validateImpl(perHost, "perHost");
    }

    private static void validate(int depth) {
        validateImpl(depth, "depth");
    }

    private static void validateImpl(int value, String type) {
        if (value <= 0) {
            throw new IllegalArgumentException(type + " can't be less or equal than zero");
        }
    }

    private static void error(String message) {
        System.err.println(message);
    }

    private static void errorIllegalArgument(String message) throws IllegalArgumentException {
        error(message);
        throw new IllegalArgumentException(message);
    }
}
