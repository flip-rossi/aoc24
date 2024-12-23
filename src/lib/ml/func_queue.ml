type 'a queue = 'a list * 'a list
type 'a t = 'a queue

let enqueue ((l, r) : 'a queue) (elt : 'a) = elt :: l, r

let rec dequeue (q : 'a queue) =
  match q with
  | [], [] -> q, None
  | l, [] -> dequeue ([], List.rev l)
  | l, y :: ys -> (l, ys), Some y
;;

let append ((l, r) : 'a queue) list = l @ list, r
let of_list l : 'a queue = [], List.rev l
let empty : 'a queue = [], []
let singleton elt : 'a queue = [], [ elt ]
