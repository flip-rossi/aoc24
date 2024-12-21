open Stdlib

(** A string of the single caracter c *)
let of_char c = Stdlib.String.make 1 c

(** Converts a string to a list of chars *)
let explode str =
  let rec explode_inner cur_index chars =
    if cur_index < String.length str
    then (
      let new_char = str.[cur_index] in
      explode_inner (cur_index + 1) (chars @ [ new_char ]))
    else chars
  in
  explode_inner 0 []
;;

(** Converts a list of chars to a string *)
let rec implode chars =
  match chars with
  | [] -> ""
  | h :: t -> of_char h ^ implode t
;;
