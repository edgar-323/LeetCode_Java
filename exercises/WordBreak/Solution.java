class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        return Solution1.wordBreak(s, wordDict);
    }

    private static class Solution1 {
        // We want to "match" all of s[0:(N-1)].
        // We can pick from wordDict with replacement.
        // At any given time, we pick a word from wordDict.
        // If the current word is a prefix of s[i:(N-1)],
        // then we have a possible match and we now check
        // s[(i + word.length):(N-1)].
        // If we reach the end of the string then we are done.

        String s;
        List<String> wordDict;
        int N;
        Boolean[] cache;

        /**
         * TimeComplexity:  O((N/K) * W * K) ==> O(N * W)
         * SpaceComplexity: O(N)
         *
         * Where:
         *      N = s.length()
         *      W = wordDict.size()
         *      K = sum{ word.length() for word in wordDict } / wordDict.size()
         */
        static boolean wordBreak(String s, List<String> wordDict) {
            Solution1 solution = new Solution1(s, wordDict);

            return solution.canBeSegmented(/* index= */ 0);
        }

        boolean canBeSegmented(int index) {
            if (inCache(index)) {
                return getCacheValue(index);
            }
            if (index == N) {
                return setCacheValue(N, true);
            }
            for (String word : wordDict) {
                if (isPrefix(index, word) && canBeSegmented(index + word.length())) {
                    return setCacheValue(index, true);
                }
            }

            return setCacheValue(index, false);
        }

        boolean isPrefix(int index, String word) {
            if (N - index < word.length()) {
                // Not enough characters to compare.
                return false;
            }

            for (int i = 0; i < word.length(); i++) {
                if (s.charAt(index + i) != word.charAt(i)) {
                    return false;
                }
            }

            return true;
        }

        boolean setCacheValue(int index, boolean value) {
            cache[index] = value;
            return value;
        }

        boolean getCacheValue(int index) {
            return cache[index];
        }

        boolean inCache(int index) {
            return cache[index] != null;
        }

        Solution1(String s, List<String> wordDict) {
            this.s = s;
            this.wordDict = wordDict;
            this.N = s.length();
            this.cache = new Boolean[N+1];
            for (int i = 0; i <= N; i++) {
                this.cache[i] = null;
            }
        }
    }
}
