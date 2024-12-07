(*
  Day 7 - Bridge Repair
  https://adventofcode.com/2024/day/7
   Start: 2024-12-07 19:46
  Finish: TODO
*)
open! Core

(*(*(*(*(*(*(*(*(*( PARSE INPUT )*)*)*)*)*)*)*)*)*)
let parsed_input =
  let open Str in let open List in
  In_channel.input_lines In_channel.stdin
  |> map ~f:(fun s -> split (regexp ": \| ") s |> map ~f:int_of_string)
  |> map ~f:(fun ws ->
    match ws with
    | x::xs -> (x, xs)
    | _ -> assert false)

(*(*(*(*(*(*(*(*(*( PART 1 )*)*)*)*)*)*)*)*)*)
let part1 tests =
  let rec valid tVal nums =
    match nums with
    | [] -> tVal = 0
    | [x] -> tVal = x
    | x::y::xs -> valid tVal ((x * y) :: xs) || valid tVal ((x + y)::xs)
  in
  List.fold ~init:0 ~f:(fun acc (v, nums) -> acc + if valid v nums then v else 0) tests

(*(*(*(*(*(*(*(*(*( PART 2 )*)*)*)*)*)*)*)*)*)
let part2 tests = raise (Invalid_argument "Part 2 not solved yet.")

(*(*(*(*(*(*(*(*(*( SOLVE )*)*)*)*)*)*)*)*)*)
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
      print_endline "Please specify the part to solve.";
      exit 1
  in
  print_endline @@ string_of_int @@ solve parsed_input
