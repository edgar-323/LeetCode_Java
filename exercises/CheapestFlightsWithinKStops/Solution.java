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

    private static class Solution1 {
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
            return solve(src, K);
        }

        int solve(int currCity, int k) {
            if (currCity == dst) {
                return 0;
            }
            if (k == 0) {
                return -1;
            }
            if (cacheHit(currCity, k)) {
                return getCacheValue(currCity, k);
            }
            int minCost = -1;
            for (FlightTo flightTo : adjList.get(currCity)) {
                int cost = solve(flightTo.getDst(), k - 1);
                if (cost == -1) {
                    continue;
                }
                cost += flightTo.getCost();
                minCost = (minCost == -1) ? cost : Math.min(minCost, cost);
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

            initAdjList(flights);
            initCache();
        }

        private void initAdjList(int[][] flights) {
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

        private void initCache() {
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
            return cache.get(i).set(j, v);
        }
    }
}
