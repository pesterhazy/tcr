(require '[babashka.process :refer [process check]])

(def !watch-process (atom nil))

(defn wait []
  (when-not @!watch-process
    (reset! !watch-process (process ["watchexec" "-p" "echo" "."])))

  (binding [*in* (clojure.java.io/reader (:out @!watch-process))]
    (read-line)))

(defn act []
  (prn :act)
  )

(loop []
  (act)
  (wait)
  (recur))
