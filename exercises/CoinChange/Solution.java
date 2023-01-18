class Solution {
    public int coinChange(int[] coins, int amount) {
        return Solution1.newSolver(coins, amount).solve();
    }

    static class Solution1 {

        static Solution1 newSolver(int[] coins, int amount) {
            return new Solution1(coins, amount);
        }

        int solve() {
            throw new RuntimeException("!NOT IMPLEMENTED!");
        }

        Solution1(int[] coins, int amount) {}
    }
}
