class Solution {

    private static int POSITION = 0;
    private static int FUEL = 1;

    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        return Solution2.newSolver(target, startFuel, stations).solve();
    }

    /**
     * TimeComplexity:  O(N*log(N))
     * SpaceComplexity: O(N)
     */
    static class Solution1 {

        private final int target;
        private final int startFuel;
        private final int[][] stations;

        static Solution1 newSolver(int target, int startFuel, int[][] stations) {
            return new Solution1(target, startFuel, stations);
        }

        int solve() {
            int N = stations.length;

            int range = startFuel;
            int minStops = 0;
            int i = 0;

            // We will use this queue in order to store available
            // fuel from gas stations that we saw at one point in
            // time.
            // When we do decide to use the fuel available from some
            // previously encountered station, we will choose the ones
            // that offer the most gas (this is optimal).
            PriorityQueue<Integer> maxHeap =
                new PriorityQueue<>(Comparator.reverseOrder());

            // Let's see if we can reach each station with the
            // current gas (which will get updated when needed).
            // Although we will preemptively break from looping
            // if we realize that we can reach our target with
            // the current gas.
            for (int[] station : stations) {
                if (range >= target) {
                    // If we can reach the target with the current
                    // stop count then we should stop trying to reach
                    // the next station.
                    break;
                }
                // While we cannot reach the current station, we will
                // pop off the gas stations that we've already seen
                // from the queue (choosing the stations with the most
                // gas first).
                while (range < station[POSITION]) {
                    if (maxHeap.isEmpty()) {
                        // No previous gas stations were able to provide
                        // enough gas to reach this station.
                        return -1;
                    }
                    range += maxHeap.poll();
                    minStops++;
                }
                // Insert the fuel that this station was able to provide
                // inro the queue.
                maxHeap.offer(station[FUEL]);
            }

            // We already reached all stations but it may be the case that
            // we still cannot reach our target with the current fuel.
            // If this is the case, then we will start looking at stations
            // that we previously saw but did not use.
            while (range < target) {
                if (maxHeap.isEmpty()) {
                    // Stations that we did encounter did not offer enough
                    // gas to reach our target.
                    return -1;
                }
                range += maxHeap.poll();
                minStops++;
            }

            return minStops;
        }

        Solution1(int target, int startFuel, int[][] stations) {
            this.target = target;
            this.startFuel = startFuel;
            this.stations = stations;
        }
    }

    static class Solution2 {

        private final int target;
        private final int startFuel;
        private final int[][] stations;
        private final int N;
        private final Integer[][] cache;

        static Solution2 newSolver(int target, int startFuel, int[][] stations) {
            return new Solution2(target, startFuel, stations);
        }

        int solve() {
            int minStops = -1;
            for (int k = N; k >= 0; k--) {
                if (canReach(k)) {
                    minStops = k;
                }
            }

            return minStops;
        }

        // Can we reach our target if we use exactly k stations?
        boolean canReach(int k) {
            if (N == 0) {
                return startFuel >= target;
            }
            return maximumRange(N-1, k) >= target;
        }

        // What is the maximum range we can reach if we consider
        // the stations[0:i] using exactly k stations?
        int maximumRange(int i, int k) {
            if (cache[i][k] != null) {
                return cache[i][k];
            }

            int maxRange;
            if (i == 0) {
                // Well, we have exactly one station available.
                // So k MUST be in either 0 or 1.
                if (k > 1) {
                    maxRange = Integer.MIN_VALUE;
                } else {
                    maxRange = startFuel;
                    if (k == 1 && startFuel >= stations[0][POSITION]) {
                        maxRange += stations[0][FUEL];
                    }
                }
            } else if (k > i+1) {
                // We have i+1 choices of stations to choose from.
                // This means k must be in range [0,i+1].
                maxRange = Integer.MIN_VALUE;
            } else if (k == 0) {
                // This means that we shouldn't use any of stations[0:i].
                // So the farthest we can go is the initial fuel.
                maxRange = startFuel;
            } else {
                // In this case, we can decide whether or not to use
                // station[i]; we would want the maximum of these two
                // decisions.
                // However, how do we know if we can even reach the
                // position of stations[i] ?
                // Well, we would have to see how far we can get by
                // considering only stations[0:(i-1)] using k-1 stops.
                // Remember, that if we decide NOT to use stations[i],
                // then we have to make exactly k stops using only
                // stations[0:(i-1)].
                int rangeUsingLessStationsAndStops =
                    maximumRange(i-1, k-1);
                if (rangeUsingLessStationsAndStops >= stations[i][POSITION]) {
                    maxRange = Math.max(
                            rangeUsingLessStationsAndStops + stations[i][FUEL],
                            maximumRange(i-1, k));
                } else {
                    maxRange = maximumRange(i-1, k);
                }
            }

            cache[i][k] = maxRange;
            return maxRange;
        }

        Solution2(int target, int startFuel, int[][] stations) {
            this.target = target;
            this.startFuel = startFuel;
            this.stations = stations;

            N = stations.length;
            cache = new Integer[N+1][N+1];
            for (int i = 0; i <= N; i++) {
                for (int j = 0; j <= N; j++) {
                    cache[i][j] = null;
                }
            }
        }
    }
}
