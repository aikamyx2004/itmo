package info.kgeorgiy.ja.mukhtarov.arrayset;

import java.util.*;

public class ReversedList<E> extends AbstractList<E> implements RandomAccess {
    private final List<E> list;
    private final boolean reversed;

    public ReversedList(ReversedList<E> reversedList) {
        this.list = reversedList.list;
        this.reversed = !reversedList.reversed;
    }

    public ReversedList(List<E> list) {
        this.list = list;
        this.reversed = false;
    }


    @Override
    public E get(int index) {
        return list.get(reversed ? size() - index - 1 : index);
    }

    @Override
    public Iterator<E> iterator() {
        return new ReversedListIterator<>(this);
    }

    @Override
    public int size() {
        return list.size();
    }

    private static class ReversedListIterator<T> implements Iterator<T> {
        private final ListIterator<T> iterator;
        private final boolean reversed;

        private ReversedListIterator(ReversedList<T> reversedList) {
            this.reversed = reversedList.reversed;
            this.iterator = reversedList.list.listIterator(
                    reversed ? reversedList.size() : 0);
        }

        @Override
        public boolean hasNext() {
            return reversed ? iterator.hasPrevious() : iterator.hasNext();
        }

        @Override
        public T next() {
            return reversed ? iterator.previous() : iterator.next();
        }
    }
}
