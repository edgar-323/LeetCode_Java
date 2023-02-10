class Solution {
    public int maximumScore(int[] nums, int[] multipliers) {
        return Solution1.newSolver(nums, multipliers).solve();
    }

    static class Solution1 {
        int solve() {
            throw new RuntimeException("NOT-IMPLEMENTED!");
        }

        static Solution1 newSolver(int[] nums, int[] multipliers) {
            return new Solution1(nums, multipliers);
        }

        Solution1(int[] nums, int[] multipliers) {}
    }
}
