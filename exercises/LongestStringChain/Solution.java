class Solution {
    private final static int MAX_WORD_LEN = 16;

    public int longestStrChain(String[] words) {
        return Solution1.newSolver(words).solve();
    }

    static class Solution1 {
        private final List<String>[] lengthToWords;
        private final Map<String, Integer> cache;

        static Solution1 newSolver(String[] words) {
            return new Solution1(words);
        }

        int solve() {
            int maxChain = 0;
            for (int len = 1; len <= MAX_WORD_LEN; len++) {
                for (String word : lengthToWords[len]) {
                    maxChain = Math.max(maxChain, solve(word));
                }
            }

            return maxChain;
        }

        /**
         * TimeComplexity:  O(W * L)
         * SpaceComplexity: O(1)
         *
         * Where:
         *      W = word.length() // This is capped by MAX_WORD_LEN.
         *      L = # of words that have length W+1.
         */
        int solve(String word) {
            if (cache.containsKey(word)) {
                return cache.get(word);
            }

            int maxChain = 1;
            for (String possiblePredecessor : lengthToWords[word.length() + 1]) {
                for (int charToDelete = 0; charToDelete < possiblePredecessor.length(); charToDelete++) {
                    if (isPredecessor(word, possiblePredecessor, charToDelete)) {
                        maxChain = Math.max(maxChain, 1 + solve(possiblePredecessor));
                    }
                }
            }

            cache.put(word, maxChain);
            return maxChain;
        }

        /**
         * TimeComplexity:  O(W)
         * SpaceComplexity: O(1)
         *
         * Where:
         *      W = word1.length()
         */
        boolean isPredecessor(String word1, String word2, int charToDelete) {
            for (int i = 0; i < word1.length(); i++) {
                char c1 = word1.charAt(i);
                char c2 = word2.charAt(i + (i < charToDelete ? 0 : 1));
                if (c1 != c2) {
                    return false;
                }
            }

            return true;
        }

        /**
         * TimeComplexity:  O(MAX_WORD_LEN + N)
         * SpaceComplexity: O(MAX_WORD_LEN + N)
         *
         * Where:
         *      N = words.length
         */
        Solution1(String[] words) {
            lengthToWords = new List[MAX_WORD_LEN+2];
            for (int i = 0; i <= MAX_WORD_LEN+1; i++) {
                lengthToWords[i] = new ArrayList<>();
            }
            for (String word : words) {
                lengthToWords[word.length()].add(word);
            }
            cache = new HashMap<>();
        }
    }
}
