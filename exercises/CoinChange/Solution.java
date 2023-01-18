class Solution {
    public int coinChange(int[] coins, int amount) {
        return Solution1.newSolver(coins, amount).solve();
    }

    /**
     * RuntimeComplexity:   O(S * N)
     * SpaceComplexity:     O(S)
     *
     * where:
     *      S = amount
     *      N = coins.length
     */
    static class Solution1 {
        private static final int IMPOSSIBLE = -1;

        private final int amount;
        private final int[] coins;
        private final Map<Integer, Integer> cache;

        static Solution1 newSolver(int[] coins, int amount) {
            return new Solution1(coins, amount);
        }

        int solve() {
            return solve(amount);
        }

        int solve(int S) {
            if (S < 0) {
                return IMPOSSIBLE;
            }

            if (S == 0) {
                return 0;
            }

            if (cache.containsKey(S)) {
                return cache.get(S);
            }

            int minCoins = IMPOSSIBLE;

            for (int coin : coins) {
                int coinsNeeded = solve(S - coin);
                if (coinsNeeded == IMPOSSIBLE) {
                    continue;
                }
                if (minCoins == IMPOSSIBLE) {
                    minCoins = 1 + coinsNeeded;
                } else {
                    minCoins = Math.min(minCoins, 1 + coinsNeeded);
                }
            }

            cache.put(S, minCoins);
            return minCoins;
        }

        Solution1(int[] coins, int amount) {
            this.coins = coins;
            this.amount = amount;

            cache = new HashMap<>();
        }
    }
}
