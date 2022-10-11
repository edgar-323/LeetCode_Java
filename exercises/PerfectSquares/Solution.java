class Solution {
    public int numSquares(int n) {
        return Solution1.newSolver(n).solve();
    }

    static class Solution1 {
        private final int N;

        static int newSolver(int n) {
            return new Solution1(n);
        }

        int solve() {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        Solution1(int n) {
            N = n;
        }
    }
}
