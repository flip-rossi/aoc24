(*
  Day 1 - Historian Hysteria
  https://adventofcode.com/2024/day/1
*)
open! Core

let ( >> ) = Fn.compose
let neg f = not >> f

let parse_input =
  In_channel.input_lines In_channel.stdin
  |> List.map ~f:(fun line ->
         let nums =
           String.split_on_chars ~on:[ ' ' ] line
           |> List.filter ~f:(neg String.is_empty)
           |> List.map ~f:int_of_string
         in
         match nums with [ x; y ] -> (x, y) | _ -> raise (Failure "Bad input"))
  |> List.unzip

let part1 ll rl =
  let ll = List.sort ll ~compare:( - ) in
  let rl = List.sort rl ~compare:( - ) in
  List.fold2_exn ll rl ~init:0 ~f:(fun acc l r -> acc + abs (r - l))

let part2 ll rl =
  List.fold ll ~init:0 ~f:(fun acc x ->
      acc + (x * List.count rl ~f:(fun y -> y = x)))

let () =
  let solve =
    try
      match int_of_string (Sys.get_argv ()).(1) with
      | 1 -> part1
      | 2 -> part2
      | _ -> print_endline "Part must be 1 or 2."; exit 1
    with (Invalid_argument _) -> print_endline "Please specify a puzzle part."; exit 1
  in
  print_endline @@ string_of_int @@ Tuple2.uncurry solve parse_input
