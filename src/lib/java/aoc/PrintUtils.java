package aoc;

public class PrintUtils {
    public static void printBoolMatrix(boolean[][] mat, char falseSymbol, char trueSymbol) {
        int n = mat.length, m = mat[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(mat[i][j] ? trueSymbol : falseSymbol);
            }
            System.out.println();
        }
    }
}
