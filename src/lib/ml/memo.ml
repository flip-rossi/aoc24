(* This is awesome: https://ocaml.org/docs/memoization *)

let memo ?(size = 42) f =
  let h = Hashtbl.create size in
  fun x ->
    try Hashtbl.find h x with
    | Not_found ->
      let y = f x in
      Hashtbl.add h x y;
      y
;;

let memo_rec ?(size = 42) f =
  let h = Hashtbl.create size in
  let rec g x =
    try Hashtbl.find h x with
    | Not_found ->
      let y = f g x in
      Hashtbl.add h x y;
      y
  in
  g
;;
