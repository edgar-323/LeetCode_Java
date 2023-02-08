class Solution {
    public int minimumTimeRequired(int[] jobs, int k) {
        return Solution1.newSolver(jobs, k).solve();
    }

    static class Solution1 {
        int solve() {
            throw new RuntimeException("__NOT__IMPLEMENTED__");
        }

        static Solution1 newSolver(int[] jobs, int k) {
            return new Solution1(jobs, k);
        }

        Solution1(int[] jobs, int k) {}
    }
}
