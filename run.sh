#!/bin/bash

SRC_DIR=./src/main
INPUT_DIR=./inputs

RUST_TARGET=./target/rs/debug
OCAML_TARGET=./_build/default/src/main/ml
JAVA_CLASSPATH=./target/java/classes
CPP_TARGET=./target/cpp

bad_args() {
    echo -e "USAGE: $0 [language] [day] <part> [OPTIONS...]" >&2
    echo "
Available options:
  -e       Read input from $INPUT_DIR/example.txt.
  -s       Read input from standard input.
  -f FILE  Read input from FILE." >&2
    exit 1
}

while getopts 'f:es' flag; do
    case "${flag}" in
        e) input="$INPUT_DIR/example.txt" ;;
        s) input="/dev/stdin" ;;
        f) input="$OPTARG" ;;
        *) bad_args ;;
    esac
done
shift $((OPTIND - 1))

print_msg() {
    echo "=====> $1" >&2
}

is_number() {
    [[ "$1" =~ ^[0-9]+$ ]]
}

if [[ $# -lt 1 ]]; then
    bad_args
fi

if ! $(is_number $1); then
    lang=$1
    shift
fi

if [[ $# == 2 ]]; then
    if ! $(is_number $1); then
        bad_args
    fi
    printf -v day "%02d" $1
    shift
else
    day=$(TZ='EST5' date +%d)
fi

if [[ -z "$input" ]]; then
    input="$INPUT_DIR/input$day.txt"
fi

if ! $(is_number $1); then
    bad_args
fi
part=$1
shift

if [[ ! -v lang ]]; then
    src=$(fd "day$day" | head -1)

    if [[ -z "$src" ]]; then
        print_msg "Didn't find any source files for day $day."
        exit 2
    fi

    lang=${src##*.}
fi

run_solution() {
    print_msg "time $@ $part < $input"
    ( time "$@" "$part" < "$input" ) | tee /dev/tty | tail -1 | wl-copy -n
}

case "$lang" in
    java|j)
        mvn compile &&
            run_solution java -cp "$JAVA_CLASSPATH" "Day$day"
        ;;
    rs|r|rust)
        cargo build --bin "day$day" &&
            run_solution "$RUST_TARGET/day$day"
        ;;
    cpp|c|c++)
        make "day$day" &&
            run_solution "$CPP_TARGET/day$day"
        ;;
    ocaml|ml)
        dune build &&
            run_solution "$OCAML_TARGET/day$day.exe"
        ;;
    *)
        print_msg "Language '$lang' not supported."
        ;;
esac

