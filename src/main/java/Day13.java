import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.tuple.Triple;

/**
 * <h1>Day 13 - Claw Contraption</h1>
 * https://adventofcode.com/2024/day/13
 * <p>
 *  Start: 2024-12-13 15:13 <br>
 * Finish: 2024-12-13 21:10
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
            System.err.printf("MACHINE A: %d, %d B: %d, %d P: %d, %d\n", m.ax, m.ay, m.bx, m.by, m.px, m.py);
            int minTokens = -1;

            for (int a = 0; a <= 100; a++) {
                int bBx = m.px - a * m.ax;
                if (bBx < 0)
                    break;

                if (bBx % m.bx == 0) {
                    int b = bBx / m.bx;
                    int tokens = a * 3 + b;
                    if (a * m.ay + b * m.by == m.py)
                        System.err.printf("Integer solution: a = %d, b = %d\n", a, b);
                    if (a * m.ay + b * m.by == m.py
                            && (minTokens == -1 || tokens < minTokens))
                        minTokens = tokens;
                }
            }

            return minTokens == -1 ? 0 : minTokens;
        }).sum();
    }

    //=============== PART 2 ===============//
    /**
     * Stein's Algorithm
     * <p> Source: hrefhttps://www.geeksforgeeks.org/how-to-find-the-gcd-greatest-common-divisor/
     */
    static long gcd(long a, long b) {
        int timesTwiceHalved = 0;

        while (a != 0 && b != 0) {
            while (a % 2 == 0 && b % 2 == 0) {
                a /= 2;
                b /= 2;
                timesTwiceHalved++;
            }
            while (a % 2 == 0)
                a /= 2;
            while (b % 2 == 0)
                b /= 2;

            if (a < b)
                b -= a;
            else
                a -= b;
        }

        return (a != 0 ? a : b) << timesTwiceHalved;
    }

    /**
     * Reduce an a = b mod n expression by dividing a and b by their GCDs
     */
    static Triple<Long, Long, Long> reduceExpr(long a, long b, long mod) {
        for (long divisor = gcd(a, b); divisor > 1; divisor = gcd(a, b)) {
            a /= divisor;
            b /= divisor;

            if (mod % divisor == 0)
                mod /= divisor;
        }

        return Triple.of(a % mod, b % mod, mod);
    }

    static long part2(ArrayList<Day13.Machine> machines) {
        /* NOTE: I hard linear algebra'd this part, without any need, as there
         * were never multiple possible solutions. But goddamnit, if there were
         * I'd have gotten them:
         * 
         * { Ax * a + Bx * b = Px
         * { Ay * a + By * b = Py
         *       ↓
         * [ Ax Bx | Px ] → [  1      Bx/Ax     |     Px/Ax     ]
         * [ Ay By | Py ]   [  0  By-(Ay*Bx/Ax) | Py-(Ay*Px/Ax) ]
         *
         * - Se a célula (2,2) for 0:
         *   - Se a célula (2,3) for 0:
         *      Há infinitas soluções. Temos que definir qual variável minimizar
         *      (a ou b) e encontrar o menor ponto inteiro da variável minimizada
         *      em que a outra também é inteira.
         *      O que eu fiz funcionou para casos báscicos como:
         *          A: (4, 8), B: (1, 2), P: (7, 14)
         *      Não testei mais do que isso
         *   - Se a célula (2,3) for diferente de 0:
         *      Não há solução.
         * - Se a célula (2,2) for diferente de 0:
         *   Há uma única solução.
         */
        return machines.stream().mapToLong(m -> {
            long ax = m.ax, ay = m.ay,
                 bx = m.bx, by = m.by;
            long px = m.px + 10000000000000l, py = m.py + 10000000000000l;

            System.err.printf("[[[ MACHINE A: (%d, %d) B: (%d, %d) P: (%d, %d) ]]]\n", ax, ay, bx, by, px, py);
            if (by * ax != bx * ay) { // <==> m.by - m.ay * m.bx / (double) m.ax != 0
                System.err.printf("ONLY ONE SOLUTION\n");
                long bNumerator = ax * py - ay * px;
                long bDivisor = ax * by - ay * bx;
                if (bNumerator % bDivisor != 0) // non-integer solution
                    return 0;

                long b = bNumerator / bDivisor;

                long aNumerator = px - b * bx;
                if (aNumerator % ax != 0) // non-integer solutuion
                    return 0;

                long a = aNumerator / ax;

                System.err.printf("a = %d, b = %d\n", a, b);

                return a * 3 + b;
            } else if (py * ax != px * ay) { // <==> py - px * ay / ax != 0
                    System.err.printf("IMPOSSIBLE\n");
                    return 0; // Impossible
            } else {
                System.err.printf("MULTIPLE SOLUTIONS\n");
                // Multiple solutions
                double aMag = Math.sqrt(ax * ax + ay * ay);
                double bMag = Math.sqrt(bx * bx + by * by);
                System.err.printf("aMag %f, bMag %f\n", aMag, bMag);

                long x1, y1;
                long x2, y2; // free variable's x, free variable's y
                /*
                 * [ x1 x2 | Px ]
                 * [ y1 y2 | Py ]
                 */
                if (aMag > 3 * bMag) {
                    System.err.printf("Minimizing B\n");
                    x1 = ax; y1 = ay;
                    x2 = bx; y2 = by;
                } else {
                    System.err.printf("Minimizing A\n");
                    x1 = bx; y1 = by;
                    x2 = ax; y2 = ay;
                }

                Triple<Long, Long, Long> reduced = reduceExpr(x2, px, x1);
                if (reduced.getLeft() != 1) // No integer solution
                    return 0;
                long period = reduced.getRight();
                long offset = reduced.getMiddle();

                long minimizedVar = offset; // k = 0 => period * k + offset = offset
                long maximizedVar = (px - x2 * minimizedVar) / x1;
                System.out.println("Offset: " + offset);
                System.out.println("Period: " + period);
                System.out.println(minimizedVar);
                System.out.println(maximizedVar);

                long a, b;
                if (aMag > 3 * bMag) {
                    a = maximizedVar;
                    b = minimizedVar;
                } else {
                    b = maximizedVar;
                    a = minimizedVar;
                }

                System.err.printf("a = %d, b = %d\n", a, b);

                return a * 3 + b;
            }
            // Return 26299 too low
        }).sum();
    }

}
