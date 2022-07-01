class Solution {
    public String longestPalindrome(String s) {
        return Solution2.longestPalindrome(s);
    }

    private static class Solution1 {
        // Lets start with a naive solution ...

        /**
         * TimeComplexity:  O(N^3)
         * SpaceComplexity: O(1)
         *
         * Where:
         *      N = s.length()
         */
        static String longestPalindrome(String s) {
            String maxPalindrome = "";
            for (int i = 0; i < s.length(); i++) {
                for (int j = s.length() - 1; i <= j; j--) {
                    if (isPalindrome(s, i, j)) {
                        if (j - i + 1 > maxPalindrome.length()) {
                            maxPalindrome = s.substring(i, j + 1);
                        }
                    }
                }
            }
            return maxPalindrome;
        }

        private static boolean isPalindrome(String s, int i, int j) {
            while (i <= j) {
                if (s.charAt(i) != s.charAt(j)) {
                    return false;
                }
                i++;
                j--;
            }
            return true;
        }
    }

    private static class Solution2 {
        // What can we learn about Solution1 ?
        // A given palindrome is defined by exactly two parameters.
        // Either:
        //      * (i, j) such that s[i:j] is a palindrome, i <= j.
        //      * (i, k) such that s[i:i+k] is a palindrome, k >= 0.
        // Given this fact, I do not see a way to define a palindrome in less O(N^2)
        // operations.
        // In Solution1, we execute isPalindrome(s, i, j) a total of O(N^2) times.
        // isPalindrome(s, i, j) is a O(N) time operation.
        // Is it possible to somehow make isPalindrome(s, i, j) a constant time operation?
        //
        // Suppose we knew the answer to isPalindrome(s, i+1, j-1), then:
        //   isPalindrome(s, i, j) = isPalindrome(s, i+1, j-1) && s[i] == s[j]; // This is O(1).
        // Is this the only way to determine whether s[i:j] is a palindrome? YES.
        // We are onto something...
        // Define:
        //      DP[i][j] := isPalindrome(s, i, j)
        //
        // In general, for i + 1 < j:
        //      DP[i][j] = DP[i+1][j-1] && s[i] == s[j];
        // For i in {0, ..., N-1}:
        //      DP[i][i] = true;
        // for i in {0, ..., N-2}:
        //      DP[i][i+1] = s[i] == s[i+1];

        /**
         * TimeComplexity:  O(N^2)
         * SpaceComplexity: O(N^2)
         *
         * Where:
         *      N = s.length()
         */
        static String longestPalindrome(String s) {
            int N = s.length();

            int maxPalindromeStart = 0;
            int maxPalindromeEnd = 0;

            boolean[][] DP = new boolean[N][N];

            for (int i = 0; i < N; i++) {
                DP[i][i] = true;
            }

            for (int i = 0; i < N-1; i++) {
                DP[i][i+1] = s.charAt(i) == s.charAt(i+1);
                if (DP[i][i+1] && ((maxPalindromeEnd - maxPalindromeStart + 1) == 1)) {
                    maxPalindromeStart = i;
                    maxPalindromeEnd = i+1;
                }
            }

            for (int len = 3; len <= N; len++) {
                for (int i = 0; i < N; i++) {
                    int j = i + len - 1;
                    if (j >= N) {
                        break;
                    }
                    DP[i][j] = DP[i+1][j-1] && s.charAt(i) == s.charAt(j);
                    if (DP[i][j] && ((j - i + 1) > (maxPalindromeEnd - maxPalindromeStart + 1))) {
                        maxPalindromeStart = i;
                        maxPalindromeEnd = j;
                    }
                }
            }

            return s.substring(maxPalindromeStart, maxPalindromeEnd + 1);
        }
    }
}
