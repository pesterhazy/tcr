(require '[babashka.process :refer [process check]])
(require '[clojure.java.io :as io])

(defn sh! [v]
  (assert (vector? v))
  (-> (process v {:inherit true})
      check))

(defn sh?! [v]
  (assert (vector? v))
  (= 0 (:exit @(process v {:inherit true}))))

(defn make-waiter []
  {:process (process ["watchexec" "-p" "-w" "tcr.mjs" "echo" "."])})

(defn wait [waiter]
  (.readLine (io/reader (:out (:process waiter)))))

(defn act []
  (println "Running...")
  (sh?! ["node" "tcr.mjs"]))

(defn revert []
  (println "Resetting...")
  (sh! ["git" "checkout" "--" "tcr.mjs"]))

(defn commit []
  (sh! ["git" "add" "tcr.mjs"])
  (sh?! ["git" "commit" "-m" "autocommit"]))

(defn notify [success?]
  (let [fname (if success?
                "build/sounds/success.wav"
                "build/sounds/fail.wav")]
    (process ["mpv" "--quiet" fname])))

(defn main []
  (let [waiter (make-waiter)]
    (loop []
      (let [result (act)]
        (notify result)
        (if result
          (commit)
          (revert)))
      (wait waiter)
      (recur))))

(main)
