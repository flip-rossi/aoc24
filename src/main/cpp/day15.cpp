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
#include <thread>
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
    EMPTY, WALL, BOX
};

typedef std::pair<int, int> Dir;

//=============== PART 1 ===============//
bool move(vector<vector<Space>>& map, int x, int y, int dx, int dy) {
    int x2 = x + dx, y2 = y + dy;
    Space space = map[y][x];
    if (space == WALL
            || x2 < 0 || map[0].size() <= x2 || y2 < 0 || map.size() <= y2
            || map[y2][x2] == WALL) {
        return false;
    } else if (map[y2][x2] == EMPTY) {
        map[y2][x2] = space;
        return true;
    } else if (move(map, x2, y2, dx, dy)) {
        cout << "Move " << space << " (" << x << "," << y << ") to (" << x2 << "," << y2 << ")" << endl;
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
        if (move(map, robotX, robotY, dx, dy)) {
            robotX += dx;
            robotY += dy;
        }
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
    }

    // Get result
    int n = map.size(), m = map[0].size();
    int64_t acc = 0;
    for (int y = 0; y < n; y++) {
        for (int x = 0; x < m; x++) {
            if (map[y][x] == BOX)
                acc += x + y * 100;

            if (x == robotX && y == robotY)
                cout << '@';
            else if (map[y][x] == WALL)
                cout << '#';
            else if (map[y][x] == BOX)
                cout << 'O';
            else if (map[y][x] == EMPTY)
                cout << '.';
        }
        cout << endl;
    }
    return acc;
}

//=============== PART 2 ===============//
int64_t part2() {
    std::exit(1); // TODO
}

//=============== SOLVE ================//
int main(int argc, char *argv[]) {
    // Parse input
    int x0 = -1, y0 = -1;
    vector<vector<Space>> map;
    map.reserve(50);
    string line;
    for (int y = 0; getline(cin, line) && line != ""; y++) {
        map.push_back(vector<Space>());
        map[y].reserve(line.size());
        for (size_t x = 0; x < line.size(); x++) {
            Space space;
            switch (line[x]) {
                case '#': space = WALL; break;
                case 'O': space = BOX; break;
                case '@':
                    x0 = x;
                    y0 = y;
                default: space = EMPTY; break;
            }
            map[y].push_back(space);
        }
    }

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
    if (argc < 2) {
        std::cerr << "Please specify the part to solve." << endl;
        return 1;
    }
    switch (std::stoi(argv[1])) {
        case 1:
            cout << part1(map, moves, x0, y0) << endl;
            break;
        case 2:
            cout << part2(/*TODO*/) << endl;
            break;
        default:
            std::cerr << "Part must be 1 or 2." << endl;
            return 1;
    }
    return 0;
}

