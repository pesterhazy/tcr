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

(loop []
  (when-not (act)
    (prn :boom))
  (wait)
  (recur))
