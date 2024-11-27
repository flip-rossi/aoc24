#!/bin/bash

#TODO not updated from last year (only the url)

# Cleanup temp file
cleanup () {
  trap - EXIT
  if [ -e "$tmp_file" ] ; then rm -rf "$tmp_file"; fi
}
trap 'cleanup' EXIT
trap 'cleanup HUP' HUP
trap 'cleanup TERM' TERM
trap 'cleanup INT' INT

# Load SESSION_TOKEN variable
eval $(cat $(dirname $0)/.env)

if [ -z $SESSION_TOKEN ]; then
    echo "SESSION_TOKEN variable not defined in $(dirname $0)/.env"
    exit 1
fi

# Options
VERBOSE=0
while getopts "v" OPTION; do
    case $OPTION in
        v) VERBOSE=1 ;;
    esac
done
shift $(($OPTIND - 1))

# Read arguments
if [[ $1 =~ ^[0-9]+-[0-9]+$ ]]; then #if $1 is in format `<number>-<number>`
    day=$(sed -E "s/([0-9]+)-[0-9]+/\1/" <<< $1)
    part=$(sed -E "s/[0-9]+-([0-9]+)/\1/" <<< $1)
else
    day=$(( 10#$(date +%d) ))
    part=$1
fi

if [ -v 2 ]; then
    answer=$2
else
    printf -v day_padded "%02d" "${day}"

    cargo build --bin "day${day_padded}"
    output="$(time "./target/debug/day${day_padded}" "${part}" < "./inputs/input${day_padded}.txt")" ||
        exit $?
    echo

    echo -e "$output\n"

    answer="$(echo "$output" | tail -1)"
    echo -e "Got $answer\n"
fi

if [[ ! "$1" =~ ^([0-9]+-)?[0-9]+$ ]] || [ -z "$answer" ]; then
    echo "Usage: ${0} [DAY-]PART [ANSWER]"
    echo -e "If no day is given, will use today.\nIf no answer is given, will run the program and send the last output line."
    exit 2
elif [[ $part != 1 ]] && [[ $part != 2 ]]; then
    echo "PART must be 1 or 2"
    exit 2
elif [ $day -lt 1 ] || [ $day -gt 25 ]; then
    echo "Day must be in the 1-25 range"
    exit 2
fi

# Do the stuff
tmp_file=$(mktemp -t aoc_ans.XXXXX)

url="https://adventofcode.com/2024/day/${day}/answer"
echo -e "Sending ${day}-${part} answer ${answer} to ${url}..."

curl_opts="--show-error"
if [ $VERBOSE -eq 0 ]; then
    curl_opts="${curl_opts} --silent"
fi

curl ${curl_opts} --cookie session=${SESSION_TOKEN} -X POST \
    -H "Content-Type: application/x-www-form-urlencoded" --data "level=${part}&answer=${answer}" \
    ${url} > $tmp_file

if [ $VERBOSE -eq 1 ]; then
    line_no=$(grep -n 'main' ${tmp_file} | \
                sed -E "s/^([0-9]+).*$/\1/")
    tail -n +${line_no} ${tmp_file}
    echo
fi

# too many recent tries
if grep "too recently" ${tmp_file}>/dev/null; then
    cooldown=$( grep "You have" ${tmp_file} | \
        sed -E "s/.*You have (([0-9]+m )?[0-9]+s).*/\1/" )
    echo "Try again in ${cooldown}..."
# wrong answer
elif grep "That's not the right answer" ${tmp_file}>/dev/null; then
    # answer is too high
    if grep "too high" ${tmp_file}>/dev/null; then
        echo "The answer (${answer}) is too high!"
    # answer is too low
    elif grep "too low" ${tmp_file}>/dev/null; then
        echo "The answer (${answer}) is too low!"
    else
        echo "The answer (${answer}) is wrong!"
    fi
# puzzle already completed
elif grep "Did you already complete it" ${tmp_file}>/dev/null; then
    echo "Puzzle already completed."
# day not available yet
elif grep "the instant this puzzle becomes available" ${tmp_file}>/dev/null; then
    echo "Puzzle not available yet."
# right answer
elif grep "That's the right answer" ${tmp_file}>/dev/null; then
    echo "That's the right answer! Part ${part} done."
# uuuuh
else
    echo "Unexpected server response:"
    cat $tmp_file
    exit 3
fi

