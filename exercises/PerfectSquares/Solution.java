class Solution {
    public int numSquares(int n) {
        return Solution1.newSolver(n).solve();
    }

    /**
     * TimeComplexity:  O(N * sqrt(N))
     * SpaceComplexity: O(N)
     */
    static class Solution1 {
        private final int N;
        private final Map<Integer, Integer> cache;

        static Solution1 newSolver(int n) {
            return new Solution1(n);
        }

        int solve() {
            return solve(N);
        }

        int solve(int n) {
            if (cache.containsKey(n)) {
                return cache.get(n);
            }

            int minSquares = Integer.MAX_VALUE;
            for (int i = (int) Math.sqrt(n); i >= 1; i--) {
                minSquares = Math.min(minSquares, 1 + solve(n - i*i));
            }

            cache.put(n, minSquares);
            return minSquares;
        }

        Solution1(int n) {
            N = n;
            cache = new HashMap<>() {{ put(0, 0); }};
        }
    }
}
