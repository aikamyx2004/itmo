package info.kgeorgiy.ja.mukhtarov.arrayset;


import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private final ReversedList<E> list;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this(Collections.emptyList());
    }

    public ArraySet(Collection<E> collection) {
        this(collection, null);
    }

    public ArraySet(Collection<E> collection, Comparator<? super E> comparator) {
        TreeSet<E> set = new TreeSet<>(comparator);
        set.addAll(collection);
        this.list = new ReversedList<>(new ArrayList<>(set));
        this.comparator = comparator;
    }

    private ArraySet(ReversedList<E> reversedList, Comparator<? super E> comparator){
        this.list = reversedList;
        this.comparator = comparator;
    }

    private ArraySet(ArraySet<E> set, int left, int right) {
        this.list = new ReversedList<>(
                left < right ? set.list.subList(left, right) : Collections.emptyList());
        this.comparator = set.comparator;
    }

    @Override
    public E lower(E e) {
        return getOrNull(e, SearchOrder.Lower, SearchType.NonInclusive);
    }

    @Override
    public E floor(E e) {
        return getOrNull(e, SearchOrder.Lower, SearchType.Inclusive);
    }

    @Override
    public E ceiling(E e) {
        return getOrNull(e, SearchOrder.Higher, SearchType.Inclusive);
    }

    @Override
    public E higher(E e) {
        return getOrNull(e, SearchOrder.Higher, SearchType.NonInclusive);
    }

    private E getOrNull(E e, SearchOrder order, SearchType type) {
        int index = binarySearch(e, order, type);
        if (0 <= index && index < size()) {
            return list.get(index);
        }
        return null;
    }

    private int binarySearch(E e, SearchOrder order, SearchType type) {
        int index = Collections.binarySearch(list, e, comparator);
        if (index >= 0) {
            if (type.equals(SearchType.Inclusive)) {
                return index;
            }
            return order.equals(SearchOrder.Lower) ? index - 1 : index + 1;
        } else {
            return order.equals(SearchOrder.Lower) ? -index - 2 : -index - 1;
        }
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException("ArraySet is unmodifiable, you can not delete last elements");
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException("ArraySet is unmodifiable, you can not delete first elements");
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public ArraySet<E> descendingSet() {
        return new ArraySet<>(new ReversedList<>(list),
                Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public ArraySet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (compare(fromElement, toElement) > 0)
            throw new IllegalArgumentException("Wrong arguments, fromKey > toKey");
        return generalSubSet(cutSetBinarySearch(fromElement, fromInclusive),
                cutSetBinarySearch(toElement, !toInclusive));
    }

    @SuppressWarnings("unchecked")
    public int compare(E left, E right) {
        return comparator == null ? ((Comparable<? super E>) left).compareTo(right) : comparator.compare(left, right);
    }

    @Override
    public ArraySet<E> headSet(E toElement, boolean inclusive) {
        return generalSubSet(0, cutSetBinarySearch(toElement, !inclusive));
    }

    @Override
    public ArraySet<E> tailSet(E fromElement, boolean inclusive) {
        return generalSubSet(cutSetBinarySearch(fromElement, inclusive), size());
    }

    private ArraySet<E> generalSubSet(int fromIndex, int toIndex) {
        return new ArraySet<>(this, fromIndex, toIndex);
    }

    private int cutSetBinarySearch(E e, boolean inclusive) {
        return binarySearch(e, SearchOrder.Higher, SearchType.getFromBoolean(inclusive));
    }

    @Override
    public ArraySet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public ArraySet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public ArraySet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public E first() {
        checkSize();
        return list.get(0);
    }

    @Override
    public E last() {
        checkSize();
        return list.get(size() - 1);
    }

    private void checkSize() {
        if (isEmpty()) {
            throw new NoSuchElementException("set size is 0, there is no last element");
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        int index = Collections.binarySearch(list, (E) o, comparator);
        return 0 <= index && index < size();
    }

    private enum SearchOrder {
        Lower, Higher
    }

    private enum SearchType {
        Inclusive, NonInclusive;

        public static SearchType getFromBoolean(boolean inclusive) {
            return inclusive ? Inclusive : NonInclusive;
        }
    }
}
