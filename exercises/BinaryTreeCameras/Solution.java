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
        /**
         * This is a greedy bottom-up approach!
         * This strategy will only install a camera in a node if at least one of
         * its child nodes are not covered.
         * Furthermore, if at least one of its children has a camera installed, then
         * it will assume that it is now covered and won't bother installing a camera.
         * If none of the above assumptions are true, then it will ask its parent node
         * to install a camera so as to ensure coverage.
         *
         * Why is this (greedy!) approach optimal?!
         */

        enum NodeState { HAS_CAMERA, COVERED, NEEDS_COVERAGE };

        private int minCamerasNeeded;
        private final TreeNode root;

        int solve() {
            minCamerasNeeded = 0;

            NodeState rootState = solve(root);

            if (rootState == NodeState.NEEDS_COVERAGE) {
                minCamerasNeeded++;
            }

            return minCamerasNeeded;
        }

        NodeState solve(TreeNode node) {
            if (node == null) {
                return NodeState.COVERED;
            }

            NodeState leftChildState = solve(node.left);
            NodeState rightChildState = solve(node.right);

            if (leftChildState == NodeState.NEEDS_COVERAGE
                    || rightChildState == NodeState.NEEDS_COVERAGE) {
                ++minCamerasNeeded;
                return NodeState.HAS_CAMERA;
            }

            if (leftChildState == NodeState.HAS_CAMERA
                    || rightChildState == NodeState.HAS_CAMERA) {
                return NodeState.COVERED;
            }

            return NodeState.NEEDS_COVERAGE;
        }

        Solution1(TreeNode root) {
            this.root = root;
        }

        static Solution1 newSolver(TreeNode root) { return new Solution1(root); }
    }
}
