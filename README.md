
# Advent of Code '24 solutions

These are my solutions for the [2024 edition](https://adventofcode.com/2024) of the annual event **Advent of Code**.

- The code for each solution is in src/main/, in either Java, Rust, C++, or OCaml.
- [Answers.md](Answers.md) – The answers for each day and languages it was solved with.

### Helper scripts

There are three helper scripts to prepare solutions, test, and submit answers:  
- [setup_day.sh](setup_day.sh)  
  Creates a new source code file in src/main/ from one of the templates in templates/
  and downloads the day's input to inputs/.
- [submit_answer.sh](submit_answer.sh)  
  Submits the answer for the day and selected puzzle part.
  If no answer is passed as argument, reads from clipboard.
- [run.sh](run.sh)  
  Compiles and runs the solution in any language for the specified day (or today,
  if none is specified).

They both need you to have a .env file in the root of the project with a line like this: `SESSION_TOKEN=yoursessiontoken`  
You can get your session token by looking at your cookies when logged on in the [AoC website](https://adventofcode.com).

**Don't let anyone have access to your session token.**

