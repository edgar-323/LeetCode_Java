class Solution {
    public int uniquePaths(int m, int n) {
        return Solution1.uniquePaths(m, n);
    }

    /**
     * TimeComplexity:  O(m*n)
     * SpaceComplexity: O(m*n)
     */
    private static class Solution1 {
        private final int M, N;
        private final Integer[][] cache;

        static int uniquePaths(int m, int n) {
            return newSolver(m, n).solve();
        }

        int solve() {
            return solve(0, 0);
        }

        int solve(int i, int j) {
            if (cache[i][j] != null) {
                return cache[i][j];
            }

            int paths;
            if (i == M && j == N) {
                paths = 1;
            } else if (i == M) {
                paths = solve(i, j+1);
            } else if (j == N) {
                paths = solve(i+1, j);
            } else {
                paths = solve(i+1, j) + solve(i, j+1);
            }

            cache[i][j] = paths;
            return paths;
        }

        static Solution1 newSolver(int m, int n) {
            return new Solution1(m, n);
        }

        Solution1(int m, int n) {
            M = m - 1;
            N = n - 1;
            cache = new Integer[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    cache[i][j] = null;
                }
            }
        }
    }
}
