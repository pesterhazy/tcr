(require '[babashka.process :refer [process shell]])

(def !watch-process (atom nil))

(defn wait []
  (when-not @!watch-process
    (reset! !watch-process (process ["watchexec" "-p" "-w" "tcr.mjs" "echo" "."])))

  (binding [*in* (clojure.java.io/reader (:out @!watch-process))]
    (read-line)))

(defn act []
  (println "Running...")
  (try
    (shell "node tcr.mjs")
    (catch clojure.lang.ExceptionInfo e
      (when-not (:exit (ex-data e))
        (throw e))
      false)
    true))

(defn revert []
  (shell "git reset --hard"))

(defn commit []
  (shell "git add tcr.mjs")
  (try
    (shell "git commit -m autocommit")
    (catch clojure.lang.ExceptionInfo e
      (when-not (:exit (ex-data e))
        (throw e)))))

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
