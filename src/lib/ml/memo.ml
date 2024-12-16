(* This is awesome: https://ocaml.org/docs/memoization *)

let memo ?(hashtbl = Hashtbl.create 42) f =
  fun x ->
    try Hashtbl.find hashtbl x with
    | Not_found ->
      let y = f x in
      Hashtbl.add hashtbl x y;
      y
;;

let memo_rec ?(hashtbl = Hashtbl.create 42) f =
  let rec g x =
    try Hashtbl.find hashtbl x with
    | Not_found ->
      let y = f g x in
      Hashtbl.add hashtbl x y;
      y
  in
  g
;;
