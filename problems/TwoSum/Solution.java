class Solution {
    public int[] twoSum(int[] nums, int target) {
        return solution1(nums, target);
    }

    public int[] solution1(int[] nums, int target) {
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

        return indices;
    }

    public int[] solution2(int[] nums, int target) {
        /*
         * TimeComplexity:  O(N)
         * SpaceComplexity: O(N)
         */
        int[] indices = new int[2];

        final int N = nums.length;

        Map<Integer,Set<Integer>> valueToIndices = new HashMap<>();
        for (int index = 0; index < N; ++index) {
            if (!valueToIndices.containsKey(nums[index])) {
                valueToIndices.put(nums[index], new HashSet<>());
            }
            valueToIndices.get(nums[index]).add(index);
        }

        for (int i = 0; i < N; ++i) {
            if (valueToIndices.containsKey(target - nums[i])) {
            }
        }

        throw new RuntimeException("Your Algo Failed Bro");
    }
}
