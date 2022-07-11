class Solution {
    public int maxProduct(int[] nums) {
        return Solution2.maxProduct(nums);
    }

    static private class Solution1 {
        // Naive Implementation.
        // Compute subproduct nums[i:j] for all (i,j) (inclusive).
        // For some i, you can keep a "running product" because:
        //     subProd[i:j] = subProd[i:(j-1)] * nums[j];

        /**
         * TimeComplexity:  O(N^2)
         * SpaceComplexity: O(1)
         *
         * Where:
         *      N = nums.length
         */
        static int maxProduct(int[] nums) {
            int N = nums.length;

            int maxProd = Integer.MIN_VALUE;
            for (int i = 0; i < N; i++) {
                int currProd = nums[i];
                maxProd = Math.max(maxProd, currProd);
                for (int j = i+1; j < N; j++) {
                    currProd *= nums[j];
                    maxProd = Math.max(maxProd, currProd);
                }
            }

            return maxProd;
        }
    }

    static private class Solution2 {
        // NOTE: THIS APPROACH WAS ACQUIRED FROM THE LEETCODE PUBLISHED OPTIMAL SOLUTION.
        // See: https://leetcode.com/problems/maximum-product-subarray/solution/

        /**
         * TimeComplexity:  O(N)
         * SpaceComplexity: O(1)
         *
         * Where:
         *      N = nums.length
         */
        static int maxProduct(int[] nums) {
            int maxProd = nums[0];

            int maxSoFar = nums[0];
            int minSoFar = nums[0];

            for (int i = 1; i < nums.length; i++) {
                int tempMax =
                    max(nums[i], maxSoFar * nums[i], minSoFar * nums[i]);
                minSoFar =
                    min(nums[i], maxSoFar * nums[i], minSoFar * nums[i]);
                maxSoFar = tempMax;

                maxProd = Math.max(maxProd, maxSoFar);
            }

            return maxProd;
        }

        private static int max(int a, int b, int c) {
            return Math.max(a, Math.max(b, c));
        }

        private static int min(int a, int b, int c) {
            return Math.min(a, Math.min(b, c));
        }
    }
}
