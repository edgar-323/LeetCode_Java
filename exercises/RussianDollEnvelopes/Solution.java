class Solution {
    public int maxEnvelopes(int[][] envelopes) {
        return Solution2.newSolver(envelopes).solve();
    }

    /**
     * TimeComplexity:  O(N^2)
     * SpaceComplexity: O(N)
     *
     * Where:
     *      N = envelopes.length
     */
    static class Solution1 {
        // THIS SOLUTION TIMES OUT ON LARGE INPUTS.

        private static final int WIDTH = 0;
        private static final int HEIGHT = 1;

        private final int N;
        private final int[][] envelopes;
        private final Map<Integer, Integer> cache;

        static Solution1 newSolver(int[][] envelopes) {
            return new Solution1(envelopes);
        }

        int solve() {
            int envelopes = 0;
            for (int i = 0; i < N; i++) {
                envelopes = Math.max(envelopes, solve(i));
            }
            return envelopes;
        }

        int solve(int i) {
            if (cache.containsKey(i)) {
                return cache.get(i);
            }

            int envelopes = 1; // Itself.
            for (int j = i+1; j < N; j++) {
                if (fits(i, j)) {
                    envelopes = Math.max(envelopes, 1 + solve(j));
                }
            }

            cache.put(i, envelopes);
            return envelopes;
        }

        boolean fits(int i, int j) {
            return envelopes[i][HEIGHT] < envelopes[j][HEIGHT]
                && envelopes[i][WIDTH] < envelopes[j][WIDTH];
        }

        Solution1(int[][] envelopes) {
            // We actually don't need to sort here because there is no way that we can end up
            // in a cycle when invoking solve(i) because of the way fits(i, j) works.
            // In other words, I was only sorting in order to enforce an ordering among calls
            // to solve(i) but it turns out that no cycles will occur even w/o this ordering.
            Arrays.sort(envelopes, (e1, e2) -> {
                if (e1[WIDTH] < e2[WIDTH]) {
                    return -1;
                }
                if (e1[WIDTH] > e2[WIDTH]) {
                    return 1;
                }
                return Integer.compare(e1[HEIGHT], e2[HEIGHT]);
            });
            this.envelopes = envelopes;
            N = envelopes.length;
            cache = new HashMap<>();
        }
    }

    /**
     * TimeComplexity:  O(N*log(N))
     * SpaceComplexity: O(1)
     *
     * Where:
     *      N = envelopes.length
     */
    static class Solution2 {
        static int newSolver(int[][] envelopes) {
            return new Solution2(envelopes);
        }

        int solve() {
            throw new RuntimeException(
                    "TRANSLATE THIS PROBLEM INTO LONGEST INCREASING SUBSEQUENCE AND IMPLEMENT IT!");
        }

        Solution2(int[][] envelopes) {}
    }
}
