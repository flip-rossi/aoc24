import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * <h1>Day 13 - Claw Contraption</h1>
 * https://adventofcode.com/2024/day/13
 * <p>
 *  Start: 2024-12-13 15:13 <br>
 * Finish: TODO
 */
public class Day13 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    static record Machine(int ax, int ay, int bx, int by, int px, int py) {}

    public static void main(String[] args) throws IOException {
        var machines = new ArrayList<Machine>(320);

        // Parse input
        for (var line = in.readLine(); line != null; line = in.readLine()) {
            var w = line.split(": X|, Y");
            int ax = parseInt(w[1]);
            int ay = parseInt(w[2]);

            line = in.readLine();
            w = line.split(": X|, Y");
            int bx = parseInt(w[1]);
            int by = parseInt(w[2]);

            line = in.readLine();
            w = line.split(": X=|, Y=");
            int px = parseInt(w[1]);
            int py = parseInt(w[2]);

            machines.add(new Machine(ax, ay, bx, by ,px, py));

            in.readLine();
        }

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1(machines));
            case 2 -> System.out.println(part2(machines));
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    //=============== PART 1 ===============//
    static long part1(ArrayList<Machine> machines) {
        return machines.stream().mapToLong(m -> {
            int minTokens = -1;

            for (int a = 0; a <= 100; a++) {
                int bBx = m.px - a * m.ax;
                if (bBx < 0)
                    break;

                if (bBx % m.bx == 0) {
                    int b = bBx / m.bx;
                    int tokens = a * 3 + b;
                    if (a * m.ay + b * m.by == m.py
                            && (minTokens == -1 || tokens < minTokens))
                        minTokens = tokens;
                }
            }

            return minTokens == -1 ? 0 : minTokens;
        }).sum();
    }

    //=============== PART 2 ===============//
    static long part2(ArrayList<Day13.Machine> machines) {
        throw new UnsupportedOperationException();
    }

}
