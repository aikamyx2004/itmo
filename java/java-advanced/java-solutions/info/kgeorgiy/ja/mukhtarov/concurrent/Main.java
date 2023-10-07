package info.kgeorgiy.ja.mukhtarov.concurrent;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ParallelMapperImpl mapper = new ParallelMapperImpl(3);
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            a.add(i);
        }
        List<Integer> b = mapper.map((Integer w) -> w + 10, a);
        for (Integer q : b) {
            System.out.println(q);
        }

        mapper.close();
    }
}
