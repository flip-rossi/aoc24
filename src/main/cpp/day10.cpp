/*
 * Day 10 - Hoof It
 *
 * https://adventofcode.com/2024/day/10
 *
 *  Start: 2024-12-10 09:43
 * Finish: 2024-12-10 12:08
 */

#include <cassert>
#include <cstdint>
#include <cstdio>
#include <iostream>
#include <ostream>
#include <string>
#include <utility>
#include <vector>
#include <ranges>

using std::cin;
using std::cout;
using std::endl;
using std::string;
using std::vector;

const int DIRS[][2] { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
const int GOAL_LVL = 9;

//=============== PART 1 ===============//
int dfs_score(const vector<vector<int>> &map, int x0, int y0) {
    int h = map.size(), w = map[0].size();

    int score = 0;

    auto visited = vector<vector<bool>>(h, vector<bool>(w, false));

    vector<std::pair<int, int>> stack;
    stack.push_back({x0, y0});

    for (std::pair<int, int> &pos = stack.back(); stack.size() > 0; pos = stack.back()) {
        int x = pos.first, y = pos.second;
        stack.pop_back();

        int lvl = map[y][x];
        visited[y][x] = true;

        if (lvl == GOAL_LVL) {
            score++;
            continue;
        }

        for(auto dir : DIRS) {
            int x2 = x + dir[0],
                y2 = y + dir[1];

            if (0 <= x2 && x2 < w && 0 <= y2 && y2 < h
                    && map[y2][x2] == lvl + 1 && !visited[y2][x2]) {
                stack.push_back({x2, y2});
            }
        }
    }

    return score;
}

int64_t part1(vector<vector<int>> &map) {
    int h = map.size(), w = map[0].size();

    int scores = 0;

    for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {
            if (map[y][x] == 0)
                scores += dfs_score(map, x, y);
        }
    }

    return scores;
}

//=============== PART 2 ===============//
int dfs_rating(const vector<vector<int>>& map, int x0, int y0) {
    int h = map.size(), w = map[0].size();

    int score = 0;

    vector<std::pair<int, int>> stack;
    stack.push_back({x0, y0});

    for (std::pair<int, int> &pos = stack.back(); stack.size() > 0; pos = stack.back()) {
        int x = pos.first, y = pos.second;
        stack.pop_back();

        int lvl = map[y][x];

        if (lvl == GOAL_LVL) {
            score++;
            continue;
        }

        for(auto dir : DIRS) {
            int x2 = x + dir[0],
                y2 = y + dir[1];

            if (0 <= x2 && x2 < w && 0 <= y2 && y2 < h
                    && map[y2][x2] == lvl + 1) {
                stack.push_back({x2, y2});
            }
        }
    }

    return score;
}

int64_t part2(vector<vector<int>> &map) {
    int h = map.size(), w = map[0].size();

    int ratings = 0;

    for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {
            if (map[y][x] == 0)
                ratings += dfs_rating(map, x, y);
        }
    }

    return ratings;
}

//=============== SOLVE ================//
int main(int argc, char *argv[]) {
    // Parse input
    auto map = std::ranges::views::istream<string>(cin)
        | std::views::transform([](string line) {
            return line
                | std::views::transform([](char c) { return c - '0'; })
                | std::ranges::to<vector>(); })
        | std::ranges::to<vector>();

    // Solve
    if (argc < 2) {
        std::cerr << "Please specify the part to solve." << endl;
        return 1;
    }
    switch (std::stoi(argv[1])) {
        case 1:
            cout << part1(map) << endl;
            break;
        case 2:
            cout << part2(map) << endl;
            break;
        default:
            std::cerr << "Part must be 1 or 2." << endl;
            return 1;
    }
    return 0;
}

