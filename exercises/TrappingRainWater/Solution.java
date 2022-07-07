/**
 * NOTE TO SELF:
 * I was unable to construct any of the solutions presented below.
 * Even my brute-force "solution" did not work out as expected (it was
 * essentially incorrect).
 * We need more practice.
 */
class Solution {
    public int trap(int[] height) {
        return Solution2.trap(height);
    }

    private static class Solution1 {
        // Brute-force solution.
        // My attempt at a brute force solution involved having two
        // index pointers (i,j) (defined via nested loops).
        // Then I would be able to compute rain water for all pairs (i,j).
        // With enough thinking, we can easily see that this will
        // result in over-counting (hence, this is an incorrect approach).
        // The correct approach is:
        //      rainWater[i] := the amount of rain water trapped at index i.
        // Analytically:
        //      rainWater[i] = min(max(height[0:i]), max(height[i:N-1])) - height[i];
        // In other words:
        //      For each element in the array, we find the maximum level of
        //      water it can trap after the rain, which is equal to the minimum
        //      of maximum height of bars on both the sides minus its own height.

        /**
         * TimeComplexity:  O(N^2)
         * SpaceComplexity: O(1)
         *
         * Where:
         *      N = height.length
         */
        static int trap(int[] height) {
            int N = height.length;

            int rainWater = 0;

            for (int i = 0; i < N; i++) {
                int leftMax = 0;
                int rightMax = 0;
                for (int j = i; j >= 0; j--) {
                    leftMax = Math.max(leftMax, height[j]);
                }
                for (int j = i; j < N; j++) {
                    rightMax = Math.max(rightMax, height[j]);
                }
                rainWater += (Math.min(leftMax, rightMax) - height[i]);
            }

            return rainWater;
        }
    }

    private static class Solution2 {
        // This solution is nearly the same as the brute-force approach,
        // except that we notice that we can precompute (leftMax[i], rightMax[i])
        // for all i in order to optimize our runtime.
        // Note that this precomputation does incur O(N) extra memory.

        /**
         * TimeComplexity:  O(N)
         * SpaceComplexity: O(N)
         *
         * Where:
         *      N = height.length
         */
        static int trap(int[] height) {
            int N = height.length;

            int[] leftMax = new int[N];
            int[] rightMax = new int[N];
            leftMax[0] = height[0];
            rightMax[N-1] = height[N-1];
            for (int i = 1; i < N; i++) {
                leftMax[i] = Math.max(leftMax[i-1], height[i]);
                rightMax[N-1-i] = Math.max(rightMax[N-i], height[N-1-i]);
            }

            int rainWater = 0;
            for (int i = 0; i < N; i++) {
                rainWater += (Math.min(leftMax[i], rightMax[i]) - height[i]);
            }

            return rainWater;
        }
    }
}
