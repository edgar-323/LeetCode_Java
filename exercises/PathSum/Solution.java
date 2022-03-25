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
    public boolean hasPathSum(TreeNode root, int targetSum) {
        return Solution2.hasPathSum(root, targetSum);
    }

    static class Solution1 {
        /**
         * TimeComplexity:  O(N)
         * SpaceComplexity: O(N) for a linear tree but can be O(log(N)) for a balanced tree.
         *
         * Where:
         *      N = number of nodes in root.
         */
        static boolean hasPathSum(TreeNode node, int targetSum) {
            if (node == null) {
                return false;
            }
            if (node.left == null && node.right == null) {
                return node.val == targetSum;
            }
            return hasPathSum(node.left, targetSum - node.val)
                || hasPathSum(node.right, targetSum - node.val);
        }
    }

    static class Solution2 {
        /**
         * TimeComplexity:  O(N)
         * SpaceComplexity: O(N) for a very short tree but can be O(1) for a linear tree.
         *
         * Where:
         *      N = number of nodes in root.
         */
        static boolean hasPathSum(TreeNode root, int targetSum) {
            if (root == null) {
                return false;
            }

            Queue<TreeNode> Q = new LinkedList<>();
            Q.add(root);

            while (!Q.isEmpty()) {
                TreeNode node = Q.poll();
                if (node.left == null && node.right == null && node.val == targetSum) {
                    return true;
                }
                if (node.left != null) {
                    node.left.val += node.val;
                    Q.add(node.left);
                }
                if (node.right != null) {
                    node.right.val += node.val;
                    Q.add(node.right);
                }
            }

            return false;
        }
    }
}
