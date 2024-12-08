import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * <h1>Day 6 - Guard Gallivant</h1>
 * https://adventofcode.com/2024/day/6
 * <p>
 *  Start: 2024-12-08 16:03 <br>
 * Finish: TODO
 */
public class Day06 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    static final int[][] DIRS = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };
    static final int N_DIRS = DIRS.length;
    static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

    public static void main(String[] args) throws IOException {
        // Parse input
        int x0 = -1, y0 = -1;
        int dir0 = -1; 

        var lines = in.lines().collect(Collectors.toList());
        var obstacles = new boolean[lines.size()][lines.getFirst().length()];

        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c == '#')
                    obstacles[y][x] = true;
                else if (c == '.')
                    obstacles[y][x] = false;
                else {
                    x0 = x;
                    y0 = y;
                    obstacles[y][x] = false;
                    dir0 = switch (c) {
                        case '^' -> UP;
                        case 'V' -> DOWN;
                        case '<' -> LEFT;
                        case '>' -> RIGHT;
                        default -> throw new IOException("Bad input");
                    };
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
            case 1 -> System.out.println(part1(obstacles, x0, y0, dir0));
            case 2 -> System.out.println(part2());
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    //=============== PART 1 ===============//
    static long part1(boolean[][] obstacles, int x0, int y0, int dir0) {
        int n = obstacles.length, m = obstacles[0].length;

        long count = 0;
        int x = x0, y = y0;
        int dir = dir0;
        int dx = DIRS[dir][0], dy = DIRS[dir][1];

        boolean[][] visited = new boolean[n][m];
        while (0 <= x && x < m && 0 <= y && y < n) {
            if (!visited[y][x]) {
                visited[y][x] = true;
                count++;
            }

            int nextX = x + dx, nextY = y + dy;
            if (0 <= nextX && nextX < m && 0 <= nextY && nextY < n
                    && obstacles[nextY][nextX]) {
                dir = (dir + 1) % N_DIRS;
                dx = DIRS[dir][0];
                dy = DIRS[dir][1];
            }

            x += dx;
            y += dy;
        }

        return count;
    }

    //=============== PART 2 ===============//
    static long part2() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

}
