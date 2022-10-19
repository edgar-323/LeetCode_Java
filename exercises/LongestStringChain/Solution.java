class Solution {
    public int longestStrChain(String[] words) {
        return Solution1.newSolver(words).solve();
    }

    static class Solution1 {
        static Solution1 newSolver(String[] words) {
            return new Solution1(words);
        }

        int solve() {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        Solution1(String[] words) {}
    }
}
