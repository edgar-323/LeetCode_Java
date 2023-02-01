class Solution {
    public int waysToPartition(int[] nums, int k) {
        return Solution1.newSolver(nums, k).solve();
    }

    /**
     * REFLECTIONS & THOUGHTS:
     * Firstly, the following solution is not my own.
     *
     * I was able to produce the following scheme:
     *  --> Iterate over nums[] while updating prefixSum and suffixSum.
     *  --> Increment the answer every time you encounter the condition:
     *  -->     `prefixSum == suffixSum` (equivalently: `prefixSum - suffixSum == 0`)
     *  --> Run the above scheme N+1 times.
     *  --> Once for the case where you don't replace the any elements
     *  --> in nums[] and another N times for the time where you replace
     *  --> nums[i] with k.
     *  --> This has a runtime of O(N^2) which is not great ...
     *
     * Improvements on the above ideas:
     * Now, only the last hint was helpful:
     *      When sweeping through each element, can you find the total
     *      number of pivots where the difference of prefix and suffix
     *      happens to equal to the changes of k-nums[i].
     *
     * In other words, the key idea is that when you DO NOT replace any element,
     * you increment the answer every time you encounter:
     *      `prefixSum - suffixSum == 0`.
     * However, if decided to replace nums[i] with k, then instead of actually
     * replacing nums[i] and recomputing prefixSum (or suffixSum) you'd instead
     * notice that you would now be interested on points where:
     *      `prefixSum - suffixSum == k - nums[i]`
     * I.e., points where the different between prefixSum and suffixSum is equal
     * to the delta that occurs from replacing nums[i] with k. These happen to be
     * points where the different between prefixSum and suffixSum would actually
     * be zero if you did do the replacement and recomputed prefixSum and suffixSum.
     * You'd want to find these points to the left of nums[i] and to the right of
     * nums[i], however, in the right side, you'd want the negative of this delta.
     * This indicates the we want to keep track off diffs to the left of the current
     * index i and to the right of the current index i.
     *
     * Implementation:
     *  Create two counters to keep track of diffs (one for the left side of the
     *  current index and another to track the right side).
     *  First pass of nums[]:
     *      All diffs will be recorded only for the right-side counter.
     *  The most basic answer will be the number of counts for the diff resulting in 0.
     *  So we set our result to this (initially).
     *      result = right-counter[0]
     *  Second pass of nums[]:
     *      diff = prefixSum - suffixSum
     *      delta = k - nums[i]
     *      numPivotsAtCurrIndexWithReplacement = left-counter[delta] + right-counter[-delta]
     *      result = max(result, numPivotsAtCurrIndexWithReplacement)
     *      // At the next iteration, the current index will be to the left of us, so
     *      // we need to transfer it's diff count over to the left-counter.
     *      left-counter[diff]++
     *      right-counter[diff]--
     *  return result
     */


    static class Solution1 {
        private final int[] nums;
        private final int k;
        private final int N;
        private final long totalSum;

        int solve() {
            Map<Long, Integer> leftDiffs = new HashMap<>();
            Map<Long, Integer> rightDiffs = new HashMap<>();

            for (int i = 0, prefixSum = 0; i < N-1; i++) {
                prefixSum += nums[i];
                long suffixSum = totalSum - prefixSum;
                long diff = prefixSum - suffixSum;
                rightDiffs.put(diff, rightDiffs.getOrDefault(diff, /* default= */ 0) + 1);
            }

            int result = rightDiffs.getOrDefault(/* diff= */ 0L, /* default= */ 0);

            for (int i = 0, prefixSum = 0; i < N; i++) {
                prefixSum += nums[i];
                long suffixSum = totalSum - prefixSum;
                long diff = prefixSum - suffixSum;
                long delta = k - nums[i];

                result = Math.max(
                        result,
                        leftDiffs.getOrDefault(delta, 0) + rightDiffs.getOrDefault(-delta, 0));

                leftDiffs.put(diff, leftDiffs.getOrDefault(diff, /* default= */ 0) + 1);
                rightDiffs.put(diff, rightDiffs.getOrDefault(diff, /* default= */ 0) - 1);
            }

            return result;
        }

        static Solution1 newSolver(int[] nums, int k) {
            return new Solution1(nums, k);
        }

        Solution1(int[] nums, int k) {
            this.nums = nums;
            this.N = nums.length;
            this.k = k;
            this.totalSum = Arrays.stream(nums).sum();
        }
    }
}
