class Solution {
    public boolean canPartition(int[] nums) {
        return Solution1.newSolver(nums).solve();
    }

    static class Solution1 {
        boolean solve() {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        static Solution1 newSolver(int[] nums) {
            return new Solution1(nums);
        }

        Solution1(int[] nums) {}
    }
}
