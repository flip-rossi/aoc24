open Core

(* For my purposes, "regex" is really only a list of strings
   (and the actual regex would be ^(foo|bar|baba)*$ *)
(*let test_words = [ "r"; "wr"; "b"; "g"; "bwu"; "rb"; "gb"; "br" ]*)
let example_words = [ "ba"; "ga"; "goo" ]
let exploded_example_words = List.map ~f:Str_utils.explode example_words

type symbol = char
(*
   type transition = symbol * state
   (* "A state is its transitions to other states."
   – Said no one, but it makes sense to me to see it like so. *)
   and state =
   | Terminal
   | Cons of transition * state
*)

type state = int
type transition = symbol * state

module StateSet = Set.Make (Int)
module StateMap = Map.Make (Int)
module SymbolMap = Map.Make (Char)

type automata =
  { init_state : state
  ; transitions : state SymbolMap.t StateMap.t
  ; accept_states : StateSet.t
  }

let words_to_automata words =
  let s0 = 0 in
  let sn = ref (s0 + 1) in
  let rec build_word state word transitions =
    match word with
    | [] -> transitions
    | [ sym ] ->
      (* Return to init state *)
      let map = Map.find transitions state |> Option.value ~default:SymbolMap.empty in
      let to_states = Map.set map ~key:sym ~data:s0 in
      Map.set transitions ~key:state ~data:to_states
    | sym :: syms ->
      let to_states, next_state =
        let map = Map.find transitions state |> Option.value ~default:SymbolMap.empty in
        match Map.add map ~key:sym ~data:!sn with
        | `Ok map ->
          sn := !sn + 1;
          map, !sn - 1
        | `Duplicate -> map, Map.find_exn map sym
      in
      Map.set transitions ~key:state ~data:to_states |> build_word next_state syms
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

let string_of_automata aut =
  "Initial state: " ^ string_of_int aut.init_state ^ "\n"
  ^ "Accept states: " ^ List.to_string (Set.to_list aut.accept_states) ~f:string_of_int ^ "\n"
  ^ "Transitions from "
  ^ (Map.to_alist aut.transitions
     |> List.map ~f:(fun (k, to_state) ->
       let v_str =
         Map.to_alist to_state
         |> List.to_string ~f:(fun (sym, state) ->
           Char.to_string sym ^ "->" ^ string_of_int state)
       in
       string_of_int k ^ ": " ^ v_str)
     |> List.fold ~init:"" ~f:(fun acc x -> acc ^ x ^ "\n                 "))
;;
