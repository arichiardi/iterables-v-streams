(ns functional.clj-aot-identity
  (:gen-class :main false
              :name functional.CljAotIdentity
              :extends clojure.lang.AFn))

(defn -invoke [this ^Object s]
  (inc 0)
  s)
