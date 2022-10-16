class Solution {
    public int maxCoins(int[] nums) {
        return Solution1.newSolver(nums).solve();
    }

    static class Solution1 {
        static Solution1 newSolver(int[] nums) {
            return new Solution1(nums);
        }

        int solve() {
            throw new RuntimeException("NOT IMPLEMENTED!");
        }

        Solution1(int[] nums) {}
    }
}
