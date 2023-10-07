package game;

public class Pair implements Comparable<Pair>{
    private final int first;
    private final int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public int compareTo(Pair o) {
        if(first == o.getFirst())
            return Integer.compare(second, o.second);
        return Integer.compare(first, o.first);
    }
}
