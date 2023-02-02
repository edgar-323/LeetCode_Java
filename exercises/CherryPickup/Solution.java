class Solution {
    public int cherryPickup(int[][] grid) {
        return Solution1.newSolver(grid).solve();
    }

    /**
     * THOUGHTS:
     * I actually don't know how to solve this problem without
     * modifying the grid. In other words, I would solve this
     * problem by "backtracking".
     * Let's assess and quantify that solution anyways:
     * Journey: (0, 0) --> (n-1, n-1)
     *  Determine whether we move down or right:
     *  In either case, if the direction had a cherry, then
     *  we would consume it, however, this means that we have
     *  to "remember" that this cherry is gone in case we tried
     *  to re-consume on the return trip.
     * What's the runtime of this approach?
     * I don't know because I am not caching anything since I
     * cannot really cache (unless I cache the board itself!).
     * This appears to be an exponential process.
     *
     * Are there any other potential ways to solve this?
     *  
     * UPDATE:
     * After reading through LeetCode, I can see WHY this problem
     * was labeled as "HARD":
     * The most common concept is slightly unorthodox.
     * Matter of fact, it was so unfamiliar to commentators that
     * many stated:
     *  "pray you don't receive this question during an interview"
     * I personally find that statement far-stretched, however, I
     * too wouldn't want to get caught unprepared with a problem
     * of this caliber.
     *
     * KEY-IDEA(S):
     * Notice that the difficulty of this problem comes from the
     * fact that you must "remember" a modified grid during the
     * return trip.
     * Additionally, notice that the return trip which goes from
     * (N-1, N-1) to (0, 0) using Left/Up moves is equivalent to
     * going from (0, 0) to (N-1, N-1) using Down/Right moves.
     * I.e, it's symmetrical to a second trip down-right, since
     * you can technically reverse the trip you took back and
     * achieve a down-right trip.
     * With this in mind, you can start TWO SIMULTANEOUS trips
     * from (0, 0) to (N-1, N-1) while ensuring that both trips
     * move at the same rate (i.e., both trips advance at together).
     * With this scheme, we won't need to revisit any previous
     * position of the grid which means we don't have to "remember"
     * our previous moves! (It is, however, possible for both trips
     * to intercept at some point, so we need to watch for this).
     * Hence, a state can be represented by: ((r1, c1), (r2, c2)).
     * |
     * *-> One more detail that will further optimize our algorithm:
     *     We mentioned earlier that both trips will progress at the
     *     same rate. Namely, one step at a time for both trips,
     *     either downwards or towards the right.
     *     This gives rise to the following invariant which holds
     *     true at any point in time:
     *          r1 + y1 = r2 + c2   ===> c2 = r1 + y1 - r2
     *     This means that we can always derive c2 from the other
     *     parameters which implies that we can reduce our state
     *     representation to: (r1, y1, r2).
     *     This observation will allow us acquire a runtime of O(N^3).
     */

    static class Solution1 {
        private final Integer[][][] cache;
        private final int[][] grid;
        private final int N;

        int solve() {
            // Calculate the max number of cherries found when both trips
            // begin at the origin: (0, 0).
            int maxCherries = solve(/* r1= */ 0, /* c1= */ 0, /* r2= */ 0);

            // Note that it is possible that the destination is not reachable
            // (for example, when a sequence of thorns blocks all paths).
            // In this case, maxCherries will have a negative value due to the
            // way in which we penalize paths that encounter thorns.
            // In those cases, we should return zero as it is not possible to
            // acquire ANY cherries.
            return Math.max(maxCherries, 0);
        }

        int solve(int r1, int c1, int r2) {
            // Derive c2 from (r1, c1, r2):
            int c2 = r1 + c1 - r2;

            // If we stepped outside of the grid or if we encountered thorns (blocked path)
            // then we chose a path the won't allow us to ever reach our destination.
            // In this case, we will return -inifinity in order to penalize this path-choice.
            if (!isValidState(r1, c1, r2, c2) || hasThorns(r1, c1) || hasThorns(r2, c2)) {
                return Integer.MIN_VALUE;
            }

            // Check for a cache hit.
            if (cache[r1][c1][r2] != null) {
                return cache[r1][c1][r2];
            }

            // If trip1 arrived at destination, it implies that trip2 also arrived at the
            // destination due to the invariant:
            //      r1+c1 = r2+c2 ==> 2*(N-1) = r2+c2 ==> r2 = c2 = N-1.
            // So we will stop computation here and return whatever is found at destination
            // since we cannot collect any more cherries.
            // Note that the invariant basically tells us that both trips reach the destination
            // together.
            if (reachedDestination(r1, c1)) {
                cache[r1][c1][r2] = grid[r1][c1];
                return grid[r1][c1];
            }
            if (reachedDestination(r2, c2)) {
                cache[r1][c1][r2] = grid[r2][c2];
                return grid[r2][c2];
            }

            // The number of cherries we can acquire locally (i.e., not exploring downstream
            // states) are those found from the current state. However, in the case the both
            // trips are currently at the same positions, we need to ensure that we do not
            // over-count.
            int cherries =
                (r1 == r2 && c1 == c2) ? grid[r1][c1] : grid[r1][c1] + grid[r2][c2];

            // Additional cherries will come from our choice of how we proceed with trip1
            // and trip2. We maximize over those choices.
            cherries += max(
                    solve(r1+1, c1,   r2+1),    // DOWN,  DOWN
                    solve(r1,   c1+1, r2  ),    // RIGHT, RIGHT
                    solve(r1+1, c1,   r2  ),    // DOWN,  RIGHT
                    solve(r1,   c1+1, r2+1));   // RIGHT, DOWN

            // Record answer in the cache and return result.
            cache[r1][c1][r2] = cherries;
            return cherries;
        }

        boolean reachedDestination(int r, int c) {
            return r == N-1 && c == N-1;
        }

        boolean hasThorns(int r, int c) {
            return grid[r][c] == -1;
        }

        boolean isValidState(int r1, int c1, int r2, int c2) {
            return
                0 <= r1 && r1 < N &&
                0 <= c1 && c1 < N &&
                0 <= r2 && r2 < N &&
                0 <= c2 && c2 < N;
        }

        static Solution1 newSolver(int[][] grid) {
            return new Solution1(grid);
        }

        Solution1(int[][] grid) {
            this.grid = grid;
            this.N = grid.length;

            cache = new Integer[N][N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    for (int k = 0; k < N; k++) {
                        cache[i][j][k] = null;
                    }
                }
            }
        }

        static int max(int a, int b, int c, int d) {
            return Math.max(Math.max(a, b), Math.max(c, d));
        }
    }
}
