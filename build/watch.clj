(require '[babashka.process :refer [process check]])

(defn shell [v]
  (assert (vector? v))
  (-> (process v {:inherit true})
      check))

(def !watch-process (atom nil))

(defn wait []
  (when-not @!watch-process
    (reset! !watch-process (process ["watchexec" "-p" "-w" "tcr.mjs" "echo" "."])))

  (binding [*in* (clojure.java.io/reader (:out @!watch-process))]
    (read-line)))

(defn act []
  (println "Running...")
  (let [proc @(process ["node" "tcr.mjs"] {:inherit true})]
    (= 0 (:exit proc))))

(defn revert []
  (shell ["git" "reset" "--hard"]))

(defn commit []
  (shell ["git" "add" "tcr.mjs"])
  @(process ["git" "commit" "-m" "autocommit"] {:inherit true}))

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
