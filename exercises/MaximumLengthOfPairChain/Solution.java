class Solution {
    private static int LEFT = 0;
    private static int RIGHT = 1;

    public int findLongestChain(int[][] pairs) {
        return Solution1.newSolver(pairs).solve();
    }

    /**
     * TimeComplexity:  O(N*log(N))
     * SpaceComplexity: O(?)
     */
    static class Solution1 {
        // I thought this was similar to LIS problem (and maybe it is
        // but I feel too dumb to think exactly how its the same).
        // Anyhow, I think I know of a slightly different approach:
        // Sort the pairs by the right endpoint.
        // Now define:
        //      maxChain[i] = length of longest chain that ends with
        //                    the i-th pair.
        // How do we compute maxChain[i] ?
        // We know that pairs[0:(i-1)] will be sorted by its right
        // endpoint and so we can conduct a binary search using
        // the left endpoint of pairs[i] as our comparison value.
        // Okay, we will find a range: pairs[k:(i-1)] such that
        // for all pairs[j] (k <= j < i) we have
        //      pairs[j][RIGHT] < pairs[i][LEFT]
        // Okay, well which pairs[j] is the "best" value?
        // Ah I don't think we could even go with this approach since
        // given the previous question.
        // So we'd just revert to an O(N^2) solution or use the very
        // easy greedy approach.
        static Solution1 newSolver(int[][] pairs) {
            return new Solution1(pairs);
        }

        int solve() {
            int N = nums.length;

        }

        Solution1(int[][] pairs) {
            Arrays.sort(pairs, (p1, p2) -> Integer.compare(p1[RIGHT], p2[RIGHT]));
            this.pairs = pairs;
        }
    }
}
