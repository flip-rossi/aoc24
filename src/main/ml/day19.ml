(*
   Day 19 - Linen Layout
   https://adventofcode.com/2024/day/19
   Start: 2024-12-20 14:32
   Finish: TODO
*)
open! Core

(*(*(*(*(*(*(*(*(*( PARSE INPUT )*)*)*)*)*)*)*)*)*)
let patterns =
  let open Str in
  match In_channel.input_line In_channel.stdin with
  | Some line -> split (regexp ", ") line
  | None -> failwith "Bad input"
;;

(* Read empty input line *)
In_channel.input_line In_channel.stdin ;;

let designs =
  In_channel.input_lines In_channel.stdin
;;

(*(*(*(*(*(*(*(*(*( PART 1 )*)*)*)*)*)*)*)*)*)
let rec part1 patterns designs =
  let pat_regexp = List.fold patterns ~init:{|^\(|} ~f:(fun acc x -> acc ^ {|\||} ^ x) ^ {|\)*$|} |> Str.regexp in
  let possible design =
    if Str.string_match pat_regexp design 0 then 1 else 0
  in
  match designs with
  | [] -> 0
  | x::xs -> possible x + part1 patterns xs
;;

(*(*(*(*(*(*(*(*(*( PART 2 )*)*)*)*)*)*)*)*)*)
let part2 _ = raise (Invalid_argument "Part 2 not solved yet.")

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
    with
    | Invalid_argument _ ->
      print_endline "Please specify the part to solve.";
      exit 1
  in
  print_endline @@ string_of_int @@ solve patterns designs
;;

