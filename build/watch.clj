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
  (let [proc (process ["watchexec" "-p" "-w" "tcr.mjs" "echo" "."])
        rdr (io/reader (:out proc))
        q (java.util.concurrent.LinkedBlockingQueue.)]
    (future
      (loop []
        (.readLine rdr)
        (.put q ".")
        (recur)))
    {:process proc
     :q q}))

(defn wait [waiter]
  (.take (:q waiter)))

(defn reset-waiter [waiter]
  (.clear (:q waiter)))

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
          (do
            (revert)
            (Thread/sleep 600)
            (reset-waiter waiter))))
      (println "Waiting...")
      (wait waiter)
      (recur))))

(main)
