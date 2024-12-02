/**
Formats and prints a message to [`io::stderr`] and
terminates the program with the given exit code.

Uses [`eprintln!`] then [`std::process::exit()`].

# Examples

```
exit_msg!(1, "Argument expected.");
```
*/
#[macro_export]
macro_rules! exit_msg {
    ($exit_code:expr, $msg:literal$(, $args:expr)*) => {{
        eprintln!($msg, $($args),*);
        std::process::exit($exit_code)
    }};
}

#[macro_export]
macro_rules! print_usage_and_exit {
    () => {{
        $crate::exit_msg!(1, "USAGE: {} {{1,2}}", std::env::args().nth(0).unwrap());
    }};
}

/**
Execute solution for a given part of the day's puzzle,
calling the approptiate function with the passed arguments.

The part is specified by the first argument the program was executed with.

Requires the functions `part1()` and `part2()` to exist.
If those functions take any arguments, they must be passed
to the macro.

# Example

```
fn main() {
    let answer = solve_puzzle!();
    println!("{answer}");
}

fn part1() -> i32 {
    let mut answer = 0;
    // code that solves part 1
    // ...
    answer
}

fn part2() -> i32 {
    let mut answer = 0;
    // code that solves part 2
    // ...
    answer
}
```
*/
#[macro_export]
macro_rules! solve_puzzle {
    ($($puzzle_args:expr),*) => {
        match std::env::args().nth(1) {
            Some(s) => {
                match u8::from_str_radix(&s, 10) {
                    Ok(1) => part1($($puzzle_args),*),
                    Ok(2) => part2($($puzzle_args),*),
                    _ => $crate::exit_msg!(1, "Usage: Part must be 1 or 2"),
                }
            },
            None => $crate::print_usage_and_exit!(),
        }
    };
}

