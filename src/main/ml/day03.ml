(*
  Day 03 - Mull It Over
  https://adventofcode.com/2024/day/3
   Start: 2024-12-03 10:08
  Finish: 2024-12-03 16:46
*)
open! Core
open Day03_interp
open Ast
open Parse

let parsed_input = In_channel.input_all In_channel.stdin |> parse |> simpl

let part1 expr =
  let rec sum_muls e =
    match e with
    | Emp -> 0
    | Mul (x, y) -> x * y
    | Seq (e1, e2) -> sum_muls e1 + sum_muls e2
    | Valid e1 | Invalid e1 -> sum_muls e1
  in
  sum_muls expr

let part2 expr =
  let rec sum_muls valid expr =
    match expr with
    | Emp -> 0
    | Mul (x, y) -> if valid then x * y else 0
    | Valid e1 -> sum_muls true e1
    | Invalid e1 -> sum_muls false e1
    | Seq (e1, e2) -> sum_muls valid e1 + sum_muls valid e2
  in
  sum_muls true expr

let () =
  let solve =
    try
      match int_of_string (Sys.get_argv ()).(1) with
      | 1 -> part1
      | 2 -> part2
      | _ ->
          print_endline "Part must be 1 or 2.";
          exit 1
    with Invalid_argument _ ->
      print_endline "Please specify a puzzle part.";
      exit 1
  in
  print_endline @@ string_of_int @@ solve parsed_input