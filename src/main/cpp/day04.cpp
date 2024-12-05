/*
 * Day 04 - Ceres Search
 *
 * https://adventofcode.com/2024/day/4
 *
 *  Start: 2024-12-04 12:31
 * Finish: TODO
 */

#include <cstddef>
#include <cstdint>
#include <cstdio>
#include <iostream>
#include <ostream>
#include <string>
#include <vector>

using std::cin;
using std::cout;
using std::endl;
using std::getline;
using std::string;
using std::vector;

const string XMAS = "XMAS";
const string SAMX = "SAMX";
const size_t X_LEN = XMAS.length();

int64_t part1(vector<string> lines) {
    int count {0};

    size_t n {lines.size()},
           m {lines[0].size()};
    for (size_t i {0}; i < n; i++) {
        for (size_t j {0}; j < m; j++) {
            string looking;
            if (lines[i][j] == XMAS[0])
                looking = XMAS;
            else if (lines[i][j] == SAMX[0])
                looking = SAMX;
            else continue;

            for (int down {0};
                    down <= (n - i >= X_LEN ? 1 : 0);
                    down++) {
                for (int right {j + 1 >= X_LEN && down == 1 ? -1 : down == 1 ? 0 : 1};
                        right <= (m - j >= X_LEN ? 1 : 0);
                        right++) {
                    int y = i + down, x = j + right;
                    for (size_t c {1}; c < X_LEN; c++, x += right, y += down) {
                        if (lines[y][x] != looking[c])
                            goto change_dir;
                    }
                    count++;
                change_dir: ;
                }
            }
        }
    }

    return count;
}

int64_t part2() {
    // TODO
    return -1;
}

int main(int argc, char *argv[]) {
    // Parse input
    vector<string> lines = vector<string>();
    for (string line; getline(cin, line); )
        lines.push_back(line);

    // Solve
    if (argc < 2) {
        std::cerr << "Please specify the part to solve." << endl;
        return 1;
    }
    switch (std::stoi(argv[1])) {
        case 1:
            cout << part1(lines) << endl;
            break;
        case 2:
            cout << part2() << endl;
            break;
        default:
            std::cerr << "Part must be 1 or 2." << endl;
            return 1;
    }
    return 0;
}

