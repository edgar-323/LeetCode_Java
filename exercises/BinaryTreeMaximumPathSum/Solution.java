/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public int maxPathSum(TreeNode root) {
        return Solution1.newSolver(root).solve();
    }

    /**
     * THOUGHTS:
     * There is no requirement that the path-sum includes the root
     * (this actually makes the problem tougher!).
     *
     * Here is a naive implementation:
     *  1.  Rebuild the graph with a `parent` pointer so that we can
     *      reach ancestors of any node with ease.
     *      This is an O(N) procedure.
     *  2.  Traverse the tree starting from root.
     *      For each node `v` of the tree, compute the time it takes
     *      to reach every other node `u` starting from `v` (`v != u`).
     *      This is equivalent of "rooting" the tree at node `v`.
     *  3.  Keep track of all path-sums, and take the maximal sum.
     * Overall runtime: O(N^2)
     *  *   This runtime is not too shabby. This means that a linear
     *      time algorithm probably exists.
     *  *   Is it possible to improve this algorithm?!
     *      Notice that the most expensive aspect of this scheme comes
     *      from compute the path-sum: `sum(v --> ... --> u)`.
     *
     * New scheme:
     *  At each node, figure out the max path-sum if the current node is included.
     *
     */

    static class Solution1 {
        private final TreeNode root;
        private int maxPathSum;

        int solve() {
            maxPathSum = Integer.MIN_VALUE;
            solve(root);

            return maxPathSum;
        }

        int solve(TreeNode node) {
            if (node == null) {
                return 0;
            }

            int leftPathSum = solve(node.left);
            int rightPathSum = solve(node.right);
            int nodeVal = node.val;

            // Maximize the global path-sum estimation.
            maxPathSum = max(
                    maxPathSum,
                    // Singleton input node is best value.
                    nodeVal,
                    // Input node and its left subtree is best value.
                    nodeVal + leftPathSum,
                    // Input node and it right substree is best value.
                    nodeVal + rightPathSum,
                    // Input node and both and both its subtrees form best path sum.
                    nodeVal + leftPathSum + rightPathSum);

            // Return the best path sum that must include the input node.
            // Remember that we cannot maximize over the case where both subtrees are
            // included because then we wouldn't have a valid path.
            return max(
                    // Singleton input node is best.
                    nodeVal,
                    // Input node and left subtree are best.
                    nodeVal + leftPathSum,
                    // Input node and right subtree are best.
                    nodeVal + rightPathSum);
        }

        int max(int a, int b, int c, int d, int e) {
            return Math.max(max(a, b, c), Math.max(d, e));
        }

        int max(int a, int b, int c) {
            return Math.max(a, Math.max(b, c));
        }

        static Solution1 newSolver(TreeNode root) {
            return new Solution1(root);
        }

        Solution1(TreeNode root) {
            this.root = root;
        }
    }
}
