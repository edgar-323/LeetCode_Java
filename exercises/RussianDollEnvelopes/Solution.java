class Solution {
    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;


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
            for (int j = 0; j < N; j++) {
                if (i != j && fits(i, j)) {
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
            this.envelopes = envelopes;
            N = envelopes.length;
            cache = new HashMap<>();
        }
    }

    /**
     * TimeComplexity:  O(N * log(N))
     * SpaceComplexity: O(N)
     *
     * Where:
     *      N = envelopes.length
     */
    static class Solution2 {
        // THOUGHTS:
        // Okay so this solution comes from intuition derived in the
        // LongestIncreasingSubsequence leetcode problem:
        //      https://leetcode.com/problems/longest-increasing-subsequence/
        // Ensure that you review the O(N * log(N)) solution to that problem
        // as it is NOT intuitive to derive (in my opinion).
        // 
        // Now how is this problem related to the LIS problem?
        // Well, because we want the longest sequence (of envelopes) in TWO
        // dimensions (rather than LIS' single dimension).
        // So we can sort one dimension and then we simply apply LIS algo
        // to the second dimension! Problem solved! Not quite...
        // The first dimension, while sorted, can have dulpicate values.
        // This means that we may be overcounting if we employ LIS as-is.
        // So to counter-act this, we must sort the first dimension (smallest
        // to largest) while sorting the second dimension in reverse order
        // (largest to smallest) so as to ensure that a value of the first
        // dimension never appears more than once in the subPermutation!

        private final int[][] envelopes;

        static Solution2 newSolver(int[][] envelopes) {
            return new Solution2(envelopes);
        }

        int solve() {
            int N = envelopes.length;
            List<Integer> subPermutation = new ArrayList<>(N);

            subPermutation.add(envelopes[0][HEIGHT]);
            for (int i = 1; i < N; i++) {
                int lastSubPermutationValue = subPermutation.get(subPermutation.size() - 1);
                int currEnvelopeHeight = envelopes[i][HEIGHT];
                if (lastSubPermutationValue < currEnvelopeHeight) {
                    subPermutation.add(currEnvelopeHeight);
                } else {
                    replaceFirstLargest(subPermutation, currEnvelopeHeight);
                }
            }

            return subPermutation.size();
        }

        void replaceFirstLargest(List<Integer> subPermutation, int envelopeHeight) {
            int left = 0;
            int right = subPermutation.size();

            while (left <= right) {
                int mid = (left + right) / 2; // left+(right-left)/2; for overflow concerns.
                if (subPermutation.get(mid) < envelopeHeight) {
                    left = mid;
                } else if (mid == 0 || subPermutation.get(mid - 1) < envelopeHeight) {
                    subPermutation.set(mid, envelopeHeight);
                    return;
                } else {
                    right = mid;
                }
            }
        }

        Solution2(int[][] envelopes) {
            Arrays.sort(envelopes, (e1, e2) -> {
                if (e1[WIDTH] < e2[WIDTH]) {
                    return -1;
                }
                if (e1[WIDTH] > e2[WIDTH]) {
                    return 1;
                }
                return -Integer.compare(e1[HEIGHT], e2[HEIGHT]);
            });
            this.envelopes = envelopes;
        }
    }
}
