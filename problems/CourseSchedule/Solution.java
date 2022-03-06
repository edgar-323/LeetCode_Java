// NOTE: LeetCode does NOT automatically import `Optional`, so
// we must import it ourselves.
import java.util.Optional;

class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
       return Solution1.canFinish(numCourses, prerequisites); 
    }

    private static class Solution1 {
        /**
         * TimeComplexity:  O(V + E)
         * SpaceComplexity: O(V + E)
         *
         * Where:
         *   V = numCourses
         *   E = prerequisites.length
         */

        // Directed graph.
        int numCourses;
        List<List<Integer>> adjList;
        List<Optional<Boolean>> hasCycle;

        static boolean canFinish(int numCourses, int[][] prerequisites) {
            // Initialze the graph that is the result of the prerequisites.
            Solution1 solution1 =
                new Solution1(numCourses, prerequisites);
            // As long as the graph has no cycles, then it is possible
            // to complete all courses.
            return !solution1.graphHasCycle();
        }

        boolean graphHasCycle() {
            // Iterate through all nodes, and check to see if any
            // of the nodes has a cycle.
            for (int v = 0; v < numCourses; v++) {
                if (nodeHasCycle(v)) {
                    return true;
                }
            }
            return false;
        }

        boolean nodeHasCycle(int node) {
            if (hasCycle.get(node).isPresent()) {
                // We've already seen `node`.
                return hasCycle.get(node).get();
            }

            // Assume `node` has a cycle (guilty before proven innocent).
            hasCycle.set(node, Optional.of(true));
            for (int neighbor : adjList.get(node)) {
                if (nodeHasCycle(neighbor)) {
                    // Direct `neighbor` has a cycle which means
                    // `node` has a cycle.
                    return true;
                }
            }
            // `node` has been cleared of all charges.
            hasCycle.set(node, Optional.of(false));

            return hasCycle.get(node).get();
        }

        Solution1(int _numCourses, int[][] prerequisites) {
            // Initialize the graph in the constructor.
            numCourses = _numCourses;
            adjList = new ArrayList<>(numCourses);
            hasCycle = new ArrayList<>(numCourses);
            for (int i = 0; i < numCourses; i++) {
                // initialize adjacency list of node `i`.
                adjList.add(new ArrayList<>());
                // We initially don't know if node `i`  has a cycle or not.
                hasCycle.add(Optional.empty());
            }
            for (int[] prereq : prerequisites) {
                // You must take course `u` before you can take course `v`.
                // Add the edge `u -> v` to the graph.
                int u = prereq[0];
                int v = prereq[1];
                adjList.get(u).add(v);
            }
        }
    }
}
