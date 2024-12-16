import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import aoc.BiRangeIter;

/**
 * <h1>Day 16 - Reindeer Maze</h1>
 * https://adventofcode.com/2024/day/16
 * <p>
 * Start: 2024-12-16 11:18 <br>
 * Finish: 2024-12-16 13:00
 */
public class Day16 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    static enum Tile {
        EMPTY, WALL, END
    }

    public static void main(String[] args) throws IOException {
        int x0 = -1, y0 = -1;
        int xFinal = -1, yFinal = -1;
        ArrayList<ArrayList<Tile>> map = new ArrayList<>(141);

        // Parse input
        String line = in.readLine();
        for (int y = 0; line != null; y++, line = in.readLine()) {
            var row = new ArrayList<Tile>(line.length());
            map.add(row);
            for (int x = 0; x < line.length(); x++) {
                switch (line.charAt(x)) {
                    case '.' -> row.add(Tile.EMPTY);
                    case '#' -> row.add(Tile.WALL);
                    case 'E' -> {
                        row.add(Tile.END);
                        xFinal = x;
                        yFinal = y;
                    }
                    case 'S' -> {
                        row.add(Tile.EMPTY);
                        x0 = x;
                        y0 = y;
                    }
                }
            }
        }

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1(map, x0, y0));
            case 2 -> System.out.println(part2(map, x0, y0, xFinal, yFinal));
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    static record Node(int length, int x, int y, int dir) implements Comparable<Node> {
        @Override
        public int compareTo(Node o) {
            return this.length() - o.length();
        }
    }

    /** { dx, dy }[] */
    static int[][] DIRS = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
    static int EAST = 0;
    static int SOUTH = 1;
    static int WEST = 2;
    static int NORTH = 3;

    // =============== PART 1 ===============//
    static long part1(ArrayList<ArrayList<Tile>> map, int x0, int y0) {
        int n = map.size(), m = map.getFirst().size();

        var selected = new boolean[n][m][4];
        var lengths = new int[n][m][4];

        var connected = new PriorityQueue<Node>(n * m * 4);

        for (var p : new BiRangeIter(0, n, 0, m))
            for (int dir = 0; dir < 4; dir++)
                lengths[p.i][p.j][dir] = Integer.MAX_VALUE;
        lengths[y0][x0][EAST] = 0;

        connected.add(new Node(0, x0, y0, EAST));
        do {
            var node = connected.remove();
            if (map.get(node.y).get(node.x) == Tile.END)
                return node.length;
            if (selected[node.y][node.x][node.dir])
                continue;
            selected[node.y][node.x][node.dir] = true;

            int nextX = node.x + DIRS[node.dir][0], nextY = node.y + DIRS[node.dir][1];
            if (0 <= nextX && nextX < m && 0 <= nextY && nextY < n
                    && map.get(nextY).get(nextX) != Tile.WALL
                    && !selected[nextY][nextX][node.dir]) {
                int newLength = node.length + 1;
                if (newLength < lengths[nextY][nextX][node.dir]) {
                    lengths[nextY][nextX][node.dir] = newLength;
                    connected.add(new Node(newLength, nextX, nextY, node.dir));
                }
            }

            for (int i = 0, dir = (node.dir + 3) % 4; i < 2; i++, dir = (dir + 2) % 4) {
                if (!selected[node.y][node.x][dir]) {
                    int newLength = node.length + 1000;
                    if (newLength < lengths[node.y][node.x][dir]) {
                        lengths[node.y][node.x][dir] = newLength;
                        connected.add(new Node(newLength, node.x, node.y, dir));
                    }
                }
            }
        } while (!connected.isEmpty());

        throw new RuntimeException("No path found.");
    }

    // =============== PART 2 ===============//
    static long part2(ArrayList<ArrayList<Tile>> map, int x0, int y0, int xFinal, int yFinal) {
        int n = map.size(), m = map.getFirst().size();

        var selected = new boolean[n][m][4];
        var lengths = new int[n][m][4];

        @SuppressWarnings("unchecked")
        List<Node>[][][] via = new List[n][m][4];
        for (var p : new BiRangeIter(0, n, 0, m))
            for (int dir = 0; dir < 4; dir++)
                via[p.i][p.j][dir] = new LinkedList<Node>();

        var connected = new PriorityQueue<Node>(n * m * 4);

        for (var p : new BiRangeIter(0, n, 0, m))
            for (int dir = 0; dir < 4; dir++)
                lengths[p.i][p.j][dir] = Integer.MAX_VALUE;
        lengths[y0][x0][EAST] = 0;

        //via[y0][x0][EAST].add(new Node(0, x0, y0, EAST));

        int minLengthFinal = Integer.MAX_VALUE;

        connected.add(new Node(0, x0, y0, EAST));
        do {
            var node = connected.remove();
            if (node.length > lengths[node.y][node.x][node.dir])
                continue;
            if (node.x == xFinal && node.y == yFinal && node.length < minLengthFinal) {
                minLengthFinal = node.length;
            }
            selected[node.y][node.x][node.dir] = true;

            int nextX = node.x + DIRS[node.dir][0], nextY = node.y + DIRS[node.dir][1];
            if (0 <= nextX && nextX < m && 0 <= nextY && nextY < n
                    && map.get(nextY).get(nextX) != Tile.WALL) {
                int newLength = node.length + 1;
                var next = new Node(newLength, nextX, nextY, node.dir);
                if (newLength < lengths[nextY][nextX][node.dir]) {
                    lengths[nextY][nextX][node.dir] = newLength;
                    connected.add(next);

                    var newVia = new LinkedList<Node>();
                    newVia.add(node);
                    via[nextY][nextX][node.dir] = newVia;
                } else if (newLength == lengths[nextY][nextX][node.dir]) {
                    via[nextY][nextX][node.dir].add(node);
                }
            }

            for (int i = 0, dir = (node.dir + 3) % 4; i < 2; i++, dir = (dir + 2) % 4) {
                int newLength = node.length + 1000;
                var next = new Node(newLength, node.x, node.y, dir);
                if (newLength < lengths[node.y][node.x][dir]) {
                    lengths[node.y][node.x][dir] = newLength;
                    connected.add(next);

                    var newVia = new LinkedList<Node>();
                    newVia.add(node);
                    via[node.y][node.x][dir] = newVia;
                } else if (newLength == lengths[node.y][node.x][dir]) {
                    via[node.y][node.x][dir].add(node);
                }
            }
        } while (!connected.isEmpty());

        // Explore backwards
        boolean[][] seated = new boolean[n][m];
        seated[yFinal][xFinal] = true;
        int nSeats = 1;

        var waiting = new ArrayList<Node>(n * m);
        for (int dir = 0; dir < 4; dir++) {
            if (lengths[yFinal][xFinal][dir] == minLengthFinal) {
                for (var node : via[yFinal][xFinal][dir]) {
                    waiting.add(node);
                }
            }
        }

        do {
            Node node = waiting.removeLast();
            if (!seated[node.y][node.x]) {
                seated[node.y][node.x] = true;
                nSeats++;
            }

            for (var prev : via[node.y][node.x][node.dir]) {
                waiting.add(prev);
            }
        } while (!waiting.isEmpty());

        // DEBUG
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                System.err.print(seated[y][x]
                    ? 'O'
                    : map.get(y).get(x) == Tile.WALL ? '#' : '.');
            }
            System.err.println();
        }

        return nSeats;
    }

}
