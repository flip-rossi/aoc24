(lang dune 3.16)

(name aoc24)

(generate_opam_files true)

(package
 (name aoc24)
 (depends ocaml dune core menhir ocamllex re2 ppx_hash))

(using menhir 3.0)

; See the complete stanza docs at https://dune.readthedocs.io/en/stable/reference/dune-project/index.html
