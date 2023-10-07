; common code hw 10 and 11
(defn divide'
  ([a] (/ 1.0 a))
  ([a & b] (/ (double a) (apply * b)))
  )

(defn parser [expr operation-map constant-ctor variable-ctor]
  (letfn [(parse [expr]
            (cond
              (list? expr) (apply (operation-map (first expr)) (map parse (rest expr)))
              (number? expr) (constant-ctor expr)
              (symbol? expr) (variable-ctor (name expr)))
            )]
    (parse expr)))

(defn mean' [& args] (divide' (apply + args) (count args)))

(defn varn' [& args]
  (let [mean-value (apply mean' args)
        square #(* % %)
        squared-mean (square mean-value)
        mean-of-squares (apply mean' (map square args))]
    (- mean-of-squares squared-mean)))

;--------------------HW-10------------------------

(defn constant [value] (constantly value))
(defn variable [x] #(get % x))
(defn negate [value] #(- (value %)))
(defn fold-operation [operation]
  (fn [& args]
    (fn [vars] (apply operation (map #(% vars) args)))
    ))

(def add (fold-operation +))
(def subtract (fold-operation -))
(def multiply (fold-operation *))
(def divide (fold-operation divide'))
(def mean (fold-operation mean'))
(def varn (fold-operation varn'))

(def function-operations {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'mean mean, 'varn varn})

(defn parseFunction [function] (parser (read-string function) function-operations constant variable))

;--------------------HW-11------------------------

(definterface Expression
  (evaluate [vars])
  (diff [v])
  (toStringSuffix []))

(defn evaluate [this vars] (.evaluate this vars))
(defn toString [this] (.toString this))
(defn diff [this v] (.diff this v))
(defn toStringSuffix [this] (.toStringSuffix this))

(declare ZERO)

(deftype JConstant [value]
  Expression
  (evaluate [this vars] value)
  (diff [this v] ZERO)
  (toStringSuffix [this] (str value))
  Object
  (toString [this] (str value)))

(defn Constant [value] (JConstant. value))

(def ZERO (Constant 0.0))
(def ONE (Constant 1.0))
(def TWO (Constant 2.0))

(deftype JVariable [value]
  Expression
  (evaluate [this vars] (get vars value))
  (diff [this v] (if (= value v) ONE ZERO))
  (toStringSuffix [this] value)
  Object
  (toString [this] value))
(defn Variable [value] (JVariable. value))

(deftype Operation [operation-on-list operator diff-operation args]
  Expression
  (evaluate [this vars] (apply operation-on-list (map #(evaluate % vars) args)))
  (diff [this v] (diff-operation args (mapv #(diff % v) args)))
  (toStringSuffix [this] (str "(" (clojure.string/join " " (mapv #(toStringSuffix %) args)) " " operator ")"))
  Object
  (toString [this] (str "(" operator " " (clojure.string/join " " (mapv #(toString %) args)) ")"))
  )
(defn Add [& args]
  (Operation.
    + "+"
    (fn [args d-args] (apply Add d-args))
    args
    )
  )

(defn Subtract [& args]
  (Operation.
    - "-"
    (fn [args d-args] (apply Subtract d-args))
    args
    )
  )

(declare multiply-diff Divide)
(defn Multiply [& args]
  (Operation.
    * "*"
    multiply-diff
    args
    ))

(defn multiply-diff [args d-args]
  (let [sum (apply Multiply args)]
    (apply
      Add (map #(Multiply %2 (Divide sum %1)) args d-args)
      )
    )
  )

(defn Negate [& arg]
  (Operation.
    - "negate"
    (fn [x dx] (apply Negate dx))
    arg
    )
  )

(defn Divide [& args]
  (Operation.
    divide' "/"
    (fn [args d-args]
      (if (== (count args) 1)
        (Negate (Divide (first d-args) (first args) (first args)))
        (let [denominator (apply Multiply (rest args))
              d-first (first d-args)]
          (Divide
            (Subtract
              (Multiply TWO d-first denominator)
              (multiply-diff args d-args)
              )
            (Multiply denominator denominator)
            )
          )))
    args
    ))

(defn mean-diff [args d-args]
  (Divide
    (apply Add d-args)
    (Constant (count args))
    )
  )

(defn Mean [& args]
  (Operation.
    mean' "mean"
    mean-diff
    args)
  )

(defn Varn [& args]
  (Operation.
    varn' "varn"
    (fn [args d-args]
      (Subtract
        (apply Mean (map #(Multiply TWO %1 %2) args d-args))
        (Multiply TWO (apply Mean args) (apply Mean d-args))
        )
      )
    args
    ))

(def object-operation {'+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate, 'mean Mean, 'varn Varn})

(defn parseObject [function] (parser (read-string function) object-operation Constant Variable))

;--------------------HW-12------------------------

(load-file "parser.clj")
(def parser-operation object-operation)
(println (-value {:value "a" :tail "b"}))

(def *all-chars (mapv char (range 0 128)))
(defn *chars-filter [p] (apply str (filter p *all-chars)))
(defn *chars [p] (+char (*chars-filter p)))
(def *letter (*chars #(Character/isLetter %)))
(def *digit (*chars #(Character/isDigit %)))
(def *space-chars (*chars-filter #(Character/isWhitespace %)))
(def *space (+char *space-chars))
(def *ws (+ignore (+star *space)))
(def *number (+map read-string (+str (+plus *digit))))
(def *const-from-arg #(+map (comp Constant read-string) %))
(def *constant (+map (comp Constant read-string) (+str (+seq (+opt (+char "-")) *number (+char ".") *number))))
(def *variable (+map Variable (+str (+plus (+char "xyzXYZ")))))
(def *argument (+str (+plus (+char-not (str "()" *space-chars)))))
(def *function (+map (comp object-operation symbol) *argument))
(defparser parseObjectSuffix
           declare *value
           *operation (+map (fn [[op & p]]
                              (apply op p))
                            (+seqf conj
                                   (+ignore (+char "("))
                                   (+plus (+seqn 0 *ws *value))
                                   *ws
                                   *function
                                   *ws
                                   (+ignore (+char ")"))
                                   )
                            )
           *value (delay (+or *constant *variable *operation))
           *expr (+seqn 0 *ws *value *ws)
           )
(defparser ParseObjectInfix
           declare *value
           (*op [& op] (fn [& p] (let [or-op (apply +or #(apply +seqf % p) mapv)])))
           *unary (*operation Negate)
           *binary  (*operation Add Subtract Multiply Divide Mean Varn)

           )

(defn -show [result]
  (if (-valid? result)
    (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
    "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (-show (parser input)))) inputs))

(println (toStringSuffix (Divide (Negate (Variable "x")) (Constant 2.0))))
(tabulate parseObjectSuffix ["(1.0 x +)", "(1.0 2.0 +)", " negate"])
;(tabulate parseSuffix ["ab", "a", "ac", "1.5", "-5.6"])
;(println (parseSuffix "1.2"))
;(println (parseSuffix "ab"))
(println (parseObjectSuffix " ( x 2.0 + )   "))
;(println (parseSuffix "-"))
;(println (parseSuffix "negate"))
;(println (parseSuffix "n"))
;(println (type \+))


;
;(defparser json
;           *null (+seqf (constantly 'null) \n \u \l \l)
;           *all-chars (mapv char (range 0 128))
;           (*chars [p] (+char (apply str (filter p *all-chars))))
;           *letter (*chars #(Character/isLetter %))
;           *digit (*chars #(Character/isDigit %))
;           *space (*chars #(Character/isWhitespace %))
;           *ws (+ignore (+star *space))
;           *number (+map read-string (+str (+plus *digit)))
;           *identifier (+str (+seqf cons *letter (+star (+or *letter *digit))))
;           *string (+seqn 1 \" (+str (+star (+char-not "\""))) \")
;           (*seq [begin p end]
;                 (+seqn 1 begin (+opt (+seqf cons *ws p (+star (+seqn 1 *ws \, *ws p)))) *ws end))
;           *array (+map vec (*seq \[ (delay *value) \]))
;           *member (+seq *identifier *ws (+ignore \:) *ws (delay *value))
;           *object (+map (partial reduce #(apply assoc %1 %2) {}) (*seq \{ *member \}))
;           *value (+or *null *number *string *object *array)
;           *json (+seqn 0 *ws *value *ws))
;(tabulate json ["[1, {a: \"hello\", b: [1, 2, 3]}, null]"])
;(println (json "[1, {a: \"hello\", b: [1, 2, 3]}, null]"))
;(println (parseSuffix "x"))