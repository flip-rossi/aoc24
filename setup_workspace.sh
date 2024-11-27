#!/bin/bash

#TODO not updated from last year (only the urls)

SESSION="AoC"
TEST_W="Test"

LEADERBOARD_URL="https://adventofcode.com/2024/leaderboard/private/view/2285997"

RUST_DOCS_URL=("https://doc.rust-lang.org/std/index.html" "https://doc.rust-lang.org/book/")

working_dir="$(realpath $(dirname "$0"))"
cd "$working_dir"

if [ -v 1 ]; then
    printf -v day_padded "%02d" $1
else
    day_padded=$(TZ='EST5' date +%d)
fi
day=$((10#$day_padded))
url="https://adventofcode.com/2024/day/${day}"

test_cmd="cargo run --bin day${day_padded} < inputs/example.txt"

# Setup tmux session
tmux new-session -d -s "$SESSION"
tmux rename-window -t 0 "$TEST_W"
tmux send-keys -t 0 "$test_cmd"

tmux new-window -t "$SESSION"
tmux send-keys -t 1 "git fetch --all" C-m \
    "./setup_day.sh ${day}" C-m

# Open editor in new Alacritty window
alacritty msg create-window --working-directory "$working_dir" -e \
    "$EDITOR" "./Answers.md" "./src/bin/day${day_padded}.rs" "./inputs/input${day_padded}.txt" "./inputs/example1.txt"

# Open firefox and attatch tmux session
firefox "$url" "$LEADERBOARD_URL" "${RUST_DOCS_URL[@]}" > /dev/null 2>&1 &
    tmux attach-session -c "$working_dir" -t "$SESSION:1"
