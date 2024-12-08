package aoc;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BiRangeIter implements Iterator<IntPair>, Iterable<IntPair>, Cloneable {
    private int i;
    private int j;

    private final int iLimit;
    private final int jLimit;

    public BiRangeIter(int i0, int iLimit, int j0, int jLimit) {
        this.i = i0;
        this.iLimit = iLimit;
        this.j = j0;
        this.jLimit = jLimit;
    }

    public static BiRangeIter of(int i0, int iLimit, int j0, int jLimit) {
        return new BiRangeIter(i0, iLimit, j0, jLimit);
    }

    @Override
    public boolean hasNext() {
        return i < iLimit;
    }

    @Override
    public IntPair next() throws NoSuchElementException {
        if (!hasNext())
            throw new NoSuchElementException();
        var next = new IntPair(i, j);
        if (++j >= jLimit) {
            i++;
            j = 0;
        }
        return next;
    }

    @Override
    public BiRangeIter iterator() {
        return this.clone();
    }

    @Override
    public BiRangeIter clone() {
        return new BiRangeIter(i, iLimit, j, jLimit);
    }

}
