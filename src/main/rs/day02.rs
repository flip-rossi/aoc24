//! Day 02 - Red-Nosed Reports
//! https://adventofcode.com/2024/day/2
//!  Start: 2024-12-02 08:51
//! Finish: 2024-12-02 11:47

use std::usize;

fn main() {
    // Parse input
    let mut reports: Vec<Vec<i32>> = Vec::new();
    for line in std::io::stdin().lines().map(|s| s.unwrap().trim().to_string()) {
        let r = line.split(" ").map(|w| w.parse().unwrap()).collect();
        reports.push(r);
    }

    // Solve
    let answer = utils::solve_puzzle!(reports);
    println!("{answer}")
}

//=============== PART 1 ===============//
#[allow(dead_code, unused_variables)]
fn part1(reports: Vec<Vec<i32>>) -> usize {
    reports.iter().filter(|r| {
        if r[0] < r[1] {
            for i in 1..r.len() {
                let diff = r[i] - r[i-1];
                if diff < 1 || diff > 3 {
                    return false
                }
            }
        } else {
            for i in 1..r.len() {
                let diff = r[i] - r[i-1];
                if diff > -1 || diff < -3 {
                    return false
                }
            }
        }
        true
    }).count()
}

//=============== PART 2 ===============//
fn part2(reports: Vec<Vec<i32>>) -> usize {
    reports.iter().filter(|r| {
        let mut prev = r[1];
        let mut safe = true;
        if prev < r[2] {
            for i in 2..r.len() {
                let diff = r[i] - prev;
                if diff < 1 || 3 < diff {
                    safe = false;
                    break;
                }
                prev = r[i];
            }
        } else {
            for i in 2..r.len() {
                let diff = r[i] - prev;
                if diff < -3 || -1 < diff {
                    safe = false;
                    break;
                }
                prev = r[i];
            }
        }
        if safe { return true; }

        for skip in 1..r.len() {
            safe = true;
            prev = r[0];
            for i in 1..r.len() {
                if i == skip { continue; }

                let diff = r[i] - prev;
                if diff < 1 || 3 < diff {
                    safe = false;
                    break;
                }
                prev = r[i];
            }
            if safe { return true; }

            safe = true;
            prev = r[0];
            for i in 1..r.len() {
                if i == skip { continue; }

                let diff = r[i] - prev;
                if diff < -3 || -1 < diff {
                    safe = false;
                    break;
                }
                prev = r[i];
            }
            if safe { return true; }
        }
        false
    }).count()
}

