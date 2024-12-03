type expr =
(* | Int of int *)
| Emp
| Mul of int * int
| Seq of expr * expr
