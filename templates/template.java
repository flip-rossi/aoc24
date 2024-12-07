import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <h1>Day ${day} - ${title}</h1>
 * ${url}
 * <p>
 *  Start: ${fetch_time} <br>
 * Finish: TODO
 */
public class Day${day_padded} {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        // Parse input
        // TODO

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1());
            case 2 -> System.out.println(part2());
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    //=============== PART 1 ===============//
    static long part1() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    //=============== PART 2 ===============//
    static long part2() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

}
