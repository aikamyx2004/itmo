package info.kgeorgiy.ja.mukhtarov.hello;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;


/**
 * Class which help to handle with {@link DatagramPacket}
 */
public class HelloUDPUtil {
    /**
     * Creates {@link DatagramPacket} from {@link String}
     *
     * @param message data of packet
     * @return {@link DatagramPacket} with given message
     */
    public static DatagramPacket createPacket(String message) {
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        return new DatagramPacket(buffer, buffer.length);
    }

    /**
     * Returns message in bytes
     * @param message string to encode
     * @return encoded byte array
     */
    public static byte[] encodeMessage(String message) {
        return message.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Decodes message from ByteBuffer
     * @param buffer buffer with message
     * @return String with message
     */
    public static String getResponseFromBuffer(ByteBuffer buffer) {
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    /**
     * Creates {@link DatagramPacket} from length of socket buffer
     *
     * @param socket socket from which shou
     * @return {@link DatagramPacket} from length of socket buffer
     */
    public static DatagramPacket createPacket(DatagramSocket socket) throws SocketException {
        byte[] buffer = new byte[socket.getReceiveBufferSize()];
        return new DatagramPacket(buffer, buffer.length);
    }

    /**
     * Creates {@link DatagramPacket} with given size
     *
     * @param bufferSize size of buffer
     * @return {@link DatagramPacket} with given size
     */
    public static DatagramPacket createPacket(int bufferSize) {
        return new DatagramPacket(new byte[bufferSize], bufferSize);
    }


    /**
     * Return string of data from packet
     *
     * @param packet from which should take
     * @return Return string of data from packet
     */
    public static String getResponse(DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    }

    /**
     * Checks if arguments of program are correct
     * @param args program arguments
     */
    public static void checkArguments(String[] args, int size) {
        checkArgsOrThrow(args == null, "Arguments can't be null");
        checkArgsOrThrow(args.length != size, String.format("There should be %d arguments", size));
        checkArgsOrThrow(Arrays.stream(args).anyMatch(Objects::isNull), "Arguments can't be null");
    }

    private static void checkArgsOrThrow(boolean need, String message) {
        if (need) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Enum of all arguments
     */
    public enum Type {
        PORT,
        THREADS,
        REQUESTS
    }

    /**
     * Return a number from a string, and if there is NumberFormatException,
     * returns IllegalArgumentException for handling it later
     * @param arg String
     * @param type type of argument
     * @return number from a string
     */
    public static int parseOrThrow(String arg, Type type) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("%s should be a number", type.name().toLowerCase()));
        }
    }
}
