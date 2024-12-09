import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * <h1>Day 9 - Disk Fragmenter</h1>
 * https://adventofcode.com/2024/day/9
 * <p>
 *  Start: 2024-12-09 11:05 <br>
 * Finish: TODO
 */
public class Day09 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        // Parse input
        String line = in.readLine();
        Deque<int[]> ranges = new ArrayDeque<>(line.length()/2);
        int currPos = 0;
        for (int i = 0; i < line.length()/2; i++) {
            int j = i * 2;

            int[] range = new int[2];
            range[0] = currPos;
            currPos += line.charAt(j) - 0x30;
            range[1] = currPos;
            currPos += line.charAt(j + 1) - 0x30;
            ranges.add(range);
            System.err.printf("New range %d, %d\n", range[0], range[1]);
        }
        int[] range = new int[]{ currPos, currPos + line.charAt(line.length() - 1) - 0x30 };
        ranges.add(range);
        System.err.printf("New range %d, %d\n", range[0], range[1]);

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1(ranges));
            case 2 -> System.out.println(part2());
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    //=============== PART 1 ===============//
    static long part1(Deque<int[]> ranges) {
        int pos = 0;
        long acc = 0;
        int file = 0;

        var nextRange = ranges.remove();
        try {
            while (!ranges.isEmpty()) {
                while (pos < nextRange[1]) {
                    System.err.printf("Seq file %d pos %d\n", file, pos);
                    System.err.printf("Range %d, %d\n", nextRange[0], nextRange[1]);
                    System.err.printf("inc %d\n", file * pos);
                    acc += file * pos++;
                }
                file++;

                nextRange = ranges.removeFirst();

                var lastRange = ranges.peekLast();
                var lastFile = file + ranges.size();
                while (pos < nextRange[0]) {
                    if (lastRange[1] <= lastRange[0]) {
                        ranges.removeLast();
                        lastRange = ranges.peekLast();
                        lastFile = file + ranges.size();
                    }
                    System.err.printf("lastfile %d pos %d\n", lastFile, pos);
                    System.err.printf("Range %d, %d\n", lastRange[0], lastRange[1]);
                    System.err.printf("inc %d\n", file * pos);

                    acc += lastFile * pos++;
                    lastRange[1]--;
                    System.err.printf("New range %d %d\n", lastRange[0], lastRange[1]);
                }

            }
        } catch (NullPointerException e) {
            System.err.println(e);
        }

        return acc;
    }

    //=============== PART 2 ===============//
    static long part2() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

}
