package aoc;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    static public Point of(int x, int y) {
        return new Point(x, y);
    }

    static public Point of(String xStr, String yStr) {
        return Point.of(Integer.parseInt(xStr), Integer.parseInt(yStr));
    }

    static public Point ofSplit(String xyStr, String splitRegex) {
        var split = xyStr.split(splitRegex);
        return Point.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    @Override
    public boolean equals(Object obj) {
        return obj == null
               ? false
               : this == obj
                 ? true
                 : obj instanceof Point other
                   ? this.x == other.x && this.y == other.y
                   : false;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public String toString(String l, String sep, String r) {
        return l + x + sep + y + r;
    }
}
