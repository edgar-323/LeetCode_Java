class Solution {
    public int superEggDrop(int k, int n) {
        return Solution1.newSolver(k, n).solve();
    }

    static class Solution1 {
        int solve() {
            throw new RuntimeException("NOT_IMPLEMENTED");
        }

        Solution1(int k, int n) {}

        static Solution1 newSolver(int k, int n) {
            return new Solution1(k, n);
        }
    }
}
