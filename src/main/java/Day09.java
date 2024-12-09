import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.random.RandomGeneratorFactory;

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
        }
        int[] range = new int[]{ currPos, currPos + line.charAt(line.length() - 1) - 0x30 };
        ranges.add(range);

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1(ranges));
            case 2 -> System.out.println(part2(ranges));
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }


    //=============== PART 1 ===============//
    static long part1(Deque<int[]> ranges) {
        int file = 0;
        long acc = 0;

        for (int pos = 0; true; pos++) {
            int[] r = ranges.peekFirst();
            if (r[1] <= pos) {
                ranges.removeFirst();
                file++;
                if (ranges.isEmpty())
                    break;
                r = ranges.peekFirst();
            }

            if (r[0] <= pos) {
                acc += file * pos;
            } else {
                r = ranges.peekLast();
                if (r[1] <= r[0]) {
                    ranges.removeLast();
                    if (ranges.isEmpty())
                        break;
                    r = ranges.peekLast();
                }
                var lastFile = file + ranges.size() - 1;
                acc += lastFile * pos;
                r[1]--;
            }
        }

        return acc;
    }

    //=============== PART 2 ===============//
    static long part2(Deque<int[]> ranges) {
        long checksum = 0;

        var gaps = new LinkedList<int[]>();
        for (int i = 0; i < ranges.size() - 1; i++) {
            var r1 = ranges.removeFirst();
            ranges.addLast(r1);
            var r2 = ranges.peekFirst();

            int gapStart = r1[0] + r1[1];
            int[] gap = new int[]{ gapStart, r2[0] - gapStart };
        }


        return checksum;
    }

}
