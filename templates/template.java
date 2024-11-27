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
public class Day${day} {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int part;
        try { part = parseInt(args[0]); }
        catch (NumberFormatException e) { System.err.println("Please specify part 1 or 2."); }

        // Parse input
        // TODO

        // Solve
        switch (part) {
            case 1 -> part1();
            case 2 -> part2();
            default -> System.err.println("Please specify part 1 or 2.");
        }
    }

    //=============== PART 1 ===============//
    static void part1() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    //=============== PART 2 ===============//
    static void part2() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

}
