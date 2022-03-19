/*
// Definition for a Node.
class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}
    
    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
*/

class Solution {
    public Node connect(Node root) {
        return Solution1.connect(root);
    }

    static class Solution1 {
        static Node connect(Node root) {
            if (root == null) {
                return null;
            }

            Queue<Node> Q = new LinkedList<>();
            Q.add(root);

            while (!Q.isEmpty()) {
                for (int nodeToRemove = Q.size(); nodeToRemove > 0; nodeToRemove--) {
                    // Pop the head node:
                    Node node = Q.remove();
                    // Populate `next` field of the head node.
                    // If the head node is the last node of the current level,
                    // then its `next` field should be null.
                    node.next = nodeToRemove == 1 ? null : Q.peek();
                    // Only add child nodes if they are non-null.
                    if (node.left != null) {
                        Q.add(node.left);
                    }
                    if (node.right != null) {
                        Q.add(node.right);
                    }
                }
            }

            return root;
        }
    }
}
