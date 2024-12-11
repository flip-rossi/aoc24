(*
   Day ${day} - ${title}
   ${url}
   Start: ${fetch_time}
   Finish: TODO
*)
open! Core

(*(*(*(*(*(*(*(*(*( PARSE INPUT )*)*)*)*)*)*)*)*)*)
let parsed_input =
  In_channel.input_lines In_channel.stdin |> List.map ~f:(fun line -> line)
;;

(*(*(*(*(*(*(*(*(*( PART 1 )*)*)*)*)*)*)*)*)*)
let part1 _ = raise (Invalid_argument "Part 1 not solved yet.")

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
  print_endline @@ string_of_int @@ (* Tuple2.uncurry *) solve parsed_input
;;

