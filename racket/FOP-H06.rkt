(define-struct world (width height))
(define-struct point (x y))
(define-struct direction-vector (x y))
(define (is-blocked p d) true)

(define (plus p d) (make-point (+ (point-x p) (direction-vector-x d))  (+ (point-y p) (direction-vector-y d))))

(define (rotate-270 d)
  (cond
    [(equal? 'UP) 'LEFT]
    [(equal? 'LEFT) 'DOWN]
    [(equal? 'DOWN) 'RIGHT]
    [else 'UP]
    )
  )

(define (rotate-90 d)
  (cond
    [(equal? 'UP) 'RIGHT]
    [(equal? 'RIGHT) 'DOWN]
    [(equal? 'DOWN) 'LEFT]
    [else 'UP]
    )
  )

(define (next-step world p d)
  (cond
    [(is-blocked p (rotate-270 d)) (rotate-270 d)]
    [(is-blocked p d) d]
    [(is-blocked p (rotate-90 d)) (rotate-90 d)]
    [else (rotate-90 (rotate-90 d))]
    )
  )

(define (solve s e d)
  (if (equal? s e d)
      (list s)
      (cons s (solve (plus (next-step s d) s) (next-step s d)))
      )
  )

