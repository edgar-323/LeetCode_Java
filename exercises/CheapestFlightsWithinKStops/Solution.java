class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        return Solution1.findCheapestPrice(n, flights, src, dst, k);
    }

    private static class FlightTo {
        private final int dst;
        private final int cost;

        public int getDst() { return dst; }
        public int getCost() { return cost; }

        FlightTo(int dst, int cost) {
            this.dst = dst;
            this.cost = cost;
        }

        static FlightTo create(int dst, int cost) {
            return new FlightTo(dst, cost);
        }
    }

    /**
     * TimeComplexity:  O(N^2 * F)
     * SpaceComplexity: O(N * (N + K))
     *
     * Where:
     *      N = number of airports
     *      F = number of possible airport-to-airport flights {i.e., size(flights)}
     *      K = Number of possible stops.
     */
    private static class Solution1 {

        private static final int INVALID_COST = -1;

        private int N;
        private int K;
        private int src;
        private int dst;
        private List<List<FlightTo>> adjList;
        private List<List<Integer>> cache;

        static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
            return createSolver(n, flights, src, dst, k).solve();
        }

        int solve() {
            // Initial state is to start from src city with the ability to make K pit stops.
            return solve(src, K);
        }

        int solve(int currCity, int k) {
            if (currCity == dst) {
                // Arrived to destination city. The remainder cost is zero.
                return 0;
            }
            if (k == -1) {
                // This is not the destination city but we cannt make any more pit stops along
                // the way. Thus, arriving to destination city from this state is impossible.
                return INVALID_COST;
            }
            if (cacheHit(currCity, k)) {
                return getCacheValue(currCity, k);
            }
            // Go through all adjacent cities and see if we can reach the destination city
            // along this neigboring city. If so, pick the one with the minimum cost.
            int minCost = INVALID_COST;
            for (FlightTo flightTo : adjList.get(currCity)) {
                int cost = solve(flightTo.getDst(), k - 1);
                if (cost == INVALID_COST) {
                    // We were not able to arrive at destination city from this neighbor.
                    continue;
                }
                // There is a path to destination city along this neigbor; let's also add the cost
                // to travel to this neighbor to begin with.
                cost += flightTo.getCost();
                // If our min-cost was not set, then choose this as the new minCost; otherwise
                // use the min() function to see if we can improve minCost.
                minCost = (minCost == INVALID_COST) ? cost : Math.min(minCost, cost);
            }
            return setCacheValue(currCity, k, minCost);
        }

        static Solution1 createSolver(int n, int[][] flights, int src, int dst, int k) {
            return new Solution1(n, flights, src, dst, k);
        }

        private Solution1(int n, int[][] flights, int src, int dst, int k) {
            N = n;
            K = k;
            this.src = src;
            this.dst = dst;

            initializeAdjList(flights);
            initializeCache();
        }

        private void initializeAdjList(int[][] flights) {
            adjList = new ArrayList<>(N);
            for (int i = 0; i < N; i++) {
                adjList.add(new ArrayList<>());
            }
            for (int[] flight : flights) {
                int source = flight[0];
                int destination = flight[1];
                int cost = flight[2];
                adjList.get(source).add(FlightTo.create(destination, cost));
            }
        }

        private void initializeCache() {
            cache = new ArrayList<>(N);
            for (int i = 0; i < N; i++) {
                cache.add(new ArrayList<>(K+1));
                for (int j = 0; j <= K; j++) {
                    cache.get(i).add(null);
                }
            }
        }

        private boolean cacheHit(int i, int j) {
            return cache.get(i).get(j) != null;
        }

        private int getCacheValue(int i, int j) {
            return cache.get(i).get(j);
        }

        private int setCacheValue(int i, int j, int v) {
            cache.get(i).set(j, v);
            return v;
        }
    }
}
