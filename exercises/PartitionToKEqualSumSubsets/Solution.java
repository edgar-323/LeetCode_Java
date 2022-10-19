class Solution {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        return Solution2.newSolver(nums, k).solve();
    }

    static class Solution1 {
        // Thoughts:
        // Assume, that is it indeed possible to partition nums into k subsets
        // whose sums are all equal to one another.
        // Then, we have the following relation:
        //      k*S = sum(P1) + ... + sum(Pk)
        // where
        //      S = value that each partition sums to.
        // Notice:
        //      sum(P1) + ... + sum(Pk) = sum(nums)
        // In other words, the sum of its parts is equal to the sum of the whole.
        // Therefore,
        //      k*S = sum(nums) ====> S = sum(nums) / k
        // Thus, for the above assumptions so be true, sum(nums) must be divisible
        // by k. What else must be true?
        // Okay now we need to "build" the subsets in order to acquire a definitive
        // yes/no answer.
        // I was concerned about any solution that I develop having a potentially
        // high runtime, however, the solution leetcode articles reference approaches
        // with exponential/factorial runtimes. I did NOT look at the intuitions
        // and/or solutions themselves, however. Rather was interested in gauging
        // acceptable runtimes.
        //
        // This is going to require some backtracking but how do we represent our
        // different possible states?
        // If you think about it, we are assigning elements in nums[] to k partitionSums.
        // Okay, at any given time we want to know which element we are currently
        // considering: nums[i] (0 <= i < N)
        // Now, we need to think about which of the k partitionSums we want to assign
        // nums[i] to. Of course, we can only assign nums[i] to the jth bucket if
        //      bucket[j] + nums[i] <= S
        // With this view, we need to keep track of k partitionSums and one index when
        // making recursive calls! A total of k+1 parameters (this is likely why
        // k has a small window constraint).
        // At the end (i == N), you loop through the k partitionSums and ensure that
        // for each bucket we have: partitionSums[j] == S.
        //
        // One more thing... We can have partitionSums[j] be the actual bucket value
        // (an integer) or we can have partitionSums[j] be a list of indices and in order
        // to acquire the values of each bucket at the end, we sum over the partitionSums
        // indices: sum(nums[p] for p in nums[j]).
        // If we do it this way, however, we cannot prune future calls:
        //      partitionSums[j] + nums[i] <= S.
        // Unless you sum up all values in partitionSums[j] which takes non-trivial amount
        // of work.

        private final int N;
        private final int K;
        private final int sum;
        private int[] nums;

        static Solution1 newSolver(int[] nums, int k) {
            return new Solution1(nums, k);
        }

        boolean solve() {
            if (sum % K != 0) {
                return false;
            }

            int[] partitionSums = new int[K];
            return solve(partitionSums, /* index= */ 0);
        }

        boolean solve(int[] partitionSums, int i) {
            if (i == N) {
                return partitionSumsAreEqual(partitionSums);
            }

            for (int k = 0; k < K; k++) {
                partitionSums[k] += nums[i];
                if (solve(partitionSums, i+1)) {
                    return true;
                }
                partitionSums[k] -= nums[i];
            }

            return false;
        }

        boolean partitionSumsAreEqual(int[] partitionSums) {
            int targetSum = sum / K;
            for (int k = 0; k < K; k++) {
                if (partitionSums[k] != targetSum) {
                    return false;
                }
            }

            return true;
        }

        Solution1(int[] nums, int k) {
            this.nums = nums;
            this.K = k;
            this.N = nums.length;
            int s = 0;
            for (int i = 0; i < N; i++) {
                s += nums[i];
            }
            this.sum = s;
        }
    }

    static class Solution2 {
        private final int N;
        private final int K;
        private final int targetSum;
        private final int sum;
        private final int[] nums;
        private final Map<String, Boolean> cache;

        static Solution2 newSolver(int[] nums, int k) {
            return new Solution2(nums, k);
        }

        boolean solve() {
            if (sum % K != 0) {
                return false;
            }

            return solve(/* minIndexToSearch= */ 0, /* runningSum= */ 0, /* numPartitions= */ 0, /* taken= */ 0);
        }

        boolean solve(int minIndexToSearch, int runningSum, int numPartitions, int taken) {
            String key = toKey(minIndexToSearch, runningSum, numPartitions, taken);
            if (cache.containsKey(key)) {
                return cache.get(key);
            }

            if (numPartitions == K) {
                return setCache(key, true);
            }
            if (runningSum > targetSum) {
                return setCache(key, false);
            }
            if (runningSum == targetSum) {
                return setCache(key, solve(/* minIndexToSearch= */ 0, /* runningSum= */ 0, numPartitions + 1, taken));
            }

            for (int i = minIndexToSearch; i < N; i++) {
                if (!numIsTaken(taken, i)) {
                    if (solve(i+1, runningSum + nums[i], numPartitions, takeNum(taken, i))) {
                        return setCache(key, true);
                    }
                }
            }

            return setCache(key, false);
        }

        boolean numIsTaken(int taken, int i) {
            return (taken & (1 << i)) != 0;
        }

        int takeNum(int taken, int i) {
            return taken | (1 << i);
        }

        String toKey(int a, int b, int c, int d) {
            return String.format("%s|%s|%s|%s", a, b, c, d);
        }

        boolean setCache(String key, boolean value) {
            cache.put(key, value);
            return value;
        }

        Solution2(int[] nums, int k) {
            Arrays.sort(nums);
            N = nums.length;
            for (int i = 0, j = N-1; i < j; i++, j--) {
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
            }
            this.nums = nums;
            this.K = k;
            int s = 0;
            for (int i = 0; i < N; i++) {
                s += nums[i];
            }
            this.sum = s;
            this.targetSum = sum / K;
            this.cache = new HashMap<>();
        }
    }
}
