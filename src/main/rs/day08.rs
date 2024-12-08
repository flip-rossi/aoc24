//! Day 8 - Resonant Collinearity
//! https://adventofcode.com/2024/day/8
//!  Start: 2024-12-08 10:42
//! Finish: TODO

use std::{collections::HashMap, env::args, io::stdin, usize};

fn main() {
    let (mut x_len, mut y_len) = (0, 0);
    let mut antennae = HashMap::new();

    // Parse input
    let mut y = 0;
    for line in stdin().lines().map(|s| s.unwrap().trim().to_string()) {
        x_len = line.len() as i32;
        y_len += 1;

        let mut x = 0;
        for c in line.chars() {
            if c != '.' {
                let locs = antennae.entry(c).or_insert_with(Vec::new);
                locs.push((x, y));
            }
            x += 1;
        }
        y += 1;
    }

    // Solve
    let answer = match args().nth(1).and_then(|s| i32::from_str_radix(&s, 10).ok()) {
        Some(1) => part1(antennae, x_len, y_len),
        Some(2) => part2(),
        _ => utils::print_usage_and_exit!(),
    };
    println!("{answer}")
}

//=============== PART 1 ===============//
fn part1(antennae: HashMap<char, Vec<(i32, i32)>>, x_len: i32, y_len: i32) -> i64 {
    let (x_lims, y_lims) = (0..x_len, 0..y_len);
    let mut found = vec![vec![false; x_len as usize]; y_len as usize];
    antennae.iter()
        .map(|(_c, locs)| {
            let mut count = 0;
            for i in 0..locs.len() - 1 {
                let (x1, y1) = locs[i];
                for (x2, y2) in &locs[i + 1..] {
                    let (dx, dy) = (x2 - x1, y2 - y1);
                    let (x1_next, y1_next) = (x1 - dx, y1 - dy);
                    if x_lims.contains(&x1_next)
                        && y_lims.contains(&y1_next)
                        && !found[y1_next as usize][x1_next as usize]
                    {
                        found[y1_next as usize][x1_next as usize] = true;
                        count += 1;
                    }
                    let (x2_next, y2_next) = (x2 + dx, y2 + dy);
                    if x_lims.contains(&x2_next)
                        && y_lims.contains(&y2_next)
                        && !found[y2_next as usize][x2_next as usize]
                    {
                        found[y2_next as usize][x2_next as usize] = true;
                        count += 1;
                    }
                }
            }
            count
        })
        .sum()
}

//=============== PART 2 ===============//
fn part2() -> i64 {
    todo!()
}
