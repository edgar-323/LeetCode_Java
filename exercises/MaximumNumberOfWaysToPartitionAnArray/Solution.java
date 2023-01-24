class Solution {
    public int waysToPartition(int[] nums, int k) {
        return Solution1.newSolver(nums, k).solve();
    }

    static class Solution1 {

        int solve() {
            throw new RuntimeException("___NOT-IMPLEMENTED___");
        }

        static Solution1 newSolver(int[] nums, int k) {
            return new Solution1(nums, k);
        }

        Solution1(int[] nums, int k) {}
    }
}
