class Solution {
    public int findLongestChain(int[][] pairs) {
        return Solution1.newSolver(pairs).solve();
    }

    static class Solution1 {
        static Solution1 newSolver(int[][] pairs) {
            return new Solution1(pairs);
        }

        int solve() {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        Solution1(int[][] pairs) {}
    }
}
