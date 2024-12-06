{
open Parser
open Core
}

let digit = ['0'-'9']
let int = '-'? digit+
let do = "do()"
let dont = "don't()"

rule read =
  parse
    | "mul(" int "," int ")" { MUL (
        let str = Lexing.lexeme lexbuf in
        let l = String.split_on_chars ~on:['('; ','; ')'] str in
        match l with
        | [_; x; y; _] -> (int_of_string x, int_of_string y)
        | _ -> assert false
    )}
    | do { DO }
    | dont { DONT }
    | eof { EOF }
    | _ { SEP }
