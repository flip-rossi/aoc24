package aoc;

public class IntPair {
    public final int i, j;
    public IntPair(int i, int j) {
        this.i = i;
        this.j = j;
    }
    public static IntPair of(int i, int j) {
        return new IntPair(i, j);
    }
}
