class Solution {
    public boolean checkValidString(String s) {
        return Solution1.checkValidString(s);
    }

    /**
     * TimeComplexity:  O(N^2)
     * SpaceComplexity: O(N^2)
     *
     * Where:
     *      N = s.length()
     */
    private static class Solution1 {

        private final String s;
        private final int N;
        private final Boolean[][] cache;


        static boolean checkValidString(String s) {
            return createSolver(s).isValid(/* index= */ 0, /* unpairedOpenParenthesis= */ 0);
        }

        private boolean isValid(int i, int unpairedOpenParenthesis) {
            if (i == N) {
                return unpairedOpenParenthesis == 0;
            }
            if (inCache(i, unpairedOpenParenthesis)) {
                return getCacheValue(i, unpairedOpenParenthesis);
            }

            boolean result;
            if (s.charAt(i) == '(') {
                result = insertOpenParenthesis(i, unpairedOpenParenthesis);
            } else if (s.charAt(i) == ')') {
                result = insertClosedParenthesis(i, unpairedOpenParenthesis);
            } else { // s.charAt(i) == '*'
                result =
                    insertOpenParenthesis(i, unpairedOpenParenthesis)
                    || insertClosedParenthesis(i, unpairedOpenParenthesis)
                    || insertNoParenthesis(i, unpairedOpenParenthesis);
            }

            return setCacheValue(i, unpairedOpenParenthesis, result);
        }

        private boolean insertOpenParenthesis(int i, int unpairedOpenParenthesis) {
            return isValid(i + 1, unpairedOpenParenthesis + 1);
        }

        private boolean insertClosedParenthesis(int i, int unpairedOpenParenthesis) {
            return unpairedOpenParenthesis > 0 && isValid (i + 1, unpairedOpenParenthesis - 1);
        }

        private boolean insertNoParenthesis(int i, int unpairedOpenParenthesis) {
            return isValid(i + 1, unpairedOpenParenthesis);
        }

        private boolean inCache(int i, int unpairedOpenParenthesis) {
            return cache[i][unpairedOpenParenthesis] != null;
        }

        private boolean getCacheValue(int i, int unpairedOpenParenthesis) {
            return cache[i][unpairedOpenParenthesis];
        }

        private boolean setCacheValue(int i, int unpairedOpenParenthesis, boolean value) {
            cache[i][unpairedOpenParenthesis] = value;
            return value;
        }

        private static Solution1 createSolver(String s) {
            return new Solution1(s);
        }

        private Solution1(String s) {
            this.s = s;
            N = s.length();
            cache = new Boolean[N][N+1];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j <= N; j++) {
                    cache[i][j] = null;
                }
            }
        }
    }
}
