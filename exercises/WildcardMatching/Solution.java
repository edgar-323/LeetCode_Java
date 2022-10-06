class Solution {
    public boolean isMatch(String s, String p) {
        return Solution1.isMatch(s, p);
    }

    /**
     * TimeComplexity:  O(N * M)
     * SpaceComplexity: O(N * M)
     *
     * Where:
     *      N = s.length()
     *      M = p.length()
     */
    private static class Solution1 {
        private static final char ANY_CHAR = '?';
        private static final char WILDCARD = '*';

        private final String s;
        private final String p;
        private final Boolean[][] cache;

        static boolean isMatch(String s, String p) {
            return newSolver(s, p).solve();
        }

        private boolean solve() {
            return solve(0, 0);
        }

        private boolean solve(int i, int j) {
            if (cacheHit(i, j)) {
                return getCacheVal(i, j);
            }

            boolean matches;
            if (i == s.length()) {
                // s is empty, so will only be true if p is also empty or if its current value is the
                // wildcard and the remaining of its values match.
                matches =
                    (j == p.length())
                    || ((p.charAt(j) == WILDCARD) && solve(i, j+1));
            } else if (j == p.length()) {
                matches = false; // This should be: i == s.length(), but we already know that's false.
            } else if (p.charAt(j) == WILDCARD) {
                matches =
                    solve(i+1, j)  // Consume s[i] and keep matching with wildcard.
                    || solve(i+1, j+1)  // Consume s[i] but stop matching with wildcard.
                    || solve(i, j+1);  // Don't consume s[i] and stop matching with wildcard.
            } else {
                // This will only be true if p[j] == '?' or if s[i] == p[j] and the rest of the
                // values also match.
                matches =
                    (p.charAt(j) == ANY_CHAR || s.charAt(i) == p.charAt(j))
                    && solve(i+1, j+1);
            }

            return setCacheVal(i, j, matches);
        }

        private boolean cacheHit(int i, int j) {
            return cache[i][j] != null;
        }

        private boolean getCacheVal(int i, int j) {
            return cache[i][j];
        }

        private boolean setCacheVal(int i, int j, boolean v) {
            cache[i][j] = v;
            return v;
        }

        private static Solution1 newSolver(String s, String p) {
            return new Solution1(s, p);
        }

        private Solution1(String s, String p) {
            this.s = s;
            this.p = p;

            cache = new Boolean[s.length() + 1][p.length() + 1];
            for (int i = 0; i <= s.length(); i++) {
                for (int j = 0; j <= p.length(); j++) {
                    cache[i][j] = null;
                }
            }
        }
    }
}
