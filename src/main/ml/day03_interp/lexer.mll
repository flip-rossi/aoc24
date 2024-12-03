{
open Parser
}

let digit = ['0'-'9']
let int = '-'? digit+
let mul = "mul"
let do = "do"
let dont = "don't"

rule read =
  parse
    | "(" { LPAREN }
    | ")" { RPAREN }
    | "," { COMMA }
    | int { INT (int_of_string (Lexing.lexeme lexbuf)) }
    | mul { MUL }
    | do { DO }
    | dont { DONT }
    | eof { EOF }
    | _ { SEP }
