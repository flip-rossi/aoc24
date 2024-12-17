import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * <h1>Day 17 - Chronospatial Computer</h1>
 * https://adventofcode.com/2024/day/17
 * <p>
 *  Start: 2024-12-17 12:52 <br>
 * Finish: TODO
 */
public class Day17 {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    static long ax, bx, cx;

    public static void main(String[] args) throws IOException {
        // Parse input
        ax = parseInt(in.readLine().split("Register A: ")[1]);
        bx = parseInt(in.readLine().split("Register B: ")[1]);
        cx = parseInt(in.readLine().split("Register C: ")[1]);

        in.readLine();

        String[] ws = in.readLine().split(",| ");
        int[] program = new int[ws.length - 1];
        for (int i = 0; i < program.length; i++)
            program[i] = parseInt(ws[i + 1]);


        // Solve
        if (args.length < 1) {
            System.err.println("Please specify the part to solve.");
            System.exit(1);
        }
        int part = parseInt(args[0]);
        switch (part) {
            case 1 -> System.out.println(part1(program).stream()
                            .map(x -> x.toString()).reduce((a, b) -> a + "," + b).get());
            case 2 -> System.out.println(part2(program));
            default -> {
                System.err.println("Part must be 1 or 2.");
                System.exit(1);
            }
        }
    }

    static final int ADV = 0;
    static final int BXL = 1;
    static final int BST = 2;
    static final int JNZ = 3;
    static final int BXC = 4;
    static final int OUT = 5;
    static final int BDV = 6;
    static final int CDV = 7;

    static long combo(int operand) {
        return switch (operand) {
            case 4 -> ax;
            case 5 -> bx;
            case 6 -> cx;
            default -> operand;
        };
    }

    //=============== PART 1 ===============//
    static ArrayList<Long> part1(int[] program) {
        ArrayList<Long> output = new ArrayList<>(20);
        int i = 0;
        while (i < program.length) {
            int oper = program[i + 1];
            switch (program[i]) {
                case ADV -> ax = ax >> combo(oper);
                case BXL -> bx ^= oper;
                case BST -> bx = combo(oper) & 0b111;
                case JNZ -> { if (ax != 0) { i = oper; continue; } }
                case BXC -> bx ^= cx;
                case OUT -> output.add(combo(oper) & 0b111);
                case BDV -> bx = ax >> combo(oper);
                case CDV -> cx = ax >> combo(oper);
            }
            i += 2;
        }
        return output;
    }

    //=============== PART 2 ===============//
    static long part2(int[] program) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

}
