package info.kgeorgiy.ja.mukhtarov.hello;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPServer extends AbstractUDPServer {
    private DatagramSocket socket;
    private ExecutorService pool;

    /**
     * Creates HelloUDPServer and runs {@link #start(int, int)}
     * with given arguments:
     * port: name or ip of computer where server runs,
     * threads: number of threads,
     *
     * @param args array with port and threads
     */
    public static void main(String[] args) {
        main(new HelloUDPServer(), args);
    }

    @Override
    public void start(int port, int threads) {
        int bufferSize;
        try {
            socket = new DatagramSocket(port);
            bufferSize = socket.getReceiveBufferSize();
        } catch (SocketException e) {
            throw new RuntimeException("Couldn't open socket", e);
        }

        pool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                DatagramPacket packet = HelloUDPUtil.createPacket(bufferSize);
                while (!Thread.interrupted() && !socket.isClosed()) {
                    try {
                        socket.receive(packet);
                        String message = HelloUDPUtil.getResponse(packet);
                        DatagramPacket response = HelloUDPUtil.createPacket(addHello(message));
                        response.setSocketAddress(packet.getSocketAddress());
                        socket.send(response);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void close() {
        socket.close();
        pool.close();
    }
}
