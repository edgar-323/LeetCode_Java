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
    public int minCameraCover(TreeNode root) {
        return Solution1.newSolver(root).solve();
    }

    static class Solution1 {

        int solve() {
            throw new RuntimeException("NOT_IMPLEMENTED");
        }

        Solution1(TreeNode root) {}

        static Solution1 newSolver(TreeNode root) { return new Solution1(root); }
    }
}
