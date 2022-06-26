(require '[babashka.process :refer [process check]])
(require '[clojure.java.io :as io])

(defn sh! [v]
  (assert (vector? v))
  (-> (process v {:inherit true})
      check))

(defn sh?! [v]
  (assert (vector? v))
  (= 0 (:exit @(process v {:inherit true}))))

(defn make-watcher []
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

(defn wait-watcher [waiter]
  (.take (:q waiter)))

(defn reset-watcher [waiter]
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
  (let [watcher (make-watcher)]
    (loop []
      (let [result (act)]
        (notify result)
        (if result
          (commit)
          (do
            (revert)
            (Thread/sleep 600)
            (reset-watcher watcher))))
      (println "Waiting...")
      (wait-watcher watcher)
      (recur))))

(main)
