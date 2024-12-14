import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Day 14 - Restroom Redoubt</h1>
 * https://adventofcode.com/2024/day/14
 * <p>
 * Start: 2024-12-14 10:27 <br>
 * Finish: 2024-12-14 11:44
 */
public class Day14 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    static class Robot {
        public int x, y;
        public final int vx, vy;

        public Robot(int px, int py, int vx, int vy) {
            this.x = px;
            this.y = py;
            this.vx = vx;
            this.vy = vy;
        }
    }

    public static void main(String[] args) throws IOException {
        // Parse input
        List<Robot> robots = new ArrayList<>(500);
        for (var line = in.readLine(); line != null && !line.isBlank(); line = in.readLine()) {
            var ws = line.split("p=|,| v=");
            robots.add(new Robot(parseInt(ws[1]), parseInt(ws[2]), parseInt(ws[3]), parseInt(ws[4])));
        }

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1(robots));
            case 2 -> System.out.println(part2(robots));
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    // =============== PART 1 ===============//
    static final int W = 101;
    static final int MID_W = W / 2;
    static final int H = 103;
    static final int MID_H = H / 2;
    static final int SECS = 100;

    static long part1(List<Robot> robots) {
        int tl = 0, tr = 0,
                bl = 0, br = 0;
        for (var r : robots) {
            int x = ((r.x + r.vx * SECS) % W + W) % W;
            int y = ((r.y + r.vy * SECS) % H + H) % H;
            if (y < MID_H) { // Top
                if (x < MID_W)
                    tl++;
                else if (x > MID_W)
                    tr++;
            } else if (y > MID_H) { // Bottom (middle doesn't count). Height is odd
                if (x < MID_W)
                    bl++;
                else if (x > MID_W)
                    br++;
            }
        }

        return tl * tr * bl * br;
    }

    // =============== PART 2 ===============//
    /**
     * Don't send EOF to stdin after parsing the puzzle input to interact with this
     * part!
     */
    static long part2(List<Robot> robots) throws IOException {
        var out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int[][] posCount = new int[H][W];
        for (var r : robots) {
            posCount[r.y][r.x]++;
        }

        boolean auto = false;
        for (int i = 1;; i++) {
            for (var r : robots) {
                posCount[r.y][r.x]--;
                r.x = (r.x + r.vx + W) % W;
                r.y = (r.y + r.vy + H) % H;
                posCount[r.y][r.x]++;
            }
            // Print iteration
            out.println("vvvvvvv SECONDS PASSED: " + i + " vvvvvvv");
            for (int y = 0; y < H; y++) {
                for (int x = 0; x < W; x++) {
                    if (posCount[y][x] == 0)
                        out.print(' ');
                    else
                        out.print('█');
                }
                out.println();
            }
            out.println("^^^^^^^ SECONDS PASSED: " + i + " ^^^^^^^");
            if (auto && i == MAGIC) {
                out.flush();
                return i;
            } else if (!auto) {
                out.print("Continue? [Y/n/auto] ");
                out.flush();
                String ans = in.readLine();
                if (ans == null || ans.trim().toUpperCase().equals("AUTO")) {
                    auto = true;
                } else if (ans.trim().toUpperCase().equals("N")) {
                    return i;
                }
                out.println();
            }
        }
    }

    static final int MAGIC = 7672;

}
