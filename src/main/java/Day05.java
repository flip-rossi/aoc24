import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * <h1>Day 05 - Print Queue</h1>
 * https://adventofcode.com/2024/day/5
 * <p>
 *  Start: 2024-12-05 08:37 <br>
 * Finish: 2024-12-05 19:54
 */
public class Day05 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int part = parseInt(args[0]);

        var outEdges = new HashMap<Integer, List<Integer>>();
        var manuals = new LinkedList<int[]>();

        // Parse input
        // Rules
        for (String line = in.readLine(); line.length() > 0; line = in.readLine()) {
            var ws = line.split("\\|");
            var a = parseInt(ws[0]);
            var b = parseInt(ws[1]);
            outEdges.computeIfAbsent(a, k -> new LinkedList<>()).add(b);
        }
        // Pages
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            var manual = Arrays.stream(line.split(","))
                    .mapToInt(Integer::parseInt).toArray();
            manuals.add(manual);
        }

        // Solve
        switch (part) {
            case 1 -> System.out.println(part1(outEdges, manuals));
            case 2 -> System.out.println(part2(outEdges, manuals));
            default -> {
                System.err.println("Please specify part 1 or 2.");
                System.exit(1);
            }
        }
    }

    //=============== PART 1 ===============//
    static long part1(Map<Integer, List<Integer>> outEdges, List<int[]> manuals) {
        var midSum = 0;

        for (var man : manuals) {
            if (isOrdered(outEdges, man))
                midSum += man[man.length/2];
        }

        return midSum;
    }

    private static boolean isOrdered(Map<Integer, List<Integer>> outEdges, int[] man) {
        var currInDegrees = new HashMap<Integer, Integer>();

        for (int page : man) {
            for (int nextPage : outEdges.getOrDefault(page, List.of())) {
                currInDegrees.put(nextPage,
                    currInDegrees.getOrDefault(nextPage, 0) + 1);
            }
        }

        for (int page : man) {
            System.err.print(page + "(" + currInDegrees.get(page)+"), ");
            if (currInDegrees.containsKey(page) && currInDegrees.get(page) > 0) {
                System.err.println("OUT");
                return false;
            }
            for (int nextPage : outEdges.getOrDefault(page, List.of()))
            currInDegrees.put(nextPage, currInDegrees.get(nextPage) - 1);
        }
        System.err.println("DONE");

        return true;
    }

    //=============== PART 2 ===============//
    static long part2(Map<Integer, List<Integer>> outEdges, List<int[]> manuals) {
        int midSum = 0;

        for (var man : manuals) {
            if (isOrdered(outEdges, man))
                continue;

            midSum += topoSortedMid(outEdges, man);
        }

        return midSum;
    }

    private static int topoSortedMid(Map<Integer, List<Integer>> outEdges, int[] man) {
        var perm = new ArrayList<Integer>(man.length/2 + 1);
        var ready = new PriorityQueue<Integer>(man.length, (a, b) -> {
            int aIdx = 0;
            while (aIdx < man.length && man[aIdx] != a)
                aIdx++;
            int bIdx = 0;
            while (bIdx < man.length && man[bIdx] != b)
                bIdx++;
            return aIdx - bIdx;
        });
        var currInDegs = new HashMap<Integer, Integer>();

        for (int page : man) {
            currInDegs.putIfAbsent(page, 0);
            for (var nextPage : outEdges.getOrDefault(page, List.of())) {
                currInDegs.compute(nextPage, (k, deg) -> deg == null ? 1 : deg + 1);
            }
        }

        for (int page : man) {
            if (currInDegs.get(page) == 0)
                ready.add(page);
        }

        do {
            var page = ready.remove();
            perm.add(page);

            /*ghost*/ int newPages = 0;
            for (var nextPage : outEdges.getOrDefault(page, List.of())) {
                if (currInDegs.computeIfPresent(nextPage, (k, v) -> v - 1) == 0) {
                    System.err.println("Adding to ready: " + nextPage);
                    ready.add(nextPage);
                    newPages++;
                }
            }
            System.err.println("NEW PAGES: " + newPages);
        } while (perm.size() <= man.length/2);

        System.err.println(perm.stream().map(x->""+x).reduce((a,b)->a+" "+b));
        System.err.println("Returning: " + perm.getLast());
        return perm.getLast();
    }

}
