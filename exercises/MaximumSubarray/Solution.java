class Solution {
    public int maxSubArray(int[] nums) {
        return Solution4.maxSubArray(nums);
    }

    private static class Solution1 {
        // Let's start with a naive implementation.

        /**
         * TimeComplexity:  O(N^3)
         * SpaceComplexity: O(1)
         *
         * Where:
         *      N = nums.length
         */
        static int maxSubArray(int[] nums) {
            int maxSum = Integer.MIN_VALUE;
            for (int i = 0; i < nums.length; i++) {
                for (int j = i; j < nums.length; j++) {
                    int currSum = calculateSum(nums, i, j);
                    if (currSum > maxSum) {
                        maxSum = currSum;
                    }
                }
            }
            return maxSum;
        }

        private static int calculateSum(int[] nums, int i, int j) {
            int sum = 0;
            while (i <= j) {
                sum += nums[i];
                i++;
            }
            return sum;
        }
    }

    private static class Solution2 {
        // How do we make this into a DP problem?
        // Define:
        //      DP[i][j] := sum(nums, i, j); // i <= j
        //
        // Then, we have a couple of recursive possibilities:
        //      1)  DP[i][j] = nums[i] + DP[i+1][j];
        //      2)  DP[i][j] = DP[i][j-1] + nums[j];
        //      3)  DP[i][j] = nums[i] + DP[i+1][j-1] + nums[j];
        //
        // Trivially:
        //      DP[i][i] = nums[i];
        //
        // With the above realizations, I believe the second approach is (most) appropriate.

        /**
         * TimeComplexity:  O(N^2)
         * SpaceComplexity: O(N^2)
         */
        static int maxSubArray(int[] nums) {
            int maxSum = Integer.MIN_VALUE;

            int N = nums.length;

            int[][] DP = new int[N][N];

            for (int i = 0; i < N; i++) {
                DP[i][i] = nums[i];
                if (DP[i][i] > maxSum) {
                    maxSum = DP[i][i];
                }
            }

            for (int len = 2; len <= N; len++) {
                for (int i = 0; i < N; i++) {
                    int j = i + len - 1;
                    if (j >= N) {
                        break;
                    }
                    DP[i][j] = DP[i][j-1] + nums[j];
                    if (DP[i][j] > maxSum) {
                        maxSum = DP[i][j];
                    }
                }
            }

            return maxSum;
        }
    }

    private static class Solution3 {
        // In this solution, we take notice it is possible to always know
        // the the sum of the current subarray by keeping a running sum.
        // This is because we see every single subarray (via the nested
        // for-loops) and so we should take advantage of creating a running
        // sum when computing a new subarray.

        /**
         * TimeComplexity:  O(N^2)
         * SpaceComplexity: O(1)
         */
        static int maxSubArray(int[] nums) {
            int maxSum = Integer.MIN_VALUE;

            for (int i = 0; i < nums.length; i++) {
                int currSubarraySum = 0;
                for (int j = i; j < nums.length; j++) {
                    currSubarraySum += nums[j];
                    maxSum = Math.max(maxSum, currSubarraySum);
                }
            }

            return maxSum;
        }
    }

    private static class Solution4 {
        // Kandane's Algorithm.

        /**
         * TimeComplexity:  O(N)
         * SpaceComplexity: O(1)
         */
        static int maxSubArray(int[] nums) {
            int globalMax = nums[0];
            int localMax = nums[0];

            for (int i = 1; i < nums.length; i++) {
                // If current_subarray is negative, throw it away. Otherwise, keep adding to it.
                localMax = Math.max(nums[i], localMax + nums[i]);
                globalMax = Math.max(globalMax, localMax);
            }

            return globalMax;
        }
    }
}
