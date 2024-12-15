#!/bin/bash

bad_args() {
    echo "USAGE: $0 <language> [day]"
    exit 1
}

SRC_DIR="src/main"
TEMPLATE_DIR="templates"

if [[ $# == 0 ]]; then
    bad_args
fi

lang=$1
shift

if [[ -v 1 ]]; then
    if [[ "$1" =~ ^[0-9]+$ ]]; then
        printf -v day_padded "%02d" $1
    else
        bad_args
    fi
else
    day_padded=$(TZ='EST5' date +%d)
fi
input_file="inputs/input${day_padded}.txt"
day=$((10#$day_padded))
url="https://adventofcode.com/2024/day/${day}"
shift

# Create source code file
case "$lang" in
    java|j)
        lang=java
        mkdir -p "$SRC_DIR/java"
        src_file="$SRC_DIR/java/Day$day_padded.java"
        template_file="$TEMPLATE_DIR/template.java"
        language_pretty="Java"
        ;;
    rs|r|rust)
        lang=rs
        mkdir -p "$SRC_DIR/rs"
        src_file="$SRC_DIR/rs/day$day_padded.rs"
        template_file="$TEMPLATE_DIR/template.rs"
        language_pretty="Rust"
        ;;
    cpp|c|c++)
        lang=cpp
        mkdir -p "$SRC_DIR/cpp"
        src_file="$SRC_DIR/cpp/day$day_padded.cpp"
        template_file="$TEMPLATE_DIR/template.cpp"
        language_pretty="C++"
        ;;
    ml|ocaml)
        lang=ml
        src_file="$SRC_DIR/ml/day$day_padded.ml"
        template_file="$TEMPLATE_DIR/template.ml"
        language_pretty="OCaml"
        ;;
    *)
        echo "Available languages: java|j, rust|rs|r, c++|cpp|c, ocaml|ml"
        exit 1
        ;;
esac

# Download personal input
. .env # .env should contain the line `SESSION_TOKEN=yoursessiontoken`

if [ ! -e $input_file ]; then
    curl -b session=${SESSION_TOKEN} "${url}/input" > $input_file &&
        echo -e "\nFetched input for day ${day} to ${input_file}.\nPreview:"
else
    echo "${input_file} already exists."
fi
head $input_file
echo

# Get day's title
title=$(curl -s ${url} | grep -m 1 "<h2>--- Day" | sed -E "s/^.*<h2>--- Day [0-9]{1,2}: (.*) ---<\/h2>.*$/\1/")
echo "Day $day: $title"

echo -e "\nSee the puzzle description at $url"

# Create source code file from template and add day to Answers.md
echo "Creating new file $src_file from template..."

fetch_time="$(date '+%Y-%m-%d %R')"
escaped_url="${url//\//\\/}"
template_substs='s/\$\{day\}/'$day'/g;
                 s/\$\{day_padded\}/'$day_padded'/g;
                 s/\$\{title\}/'$title'/g;
                 s/\$\{url\}/'$escaped_url'/g;
                 s/\$\{fetch_time\}/'$fetch_time'/g'

if [ -e $src_file ]; then
    echo "$src_file already exists."
    exit
fi

cp "$template_file" $src_file
sed -E "$template_substs" "$template_file" > "$src_file"
echo "| [Day $day: $title]($url) |           |           | [$language_pretty]($src_file) |" >> Answers.md

# Do extra stuff, depending on language
case "$lang" in
    #java) nothing ;;
    rs)
        echo \
"[[bin]]
name = \"day$day_padded\"
path = \"$src_file\"" \
            >> Cargo.toml
        ;;
    #cpp) nothing ;;
    ml)
        sed -E -i 's/\((names|public_names)(.*)\)/\(\1\2 '"day$day_padded"'\)/' "$SRC_DIR/ml/dune"
        ;;
esac

# Open puzzle in browser and editor
# TODO pass script argument to do this instead

xdg_open_background() {
    url="$1"
    nohup xdg-open "$url" &> /dev/null &
    disown
}

nohup xdg-open "$url" &> /dev/null & disown
nohup alacritty -e nvim "$src_file" "./inputs/example.txt" "./inputs/input${day_padded}.txt" "./Answers.md" &> /dev/null & disown

