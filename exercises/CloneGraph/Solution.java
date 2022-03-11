/*
// Definition for a Node.
class Node {
    public int val;
    public List<Node> neighbors;
    public Node() {
        val = 0;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}
*/

class Solution {
    public Node cloneGraph(Node node) {
        return Solution1.cloneGraph(node);
    }

    private static class Solution1 {
        /*
         * TimeComplexity:  O(N)
         * SpaceComplexity: O(N)
         */
        Map<Integer,Node> seen;

        Node copy(Node node) {
            if (node == null) {
                return null;
            }
            if (seen.containsKey(node.val)) {
                return seen.get(node.val);
            }
            Node newNode = new Node(node.val, new ArrayList());
            seen.put(node.val, newNode);
            for (Node neighbor : node.neighbors) {
                newNode.neighbors.add(copy(neighbor));
            }
            return newNode;
        }

        static Node cloneGraph(Node node) {
            return new Solution1().copy(node);
        }

        private Solution1() {
            seen = new HashMap<>();
        }
    }
}
