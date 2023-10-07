public class Count {
    private final IntList list;
    private int count;

    public Count() {
        list = new IntList();
        count = 0;
    }

    public IntList getList() {
        return list;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int c) {
        count += c;
    }

    public void add(int n) {
        list.add(n);
    }
}
