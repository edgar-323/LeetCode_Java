class Solution {
    public int lengthOfLIS(int[] nums) {
        return Solution1.newSolver(nums).solve();
    }

    /**
     * TimeComplexity:  O(N^2)
     * SpaceComplexity: O(N)
     */
    static class Solution1 {
        private final int[] nums;
        private final Map<Integer,Integer> cache;

        static Solution1 newSolver(int[] nums) {
            return new Solution1(nums);
        }

        int solve() {
            int maxSubsequenceLength = 0;
            for (int i = 0; i < nums.length; i++) {
                maxSubsequenceLength = Math.max(maxSubsequenceLength, solve(i));
            }

            return maxSubsequenceLength;
        }

        int solve(int i) {
            if (cache.containsKey(i)) {
                return cache.get(i);
            }

            int maxSubsequenceLength = 1; // Itself.
            for (int j = i+1; j < nums.length; j++) {
                if (nums[i] < nums[j]) {
                    maxSubsequenceLength = Math.max(maxSubsequenceLength, 1 + solve(j));
                }
            }

            cache.put(i, maxSubsequenceLength);
            return maxSubsequenceLength;
        }

        Solution1(int[] nums) {
            this.nums = nums;
            cache = new HashMap<>();
        }
    }

    /**
     * TimeComplexity:  O(N * log(N))
     * SpaceComplexity: O(N)
     */
    static class Solution2 {
        // Thoughts:
        // Let's focus on nums[i:].
        // Ideally, we want to find the first value nums[j] such that:
        //      nums[i] < nums[j] and i < j
        // Well, as it stands the second condition (i < j) is trivially true
        // since we are looking at nums[i:].
        // However, we want the FIRST such value nums[j] (there can be multiple
        // values that meet this condition; if it exists, that is).
        // If nums[i:] was sorted, then we know that second condition is trivially met
        // and we would be able to binary search for the first value nums[j] that we are
        // seeking! This search would take O(log(N - i)) operations to complete.
        // But how would we ensure that nums[i:] is sorted for each value of i ?
        // Making a copy of nums[i:] and sorting it would take O(k*log(k)) operations
        // where k = (N - i) and repeating this N times would yield O(N^2 * log(N)).
        // Suppose we did decide to sort nums[], how would we ensure that for position
        // i, we would search values restricted to nums[i:] ?
        // What if ...
        //
        // UPDATE FROM THE FOLLOWING DAY:
        // I suspected that trying to conform to the constraints noted above would be
        // nearly impossible.
        // The intuition/inspiration from the documented leetcode solution can be found at:
        //      https://leetcode.com/problems/longest-increasing-subsequence/solution/
        // Notice that they break the index constraint and hence the intermediate
        // subsequences are NOT valid.
        // However, the LENGTH of the generated "subsequences" matches the length of
        // the actual/optimal subsequence.
        // Hence, it works out in this case.
        // Consider formulating a proof for this finding to better develop your intuition
        // of these problems!

        private final int[] nums;

        static Solution2 newSolver(int[] nums) {
            return new Solution2(nums);
        }

        int solve() {
            int N = nums.length;

            // Let's call it "subPermutation" to acknowledge the fact that it is not
            // a valid subsequence but rather a sub-permutation of the original array.
            List<Integer> subPermutation = new ArrayList<>(N);

            subPermutation.add(nums[0]);
            for (int i = 1; i < N; i++) {
                int lastSubPermutationValue = subPermutation.get(subPermutation.size() - 1);
                if (lastSubPermutationValue < nums[i]) {
                    subPermutation.add(nums[i]);
                } else {
                    // Find the first element in sub that is greater than or equal to nums[i].
                    replaceFirstLargest(subPermutation, nums[i]);
                }
            }

            return subPermutation.size();
        }

        /**
         * TimeComplexity:  O(K * log(K))
         * SpaceComplexity: O(1)
         * 
         * Where:
         *      K = subPermutation.size()
         */
        void replaceFirstLargest(List<Integer> subPermutation, int num) {
            int left = 0;
            int right = subPermutation.size();;

            while (left <= right) {
                int mid = (left + right) / 2;

                if (subPermutation.get(mid) < num) {
                    // Move left endpoint towards the right.
                    left = mid;
                } else if (mid == 0 || subPermutation.get(mid - 1) < num) {
                    // subPermutation[mid] >= num and either we are the beggining
                    // of subPermutation[] or subPermutation[mid] is the first value
                    // that is greater than or equal to num.
                    // Either way, we found our target.
                    subPermutation.set(mid, num);
                    return;
                } else {
                    // There are still values towards the left side that are bigger than num
                    // so move right endpoint points the left.
                    right = mid;
                }
            }
            // In a real-world application, we would throw an exception at this point because
            // one of the following points is likely occuring:
            //  1. The binary search procedure is incorrect and/or has bugs.
            //  2. The input subPermutation is empty.
            //  3. The input subPermutation is not sorted.
            //  4. The input num is greater than all values found in subPermutation.
        }

        Solution2(int[] nums) {
            this.nums = nums;
        }
    }
}
