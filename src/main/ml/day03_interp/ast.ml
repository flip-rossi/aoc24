  open Core

type instr =
  | Mul of int * int
  | Valid
  | Invalid

type expr = instr list

let string_of_instr i =
    match i with
    | Mul (x, y) -> Printf.sprintf "Mul(%d,%d)" x y
    | Valid -> "Valid"
    | Invalid -> "Invalid"

  let string_of_expr e = List.fold e ~init:"[" ~f:(fun acc x -> acc^" "^(string_of_instr x)) ^ " ]"
