type expr =
  (* | Int of int *)
  | Emp
  | Seq of expr * expr
  | Mul of int * int
  | Valid of expr
  | Invalid of expr
