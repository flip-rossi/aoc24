#!/bin/bash

bad_args() {
    echo "USAGE: $0 [language] [day] <part>"
    exit 1
}

is_number() {
    [[ "$1" =~ ^[0-9]+$ ]]
}

SRC_DIR=./src/main
INPUT_DIR=./inputs

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

input="$INPUT_DIR/input$day.txt"

if ! $(is_number $1); then
    bad_args
fi
part=$1
shift

if [[ ! -v lang ]]; then
    src=$(fd "day$day" | head -1)

    if [[ -z "$src" ]]; then
        echo "Didn't find any source files for day $day."
        exit 2
    fi

    lang=${src##*.}
fi

case "$lang" in
    java|j)
        mvn compile &&
            java -cp "./target/java/classes" "Day$day" "$part" < "$input"
        ;;
    rs|r|rust)
        cargo run --bin "day$day" "$part" < "$input"
        ;;
    cpp|c|c++)
        make "day$day" &&
            "./target/cpp/day$day" < "$input"
        ;;
    ocaml|ml)
        DUNE_BUILD_DIR=$PWD/target/ml dune exec "day$day" < "$input"
        ;;
    *)
        echo "Language '$lang' not supported."
        ;;
esac

