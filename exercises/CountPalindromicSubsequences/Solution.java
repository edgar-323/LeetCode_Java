class Solution {
    private static int MOD_VAL = 1000_000_007;
    private static int PALINDROMIC_LEN = 5;

    public int countPalindromes(String s) {
        return Solution2.newSolver(s).solve();
    }

    /**
     * THOUGHTS:
     * I'm going to spitball my first attempt (naive):
     * Define state by:
     *          (i [int], candidate [string])
     * where i denotes the current index and candidate denotes
     * the current string that may or may not be a palindrome.
     * Let's assume that candidate does NOT include s[i].
     * Then:
     *      if len(candidate) == PALINDROMIC_LEN:
     *          # You formed a long enough candidate, now let's
     *          # check if it's a valid palindrome.
     *          return isPalindrome(candidate) ? 1 : 0
     *      if i == N:
     *          # Not enough characters to complete candidate
     *          return 0
     *      # return the sum of the case where we add s[i] to the
     *      # current candidate and when we don't add it.
     *      return dp(i+1, candidate) + dp(i+1, candidate + s[i])
     * 
     * What would be the time complexity of the above algorithm?
     *  ==> O(T * N) ==> O(N^6) ?!
     *      where:
     *          T := different # of possible values of candidate
     *               there's probably O(N^PALINDROMIC_LEN).
     *          N := len(s)
     *
     * Can we do better?
     * Let's propose some state representations:
     *
     *  numPalindromes(i,j)     :=  total number of palindromic subsequences
     *                              of length PALINDROMIC_LEN in s[i:(j+1)].
     *
     *  numPalindromes(i,k)     :=  total number of palindromic subsequences
     *                              of length k in s[i:N].
     *
     *  numPalindromes(i,j,k)   :=  total number of palindromic subsequences
     *                              of length k in s[i:(j+1)].
     *
     * From the above, which state rep makes it possible to express problem
     * in terms of smaller problems?
     * |
     * *--> Let's try numPalindromes(i,j,k):
     *          availableChars = 1 + j - i
     *          if k > availableChars:
     *              return 0
     *          if k == 0:
     *              # Don't know if this is correct ...
     *              # Actually, it doesn't even make sense!
     *              return 1
     *          if k == 1:
     *              # Every character in s[i:(j+1)] forms a palindrome of
     *              # size 1.
     *              return availableChars
     *          if k == 2:
     *              return (s[i] == s[j] ? 1 : 0)
     *                  + numPalindromes(i+1,j,k)
     *                  + numPalindromes(i,j-1,k)
     *          return (s[i] == s[j] ? numPalindromes(i+1,j-1,k-2) : 0)
     *              + numPalindromes(i+1,j,k)
     *              + numPalindromes(i,j-1,k)
     *          |
     *          *--> THE ABOVE ALGORITHM IS INCORRECT.
     *               IN PARTICULAR, IT OVER-COUNTS.
     *               I CANNOT FIGURE OUT WHERE THE OVERCOUNTING OCCURS,
     *               SO I AM ABONDONING IT IN FAVOR OF THE OPTIMAL SOLUTION.
     */

    static class Solution1 {
        /**
         * NOTE: THIS APPROACH IS INCORRECT! IT OVER-COUNTS!
         */

        private final String s;
        private final int N;
        private final Integer[][][] cache;

        int solve() {
            return solve(0, N-1, PALINDROMIC_LEN);
        }

        int solve(int i, int j, int k) {
            if (i > j) {
                return 0;
            }
            if (cache[i][j][k] != null) {
                return cache[i][j][k];
            }

            int palindromes;

            int availableChars = 1 + j - i;
            if (k > availableChars) {
                palindromes = 0;
            } else if (k == 1) {
                palindromes = availableChars;
            } else if (k == 2) {
                palindromes =
                    (s.charAt(i) == s.charAt(j) ? 1 : 0)
                    + solve(i+1, j,   k)
                    + solve(i,   j-1, k)
                    - solve(i+1, j-1, k);
            } else {
                palindromes =
                    (s.charAt(i) == s.charAt(j) ? solve(i+1, j-1, k-2) : 0)
                    + solve(i+1, j,   k)
                    + solve(i,   j-1, k)
                    - solve(i+1, j-1, k);
            }

            palindromes %= MOD_VAL;

            cache[i][j][k] = palindromes;
            return palindromes;
        }

        static Solution1 newSolver(String s) {
            return new Solution1(s);
        }

        Solution1(String s) {
            this.s = s;
            this.N = s.length();
            cache = new Integer[N][N][PALINDROMIC_LEN+1];

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    for (int k = 0; k <= PALINDROMIC_LEN; k++) {
                        cache[i][j][k] = null;
                    }
                }
            }
        }
    }

    static class Solution2 {
        /**
         * EXPLANATION & REFERENCE:
         *  https://leetcode.com/problems/count-palindromic-subsequences/solutions/2850466/c-java-python3-counting-prefixes-and-suffixes/
         */
        private static int NUM_SYMBOLS = 10;

        private final String s;
        private final int N;
        private final int prefix[][][];
        private final int suffix[][][];


        int solve() {
            computePrefix();
            computeSuffix();

            return computeNumPalindromes();
        }

        void computePrefix() {
            int[] count = new int[NUM_SYMBOLS];
            for (int i = 0; i < N; i++) {
                int c = s.charAt(i) - '0';
                if (i > 0) {
                    for (int j = 0; j < NUM_SYMBOLS; j++) {
                        for (int k = 0; k < NUM_SYMBOLS; k++) {
                            prefix[i][j][k] = prefix[i-1][j][k];
                            if (c == k) {
                                prefix[i][j][k] += count[j];
                            }
                        }
                    }
                }
                count[c]++;
            }
        }

        void computeSuffix() {
            int[] count = new int[NUM_SYMBOLS];

            for (int i = N-1; i >= 0; i--) {
                int c = s.charAt(i) - '0';
                if (i < N-1) {
                    for (int j = 0; j < NUM_SYMBOLS; j++) {
                        for (int k = 0; k < NUM_SYMBOLS; k++) {
                            suffix[i][j][k] = suffix[i+1][j][k];
                            if (c == k) {
                                suffix[i][j][k] += count[j];
                            }
                        }
                    }
                }
                count[c]++;
            }
        }

        int computeNumPalindromes() {
            int palindromes = 0;

            for (int i = 2; i < N-2; i++) {
                for (int j = 0; j < NUM_SYMBOLS; j++) {
                    for (int k = 0; k < NUM_SYMBOLS; k++) {
                        palindromes =
                            (int)((palindromes + 1L * prefix[i-1][j][k] * suffix[i+1][j][k]) % MOD_VAL);
                    }
                }
            }

            return palindromes;
        }

        static Solution2 newSolver(String s) {
            return new Solution2(s);
        }

        Solution2(String s) {
            this.s = s;
            N = s.length();
            prefix = new int[N][NUM_SYMBOLS][NUM_SYMBOLS];
            suffix = new int[N][NUM_SYMBOLS][NUM_SYMBOLS];
        }
    }
}
