class Solution {
    public int maxEnvelopes(int[][] envelopes) {
        return Solution1.newSolver(envelopes).solve();
    }

    static class Solution1 {
        static Solution1 newSolver(int[][] envelopes) {
            return new Solution1(envelopes);
        }

        int solve() {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        Solution1(int[][] envelopes) {}
    }
}
