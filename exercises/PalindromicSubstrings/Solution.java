class Solution {
    public int countSubstrings(String s) {
        return Solution2.countSubstrings(s);
    }

    /**
     * Utility that computes isPalindrome[i][j] (if s[i:j] is a palindrome).
     *
     * TimeComplexity:  O(N^2)
     * SpaceComplexity: O(N^2)
     */
    private static boolean[][] computeIsPalindrome(String s) {
        int N = s.length();

        boolean[][] isPalindrome = new boolean[N][N];

        for (int i = 0; i < N; i++) {
            isPalindrome[i][i] = true;
            if (i < N - 1) {
                isPalindrome[i][i+1] = (s.charAt(i) == s.charAt(i+1));
            }
        }

        for (int subarraySize = 3; subarraySize <= N; subarraySize++) {
           for (int i = 0; (i + subarraySize) <= N; i++) {
               isPalindrome[i][i + subarraySize - 1] =
                   isPalindrome[i+1][i + subarraySize - 2]
                   && (s.charAt(i) == s.charAt(i + subarraySize - 1));
           }
        }

        return isPalindrome;
    }

    private static class Solution1 {
        // NOTE: THE FOLLOWING ASSUMPTION IS VASTLY INCORRECT (I WAS VERY SLEEP-DEPRIVED).
        //
        // How many substrings in s[i:j] ?
        // DP[i:j] = DP[(i+1):(j-1)] + DP[(i+1):j] + DP[i:(j-1)] + (isPalindrome[i:j] ? 1 : 0);
        // Furthermore:
        // isPalindrome[i:j] = (s[i] == s[j]) && isPalindrome[(i+1):(j-1)];

        static int countSubstrings(String s) {
            if (s.isEmpty()) {
                return 0;
            }

            int N = s.length();

            boolean[][] isPalindrome = computeIsPalindrome(s);

            int[][] DP = new int[N][N];

            for (int i = 0; i < N; i++) {
                DP[i][i] = 1;
                if (i < N - 1) {
                    DP[i][i + 1] = 2 + (isPalindrome[i][i+1] ? 1 : 0);
                }
            }

            for (int subarraySize = 3; subarraySize <= N; subarraySize++) {
                for (int i = 0; (i + subarraySize) <= N; i++) {
                    DP[i][i + subarraySize - 1] =
                        DP[i + 1][i + subarraySize - 2]
                        + DP[i + 1][i + subarraySize - 1]
                        + DP[i][i + subarraySize - 2]
                        + (isPalindrome[i + 1][i + subarraySize - 2] ? 1 : 0);
                }
            }

            return DP[0][N-1];
        }
    }

    /**
     * TimeComplexity:  O(N^2)
     * SpaceComplexity: O(N^2)
     */
    private static class Solution2 {
        // Let's be naive. (I am stuck right now).
 
        static int countSubstrings(String s) {
            if (s.isEmpty()) {
                return 0;
            }

            int N = s.length();

            boolean[][] isPalindrome = computeIsPalindrome(s);

            int numPalindromes = 0;

            for (int subarraySize = 1; subarraySize <= N; subarraySize++) {
                for (int i = 0; i + subarraySize <= N; i++) {
                    int j = i + subarraySize - 1;
                    if (isPalindrome[i][j]) {
                        numPalindromes++;
                    }
                }
            }

            return numPalindromes;
        }
    }
}
