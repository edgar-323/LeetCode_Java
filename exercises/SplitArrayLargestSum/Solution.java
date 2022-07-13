class Solution {
    public int splitArray(int[] nums, int m) {
        return Solution1.splitArray(nums, m);
    }

    private static class Solution1 {
        // Naive Implementation.
        // If we want m subarrays, then we have to cut nums a total
        // of (m-1) times.
        // Among these (m-1) subarrays, one will have the largest sum.
        // We want to choose the positions of these "cuts" such that
        // the aforemention sum is minimized.
        // Assume that all possible cuts can be enumerated (i.e., they're
        // all countable).
        // A given cut must between nums[0] and nums[N-1] (otherwise we can
        // have an empty subarray, which is not allowed).
        // So we will say that nums was split at index i, if there is a cut
        // between nums[i] and nums[i+1]:
        //      cut(i, nums, m) := true iff nums was split between nums[i]
        //                         and nums[i+1]; false otherwise.
        // The above should definitely have restrictions on m.
        // For example, you should have another parameter called: cutsRemaining.
        // What is the relationship between m and nums.length?
        // Definitely: m <= nums.length (otherwise, they are asking you to split
        // nums into more parts than its constituents)
        // Also: cutsRemaining <= (m-1).
        // If we are at (i, cutsRemaining), we must answer:
        // * Can we cut between nums[i] and nums[i+1]?
        //   Onlye if i < N-1 and cutsRemaining > 0;
        // * Must we cut between nums[i] and nums[i+1]?
        //   You HAVE to cut if (N - 1 - i) == cutsRemaining.
        //   Otherwise, you will end up in a state where you still have cuts
        //   remaining but nowhere to cut.
        // I think we should now phrase our state:
        //      minCutAssignment(i, cuts) := minimum maximum cuts assignment on nums[i:(N-1)].
        // At a given state, we can cut at any point between i and (N - 1 - j) where j > i.

        int N;
        int m;
        int[] nums;
        Integer[][] cache;

        /**
         * TimeComplexity:  O(N^2 * m)
         * SpaceComplexity: O(N * m)
         *
         * Where:
         *      N = nums.length
         *      m = # of subarrays
         */
        static int splitArray(int[] nums, int m) {
            Solution1 solution = new Solution1(nums, m);

            return solution.minMaxSubarraySplit(/* index= */ 0, /* cutsRemaining= */ m-1);
        }

        int minMaxSubarraySplit(int index, int cutsRemaining) {
            if (inCache(index, cutsRemaining)) {
                return getCacheValue(index, cutsRemaining);
            }
            if (index == N) {
                // MUST BE THE CASE THAT cutsRemaining == 0.
                // In production code, we would throw an exception if this
                // invariant is violated and/or increment some metric, and
                // possibly even fire an alert depending on how important
                // this logic is.
                return setCacheValue(index, cutsRemaining, 0);
            }
            if (cutsRemaining == 0) {
                // No more cuts.
                // Only choice is sum(nums[index:(N-1)]) but instead of adding
                // directly, let's call next state so that we populate the cache
                // with more data (state values).
                return setCacheValue(index, cutsRemaining, nums[index] + minMaxSubarraySplit(index + 1, cutsRemaining));
            }
            int minVal = Integer.MAX_VALUE;
            int sum = 0;
            for (int i = index; (N - 1 - i) >= cutsRemaining; i++) {
                sum += nums[i];
                int maxVal = Math.max(sum, minMaxSubarraySplit(i+1, cutsRemaining-1));
                minVal = Math.min(minVal, maxVal);
            }
            return setCacheValue(index, cutsRemaining, minVal);
        }

        boolean inCache(int index, int cutsRemaining) {
            return cache[index][cutsRemaining] != null;
        }

        int getCacheValue(int index, int cutsRemaining) {
            return cache[index][cutsRemaining];
        }

        int setCacheValue(int index, int cutsRemaining, int value) {
            cache[index][cutsRemaining] = value;
            return value;
        }

        Solution1(int[] nums, int m) {
            this.nums = nums;
            this.m = m;
            this.N = nums.length;
            this.cache = new Integer[N+1][m];
            for (int i = 0; i <= N; i++) {
                for (int j = 0; j < m; j++) {
                    cache[i][j] = null;
                }
            }
        }
    }
}
