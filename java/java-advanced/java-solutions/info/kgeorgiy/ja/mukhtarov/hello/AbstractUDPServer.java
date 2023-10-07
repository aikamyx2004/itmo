package info.kgeorgiy.ja.mukhtarov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

public abstract class AbstractUDPServer implements HelloServer {
    protected static final int TIMEOUT = 500;

    protected static void main(HelloServer server, String[] args) {
        try {
            HelloUDPUtil.checkArguments(args, 2);
            int port = HelloUDPUtil.parseOrThrow(args[0], HelloUDPUtil.Type.PORT);
            int threads = HelloUDPUtil.parseOrThrow(args[1], HelloUDPUtil.Type.THREADS);
            server.start(port, threads);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.printf("Usage: %s <host> <port>%n", server.getClass().getSimpleName());
        }
    }

    protected String addHello(String request) {
        return "Hello, " + request;
    }
}
