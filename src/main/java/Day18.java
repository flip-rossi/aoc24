import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;

import org.apache.commons.lang3.tuple.Pair;

import aoc.NoSolutionException;
import aoc.Point;

/**
 * <h1>Day 18 - RAM Run</h1>
 * https://adventofcode.com/2024/day/18
 * <p>
 *  Start: 2024-12-18 10:29 <br>
 * Finish: 2024-12-18 14:05
 */
public class Day18 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        // Parse input
        // Done in each part's solution

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1());
            case 2 -> System.out.println(part2().toString("", ",", ""));
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    //static final int MEM_SIDE_LEN = 7;//71;
    static final int MEM_SIDE_LEN = 71;

    static final int[][] DIRS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    //=============== PART 1 ===============//
    //static final int PART1_N_BYTES = 0;//1024;
    static final int PART1_N_BYTES = 1024;

    static long part1() throws IOException {
        var map = new boolean[MEM_SIDE_LEN][MEM_SIDE_LEN];
        for (int i = 0; i < PART1_N_BYTES; i++) {
            var split = in.readLine().split(",");
            map[parseInt(split[1])][parseInt(split[0])] = true;
        }

        // BFS
        var queue = new ArrayDeque<Point>(MEM_SIDE_LEN * MEM_SIDE_LEN);
        var nextQueue = new ArrayDeque<Point>(MEM_SIDE_LEN * MEM_SIDE_LEN);
        var added = new boolean[MEM_SIDE_LEN][MEM_SIDE_LEN];
        int level = 0;

        queue.add(Point.of(0, 0));
        added[0][0] = true;
        do {
            do {
                var pos = queue.remove();
                for (var dir : DIRS) {
                    int dx = dir[0], dy = dir[1];
                    int x = pos.x + dx, y = pos.y + dy;
                    if (0 <= x && x < MEM_SIDE_LEN && 0 <= y && y < MEM_SIDE_LEN
                            && !map[y][x]) {
                        if (x == MEM_SIDE_LEN - 1 && y == MEM_SIDE_LEN - 1) {
                            return level + 1;
                        } else if (!added[y][x]) {
                            nextQueue.add(Point.of(x, y));
                            added[y][x] = true;
                        }
                    }
                }
            } while (!queue.isEmpty());

            var tmp = queue;
            queue = nextQueue;
            nextQueue = tmp;

            level++;
        } while (!queue.isEmpty());

        throw new NoSolutionException("No path found.");
    }

    //=============== PART 2 ===============//
    static boolean[][] hasPath(boolean[][] map) {
        if (map[0][0])
            return null;

        // BFS
        Point[][] via = new Point[MEM_SIDE_LEN][MEM_SIDE_LEN];

        var start = Point.of(0, 0);

        var waiting = new ArrayDeque<Pair<Point, Point>>(MEM_SIDE_LEN * MEM_SIDE_LEN);
        waiting.addLast(Pair.of(start, start));

        outer: do {
            var pair = waiting.removeFirst();
            var prev = pair.getLeft();
            var pos = pair.getRight();
            if (via[pos.y][pos.x] != null)
                continue;

            via[pos.y][pos.x] = prev;

            for (var dir : DIRS) {
                int dx = dir[0], dy = dir[1];
                int x = pos.x + dx, y = pos.y + dy;
                if (0 <= x && x < MEM_SIDE_LEN && 0 <= y && y < MEM_SIDE_LEN
                        && !map[y][x]) {
                    if (x == MEM_SIDE_LEN - 1 && y == MEM_SIDE_LEN - 1) {
                        via[y][x] = pos;
                        break outer;
                    } else if (via[y][x] == null) {
                        waiting.addLast(Pair.of(pos, Point.of(x, y)));
                    }
                }
            }

        } while (!waiting.isEmpty());

        if (via[MEM_SIDE_LEN - 1][MEM_SIDE_LEN - 1] == null) {
            /* DEBUG PRINTING
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j])
                        if (via[i][j] != null)
                            throw new AssertionError();
                    else
                        System.out.print('█');
                    else if (via[i][j] != null)
                        System.out.print('+');
                    else
                        System.out.print(' ');
                }
                System.out.println();
            }
            //*/
            return null;
        }

        // Fill matrix with path
        var path = new boolean[MEM_SIDE_LEN][MEM_SIDE_LEN];
        path[MEM_SIDE_LEN - 1][MEM_SIDE_LEN - 1] = true;
        for (var pos = via[MEM_SIDE_LEN - 1][MEM_SIDE_LEN - 1]; pos != start; pos = via[pos.y][pos.x]) 
            path[pos.y][pos.x] = true;

        /* DEBUG PRINTING
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j])
                    if (via[i][j] != null)
                        throw new AssertionError();
                    else
                        System.out.print('█');
                else if (via[i][j] != null)
                    if (out_path[i][j])
                        System.out.print('O');
                    else
                        System.out.print('+');
                else
                    System.out.print(' ');
            }
            System.out.println();
        }
        //*/

        return path;
    }

    static Point part2() throws IOException {
        var map = new boolean[MEM_SIDE_LEN][MEM_SIDE_LEN];
        // We know a path is possible at least after 1024 bytes have fallen
        for (int i = 0; i < PART1_N_BYTES; i++) {
            var split = in.readLine().split(",");
            map[parseInt(split[1])][parseInt(split[0])] = true;
        }

        var path = hasPath(map);
        if (path == null)
            throw new NoSolutionException("A path should still exist by the " + PART1_N_BYTES + "th byte.");

        for (var line = in.readLine(); line != null; line = in.readLine()) {
            var newByte = Point.ofSplit(line, ",");
            map[newByte.y][newByte.x] = true;

            if (path[newByte.y][newByte.x]) {
                path = hasPath(map);

                if (path == null)
                    return newByte;
            }
        }

        throw new NoSolutionException("There was always a possible path.");
    }

}
