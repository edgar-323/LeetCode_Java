class Solution {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        return Solution1.newSolver(nums, k).solve();
    }

    static class Solution1 {
        static Solution1 newSolver(int[] nums, int k) {
            return new Solution1(nums, k);
        }

        boolean solve() {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        Solution1(int[] nums, int k) {}
    }
}
