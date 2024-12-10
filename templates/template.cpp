/*
 * Day ${day} - ${title}
 *
 * ${url}
 *
 *  Start: ${fetch_time}
 * Finish: TODO
 */

#include <cstdint>
#include <cstdio>
#include <iostream>
#include <ostream>
#include <string>

using std::cin;
using std::cout;
using std::endl;
using std::getline;
using std::string;


//=============== PART 1 ===============//
int64_t part1() {
    return -1; // TODO
}

//=============== PART 2 ===============//
int64_t part2() {
    return -1; // TODO
}

//=============== SOLVE ================//
int main(int argc, char *argv[]) {
    // Parse input
    for (string line; getline(cin, line); ) {
        // TODO
    }

    // Solve
    if (argc < 2) {
        std::cerr << "Please specify the part to solve." << endl;
        return 1;
    }
    switch (std::stoi(argv[1])) {
        case 1:
            cout << part1(/*TODO*/) << endl;
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

