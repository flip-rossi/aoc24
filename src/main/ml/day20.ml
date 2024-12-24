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
[@@deriving sexp, compare, hash]

module Position = struct
  module T = struct
    type t = pos [@@deriving sexp, compare, hash]

    let to_string pos =
      "{x=" ^ string_of_int pos.x ^ "; y=" ^ string_of_int pos.y ^ "}"
    ;;
  end

  include T
  include Comparator.Make (T)
end

let dirs = [ { x = 1; y = 0 }; { x = -1; y = 0 }; { x = 0; y = 1 }; { x = 0; y = -1 } ]

type space =
  | Free
  | Wall
  | End

(*(*(*(*(*(*(*(*(*( PARSE INPUT )*)*)*)*)*)*)*)*)*)
let parsed_input =
  let start = ref None in
  let finish = ref None in
  let map =
    In_channel.input_lines In_channel.stdin
    |> List.mapi ~f:(fun y line ->
      String.to_list line
      |> List.mapi ~f:(fun x c ->
        match c with
        | '#' -> Wall
        | '.' -> Free
        | 'E' ->
          finish := Some { x; y };
          End
        | 'S' ->
          start := Some { x; y };
          Free
        | c -> failwith ("Bad input. Unrecognized space '" ^ Char.to_string c ^ "'."))
      |> List.to_array)
    |> List.to_array
  in
  Option.value_exn !start, map, Option.value_exn !finish
;;

(*(*(*(*(*(*(*(*(*( PART 1 )*)*)*)*)*)*)*)*)*)
(* Takes quite a while (around 43s), but it works. (Part 2 will hurt) *)
let part1 start map _ =
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
let neighbors n m from_pos =
  List.filter_map dirs ~f:(fun { x = dx; y = dy } ->
    let x, y = from_pos.x + dx, from_pos.y + dy in
    if 0 <= y && y < n && 0 <= x && x < m then Some { x; y } else None)
;;

let mat_get mat pos = mat.(pos.y).(pos.x)
let mat_set mat pos value = mat.(pos.y).(pos.x) <- value

(* 180473 too low; 5660267 too high; 2104420, 2118665, 2506901, 2583534 wrong *)
let part2 _start map finish =
  let max_cheat = 20 in
  let min_save = 100 in
  let n, m = Array.length map, Array.length map.(0) in
  let neighbors = neighbors n m in
  let dists = Array.make_matrix ~dimx:n ~dimy:m None in
  let rec get_dists_to_end pos dist =
    match mat_get map pos, mat_get dists pos with
    | Wall, _ | _, Some _ -> ()
    | _ ->
      mat_set dists pos (Some dist);
      List.iter (neighbors pos) ~f:(fun next -> get_dists_to_end next (dist + 1))
  in
  get_dists_to_end finish 0;
  let dcheat = List.range ~start:`inclusive ~stop:`inclusive (-max_cheat) max_cheat in
  Array.foldi dists ~init:0 ~f:(fun y acc row ->
    let dys = List.filter dcheat ~f:(fun dy -> 0 <= y + dy && y + dy < n) in
    acc
    + Array.foldi row ~init:0 ~f:(fun x acc cell ->
      acc
      +
      match cell with
      | None -> 0
      | Some dist0 ->
        let dxs = List.filter dcheat ~f:(fun dx -> 0 <= x + dx && x + dx < m) in
        List.count (List.cartesian_product dxs dys) ~f:(fun (dx, dy) ->
          if dx + dy > max_cheat
          then false
          else (
            let pos = { x = x + dx; y = y + dy } in
            match mat_get dists pos with
            | None -> false
            | Some dist ->
              print_endline @@ "Saving " ^ string_of_int (dist0 - (dist + abs dx + abs dy)) ^ "ps at " ^ Position.to_string { x; y } ^ "(" ^ string_of_int dist0 ^ ") -> " ^ Position.to_string pos ^ "(" ^ string_of_int dist ^ ")";
              dist0 - (dist + abs dx + abs dy) >= min_save))))
;;

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
  print_endline @@ string_of_int @@ Tuple3.uncurry solve parsed_input
;;
