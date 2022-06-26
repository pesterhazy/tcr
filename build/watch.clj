(require '[babashka.process :refer [process check]])

(defn sh! [v]
  (assert (vector? v))
  (-> (process v {:inherit true})
      check))

(defn sh?! [v]
  (assert (vector? v))
  (= 0 (:exit @(process v {:inherit true}))))

(def !watch-process (atom nil))

(defn wait []
  (when-not @!watch-process
    (reset! !watch-process (process ["watchexec" "-p" "-w" "tcr.mjs" "echo" "."])))

  (binding [*in* (clojure.java.io/reader (:out @!watch-process))]
    (read-line)))

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

(loop []
  (let [result (act)]
    (notify result)
    (if result
      (commit)
      (revert)))
  (wait)
  (recur))
