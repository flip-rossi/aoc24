{
open Parser
}

let digit = ['0'-'9']
let int = '-'? digit+
let mul = "mul"

rule read =
  parse
    | "(" { LPAREN }
    | ")" { RPAREN }
    | "," { COMMA }
    | int { INT (int_of_string (Lexing.lexeme lexbuf)) }
    | mul { MUL }
    | eof { EOF }
    | _ { SEP }
