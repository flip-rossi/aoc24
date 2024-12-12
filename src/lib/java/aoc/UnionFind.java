package aoc;

import java.util.NoSuchElementException;

/**
 * Taken from my ADA course at FCT. Code adapted from Professor Mamede's
 */
public class UnionFind {

    static final long serialVersionUID = 0L;

    // The partition is a forest implemented in an array.
    protected int[] partition; 

    // Definition of the range of valid elements.
    protected String validRangeMsg;


    // Creates the partition {{0}, {1}, ..., {domainSize-1}}.
    public UnionFind(int domainSize) {
        partition = new int[domainSize];
        for ( int i = 0; i < domainSize; i++ )
            partition[i] = -1;

        int lastElement = domainSize - 1;
        validRangeMsg = "Range of valid elements: 0, 1, ..., " + lastElement;
    }


    protected boolean isInTheDomain( int number ) {
        return ( number >= 0 ) && ( number < partition.length );
    }


    // Pre-condition: 0 <= element < partition.length.
    protected boolean isRepresentative( int element ) {
        return partition[element] < 0;
    }

    // Returns the representative of the set that contains
    // the specified element.
    //
    // With path compression.
    public int find( int element ) throws NoSuchElementException {
        if ( !this.isInTheDomain(element) )
            throw new NoSuchElementException(validRangeMsg);

        return this.findPathCompr(element);
    }


    // Pre-condition: 0 <= element < partition.length.
    protected int findPathCompr( int element ) {
        if ( partition[element] < 0 )
            return element;

        int root = this.findPathCompr( partition[element] );
        partition[element] = root;
        return root;
    }

    // Removes the two distinct sets S1 and S2 whose representatives are
    // the specified elements, and inserts the set S1 U S2.
    // The representative of the new set S1 U S2 can be any of its members.
    //
    // Union by height or by rank.
    public void union(int el1, int el2) throws NoSuchElementException {
        int rep1 = find(el1);
        int rep2 = find(el2);
        if (rep1 == rep2)
            return;

        if (partition[rep1] <= partition[rep2]) {
            // Height(S1) >= Height(S2).
            if (partition[rep1] == partition[rep2])
                partition[rep1]--;
            partition[rep2] = rep1;
        } else
            // Height(S1) < Height(S2).
            partition[rep1] = rep2;
    }
}























