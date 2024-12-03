open Ast

let parse (s: string) : expr =
  let lexbuf = Lexing.from_string s in
  let ast = Parser.prog Lexer.read lexbuf in
  ast

let rec simpl (e: expr) : expr =
  match e with
  | Seq (Emp, Emp) -> Emp
  | Seq (Emp, x) | Seq (x, Emp) -> simpl (simpl x)
  | Seq (x, y) -> Seq (simpl x, y)
  | _ -> e
