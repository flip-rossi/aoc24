/*
 * Day 15 - Warehouse Woes
 *
 * https://adventofcode.com/2024/day/15
 *
 *  Start: 2024-12-15 11:51
 * Finish: TODO
 */

#include <cassert>
#include <cstdint>
#include <cstdio>
#include <cstdlib>
#include <iostream>
#include <ostream>
#include <string>
#include <unistd.h>
#include <vector>
#include <ranges>

using std::cin;
using std::cout;
using std::endl;
using std::getline;
using std::vector;
using std::string;

enum Space {
    EMPTY, WALL, BOX, BOX_L, BOX_R
};

typedef std::pair<int, int> Dir;

//=============== PART 1 ===============//
bool move1(vector<vector<Space>>& map, int x, int y, int dx, int dy) {
    int x2 = x + dx, y2 = y + dy;
    Space space = map[y][x];
    if (space == WALL
            || x2 < 0 || (int)map[0].size() <= x2 || y2 < 0 || (int)map.size() <= y2
            || map[y2][x2] == WALL) {
        return false;
    } else if (map[y2][x2] == EMPTY || move1(map, x2, y2, dx, dy)) {
        //cout << "Move " << space << " (" << x << "," << y << ") to (" << x2 << "," << y2 << ")" << endl;
        map[y2][x2] = space;
        map[y][x] = EMPTY;
        return true;
    } else {
        return false;
    }
}

int64_t part1(vector<vector<Space>>& map, vector<Dir>& moves, int robotX, int robotY) {
    // Move around
    for (auto& [dx, dy] : moves) {
        if (move1(map, robotX, robotY, dx, dy)) {
            robotX += dx;
            robotY += dy;
        }
        /*
        cout << dx << ", " << dy << endl;
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map[0].size(); x++) {
                if (x == robotX && y == robotY)
                    cout << '@';
                else if (map[y][x] == WALL)
                    cout << '#';
                else if (map[y][x] == EMPTY)
                    cout << '.';
                else if (map[y][x] == BOX)
                    cout << 'O';
            }
            cout << endl;
        }
        */
    }

    // Get result
    int n = map.size(), m = map[0].size();
    int64_t acc = 0;
    for (int y = 0; y < n; y++) {
        for (int x = 0; x < m; x++) {
            if (map[y][x] == BOX)
                acc += x + y * 100;

            /*
            if (x == robotX && y == robotY)
                cout << '@';
            else if (map[y][x] == WALL)
                cout << '#';
            else if (map[y][x] == BOX)
                cout << 'O';
            else if (map[y][x] == EMPTY)
                cout << '.';
            */
        }
        //cout << endl;
    }
    return acc;
}

void parseMap1(vector<vector<Space>>& map_out, int& x0_out, int& y0_out) {
    map_out.reserve(50);
    string line;
    for (int y = 0; getline(cin, line) && line != ""; y++) {
        map_out.push_back(vector<Space>());
        map_out[y].reserve(line.size());
        for (size_t x = 0; x < line.size(); x++) {
            Space space;
            switch (line[x]) {
                case '#': space = WALL; break;
                case 'O': space = BOX; break;
                case '@':
                    x0_out = x;
                    y0_out = y;
                default: space = EMPTY; break;
            }
            map_out[y].push_back(space);
        }
    }
}

//=============== PART 2 ===============//
bool can_move2(vector<vector<Space>>& map, int x, int y, int dx, int dy, bool chained) {
    int x2 = x + dx, y2 = y + dy;
    Space space = map[y][x];
    if (space == WALL
            || x2 < 0 || (int)map[0].size() <= x2 || y2 < 0 || (int)map.size() <= y2
            || map[y2][x2] == WALL) {
        return false;
    } else if (map[y2][x2] == EMPTY) {
        return true;
    } else if (dy == 0 || chained) {
        return can_move2(map, x2, y2, dx, dy, false);
    } else if (map[y2][x2] == BOX_L) {
        return can_move2(map, x2, y2, dx, dy, false) && can_move2(map, x + 1, y, dx, dy, true);
    } else if (map[y2][x2] == BOX_R) {
        return can_move2(map, x2, y2, dx, dy, false) && can_move2(map, x - 1, y, dx, dy, true);
    } else {
        cout << "SHOULDN'T HAPPEN" << endl;
        return false;
    }
}

void move2(vector<vector<Space>>& map, int x, int y, int dx, int dy, bool chained) {
    int x2 = x + dx, y2 = y + dy;
    Space space = map[y][x];
    if (space == EMPTY || space == WALL) {
        return;
    } else if (dy == 0 || chained) {
        move2(map, x2, y2, dx, dy, false);
        map[y2][x2] = space;
        map[y][x] = EMPTY;
    } else if (space == BOX_L) {
        move2(map, x2, y2, dx, dy, false);
        move2(map, x + 1, y, dx, dy, true);
        map[y2][x2] = space;
        map[y][x] = EMPTY;
    } else if (space == BOX_R) {
        move2(map, x2, y2, dx, dy, false);
        move2(map, x - 1, y, dx, dy, true);
        map[y2][x2] = space;
        map[y][x] = EMPTY;
    }
}

int64_t part2(vector<vector<Space>>& map, vector<Dir>& moves, int robotX, int robotY) {
    // Move around
    for (auto& [dx, dy] : moves) {
        if (can_move2(map, robotX, robotY, dx, dy, false)) {
            move2(map, robotX + dx, robotY + dy, dx, dy, false);
            robotX += dx;
            robotY += dy;
        }

        cout << dx << ", " << dy << endl;
        for (int y = 0; y < (int)map.size(); y++) {
            for (int x = 0; x < (int)map[0].size(); x++) {
                if (x == robotX && y == robotY)
                    cout << '@';
                else if (map[y][x] == WALL)
                    cout << '#';
                else if (map[y][x] == EMPTY)
                    cout << '.';
                else if (map[y][x] == BOX_L)
                    cout << '[';
                else if (map[y][x] == BOX_R)
                    cout << ']';
            }
            cout << endl;
        }
    }

    // Get result
    int n = map.size(), m = map[0].size();
    int64_t acc = 0;
    for (int y = 0; y < n; y++) {
        for (int x = 0; x < m; x++) {
            if (map[y][x] == BOX_L)
                acc += x + y * 100;

            /*
            if (x == robotX && y == robotY)
                cout << '@';
            else if (map[y][x] == WALL)
                cout << '#';
            else if (map[y][x] == BOX)
                cout << 'O';
            else if (map[y][x] == EMPTY)
                cout << '.';
            */
        }
        //cout << endl;
    }
    // TODO 1570684 too high
    return acc;
}

void parseMap2(vector<vector<Space>>& map_out, int& x0_out, int& y0_out) {
    map_out.reserve(50 * 2);
    string line;
    for (int y = 0; getline(cin, line) && line != ""; y++) {
        map_out.push_back(vector<Space>());
        map_out[y].reserve(line.size());
        for (size_t j = 0; j < line.size(); j++) {
            int x = j * 2;
            Space space_l, space_r;
            switch (line[j]) {
                case '#': space_l = WALL; space_r = WALL; break;
                case 'O': space_l = BOX_L; space_r = BOX_R; break;
                case '@':
                    x0_out = x;
                    y0_out = y;
                default: space_l = EMPTY; space_r = EMPTY; break;
            }
            map_out[y].push_back(space_l);
            map_out[y].push_back(space_r);
        }
    }
}

//=============== SOLVE ================//
int main(int argc, char *argv[]) {
    if (argc < 2) {
        std::cerr << "Please specify the part to solve." << endl;
        return 1;
    }
    int part = std::stoi(argv[1]);

    // Parse input
    int x0 = -1, y0 = -1;
    vector<vector<Space>> map;
    if (part == 1)
        parseMap1(map, x0, y0);
    else
        parseMap2(map, x0, y0);

    vector<Dir> moves = std::ranges::views::istream<string>(cin)
        | std::views::join
        | std::views::transform([](auto& c) {
            Dir dir;
            switch (c) {
                case '^': dir = {  0, -1 }; break;
                case 'v': dir = {  0,  1 }; break;
                case '<': dir = { -1,  0 }; break;
                case '>': dir = {  1,  0 }; break;
            }
            return dir;
        })
        | std::ranges::to<vector>();

    // Solve
    switch (part) {
        case 1:
            cout << part1(map, moves, x0, y0) << endl;
            break;
        case 2:
            cout << part2(map, moves, x0, y0) << endl;
            break;
        default:
            std::cerr << "Part must be 1 or 2." << endl;
            return 1;
    }
    return 0;
}

