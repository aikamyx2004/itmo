package info.kgeorgiy.ja.mukhtarov.hello;


import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HelloUDPClient extends AbstractUDPClient {

    /**
     * Creates HelloUDPClient and runs {@link #run(String, int, String, int, int)}
     * with given arguments:
     * host: name or ip of computer where server runs,
     * prefix: prefix of each request,
     * threads: number of threads,
     * requests: how many requests in each thread.
     *
     * @param args array of host, prefix, threads, requests
     */
    public static void main(String[] args) {
        main(new HelloUDPClient(), args);
    }

    protected void run(SocketAddress address, String prefix, int threads, int requests) {
        try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
            IntStream.rangeClosed(1, threads).forEach(currentThread -> pool.submit(() -> {
                try (DatagramSocket socket = new DatagramSocket()) {
                    socket.setSoTimeout(TIMEOUT);
                    socket.connect(address);
                    DatagramPacket packet, out;
                    out = HelloUDPUtil.createPacket(socket);
                    for (int currentRequest = 1; currentRequest <= requests; currentRequest++) {
                        String message = createMessage(prefix, currentThread, currentRequest);
                        packet = HelloUDPUtil.createPacket(message);
                        while (true) {
                            try {
                                socket.send(packet);
                                socket.receive(out);
                                String responseMessage = HelloUDPUtil.getResponse(out);
                                if (correctResponse(responseMessage, prefix, currentThread, currentRequest)) {
                                    System.out.println(responseMessage);
                                    break;
                                }
                            } catch (IOException ignored) {
                            }
                        }
                    }
                } catch (SocketException e) {
                    throw new RuntimeException(String.format("Couldn't create socket in %s thread", currentThread), e);
                }
            }));
        }
    }
}