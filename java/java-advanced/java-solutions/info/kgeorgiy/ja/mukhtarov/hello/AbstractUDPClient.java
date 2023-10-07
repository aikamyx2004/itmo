package info.kgeorgiy.ja.mukhtarov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public abstract class AbstractUDPClient implements HelloClient {
    protected static final int TIMEOUT = 500;
    protected static void main(HelloClient client, String[] args) {
        try {
            HelloUDPUtil.checkArguments(args, 5);
            String host = args[0];
            int port = HelloUDPUtil.parseOrThrow(args[1], HelloUDPUtil.Type.PORT);
            String prefix = args[2];
            int threads = HelloUDPUtil.parseOrThrow(args[3], HelloUDPUtil.Type.THREADS);
            int requests = HelloUDPUtil.parseOrThrow(args[4], HelloUDPUtil.Type.REQUESTS);
            client.run(host, port, prefix, threads, requests);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.printf("Usage: %s <host> <prefix> <threads> <requests>%n", client.getClass().getSimpleName());
        }
    }

    protected boolean correctResponse(String response, String prefix, int thread, int request) {
        String message = String.format("Hello, %s", prefix);
        if (!response.startsWith(message)) {
            return false;
        }
        try {
            String[] numbers = response.substring(message.length()).split("_");
            return numbers.length == 2 && Integer.parseInt(numbers[0]) == thread && Integer.parseInt(numbers[1]) == request;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    protected String createMessage(String prefix, int thread, int request){
        return String.format("%s%d_%d", prefix, thread, request);
    }

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        run(new InetSocketAddress(host, port), prefix, threads, requests);
    }

    protected abstract void run(SocketAddress address, String prefix, int threads, int requests);
}
