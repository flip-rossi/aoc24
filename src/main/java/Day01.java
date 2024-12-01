import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>Day 01 - Historian Hysteria</h1>
 * https://adventofcode.com/2024/day/1
 */
public class Day01 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int part = parseInt(args[0]);

        // Parse input
        int maxLeft = 0;
        var idsLeft = new LinkedList<Integer>();
        var idsRight = new LinkedList<Integer>();
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            var words = line.split(" +");
            int left = parseInt(words[0]);
            if (left > maxLeft)
                maxLeft = left;
            idsLeft.add(left);
            idsRight.add(parseInt(words[1]));
        }

        // Solve
        switch (part) {
            case 1 -> System.out.println(part1(idsLeft, idsRight));
            case 2 -> System.out.println(part2(idsLeft, idsRight, maxLeft));
            default -> {
                System.err.println("Please specify part 1 or 2.");
                System.exit(1);
            }
        }
    }

    //=============== PART 1 ===============//
    static int part1(List<Integer> idsLeft, List<Integer> idsRight) {
        int n = idsLeft.size();

        Integer[] leftArr = new Integer[n];
        idsLeft.toArray(leftArr);
        Arrays.sort(leftArr);

        Integer[] rightArr = new Integer[n];
        idsRight.toArray(rightArr);
        Arrays.sort(rightArr);

        int res = 0;
        for (int i = 0; i < n; i++) {
            res += Math.abs(leftArr[i] - rightArr[i]);
        }
        return res;
    }

    //=============== PART 2 ===============//
    static long part2(List<Integer> idsLeft, List<Integer> idsRight, int maxLeft) {
        int[] rightCount = new int[maxLeft];
        for (var id : idsRight) {
            if (id <= maxLeft)
                rightCount[id - 1]++;
        }

        long res = 0;
        for (var id : idsLeft) {
            res += id * rightCount[id - 1];
        }
        return res;
    }

}
