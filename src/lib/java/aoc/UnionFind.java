package aoc;

import java.util.NoSuchElementException;

/**
 * Taken from my ADA course at FCT. Code adapted from Professor Mamede's
 */
public class UnionFind {
    // The partition is a forest implemented in an array.
    protected int[] partition;
    protected String validRangeMsg;

    protected int nSets;

    /** Creates the partition {{0}, {1}, ..., {domainSize-1}}. */
    public UnionFind(int domainSize) {
        partition = new int[domainSize];
        for (int i = 0; i < domainSize; i++)
            partition[i] = -1;
        nSets = domainSize;

        int lastElement = domainSize - 1;
        validRangeMsg = "Range of valid elements: 0, 1, ..., " + lastElement;
    }

    public boolean containsElement(int element) {
        return (element >= 0) && (element < partition.length);
    }

    /** @return the number of distinct sets in the partition. */
    public int distinctSets() {
        return nSets;
    }

    /** @return the total number of elements across all sets in the partition. */
    public int size() {
        return partition.length;
    }

    /** Pre-condition: 0 <= element < partition.length */
    protected boolean isRepresentative(int element) {
        return partition[element] < 0;
    }

    /**
     * Returns the representative of the set that contains the specified element.
     * <p> With path compression.
     */
    public int find(int element) throws NoSuchElementException {
        if (!this.containsElement(element))
            throw new NoSuchElementException(validRangeMsg);

        return this.findPathCompr(element);
    }


    /** Pre-condition: 0 <= element < partition.length */
    protected int findPathCompr(int element) {
        if (partition[element] < 0)
            return element;

        int root = this.findPathCompr(partition[element]);
        partition[element] = root;
        return root;
    }

    /**
     * Removes the two distinct sets S1 and S2, that contain the specified elements,
     * and inserts the set S1 U S2. <br>
     * The representative of the new set S1 U S2 can be any of its members.
     * <p> Union by height (or by rank).
     * @return the representative of the new set.
     */
    public int union(int element1, int element2) throws NoSuchElementException {
        int rep1 = find(element1);
        int rep2 = find(element2);
        if (rep1 == rep2)
            return rep1;

        nSets--;

        if (partition[rep1] <= partition[rep2]) {
            // Height(S1) >= Height(S2).
            if (partition[rep1] == partition[rep2])
                partition[rep1]--;
            partition[rep2] = rep1;
            return rep1;
        } else {
            // Height(S1) < Height(S2).
            partition[rep1] = rep2;
            return rep2;
        }
    }

}
