//! Day ${day} - ${title}
//! ${url}
//!  Start: ${fetch_time}
//! Finish: TODO

use std::{env::args, io::stdin};

fn main() {
    // Parse input
    for line in stdin().lines().map(|s| s.unwrap().trim().to_string()) {
        // ...
    }

    // Solve
    let answer = match args().nth(1).and_then(|s| i32::from_str_radix(&s, 10).ok()) {
        Some(1) => part1(),
        Some(2) => part2(),
        _ => utils::print_usage_and_exit!()
    };
    println!("{answer}")
}

//=============== PART 1 ===============//
fn part1() -> i64 {
    todo!()
}

//=============== PART 2 ===============//
fn part2() -> i64 {
    todo!()
}

