class Solution {
    public int minDistance(String word1, String word2) {
        return Solution1.minDistance(word1, word2);
    }

    /**
     * TimeComplexity:  O(M*N)
     * SpaceComplexity: O(M*N)
     */
    private static class Solution1 {
        private static int INSERT_COST = 1;
        private static int DELETE_COST = 1;
        private static int REPLACE_COST = 1;

        private final int M,N;
        private final String word1, word2;
        private final Integer[][] cache;
        
        // Thoughts:
        // Let's work with word1[i:] and word2[j:].
        // Define:
        //  cost[i,j] = cost to transform word1[i:] into word2[j:].
        // Well first thing we want to do is the character transformation:
        //  word1[i] --> word2[j]
        // We have the following options:
        //  1. Insert word2[j] BEFORE word1[i]
        //     In this case, we now have to still account for word1[i:] and word2[(j+1):].
        //  2. Delete word1[i].
        //     Hmm, we no have to account for word1[(i+1):] and word2[j:]
        //  3. Replace word1[i] with word2[j].
        //     We have to account for word1[(i+1):] and word2[(j+1):]
        // In the three above cases we must add 1 to each case since each operation has
        // unit cost.
        // There is one more case that is implicit:
        //  4. If word1[i] == word2[j]
        //     We have to account for word1[(i+1):] and word2[(j+1):]
        //     This has a zero cost because we didn't have to do any operations!
        //
        // More additional corner cases:
        //  5. If i == word1.length() and j == word2.length()
        //     Then cost is 0 (no additonal transformations needed)
        //  6. If i == word1.length() and j < word2.length()
        //     We only have the option to INSERT word2[j] and then have to account for
        //     word1[i:] AND word2[(j+1):]
        //  7. If i < word1.length() and j == word2.length()
        //     We only have option to DELETE word1[i] and then have to account for
        //     word1[(i+1):] and word2[j:]

        static int minDistance(String word1, String word2) {
            return newSolver(word1, word2).solve();
        }

        private int solve() {
            return solve(0, 0);
        }

        private int solve(int i, int j) {
            if (cacheHit(i, j)) {
                return getCache(i, j);
            }

            int minCost;
            if (i == M && j == N) {
                minCost = 0;
            } else if (i == M) {
                minCost = solve(i, j+1) + INSERT_COST;
            } else if (j == N) {
                minCost = solve(i+1, j) + DELETE_COST;
            } else {
                minCost = min(
                        solve(i, j+1)   + INSERT_COST,
                        solve(i+1, j)   + DELETE_COST,
                        solve(i+1, j+1) + replaceCost(i, j));
            }

            return setCache(i, j, minCost);
        }

        private static Solution1 newSolver(String word1, String word2) {
            return new Solution1(word1, word2);
        }

        private boolean cacheHit(int i, int j) {
            return cache[i][j] != null;
        }

        private int getCache(int i, int j) {
            return cache[i][j];
        }

        private int setCache(int i, int j, int v) {
            cache[i][j] = v;
            return v;
        }

        private int replaceCost(int i, int j) {
            return word1.charAt(i) == word2.charAt(j) ? 0 : REPLACE_COST;
        }

        private static int min(int a, int b, int c) {
            return Math.min(a, Math.min(b, c));
        }

        private Solution1(String word1, String word2) {
            this.M = word1.length();
            this.N = word2.length();
            this.word1 = word1;
            this.word2 = word2;

            cache = new Integer[M+1][N+1];
            for (int i = 0; i <= M; i++) {
                for (int j = 0; j <= N; j++) {
                    cache[i][j] = null;
                }
            }
        }
    }
}
