class Solution {
    public int numDecodings(String s) {
        return Solution1.numDecodings(s);
    }

    /**
     * TimeComplexity:  O(N)
     * SpaceComplexity: O(N)
     */
    static class Solution1 {
        // Thoughts:
        // Let's focus on s[i:] and define:
        //  DP[i] = # of decodings in s[i:] assuming that
        //          a decoding starts at index i.
        // We want DP[0].
        // If we have s[i] == '0', then DP[i] = 0
        // This is because we don't have any decodings with
        // leading zeros.

        private final int N;
        private final String s;
        private final Integer[] cache;

        static int numDecodings(String s) {
            return newSolver(s).solve();
        }

        int solve() {
            return solve(0);
        }

        int solve(int i) {
            if (cache[i] != null) {
                return cache[i];
            }

            int decodings;
            if (i == N) {
                decodings = 1;
            } else if (s.charAt(i) == '0') {
                decodings = 0;
            } else {
                // Ones:
                // 1 - 9    ===>> A - I
                // Teens:
                // 10 - 19  ===>> J - S
                // Twenties:
                // 20 - 26  ===>> T - Z 
                decodings = solve(i+1);
                if (validDoubleDigitEncoding(i)) {
                    decodings += solve(i+2);
                }
            }
            cache[i] = decodings;
            return decodings;
        }

        boolean validDoubleDigitEncoding(int i) {
            if (i >= N-1) {
                return false;
            }
            int d1 = getDigit(i);
            if (d1 != 1 && d1 != 2) {
                return false;
            }
            if (d1 == 1) {
                return true;
            }
            int d2 = getDigit(i+1);
            return d2 <= 6;
        }

        int getDigit(int i) {
            return s.charAt(i) - '0';
        }

        static Solution1 newSolver(String s) {
            return new Solution1(s);
        }

        Solution1(String s) {
            this.s = s;
            this.N = s.length();
            cache = new Integer[N+1];
            for (int i = 0; i <= N; i++) {
                cache[i] = null;
            }
        }
    }
}
