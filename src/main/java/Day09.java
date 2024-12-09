import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * <h1>Day 9 - Disk Fragmenter</h1>
 * https://adventofcode.com/2024/day/9
 * <p>
 *  Start: 2024-12-09 11:05 <br>
 * Finish: 2024-12-09 15:07
 */
public class Day09 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        // Parse input
        String line = in.readLine();
        Deque<Block> blocks = new ArrayDeque<>(line.length()/2);
        int currPos = 0;
        int file = 0;
        for (int i = 0; i < line.length()/2; i++) {
            int j = i * 2;

            Block block = new Block(file++);
            block.start = currPos;
            currPos += line.charAt(j) - 0x30;
            block.end = currPos;
            currPos += line.charAt(j + 1) - 0x30;
            blocks.add(block);
        }
        var range = new Block(file, currPos, currPos + line.charAt(line.length() - 1) - 0x30);
        blocks.add(range);

        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1(blocks));
            case 2 -> System.out.println(part2(blocks));
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    static class Block {
        int file, start, end;
        public Block(int file) {
            this.file = file;
        }
        public Block(int file, int start, int end) {
            this.file = file;
            this.start = start;
            this.end = end;
        }
        int getSize() {
            return end - start;
        }
    }

    //=============== PART 1 ===============//
    static long part1(Deque<Block> blocks) {
        long acc = 0;

        for (int pos = 0; true; pos++) {
            var b = blocks.peekFirst();
            if (b.end <= pos) {
                blocks.removeFirst();
                if (blocks.isEmpty())
                    break;
                b = blocks.peekFirst();
            }

            if (b.start <= pos) {
                acc += b.file * pos;
            } else {
                b = blocks.peekLast();
                if (b.end <= b.start) {
                    blocks.removeLast();
                    if (blocks.isEmpty())
                        break;
                    b = blocks.peekLast();
                }
                acc += b.file * pos;
                b.end--;
            }
        }

        return acc;
    }

    //=============== PART 2 ===============//
    static long part2(Deque<Block> blocks) {
        long checksum = 0;

        var gaps = new LinkedList<int[]>();
        for (int i = 0; i < blocks.size() - 1; i++) {
            var b1 = blocks.removeFirst();
            blocks.addLast(b1);
            var b2 = blocks.peekFirst();

            int[] gap = new int[]{ b1.end, b2.start };
            if (gap[0] != gap[1])
                gaps.add(gap);
        }
        // The original last element never got removed
        blocks.addLast(blocks.removeFirst());

        while (!blocks.isEmpty()) {
            Block b = blocks.removeLast();

            var it = gaps.iterator();
            while (it.hasNext()) {
                var gap = it.next();
                if (gap[0] > b.start)
                    break;

                if (gap[1] - gap[0] >= b.getSize()) {
                    b.end = gap[0] + b.getSize();
                    b.start = gap[0];
                    gap[0] = b.end;
                    if (gap[0] == gap[1])
                        it.remove();
                    break;
                }
            }

            for (int pos = b.start; pos < b.end; pos++) {
                checksum += pos * b.file;
            }
        }

        return checksum;
    }

}
