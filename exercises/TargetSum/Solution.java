class Solution {
    private static final int MAX_VALUE = 1000;

    public int findTargetSumWays(int[] nums, int target) {
        return Solution3.findTargetSumWays(nums, target);
    }

    private static class Solution1 {
        // NOTE:
        // This will be the most naive solution I can think of.
        //
        // THOUGHTS:
        // Every elements in `nums` needs a binary assignment in
        // the set {-,+}.
        // By the end of this assignment, every `nums[i]` will have
        // this assignmemt.
        // So for all other combinations `nums[i]` has two possible values.
        // At the end of the assignmemt, check `current_sum = target` and,
        // if so, return `1` (meaning a match), otherwise return `0`.

        /**
         * TimeComplexity:  O(2^N)
         * SpaceComplexity: O(N)
         *
         * Where:
         *   N = nums.length
         */

        int[] nums;

        static int findTargetSumWays(int[] nums, int target) {
            return new Solution1(nums).findTargetSumWays(0, -target);
        }

        int findTargetSumWays(int index, int sum) {
            if (index == nums.length) {
                // Every element has an assigned operator.
                // Let's check if there's a match.
                // Fyi, the sum starts at `-target`. If the current
                // sum if `0`, then it means we assigned operators to
                // all elements such that:
                //     ASSIGNED_SUM + (-target) = 0
                //     ==> ASSIGNED_SUM = target
                return sum == 0 ? 1 : 0;
            }
            // Add num ways when we assign `nums[i]` a `-` and num ways
            // when we assign it a `+`.
            return
                findTargetSumWays(index + 1, sum + nums[index]) +
                findTargetSumWays(index + 1, sum - nums[index]);
        }

        Solution1(int[] _nums) {
            nums = _nums;
        }
    }

    private static class Solution2 {
        // NOTE: This will be a memory-cached (still recursive) version
        // of Solution1.
        //
        // THOUGHTS:
        // If you look at Solution1, you may notice that each recursive
        // call is influenced by two parameters:
        //      (sum, index).
        // The order of these parameters is NOT important (is it ever!?).
        // How can we cache this information in a datastructure (conveniently)?
        // How about the following mappings:
        //      `index -> sum -> numWays`
        // Sounds reasonable. Let's give it a shot...

        /**
         * TimeComplexity:  O(N * S)
         * SpaceComplexity: O(N * S)
         *
         * Where:
         *   N = nums.length
         *   S = 2 * MAX_VALUE * N
         */

        private final int[] nums;
        private final List<Map<Integer,Integer>> cache;

        static int findTargetSumWays(int[] nums, int target) {
            return new Solution2(nums).findTargetSumWays(0, -target);
        }

        int findTargetSumWays(int index, int sum) {
            if (index == nums.length) {
                // Primitive case from Solution1.
                // (There is no need to cache this).
                return sum == 0 ? 1 : 0;
            }
            if (cache.get(index).containsKey(sum)) {
                // Cache hit.
                return cache.get(index).get(sum);
            }
            // Cache miss.
            final int numWays =
                findTargetSumWays(index + 1, sum - nums[index]) +
                findTargetSumWays(index + 1, sum + nums[index]);

            cache.get(index).put(sum, numWays);

            return numWays;
        }

        Solution2(int[] _nums) {
            nums = _nums;
            // Initialize the cache.
            cache = new ArrayList<>(nums.length);
            for (int i = 0; i < nums.length; i++) {
                cache.add(new HashMap<>());
            }
        }
    }

    private static class Solution3 {
        // NOTE: Now let's attempt to convert Solution2 into
        // a DynamicProgramming solution.

        /**
         * TimeComplexity:  O(N * S)
         * SpaceComplexity: O(N * S)
         *
         * Where:
         *   N = nums.length
         *   S = 2 * MAX_SUM * N
         */
        private static final int INVALID_INDEX = -1;
        private final int[] nums;
        private final int target;
        private final int N;
        private final int MAX_SUM;

        int findTargetSumWays() {
            int[][] numWays = new int[N + 1][2 * MAX_SUM + 1];

            // Primite case:
            // (index == nums.length, sum in [-MAX_SUM, MAX_SUM])
            /*
            for (int sum = -MAX_SUM; sum <= MAX_SUM; sum++) {
                numWays[N][getSumIndex(sum)] = sum == 0 ? 1 : 0;
            }
            */
            numWays[N][getSumIndex(0)] = 1;
            for (int i = N-1; i >= 0; i--) {
                for (int s = -MAX_SUM; s <= MAX_SUM; s++) {
                    int negCase = getSumIndex(s - nums[i]);
                    int posCase = getSumIndex(s + nums[i]);

                    numWays[i][getSumIndex(s)] =
                        (isValidIndex(negCase) ? numWays[i+1][negCase] : 0) +
                        (isValidIndex(posCase) ? numWays[i+1][posCase] : 0);
                }
            }

            return numWays[0][getSumIndex(-target)];
        }
        
        static int findTargetSumWays(int[] nums, int target) {
            return new Solution3(nums, target).findTargetSumWays();
        }

        int getSumIndex(int sum) {
            return isValidIndex(sum + MAX_SUM) ? sum + MAX_SUM : -1;
        }

        boolean isValidIndex(int i) {
            return i >= 0 && i <= 2 * MAX_SUM;
        }

        Solution3(int[] _nums, int _target) {
            nums = _nums;
            target = _target;
            N = nums.length;
            MAX_SUM = N * MAX_VALUE;
        }
    }
}
