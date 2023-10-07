package info.kgeorgiy.ja.mukhtarov.hello;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPNonblockingServer extends AbstractUDPServer {
    private Selector selector;
    private DatagramChannel channel;
    private ExecutorService executor;
    private ExecutorService pool;

    private final Queue<Response> queue = new ConcurrentLinkedQueue<>();


    @Override
    public void start(int port, int threads) {
        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(port))
                    .configureBlocking(false)
                    .register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(channel.socket().getReceiveBufferSize()));
            executor = Executors.newSingleThreadExecutor();
            pool = Executors.newFixedThreadPool(threads);
        } catch (IOException e) {
            close();
            throw new RuntimeException("Couldn't start server", e);
        }

        executor.submit(() -> {
            while (!Thread.interrupted() && channel.isOpen()) {
                try {
                    selector.select(this::handleKey, TIMEOUT);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        });
    }

    @Override
    public void close() {
        try {
            selector.close();
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't stop server", e);
        }
        closeExecutor(executor);
        closeExecutor(pool);
    }

    private void closeExecutor(ExecutorService executor) {
        if (executor != null) {
            executor.close();
        }
    }

    private void handleKey(SelectionKey key) {
        if (!key.isValid()) {
            return;
        }
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        try {
            if (key.isReadable()) {
                read(key, buffer);
            }
            if (key.isWritable()) {
                write(key);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    private void write(SelectionKey key) throws IOException {
        if (queue.isEmpty()) {
            key.interestOps(SelectionKey.OP_READ);
        } else {
            Response response = queue.remove();
            channel.send(ByteBuffer.wrap(HelloUDPUtil.encodeMessage(response.message)), response.address);
            key.interestOpsOr(SelectionKey.OP_READ);
        }
    }

    private void read(SelectionKey key, ByteBuffer buffer) {
        try {
            SocketAddress address = channel.receive(buffer.clear());
            String request = HelloUDPUtil.getResponseFromBuffer(buffer.flip());
            pool.submit(() -> {
                queue.add(new Response(addHello(request), address));
                key.interestOps(SelectionKey.OP_WRITE);
                selector.wakeup();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Response {
        private final String message;
        private final SocketAddress address;

        private Response(String message, SocketAddress address) {
            this.message = message;
            this.address = address;
        }
    }
}
