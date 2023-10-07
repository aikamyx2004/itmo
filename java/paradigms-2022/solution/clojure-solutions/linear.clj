;(defn size-equals? [args]
;  (every? (fn [x] (== (count x) (count (first args)))) args)
;  )
;
;(defn v? [a]
;  (and (vector? a) (every? number? a)))
;
;(defn v-operation [op]
;  (fn [& args]
;    {:pre  [(and (every? v? args) (size-equals? args))]
;     :post [(fn [x] (== (count x) (count (first args))))]
;     }
;    (apply mapv op args))
;  )
;
;(def v+ (v-operation +))
;(def v- (v-operation -))
;(def v* (v-operation *))
;(def vd (v-operation /))
;
;(defn scalar [& args]
;  {
;   :pre  [(every? v? args)]
;   :post [(fn [x] number? x)]
;   }
;  (reduce + (apply v* args)))
;(defn vect [& args]
;  {
;   :pre  [(and (every? v? args) (== 3 (count (first args))) (size-equals? args))]
;   :post [(fn [x] (== 3 (count x)))]
;   }
;  (reduce
;    (fn [a b]
;      (vector (- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
;              (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
;              (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))
;              )
;      ) args
;    )
;  )
;;  x  y  z
;; a0 a1 a2
;; b0 b1 b2
;(defn v*s [a & args]
;  {
;   :pre  [(and (v? a) (every? number? args))]
;   :post [(fn [x] (and (v? x) (== (count a) (count x))))]
;   }
;  (let [p (apply * args)] (mapv #(* % p) a))
;
;  )
;(defn m? [a]
;  (and (vector? a) (every? v? a))
;  )
;(defn m*s [a & args]
;  {
;   :pre  [(and (m? a) (every? number? args))]
;   :post [(fn [x] (and (m? x) (size-equals? (vector x a))))]
;   }
;  (let [p (apply * args)]
;    (mapv #(v*s % p) a)))
;(defn transpose [a] (apply mapv vector a))
;(defn m*v [a b] (mapv (fn [x] (scalar x b)) a))
;
;
;
;
;
;(defn m-operation [v-op]
;  (fn [& args]
;    {:pre [(and (every? m? args) (size-equals? args))]}
;    (apply mapv v-op args))
;  )
;(def m+ (m-operation v+))
;(def m- (m-operation v-))
;(def m* (m-operation v*))
;(def md (m-operation vd))
;
;(defn m*m [& args]
;  {
;   :pre [(every? m? args)]
;   }
;  (letfn [(multiply-2 [a b]
;            {:pre  [(== (count (first a)) (count b))]
;             :post [(fn [x]
;                      (and
;                        (== (count x) (count (a))))
;                      (== (count (first x)) (count (first b))))]
;
;             }
;            (mapv (fn [x] (m*v (transpose b) x)) a))]
;
;    (reduce multiply-2 args))
;  )
;
;;(def a (vector (vector 1)))
;;(def b (vector (vector 1)))
;;(println (multiply-2 a b))