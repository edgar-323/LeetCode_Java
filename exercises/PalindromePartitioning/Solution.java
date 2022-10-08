class Solution {
    public List<List<String>> partition(String s) {
        return Solution1.partition(s);
    }

    /**
     * NOTE: UNCERTAIN ABOUT COMPUTATIONAL COMPLEXITY.
     */
    static class Solution1 {
        // I want a list of pairs: (i,j).
        // These pairs indicate that s[i:j] forms a palindrome.
        // These pairs should be sorted by first index (i) and
        // then by second index (j).
        // Then, we could develop a recursive method that will
        // focus on s[i:].
        // We need this method to generate a list of palindromes
        // for s[i:].
        // Let's define this method:
        //      `List<List<String>> solve(int i)`
        // What will we do once we acquire the return stmt?
        //  solve(i)
        //  |
        //  *-> Assume s[i:j] is a palindrome.
        //  *-> We need to include s[i:j] in our final answer
        //  |   but we also need the answer for s[(j+1):].
        //  *-> List<List<String>> theRest = solve(j+1);
        //  *-> Prepend s[i:j] to all sublist in theRest.
        //      We should internally use LinkedList<T> for
        //      this operation (for efficiency); also, it is
        //      convenient that LinkedList<T> implements the
        //      interface List<T>.

        private final String s;
        private final List<String>[] palindromes;
        private final Map<Integer, List<List<String>>> cache;

        static List<List<String>> partition(String s) {
            return newSolver(s).solve();
        }

        List<List<String>> solve() {
            return solve(0);
        }

        List<List<String>> solve(int i) {
            if (cache.containsKey(i)) {
                return cache.get(i);
            }

            List<List<String>> results = new ArrayList<>();
            for (String palindrome : palindromes[i]) {
                for (List<String> partition : solve(i + palindrome.length())) {
                    results.add(newCopy(palindrome, partition));
                }
            }

            cache.put(i, results);
            return results;
        }

        List<String> newCopy(String palindrome, List<String> partition) {
            List<String> newPartition = new ArrayList<>(1 + partition.size());
            newPartition.add(palindrome);
            newPartition.addAll(partition);

            return newPartition;
        }

        static Solution1 newSolver(String s) {
            return new Solution1(s);
        }

        /**
         * There is a non-trivial degree of computation being done here.
         * Additional complexities:
         *      TimeComplexity: O(N^2 * N_Choose_2) ==> O(N^3)
         *      SpaceComplexity: O(N^2 + N_Choose_2) ==> O(N^2)
         */
        Solution1(String s) {
            this.s = s;
            int N = s.length();

            // In the base case (i == N), we will return a list of
            // a single empty partition (also an empty list).
            // This is because there is no palindomes in the empty
            // string slice: S[N:].
            cache = new HashMap<>() {{
                put(N, new ArrayList<>() {{
                    add(new ArrayList<>());
                }});
            }};

            palindromes = new ArrayList[N];
            for (int i = 0; i < N; i++) {
                palindromes[i] = new ArrayList<>();
            }

            // isPalindrome[i][j] will help us compute palindromes[i].
            boolean[][] isPalindrome = new boolean[N][N];
            // Compute palindromes of lengths: {1, 2}.
            for (int i = 0; i < N; i++) {
                isPalindrome[i][i] = true;
                palindromes[i].add(s.substring(i, i+1));
                if (i < N - 1) {
                    isPalindrome[i][i+1] = (s.charAt(i) == s.charAt(i+1));
                    if (isPalindrome[i][i+1]) {
                        palindromes[i].add(s.substring(i, i+2));
                    }
                }
            }
            // Compute palindromes of lengths: {3, 4, ..., N}.
            for (int subarraySize = 3; subarraySize <= N; subarraySize++) {
                for (int i = 0; (i + subarraySize) <= N; i++) {
                    int j = i + subarraySize - 1;
                    isPalindrome[i][j] =
                        isPalindrome[i+1][j-1] && (s.charAt(i) == s.charAt(j));
                    if (isPalindrome[i][j]) {
                        palindromes[i].add(s.substring(i, j+1));
                    }
                }
            }
        }
    }
}
