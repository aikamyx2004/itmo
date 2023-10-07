import java.util.Arrays;

public class IntList {
    private int[] list;
    private int size, length;

    public IntList() {
        length = 1;
        list = new int[length];
        size = 0;
    }

    public void add(int n) {
        if (size == length) {
            list = Arrays.copyOf(list, 2 * size);
            length *= 2;
        }
        list[size++] = n;
    }

    public int size() {
        return size;
    }

    public int get(int index) {
        return list[index];
    }
}