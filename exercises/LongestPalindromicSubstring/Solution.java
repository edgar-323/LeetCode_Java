class Solution {
    public String longestPalindrome(String s) {
        return Solution1.longestPalindrome(s);
    }

    private static class Solution1 {
        // Lets start with a naive solution.
        static String longestPalindrome(String s) {
            String maxPalindrome = "";
            for (int i = 0; i < s.length(); i++) {
                for (int j = s.length() - 1; i <= j; j--) {
                    if (isPalindrome(s, i, j)) {
                        if (j - i + 1 > maxPalindrome.length()) {
                            maxPalindrome = s.subString(i, j + 1);
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

    //private static class Solution2 {
        // What can we learn from Solution1 ?
    //}
}
