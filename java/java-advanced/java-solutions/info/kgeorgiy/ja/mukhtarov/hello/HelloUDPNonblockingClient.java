package info.kgeorgiy.ja.mukhtarov.hello;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HelloUDPNonblockingClient extends AbstractUDPClient {
    @Override
    protected void run(SocketAddress address, String prefix, int threads, int requests) {
        List<DatagramChannel> channels = new ArrayList<>();
        try (Selector selector = Selector.open()) {
            for (int i = 1; i <= threads; i++) {
                DatagramChannel channel = DatagramChannel.open();
                channels.add(channel);
                channel.connect(address)
                        .configureBlocking(false)
                        .register(selector, SelectionKey.OP_WRITE, new Context(i, channel.socket()));
            }

            while (!Thread.interrupted() && !selector.keys().isEmpty()) {
                selector.select(TIMEOUT);
                Set<SelectionKey> keys = selector.selectedKeys();

                if (keys.isEmpty()) {
                    for (SelectionKey key : selector.keys()) {
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                }

                for (Iterator<SelectionKey> it = keys.iterator(); it.hasNext(); ) {
                    SelectionKey key = it.next();
                    if (key.isValid()) {
                        try {
                            if (key.isReadable()) {
                                read(key, prefix, requests);
                            } else if (key.isWritable()) {
                                write(key, prefix, address);
                            }
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    }
                    it.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't open selector", e);
        } finally {
            channels.forEach(c -> {
                try {
                    c.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


    private void read(SelectionKey key, String prefix, int totalRequests) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        Context context = (Context) key.attachment();
        channel.receive(context.buffer.clear());
        String response = HelloUDPUtil.getResponseFromBuffer(context.buffer.flip());
        if (correctResponse(response, prefix, context.currentThread, context.currentRequest + 1)) {
            System.out.println(response);
            context.currentRequest++;
        }
        if (context.currentRequest == totalRequests) {
            channel.close();
        } else {
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }


    private void write(SelectionKey key, String prefix, SocketAddress address) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        Context context = (Context) key.attachment();
        String request = createMessage(prefix, context.currentThread, context.currentRequest + 1);
        channel.send(ByteBuffer.wrap(HelloUDPUtil.encodeMessage(request)), address);
        key.interestOps(SelectionKey.OP_READ);
    }

    private static class Context {
        private final int currentThread;
        private int currentRequest = 0;
        private final ByteBuffer buffer;

        private Context(int threadNumber, DatagramSocket socket) throws SocketException {
            this.currentThread = threadNumber;
            buffer = ByteBuffer.allocate(socket.getReceiveBufferSize());
        }
    }
}
