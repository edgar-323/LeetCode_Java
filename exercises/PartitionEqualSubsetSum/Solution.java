class Solution {
    public boolean canPartition(int[] nums) {
        return Solution1.newSolver(nums).solve();
    }

    /**
     * TimeComplexity:  O(N * S)
     * SpaceComplexity: O(N * S)
     *
     * Where:
     *      N = nums.length
     *      S = sum(nums)
     */
    static class Solution1 {
        /**
         * THOUGHTS:
         * ...
         * Assume we could partition the array into 2 "equal" subsets S1 and S2.
         * Then:
         *      sum(nums) = sum(S1) + sum(S2) = S
         *      sum(S1) = sum(S2) = G
         *      ==> S = 2*G
         *      ==> G = S / 2
         *      ==> S is divisible by 2 (i.e, it's even).
         *      ==> You need to find the existence of a subset whose sum is S/2.
         * ...
         */

        private final int[] nums;
        private final Boolean[][] cache;
        private int totalSum;

        /**
         * Returns true if the sum of the all elements of nums[] is divisible by 2 and if there
         * exists a subset that sums to half the total sum of nums[].
         */
        boolean solve() {
            return totalSum%2 == 0 && solve(/* index= */ 0, /* sum= */ totalSum/2);
        }

        /**
         * Returns true if there exists a subset in nums[i:] whose sum is equal to
         * the input `sum`. Returns false otherwise.
         */
        boolean solve(int i, int sum) {
            if (i == nums.length) {
                return sum == 0;
            }

            if (cache[i][sum] != null) {
                return cache[i][sum];
            }

            boolean partitioned =
                solve(i+1, sum) || ((sum - nums[i] >= 0) ? solve(i+1, sum - nums[i]) : false);

            cache[i][sum] = partitioned;

            return partitioned;
        }

        static Solution1 newSolver(int[] nums) {
            return new Solution1(nums);
        }

        Solution1(int[] nums) {
            this.nums = nums;

            totalSum = 0;
            for (int num : nums) {
                totalSum += num;
            }

            if (totalSum%2 != 0) {
                cache = null;
                return;
            }

            cache = new Boolean[nums.length + 1][(totalSum/2) + 1];

            for (int i = 0; i <= nums.length; i++) {
                for (int j = 0; j <= totalSum/2; j++) {
                    cache[i][j] = null;
                }
            }
        }
    }
}
