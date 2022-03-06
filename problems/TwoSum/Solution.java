class Solution {
    public int[] twoSum(int[] nums, int target) {
        return Solution3(nums, target);
    }
    /********************************************************/
    private static int[] Solution1(int[] nums, int target) {
        /*
         * TimeComplexity: O(N^2)
         * SpaceComplexity: O(1)
         */
        int[] indices = new int[2];

        final int N = nums.length;
        for (int i = 0; i < N; ++i) {
            for (int j = i+1; j < N; ++j) {
                if (nums[i] + nums[j] == target) {
                    indices[0] = i;
                    indices[1] = j;
                    return indices;
                }
            }
        }

        throw new RuntimeException("Solution1 failed");
    }
    /********************************************************/
    private static int[] Solution2(int[] nums, int target) {
        /*
         * TimeComplexity:  O(N)
         * SpaceComplexity: O(N)
         */
        int[] indices = new int[2];

        final int N = nums.length;

        Map<Integer,List<Integer>> valueToIndices = new HashMap<>();
        for (int index = 0; index < N; ++index) {
            if (!valueToIndices.containsKey(nums[index])) {
                valueToIndices.put(nums[index], new ArrayList<>());
            }
            valueToIndices.get(nums[index]).add(index);
        }

        for (int i = 0; i < N; ++i) {
            if (!valueToIndices.containsKey(target - nums[i])) {
                continue;
            }
            for (int j : valueToIndices.get(target - nums[i])) {
                if (i != j) {
                    indices[0] = Math.min(i, j);
                    indices[1] = Math.max(i, j);
                    return indices;
                }
            }
        }

        throw new RuntimeException("Solution2 failed");
    }
    /********************************************************/
    private static class Node {
        private final int value;
        private final int index;

        Node(int value, int index) {
            this.value = value;
            this.index = index;
        }

        int getValue() {
            return value;
        }

        int getIndex() {
            return index;
        }
    }

    private static int[] Solution3(int[] nums, int target) {
        /*
         * TimeComplexity:  O(N*log(N))
         * SpaceComplexity: O(N)
         */
        int[] indices = new int[2];
        final int N = nums.length;

        // Ah this solution is tricky.
        // After sorting we need a way to map back every value
        // to its former index.
        // We would have to create a new subclass that hold the value
        // as well as its initial index.
        // Then, we would pass it to the Java sorter with a custom
        // comparator that is based on the value. Okay got it.

        Node[] nodes = new Node[N];
        for (int i = 0; i < N; ++i) {
            nodes[i] = new Node(nums[i], i);
        }

        Arrays.sort(
                nodes,
                (Node n1, Node n2)
                    -> Integer.compare(n1.getValue(), n2.getValue()));

        int left = 0;
        int right = N - 1;

        while (left < right) {
            int sum = nodes[left].getValue() + nodes[right].getValue();
            if (sum == target) {
                indices[0] =
                    Math.min(nodes[left].getIndex(), nodes[right].getIndex());
                indices[1] =
                    Math.max(nodes[left].getIndex(), nodes[right].getIndex());
                return indices;
            }
            if (sum < target) {
                // `sum` is too small, make it bigger
                // in order to get numerically closer to `target`.
                left++;
            } else {
                // `sum` is too big, make it smaller
                // in irder to get numerically closer to `target`.
                right--;
            }
        }

        throw new RuntimeException("Solution3 failed");
    }
    /********************************************************/
}
