class Solution {
    public int countPalindromes(String s) {
        return Solution1.newSolver(s).solve();
    }

    static class Solution1 {
        int solve() {
            throw new RuntimeException("_NOT_IMPLEMENTED_");
        }

        static Solution1 newSolver(String s) {
            return new Solution1(s);
        }

        Solution1(String s) {}
    }
}
