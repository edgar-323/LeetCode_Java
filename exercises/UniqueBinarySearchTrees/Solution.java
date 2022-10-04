class Solution {
    public int numTrees(int n) {
        return Solution1.numTrees(n);
    }

    /**
     * TimeComplexity:  O(N^2)
     * SpaceComplexity: O(N)
     */
    private static class Solution1 {
        int N;
        Integer[] cache;

        static int numTrees(int n) {
            return createSolver(n).solve();
        }

        int solve() {
            return solve(N);
        }

        int solve(int n) {
            if (n == 0 || n == 1) {
                // There is exactly one way to arrange zero or one node.
                return 1;
            }
            if (cache[n] != null) {
                return cache[n];
            }
            // Total number of trees will be equal to the total number of ways we can
            // arrange (i - 1) nodes in the left tree multiplied by the total number
            // of ways that we can arrange (n - i) nodes in the right tree; assuming
            // that the root of this said tree has node i.
            // Any possible node between 1 and n can be the root; thus i must iterate
            // in this range.
            int trees = 0;
            for (int i = 1; i <= n; i++) {
                // pick i as the root.
                int waysToArrangeLeftTree = solve(i - 1);
                int waysToArrangeRightTree = solve(n - i);
                trees += waysToArrangeLeftTree * waysToArrangeRightTree;
            }
            cache[n] = trees;
            return trees;
        }

        static Solution1 createSolver(int n) {
            return new Solution1(n);
        }

        private Solution1(int n) {
            N = n;
            cache = new Integer[N+1];
            for (int i = 0; i <= N; i++) {
                cache[i] = null;
            }
        }
    }
}
