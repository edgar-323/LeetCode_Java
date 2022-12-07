class Solution {
    public int superEggDrop(int k, int n) {
        return Solution1.newSolver(k, n).solve();
    }

    static class Solution1 {
        // Inspired by Approach#1 under official solution:
        //      https://leetcode.com/problems/super-egg-drop/solutions/158936/super-egg-drop/
        //
        // Let's define the states (i.e., sub-problems) as:
        //      T(k,n) := minimum # of moves required to determine f <= n, where
        //                f denotes the floor such that any egg dropped from
        //                floor i <= f will break.
        //
        // Suppose, we instead wanted to use three parameters to describe our subproblem:
        //      F(k, bottom, top) := min # of moves required to find f with the assumption
        //                           that f lies in the range { bottom, ..., top }.
        //
        // The important thing to notice is the following reduction:
        //      F(k, bottom, top) <--> T(k, top - bottom)
        // The reason why the above statement is true is because we can map the floor sets
        // as follows:
        //      { bottom, ..., top } <--> { 1, ..., (top - bottom) }
        // this is, in turn, also true because each floor is equivalent (i.e., they are not
        // weighted by any additonal variables).
        // All to say that T(k, n) is a good (more effecient!) representation of our sub-states.
        //
        // Okay, now back to solving T(k, n) ...
        // Let's get some absurdities out of the way:
        // * If k == 0 (no eggs left to test with), then the answer is infinity so let's try
        //   very hard to never allow that state to occur.
        // * If k == 1 (a single egg left), then we need to test every single floor (from 1 to n)
        //   because we must calculate f with probability 1.0 so the answer in this case is n.
        // * If N == 0, then the answer is zero!
        //
        // Let's now assume k >= 2 (at least two eggs).
        // We should aim to reduce our search space size but more importantly we should find
        // the optimal floor to drop an egg from. You may be tempted to drop it from the n/2 floor
        // but this is not necessarily the floor that will yeild the correct answer (optimal solution).
        //
        // Assume we drop it from the i-th floor (1 <= i <= n), then the following occurs:
        // * We use-up a move (so our answer should be augmented by 1).
        // * The problems are sub-divided:
        //   * If the k-th egg break:       T(k-1, i-1)
        //   * If the k-th does NOT break:  T(k,   n-i)
        //   Because we want to know the answer with probability 1.0, we need to choose the
        //   maximum betweem the above two cases; therefore, if we did decide to cut at the
        //   i-th floor, we must do:
        //      T(k, n) <-- max( T(k-1, i-1), T(k, n-i) )
        //   However, how do we know which floor i should be?! We want the "best" floor, so
        //   we need the floor that minimizes the above max-stmt! I.e., our problem now becomes
        //   a mini-max calculation:
        //      T(k, n) := 1 + min(max(T(k-1, i-1), T(k, n-i))), for i in {1, ..., n}
        //
        // We can now solve the problem with the above discovery; as-is, we are left with a
        // algorithm with a time-complexity of K*N^2 (but this can be improved!).
        //
        // Define:
        //      T1 := T(k-1, i-1)
        //      T2 := T(k,   n-i)
        // Since, we are searching for the optimal value of i, both T1 & T2 are a function of i.
        // Most importantly, T1 is an increasing function of i while T2 is a decreasing function of i.
        // Now, define:
        //      T3 := max(T1, T2)
        // As we vary the value of i, the graph shape of T3 will be like a "cone" in the sense that
        // the optimal value of i will be the lowest point in this graph (denote this point by i*).
        // For values of i < i*, we will have T1 < T2, wheras for value of i > i* we will have T1 > T2
        // Again, this is because T1 is an increasing function while T2 is a decreasing function of
        // the same parameter -- In other words, i* is the point where T1 overtakes T2.
        //
        // Thus, the goal is to locate i*, which we can do via binary search!
        // With this, we can achieve a run-time of K*N*log(N).
        //
        // In an actual interview, I think realizing the increasing-decreasing properties of T1-T2
        // is a stretch, however, discovering the mini-max relationship should definitely be in-scope.

        private final int K, N;
        private final Integer[][] cache;

        int solve() {
            return solve(K, N);
        }

        int solve(int k, int n) {
            if (cache[k][n] != null) {
                return cache[k][n];
            }

            int result;
            if (n == 0) {
                result = 0;
            } else if (k == 0) {
                throw new RuntimeException(String.format("Invoked solve(k=%d, n=%d)",k, n));
            } else if (k == 1) {
                result = n;
            } else {
                result = binarySearchMiniMax(k, n);
            }

            cache[k][n] = result;

            return result;
        }

        int binarySearchMiniMax(int k, int n) {
            int left = 1;
            int right = n;

            while (left+1 < right) {
                int mid = (right + left) / 2;
                int t1 = solve(k-1, mid-1);
                int t2 = solve(k, n-mid);

                if (t1 < t2) {
                    left = mid;
                } else if (t1 > t2) {
                    right = mid;
                } else {
                    left = right = mid;
                }
            }

            return 1 + Math.min(Math.max(solve(k-1, left-1), solve(k, n-left)),
                                Math.max(solve(k-1, right-1), solve(k, n-right)));
        }

        Solution1(int K, int N) {
            this.K = K;
            this.N = N;

            // TimeComplexity: O(K * N)
            cache = new Integer[K+1][N+1];
            for (int k = 0; k <= K; k++) {
                for (int n = 0; n <= N; n++) {
                    cache[k][n] = null;
                }
            }
        }

        static Solution1 newSolver(int k, int n) {
            return new Solution1(k, n);
        }
    }
}
