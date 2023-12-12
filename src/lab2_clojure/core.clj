(ns lab2-clojure.core)
(ns lab2-clojure.core
  (:require [clojure.core.async :as a]))

(defn getChannels [origChannel number length]
  (let [chan1 (a/chan length) chan2 (a/chan length)]
    (doseq [x (range 0 length)]
      (let [value (a/<!! origChannel)]
        (println (str "putting val = " value))
        (println (str "mod = " (mod value number)))
        (if (even? (mod value number))
          (a/>!! chan1 value)
          (a/>!! chan2 value)
          )
        (println "pass")
        )
      )
    (vector chan1 chan2))
  )

(def chanLength 10)
(def divideNum 3 )
(def maxNum 10000)
(def myChannel (a/chan chanLength))

(doseq [x (range 0 chanLength)]
  (let [value (rand-int maxNum)]
    (println "put" value)
    (a/>!! myChannel value)
    )
  )

(println "yes")

(def newChannels (getChannels myChannel divideNum chanLength))

(println "yes")

(println (count newChannels))
(println (get newChannels 0))

;; Тут надежда на то, что в обоих есть число, иначе он блокает.
;; Можно сделать a/go, но он тогда просто проходит в конец проги, не успевая ничего вывести
(println (str "chan1 value = " (a/<!! (get newChannels 0))))
(println (str "chan2 value = " (a/<!! (get newChannels 1))))

(a/close! (get newChannels 0))
(a/close! (get newChannels 1))
(a/close! myChannel)
