(*
   Day 11 - Plutonian Pebbles
   https://adventofcode.com/2024/day/11
   Start: 2024-12-11 09:57
   Finish: 2024-12-11 13:43
*)
open! Core

(*(*(*(*(*(*(*(*(*( PARSE INPUT )*)*)*)*)*)*)*)*)*)
let parsed_input =
  match In_channel.input_lines In_channel.stdin with
  | line :: _ -> String.split line ~on:' ' |> List.map ~f:int_of_string
  | [] -> raise (Invalid_argument "Bad input.")
;;

(*(*(*(*(*(*(*(*(*( PART 1 )*)*)*)*)*)*)*)*)*)
let int_log10 x = int_of_float @@ Float.log10 @@ float_of_int x
let int_exp10 x = int_of_float @@ (10. ** float_of_int x)

let rec blink blinks stone =
  let nxt_blink = blinks - 1 in
  if blinks = 0
  then 1
  else if stone = 0
  then blink nxt_blink 1
  else (
    let digits = 1 + int_log10 stone in
    if digits % 2 = 0
    then (
      let exp = int_exp10 (digits / 2) in
      let left = stone / exp in
      let right = stone % exp in
      (* printf "%d -> (%d / %d), (%d %% %d) = %d, %d\n" stone stone exp stone exp left right; *)
      blink nxt_blink left + blink nxt_blink right)
    else blink nxt_blink (stone * 2024))
;;

let rec part1 pebbles =
  match pebbles with
  | [] -> 0
  | x :: xs -> blink 25 x + part1 xs
;;

(*(*(*(*(*(*(*(*(*( PART 2 )*)*)*)*)*)*)*)*)*)
let blink_memo blinks stone =
  let blink self blinks stone =
    let nxt_blink = blinks - 1 in
    if blinks = 0
    then 1
    else if stone = 0
    then self nxt_blink 1
    else (
      let digits = 1 + int_log10 stone in
      if digits % 2 = 0
      then (
        let exp = int_exp10 (digits / 2) in
        let left = stone / exp in
        let right = stone % exp in
        self nxt_blink left + self nxt_blink right)
      else self nxt_blink (stone * 2024))
  in
  let uncurried self (x, y) = blink (Tuple2.curry self) x y in
  Utils.Memo.memo_rec uncurried (blinks, stone)
;;

let rec part2 pebbles =
  match pebbles with
  | [] -> 0
  | x :: xs -> blink_memo 75 x + part2 xs
;;

(*(*(*(*(*(*(*(*(*( SOLVE )*)*)*)*)*)*)*)*)*)
let () =
  let open Core in
  let solve =
    try
      match int_of_string (Sys.get_argv ()).(1) with
      | 1 -> part1
      | 2 -> part2
      | _ ->
        print_endline "Part must be 1 or 2.";
        exit 1
    with
    | Invalid_argument _ ->
      print_endline "Please specify the part to solve.";
      exit 1
  in
  print_endline @@ string_of_int @@ solve parsed_input
;;
