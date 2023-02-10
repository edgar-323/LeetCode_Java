class Solution {
    public List<String> wordBreak(String s, List<String> wordDict) {
        return Solution1.newSolver(s, wordDict).solve();
    }

    static class Solution1 {
        private final List<String> wordDict;
        private final String s;

        // The following queue will help us store the list of indices that may
        // form a sentence (we convert this queue to a sentence only if we reached
        // the end of `s`).
        private final LinkedList<Integer> indexQueue;
        // This will be our final list of sentences (i.e., the result).
        private final List<String> sentences;
        // Maps from word-length to map of words to lengthToWordIndices.
        private final Map<Integer, Map<String, Integer>> lengthToWordIndices;
        // We will use the max word length in our dictionary in order to determine
        // the length of the substrings that we should consider.
        private int maxWordLen;

        List<String> solve() {
            // Generate all possible sentences.
            solve(/* i = */ 0);

            return sentences;
        }

        void solve(int i) {
            if (i == s.length()) {
                // We were able to complete a sentence.
                sentences.add(queueToSentence());
                return;
            }

            for (int wordLen = 1; wordLen <= maxWordLen && i + wordLen <= s.length(); wordLen++) {
                if (!lengthToWordIndices.containsKey(wordLen)) {
                    // We don't have any words of this size in our dictionary.
                    continue;
                }

                Map<String, Integer> wordToIndex = lengthToWordIndices.get(wordLen);
                String word = s.substring(i, i + wordLen);

                if (wordToIndex.containsKey(word)) {
                    // Current substring forms a valid word.
                    // Let's add it's index to the queue in an effort that it will eventually be
                    // part of a full sentence.
                    indexQueue.addLast(wordToIndex.get(word));
                    solve(i + wordLen);
                    indexQueue.removeLast();
                }
            }
        }

        String queueToSentence() {
            // Use a StringBuilder object to form our sentence since regular String objects
            // are immutable in Java and we don't want to continuously create new String objects
            // when forming the final result.
            //
            // The length of a sentence will be equal len(s) + # of spaces between each word.
            StringBuilder sentence =
                new StringBuilder(s.length() + indexQueue.size() - 1);

            // Iterate through the current index queue, get the corresponding word, add the word
            // to the sentence, and possibly add a space after the word.
            int i = 0;
            for (Integer index : indexQueue) {
                sentence.append(wordDict.get(index));
                if (i < indexQueue.size() - 1) {
                    sentence.append(' ');
                }
                i++;
            }

            return sentence.toString();
        }

        static Solution1 newSolver(String s, List<String> wordDict) {
            return new Solution1(s, wordDict);
        }

        Solution1(String s, List<String> wordDict) {
            this.s = s;
            this.wordDict = wordDict;

            lengthToWordIndices = new HashMap<>();
            indexQueue = new LinkedList<>();
            sentences = new ArrayList<>();
            maxWordLen = 0;

            populateWordIndices();
        }

        void populateWordIndices() {
            // Preprocessing step.
            for (int index = 0; index < wordDict.size(); index++) {
                String word = wordDict.get(index);
                int wordLen = word.length();
                if (!lengthToWordIndices.containsKey(wordLen)) {
                    lengthToWordIndices.put(wordLen, new HashMap<>());
                }
                lengthToWordIndices.get(wordLen).put(word, index);
                maxWordLen = Math.max(wordLen, maxWordLen);
            }
        }
    }
}
