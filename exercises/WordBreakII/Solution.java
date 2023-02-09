class Solution {
    public List<String> wordBreak(String s, List<String> wordDict) {
        return Solution1.newSolver(s, wordDict).solve();
    }

    static class Solution1 {
        int solve() {
            throw new RuntimeException("NOT_IMPLEMENTED");
        }

        static Solution1 newSolver(String s, List<String> wordDict) {
            return new Solution1(s, wordDict);
        }

        Solution1(String s, List<String> wordDict) {
        }
    }
}
