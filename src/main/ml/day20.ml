(*
   Day 20 - Race Condition
   https://adventofcode.com/2024/day/20
   Start: 2024-12-23 11:16
   Finish: TODO
*)
open! Core

type pos =
  { x : int
  ; y : int
  }

module Position = struct
  type t = pos

  let compare a b =
    match a.x - b.x with
    | 0 -> a.y - b.y
    | comp -> comp
  ;;

  let sexp_of_t { x; y } = sexp_of_pair sexp_of_int sexp_of_int (x, y)
  let to_string { x; y } = "{ x = " ^ string_of_int x ^ "; y = " ^ string_of_int y ^ " }"
end

let dirs = [ { x = 1; y = 0 }; { x = -1; y = 0 }; { x = 0; y = 1 }; { x = 0; y = -1 } ]

type space =
  | Free
  | Wall
  | End

(*(*(*(*(*(*(*(*(*( PARSE INPUT )*)*)*)*)*)*)*)*)*)
let parsed_input =
  let start = ref None in
  let map =
    In_channel.input_lines In_channel.stdin
    |> List.mapi ~f:(fun y line ->
      Utils.Str_utils.explode line
      |> List.mapi ~f:(fun x c ->
        match c with
        | '#' -> Wall
        | '.' -> Free
        | 'E' -> End
        | 'S' ->
          start := Some { x; y };
          Free
        | c -> failwith ("Bad input. Unrecognized space '" ^ Char.to_string c ^ "'."))
      |> List.to_array)
    |> List.to_array
  in
  Option.value_exn ~message:"Bad input. No start position." !start, map
;;

(*(*(*(*(*(*(*(*(*( PART 1 )*)*)*)*)*)*)*)*)*)
(* Takes quite a while (around 43s), but it works. (Part 2 will hurt) *)
let part1 start map =
  let open Utils in
  let n, m = Array.length map, Array.length map.(0) in
  let rec bfs_explore count_fun found q node lvl cheat =
    let found = Set.add found node in
    let q =
      List.filter_map dirs ~f:(fun { x = dx; y = dy } ->
        let x, y = node.x + dx, node.y + dy in
        if 0 <= y && y < n && 0 <= x && x < m && not (Set.mem found { x; y })
        then Some ({ x; y }, lvl + 1)
        else None)
      |> Func_queue.append q
    in
    bfs_count count_fun found q cheat
  and bfs_count count_fun found q cheat =
    match Func_queue.dequeue q with
    | _, None -> failwith "No path found."
    | q, Some (node, lvl) ->
      (match map.(node.y).(node.x) with
       | End -> count_fun lvl
       | Wall ->
         bfs_count count_fun found q cheat
         + if cheat then bfs_explore count_fun found q node lvl false else 0
       | Free -> bfs_explore count_fun found q node lvl cheat)
  in
  let queue0 = Func_queue.singleton (start, 0) in
  let found0 =
    Set.singleton
      (module struct
        include Position
        include Comparator.Make (Position)
      end)
      start
  in
  let count_lvl lvl = lvl in
  let cheatless_len = bfs_count count_lvl found0 queue0 false in
  let count_100_diff lvl = if lvl <= cheatless_len - 100 then 1 else 0 in
  bfs_count count_100_diff found0 queue0 true
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
  print_endline @@ string_of_int @@ Tuple2.uncurry solve parsed_input
;;
