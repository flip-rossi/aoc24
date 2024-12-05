import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * <h1>Day 05 - Print Queue</h1>
 * https://adventofcode.com/2024/day/5
 * <p>
 *  Start: 2024-12-05 08:37 <br>
 * Finish: TODO
 */
public class Day05 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int part = parseInt(args[0]);

        var outEdges = new HashMap<Integer, List<Integer>>();
        var inDegrees = new HashMap<Integer, Integer>();

        var manuals = new LinkedList<int[]>();

        // Parse input
        // Rules
        for (String line = in.readLine(); line.length() > 0; line = in.readLine()) {
            var ws = line.split("\\|");
            var a = parseInt(ws[0]);
            var b = parseInt(ws[1]);
            outEdges.computeIfAbsent(a, k -> new LinkedList<>()).add(b);
            inDegrees.put(b, inDegrees.getOrDefault(b, 0) + 1);
        }
        // Pages
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            var manual = Arrays.stream(line.split(","))
                    .mapToInt(Integer::parseInt).toArray();
            manuals.add(manual);
        }

        // Solve
        switch (part) {
            case 1 -> System.out.println(part1(outEdges, inDegrees, manuals));
            case 2 -> System.out.println(part2(outEdges, inDegrees, manuals));
            default -> {
                System.err.println("Please specify part 1 or 2.");
                System.exit(1);
            }
        }
    }

    //=============== PART 1 ===============//
    static long part1(Map<Integer, List<Integer>> outEdges, Map<Integer, Integer> inDegrees,
            List<int[]> manuals) {
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
                //System.out.println(nextPage + " " + currInDegrees.get(nextPage));
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
    static long part2(Map<Integer, List<Integer>> outEdges, Map<Integer, Integer> inDegrees,
            List<int[]> manuals) {
        int midSum = 0;

        for (var man : manuals) {
            if (isOrdered(outEdges, man))
                continue;

            midSum += topoSortedMid(outEdges, inDegrees, man);
        }

        return midSum;
    }

    private static int topoSortedMid(Map<Integer, List<Integer>> outEdges,
                Map<Integer, Integer> inDegrees, int[] man) {
        int[] perm = new int[man.length];
        int permSize = 0;
        var ready = new PriorityQueue<Integer>(man.length);
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
            perm[permSize++] = page;

            /*ghost*/ int newPages = 0;
            for (var nextPage : outEdges.getOrDefault(page, List.of())) {
                if (currInDegs.computeIfPresent(nextPage, (k, v) -> v - 1) == 0) {
                    System.err.println("Adding to ready: " + nextPage);
                    ready.add(nextPage);
                    newPages++;
                }
            }
            System.err.println("NEW PAGES: " + newPages);
        } while (permSize <= man.length/2);

        System.err.println(Arrays.stream(perm).mapToObj(x->""+x).reduce((a,b)->a+" "+b));
        System.err.println("Returning: " + perm[permSize-1]);
        return perm[permSize-1];
    }

}