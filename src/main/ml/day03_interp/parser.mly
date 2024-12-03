%{
open Ast
%}

%token <int> INT
%token LPAREN
%token RPAREN
%token COMMA
%token MUL
%token DO
%token DONT
%token EOF
%token SEP

%start <Ast.expr> prog

%%

prog:
| e=expr; EOF { e }
| EOF { Emp }
;

expr:
| MUL; LPAREN; x=INT; COMMA; y=INT; RPAREN { Mul (x, y) }
| DO; LPAREN; RPAREN; e=expr { Valid (e) }
| DONT; LPAREN; RPAREN; e=expr { Invalid (e) }
| e1=expr; e2=expr { Seq (e1, e2) }
(* TODO is there a better way to do this? *)
| SEP { Emp }
| INT { Emp }
| LPAREN { Emp }
| RPAREN { Emp }
| COMMA { Emp }
| MUL { Emp }
      | MUL; LPAREN { Emp }
      | MUL; LPAREN; INT { Emp }
      | MUL; LPAREN; INT; COMMA { Emp }
      | MUL; LPAREN; INT; COMMA; INT { Emp }
| DO { Emp }
      | DO; LPAREN { Emp }
      | DO; LPAREN; RPAREN { Emp }
| DONT { Emp }
      | DONT; LPAREN { Emp }
      | DONT; LPAREN; RPAREN { Emp }
;
