import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import aoc.UnionFind;

/**
 * <h1>Day 12 - Garden Groups</h1>
 * https://adventofcode.com/2024/day/12
 * <p>
 *  Start: 2024-12-12 10:09 <br>
 * Finish: TODO
 */
public class Day12 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        // Parse input
        List<List<Character>> plants = in.lines()
                .map(str -> str.chars().mapToObj(x -> (char) x).collect(Collectors.toList()))
                .collect(Collectors.toList());

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1(plants));
            case 2 -> System.out.println(part2());
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    //=============== PART 1 ===============//
    static class ComponentInfo {
        int area, minX, minY, maxX, maxY;
        public ComponentInfo(int area, int minX, int minY, int maxX, int maxY) {
            this.area = area;
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }
    }

    // Neighborhood of 4
    static Pair<int[][], ComponentInfo[]> connectedComponents(List<List<Character>> plants) {
        int n = plants.size(), m = plants.getFirst().size();
        int[][] tagsPadded = new int[n + 2][m + 2];
        var uf = new UnionFind(n * m + 1);

        int nextTag = 1;

        for (int i = 0; i < n; i++) {
            var prevRow = i > 0 ? plants.get(i - 1) : null;
            var row = plants.get(i);
            for (int j = 0; j < m; j++) {
                var plant = row.get(j);

                if (tagsPadded[i][j + 1] != 0 && prevRow.get(j).equals(plant)) // Top
                    uf.union(tagsPadded[i][j + 1], nextTag);
                if (tagsPadded[i + 1][j] != 0 && row.get(j - 1).equals(plant)) // Left
                    uf.union(tagsPadded[i + 1][j], nextTag);

                tagsPadded[i + 1][j + 1] = uf.find(nextTag);
                nextTag++;
            }
        }

        var infos = new ComponentInfo[n * m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                var tag = uf.find(tagsPadded[i + 1][j + 1]);
                tagsPadded[i + 1][j + 1] = tag;

                var info = infos[tag];
                if (info == null) {
                    info = new ComponentInfo(0, j, i, j, i);
                    infos[tag] = info;
                }

                info.area++;
                if (j < info.minX) info.minX = j;
                if (i < info.minY) info.minY = i;
                if (j > info.maxX) info.maxX = j;
                if (i > info.maxY) info.maxY = i;

                System.err.println("Plant " + plants.get(i).get(j) + " tag " + tag);
            }
        }

        return Pair.of(tagsPadded, infos);
    }

    static int countPerimeter(int[][] tagsPadded, int component, ComponentInfo info) {
        int perimeter = 0;

        var t = tagsPadded;
        for (int i = info.minY + 1; i <= info.maxY + 1; i++) {
            for (int j = info.minX + 1; j <= info.maxX + 1; j++) {
                if (t[i][j] == component) {
                    if (t[i - 1][j] != component)
                        perimeter++;
                    if (t[i + 1][j] != component)
                        perimeter++;
                    if (t[i][j - 1] != component)
                        perimeter++;
                    if (t[i][j + 1] != component)
                        perimeter++;
                }
            }
        }

        return perimeter;
    }

    static long part1(List<List<Character>> plants) {
        var compRes = connectedComponents(plants);
        int[][] tagsPadded = compRes.getLeft();
        var infos = compRes.getRight();

        long acc = 0;
        for (int tag = 1; tag < infos.length; tag++) {
            var info = infos[tag];
            if (info == null)
                continue;
            int perim = countPerimeter(tagsPadded, tag, info);
            acc += info.area * perim;
            System.err.printf("%d: area (%d) * perim (%d) = %d\n", tag, info.area, perim, info.area * perim);
            System.err.printf("min (%d, %d) max (%d, %d)\n", info.minX, info.minY, info.maxX, info.maxY);
        }

        return acc;
    }

    //=============== PART 2 ===============//
    static long part2() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

}
