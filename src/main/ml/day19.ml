(*
   Day 19 - Linen Layout
   https://adventofcode.com/2024/day/19
   Start: 2024-12-20 14:32
   Finish: 2024-12-21 22:16
*)
open! Core

(*(*(*(*(*(*(*(*(*( PARSE INPUT )*)*)*)*)*)*)*)*)*)
let patterns =
  let open Str in
  match In_channel.input_line In_channel.stdin with
  | Some line -> split (regexp ", ") line
  | None -> failwith "Bad input"
;;

(* Skip empty input line *)
In_channel.input_line In_channel.stdin

let designs = In_channel.input_lines In_channel.stdin

(*(*(*(*(*(*(*(*(*( PART 1 )*)*)*)*)*)*)*)*)*)
let rec part1 patterns designs =
  let pat_regexp =
    List.fold patterns ~init:{|^\(|} ~f:(fun acc x -> acc ^ {|\||} ^ x) ^ {|\)*$|}
    |> Str.regexp
  in
  let possible design = if Str.string_match pat_regexp design 0 then 1 else 0 in
  match designs with
  | [] -> 0
  | x :: xs -> possible x + part1 patterns xs
;;

(*(*(*(*(*(*(*(*(*( PART 2 )*)*)*)*)*)*)*)*)*)
module Symbol = struct
  type t =
  | Epsilon
  | Sym of char
  [@@deriving sexp]

  let compare x y =
    match (x, y) with
    | Epsilon, Epsilon -> 0
    | Epsilon, _ -> -1
    | _, Epsilon -> 1
    | Sym a, Sym b -> Char.compare a b

  let to_string sym =
    match sym with
    | Epsilon -> "Eps" (* "\u{0190}" *) (* Captial epsilon *)
    | Sym c -> Char.to_string c
end

type state = int

module StateSet = Int.Set
module StateMap = Int.Map
module SymbolMap = Map.Make (Symbol)

type automaton =
  { init_state : state
  ; transitions : state SymbolMap.t StateMap.t
  ; accept_states : StateSet.t
  }

let words_to_automaton words =
  let s0 = 0 in
  let sn = ref (s0 + 1) in
  let rec build_word state word transitions =
    match word with
    | [] ->
      let map = Map.find transitions state |> Option.value ~default:SymbolMap.empty in
      let to_states = Map.set map ~key:Epsilon ~data:s0 in
      Map.set transitions ~key:state ~data:to_states
    | c :: cs ->
      let to_states, next_state =
        let map = Map.find transitions state |> Option.value ~default:SymbolMap.empty in
        match Map.add map ~key:(Sym c) ~data:!sn with
        | `Ok map ->
          sn := !sn + 1;
          map, !sn - 1
        | `Duplicate -> map, Map.find_exn map (Sym c)
      in
      Map.set transitions ~key:state ~data:to_states |> build_word next_state cs
  in
  let rec build_aut words transitions =
    match words with
    | [] -> transitions
    | w :: ws -> build_word s0 w transitions |> build_aut ws
  in
  { init_state = s0
  ; transitions = build_aut words StateMap.empty
  ; accept_states = StateSet.singleton 0
  }
;;

let string_of_automaton aut =
  "Initial state: " ^ string_of_int aut.init_state ^ "\n"
  ^ "Accept states: " ^ List.to_string (Set.to_list aut.accept_states) ~f:string_of_int ^ "\n"
  ^ "Transitions from "
  ^ (Map.to_alist aut.transitions
     |> List.map ~f:(fun (k, to_state) ->
       let v_str =
         Map.to_alist to_state
         |> List.to_string ~f:(fun (sym, state) ->
           Symbol.to_string sym ^ "->" ^ string_of_int state)
       in
       string_of_int k ^ ": " ^ v_str)
     |> List.fold ~init:"" ~f:(fun acc x -> acc ^ x ^ "\n                 "))
;;
let part2 patterns designs =
  let patterns, designs =
    Utils.Str_utils.(List.map patterns ~f:explode, List.map designs ~f:explode)
  in
  let automaton = words_to_automaton patterns in
  print_endline (string_of_automaton automaton);
  let possibilities self state design =
    let to_states = Map.find_exn automaton.transitions state in
    (match Map.find to_states Epsilon with
     | None -> 0
     | Some next_state -> self next_state design)
    +
    match design with
    | [] -> if Set.mem automaton.accept_states state then 1 else 0
    | c :: cs ->
      (match Map.find to_states (Sym c) with
       | None -> 0
       | Some next_state -> self next_state cs)
  in
  let h = Stdlib.Hashtbl.create 2048 in
  let possibilities_mem state design =
    Utils.Memo.memo_rec
      ~hashtbl:h
      (fun self (x, y) -> possibilities (Tuple2.curry self) x y)
      (state, design)
  in
  List.sum (module Int) designs ~f:(possibilities_mem automaton.init_state)
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
  print_endline @@ string_of_int @@ solve patterns designs
;;
