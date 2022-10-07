class Solution {
    public int numDecodings(String s) {
        return Solution1.numDecodings(s);
    }

    static class Solution1 {
        private final String s;

        static int numDecodings(String s) {
            return newSolver(s).solve();
        }

        int solve() {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        static Solution1 newSolver(String s) {
            return new Solution1(s);
        }

        Solution1(String s) {
            this.s = s;
        }
    }
}
