class Solution {
    public int maximumScore(int[] nums, int[] multipliers) {
        return Solution1.newSolver(nums, multipliers).solve();
    }

   /**THOUGHTS:
    *  Use state representation:
    *       (i, j, k)
    *  which indicates that we are considering:
    *       nums[i:(j+1)] and multipliers[k].
    *  Then notice that you can derive k from (i, j) as follows:
    *       k := i + (N-1 - j)
    *  Which means our representation can be collapsed to: (i, j).
    *
    *  Define:
    *       MS(i, j) := maxScore when considering nums[i:(j+1)]
    *                   and multipliers[k] (k is derivable from i,j).
    *  Then, in general:
    *       MS(i, j) :=
    *           max{ multipliers[k] * nums[i] + MS(i+1, j), multipliers[k] * nums[j] + MS(i, j-1) }
    *  Of course:
    *       MS(i, j) = 0, if k >= M
    *       MS(i, j) = 0, if i > j.
    *
    *  That said, we want: MS(0, N-1).
    *
    *  Ahhh, the above algo gives a runtime of O(N^2), however, the constraints clearly
    *  indicate that N >> M.
    *  Therefore, this solution is largely inefficient!
    *  How do we improve/resolve this?
    *
    *  Well, one thing to notice: the array nums[] is always reduced at it's outter-endpoints.
    *  In other words, nums[] can be reduced by at most k elements off of its endpoints.
    *  This does mean that the above algorithm actually has a runtime of O(M^2).
    *  Maybe we should use a hashmap this time since a table actually creates all possible states.
    */ 
    static class Solution1 {
        private final int N;
        private final int M;
        private final int[] nums;
        private final int[] multipliers;
        private final Map<Integer, Map<Integer, Integer>> cache;


        int solve() {
            return solve(/* i= */ 0, /* j= */ N-1);
        }

        int solve(int i, int j) {
            int k = i + (N-1 - j);
            if (k >= M) {
                return 0;
            }

            if (cache.containsKey(i) && cache.get(i).containsKey(j)) {
                return cache.get(i).get(j);
            }

            int maxScore = Math.max(
                    multipliers[k] * nums[i] + solve(i+1, j),
                    multipliers[k] * nums[j] + solve(i, j-1));

            if (!cache.containsKey(i)) {
                cache.put(i, new HashMap<>());
            }
            cache.get(i).put(j, maxScore);

            return maxScore;
        }

        static Solution1 newSolver(int[] nums, int[] multipliers) {
            return new Solution1(nums, multipliers);
        }

        Solution1(int[] nums, int[] multipliers) {
            this.N = nums.length;
            this.M = multipliers.length;
            this.nums = nums;
            this.multipliers = multipliers;

            cache = new HashMap<>();
        }
    }
}
