%{
open Ast
%}

%token <int * int> MUL
%token DO
%token DONT
%token EOF
%token SEP

%start <Ast.expr> prog

%%

prog:
| e=expr; EOF { e }
;

expr:
| i=instr; e=expr { i::e }
| SEP; e=expr { e }
| { [] }
;

instr:
| m=MUL { let (x, y) = m in Mul (x, y) }
| DO { Valid }
| DONT { Invalid }
;
