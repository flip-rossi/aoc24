import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>Day 14 - Restroom Redoubt</h1>
 * https://adventofcode.com/2024/day/14
 * <p>
 * Start: 2024-12-14 10:27 <br>
 * Finish: TODO
 */
public class Day14 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    static record Robot(int px, int py, int vx, int vy) {
    }

    public static void main(String[] args) throws IOException {
        // Parse input
        List<Robot> robots = in.lines()
                .map(s -> {
                    var ws = s.split("p=|,| v=");
                    return new Robot(parseInt(ws[1]), parseInt(ws[2]), parseInt(ws[3]), parseInt(ws[4]));
                }).collect(Collectors.toList());

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
            int x = ((r.px() + r.vx() * SECS) % W + W) % W;
            int y = ((r.py() + r.vy() * SECS) % H + H) % H;
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
    static long part2(List<Robot> robots) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

}
