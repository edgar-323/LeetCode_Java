class Solution {
    public int longestValidParentheses(String s) {
        return Solution1.newSolver(s).solve();
    }

    static class Solution1 {
        private final String s;

        int solve() {
            throw new RuntimeException("__NOT-IMPLEMENTED__");
        }

        static Solution1 newSolver(String s) {
            return new Solution1(s);
        }

        Solution1(String s) {
            this.s = s;
        }
    }
}
