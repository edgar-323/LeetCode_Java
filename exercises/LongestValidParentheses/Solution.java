class Solution {
    public int longestValidParentheses(String s) {
        return Solution1.newSolver(s).solve();
    }

    /**
     * THOUGHTS:
     * Let's think of a brute-force approach:
     * We could check every substring of s[i:j] (for all 0 <= i <= j < N).
     * There are O(N^2) such substrings.
     * For each substring s[i][j], we check whether it is valid paranthesis
     * and, if so, we check if its length is better than our current estimate.
     * Since we check all such substrings, our estimate eventually converges
     * to the true maximum valid paranthesis.
     * Checking each substring take O(N) work.
     * Thus, this overall algorithm would take O(N^3) amount of work.
     * This approach does not bootstrap (i.e., it doesn't take a dynamic
     * programming technique).
     *
     * Can we do better? Can we bootstrap?
     * I.e., can we employ dynamic programming?
     * Let's define:
     *  longest(i) := longest valid substring startig at position i.
     * Then:
     *  If s[i] == ')':
     *      longest(i) = 0, because a valid substring cannot start with ')'.
     *  If i+1 == N:
     *      longest(i) = 0, because s[i:] = "(" which is not a valid substring.
     *  If s[i+1] == ')':
     *      longest(i) = 2 + longest(i+2)
     *  If s[i+1] == '(':
     *      L := longest(i+1)
     *      j := i + L + 1
     *      if j < N and s[j] == ")":
     *          longest(i) = 2 + L + longest(j+1)
     *      else:
     *          longest(i) = 0
     *
     * If the above algorithm is correct, it will cost us O(N) work.
     */

    static class Solution1 {

        private final String s;
        private final int N;
        private final Integer[] cache;

        int solve() {
            int maxValidParanthesis = 0;
            for (int i = 0; i < N; i++) {
                maxValidParanthesis = Math.max(maxValidParanthesis, solve(i));
            }

            return maxValidParanthesis;
        }

        int solve(int i) {
            if (i == N) {
                return 0;
            }
            if (cache[i] != null) {
                return cache[i];
            }

            int validParanthesis;
            if (s.charAt(i) == ')') {
                validParanthesis = 0;
            } else if (i+1 == N) {
                validParanthesis = 0;
            } else if (s.charAt(i+1) == ')') {
                validParanthesis = 2 + solve(i+2);
            } else {
                int innerValidParanthesis = solve(i+1);
                int j = i + innerValidParanthesis + 1;
                if (j < N && s.charAt(j) == ')') {
                    validParanthesis = 2 + innerValidParanthesis + solve(j+1);
                } else {
                    validParanthesis = 0;
                }
            }

            cache[i] = validParanthesis;
            return validParanthesis;
        }

        static Solution1 newSolver(String s) {
            return new Solution1(s);
        }

        Solution1(String s) {
            this.s = s;
            this.N = s.length();

            cache = new Integer[N];
            for (int i = 0; i < N; i++) {
                cache[i] = null;
            }
        }
    }
}
