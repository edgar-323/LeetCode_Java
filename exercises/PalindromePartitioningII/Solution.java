class Solution {
    public int minCut(String s) {
        return Solution1.newSolver(s).solve();
    }

    /**
     * TimeComplexity:  O(N^2)
     * SpaceComplexity: O(N^2)
     */
    static class Solution1 {

        // Intuition:
        // Focus on s[i:], and let:
        //      minCuts[i] = min cuts needed to partition s[i:].
        // We want minCuts[0].
        // Then, we could search for all palindromes s[i:j] (i <= j),
        // and decide to cut after position j.
        // The resulting cost would be: 1 + minCuts[j+1].
        // We need to minimize over all possible values of j (these
        // are dictated by all palindromes s[i:j]).

        private final int N;
        private final boolean[][] isPalindrome;
        private final Map<Integer, Integer> cache;

        static Solution1 newSolver(String s) {
            return new Solution1(s);
        }

        int solve() {
            return solve(0);
        }

        int solve(int i) {
            if (cache.containsKey(i)) {
                return cache.get(i);
            }

            int minCuts = Integer.MAX_VALUE;
            for (int j = i; j < N; j++) {
                if (isPalindrome[i][j]) {
                    minCuts = Math.min(minCuts, cutCost(j+1) + solve(j+1));
                }
            }

            cache.put(i, minCuts);
            return minCuts;
        }

        int cutCost(int k) {
            return k == N ? 0 : 1;
        }

        Solution1(String s) {
            N = s.length();

            cache = new HashMap<>() {{
                put(N, 0);
            }};

            isPalindrome = precomputeIsPalindrome();
        }

        boolean[][] precomputeIsPalindrome() {
            boolean[][] isPalindromeArray = new boolean[][];
            for (int i = 0; i < N; i++) {
                isPalindromeArray[i][i] = true;
                if (i < N-1) {
                    isPalindromeArray[i][i+1] = (s.charAt(i) == s.charAt(i+1));
                }
            }
            for (int subarraySz = 3; subarraySz <= N; subarraySz++) {
                for (int i = 0; (i + subarraySz) <= N; i++) {
                    int j = i + subarraySz - 1;
                    isPalindromeArray[i][j] =
                        isPalindromeArray[i+1][j-1] && (s.charAt(i) == s.charAt(j));
                }
            }

            return isPalindromeArray;
        }
    }
}
