class Solution {
    public int minimumFinishTime(int[][] tires, int changeTime, int numLaps) {
        return Solution2.newSolver(tires, changeTime, numLaps).solve();
    }

    /**
     * THOUGHTS:
     * There is no official solution for this problem.
     * Furthermore, the acclaimed runtimes for this problem are all over the place.
     * Noticed: { O(N), O(N*logN), O(N^2) }.
     *
     * We have to sum over the number of laps: `numLaps`.
     * What information do we need in order to figure out how much the i-th lap
     * will elapse?
     *      1. The current tire
     *      2. The number of successive laps that have occurred with the current tire.
     * With this info, we can calculate the time for the i-th lap.
     * Okay, so our state can be represented as:
     *      (lap, tireIndex, successiveLaps)
     * How do we decide whether we should reset to a new tire or if we should continue
     * to use the same tire?
     *      minTime(lap, tire, successiveLaps) := minimum race time from current lap to
     *                                            numLaps given that we used tires[tire]
     *                                            successiveLaps.
     *          f, r := tires[tire]
     *          lapTime := f * r^(successiveLaps - 1)
     *          futureLapTimes := min(
     *              minTime(lap+1, tire, successiveLaps+1),
     *              changeTime + minTime(lap+1, j, 1) for all j tires)
     *
     *          return lapTime + futureLapTimes
     *
     * What would be the runtime of the above scheme? Letting N := tires.length, we get:
     *      O(numLaps * N * numLaps * N)
     *      O(numLaps^2 * N^2)
     *
     * Can we do better?
     * Can we do some kind of greedy approach where we tag states with a timestamp, which
     * is just the time elapsed to reach that state.
     * Then we can shove initial states into a priority-queue that places states with
     * smallest timestamp at the top.
     * Once you reach a state that is also tagged with being on # lap: `numLaps`,
     * then you know that state has reached the end of the race.
     * The first state that reaches the end should have the smallest timestamps correct?
     * Can it be possible that some later state will reacht the end of the race with a
     * smaller timestamp? Well, this might occur if there is another state whose current
     * timestamp is smaller and whose descendants will have a much smaller time stamp than
     * the final state, however, the queue will pop states with smallest timestamps first!
     * This is a property of priority queues so I don't know that it can happen!
     * Does this prove we can use this scheme? Also why is this scheme better than the
     * previous DFS / Top-Down-DP approach?
     * It appears to be better due to the fact that we prune a large sector of the search
     * space once we arrive at the finishing lap!
     */

    static class Solution1 {
        /**
         * NOTE: THIS SOLUTION DOES TLE!
         */
        private final int[][] tires;
        private final int changeTime;
        private final int numLaps;

        int solve() {
            PriorityQueue<Node> queue =
                new PriorityQueue<>(
                        /* initialCapacity= */ tires.length, 
                        /* comparator= */ (a, b) -> {
                            int tsA = a.getTimestamp();
                            int tsB = b.getTimestamp();
                            if (tsA != tsB) {
                                return Integer.compare(tsA, tsB);
                            }
                            int lapA = a.getLap();
                            int lapB = b.getLap();
                            return -Integer.compare(lapA, lapB);
                        });

            queue.addAll(produceInitialNodes());

            while (!queue.isEmpty()) {
                Node node = queue.poll();
                if (node.getLap() == numLaps) {
                    return node.getTimestamp();
                }
                queue.addAll(node.produceChildNodes());
            }

            throw new RuntimeException("IMPOSSIBLE!");
        }

        List<Node> produceInitialNodes() {
            List<Node> nodes = new ArrayList<>(/* initialCapacity= */ tires.length);
            for (int tire = 0; tire < tires.length; tire++) {
                nodes.add(
                        Node.create(
                            /* timestamp= */ 0,
                            /* lap= */ 1,
                            /* tire= */ tire,
                            /* successiveLaps= */ 0,
                            tires,
                            changeTime));
            }

            return nodes;
        }

        static Solution1 newSolver(int[][] tires, int changeTime, int numLaps) {
            return new Solution1(tires, changeTime, numLaps);
        }

        Solution1(int[][] tires, int changeTime, int numLaps) {
            this.tires = tires;
            this.changeTime = changeTime;
            this.numLaps = numLaps;
        }

        static class Node {
            private final int[][] tires;
            private final int changeTime;

            private final int timestamp;
            private final int lap;
            private final int tire;
            private final int successiveLaps;

            int calculateLapTime() {
                int f = tires[tire][0];
                int r = tires[tire][1];
                int pow = (int) Math.pow(r, successiveLaps);
                return f * pow;
            }

            List<Node> produceChildNodes() {
                List<Node> childNodes = new ArrayList<>(/* initialCapacity= */ 1 + tires.length);

                int nextTimestamp = getTimestamp() + calculateLapTime();

                childNodes.add(
                        Node.create(
                            nextTimestamp,
                            lap+1,
                            tire,
                            successiveLaps+1,
                            tires,
                            changeTime));
                for (int newTire = 0; newTire < tires.length; newTire++) {
                    childNodes.add(
                            Node.create(
                                nextTimestamp + changeTime,
                                lap+1,
                                newTire,
                                /* successiveLaps= */ 0,
                                tires,
                                changeTime));
                }

                return childNodes;
            }

            int getTimestamp() { return timestamp; }

            int getLap() { return lap; }

            static Node create(int ts, int lap, int tire, int successiveLaps, int[][] tires, int changeTime) {
                return new Node(ts, lap, tire, successiveLaps, tires, changeTime);
            }

            Node(int timestamp, int lap, int tire, int successiveLaps, int[][] tires, int changeTime) {
                this.timestamp = timestamp;
                this.lap = lap;
                this.tire = tire;
                this.successiveLaps = successiveLaps;
                this.tires = tires;
                this.changeTime = changeTime;
            }
        }
    }

    static class Solution2 {
        /**
         * ALL CREDIT GOES TO rupakk:
         *  https://leetcode.com/problems/minimum-time-to-finish-the-race/solutions/1804216/easy-to-understand-clean-c-code-dp-greedy/
         *
         *
         * TimeComplexity   :   O(numLaps^2 + numLaps*N)
         * SpaceComplexity  :   O(numLaps)
         *
         * Where:
         *      N = tires.length
         */

        private final int[][] tires;
        private final int changeTime;
        private final int numLaps;

        int solve() {
            // minTime[lap] := min times needed to complete `lap` laps.
            long[] minTime = new long[numLaps+1];
            for (int lap = 0; lap <= numLaps; lap++) {
                minTime[lap] = Long.MAX_VALUE;
            }

            minTime[0] = 0; // Min time to complete 0 laps is 0.

            computeMinTimeWithoutTireChange(minTime);
            computeMinTimeWithTireChange(minTime);

            return (int) minTime[numLaps];
        }

        void computeMinTimeWithoutTireChange(long[] minTime) {
            for (int[] tire : tires) {
                long f = tire[0];
                long r = tire[1];
                minTime[1] = Math.min(minTime[1], f); // Base case.
                long lastLapTime = f;
                long totalTime = f;
                int lap = 2;
                while (lastLapTime * r < f + changeTime && lap <= numLaps) {
                    // we can prove that this loop will not run many times because we are
			        // moving in powers of r and even a small r like 2 will terminate this
			        // loop under 20 iterations.
                    long currLapTime = lastLapTime * r;
                    totalTime += currLapTime;
                    minTime[lap] = Math.min(minTime[lap], totalTime);
                    lastLapTime = currLapTime;
                    lap++;
                }
            }
        }

        void computeMinTimeWithTireChange(long[] minTime) {
            for (int lap = 1; lap <= numLaps; lap++) {
                for (int smallerLap = 1; smallerLap <= lap; smallerLap++) {
                    // Bread & Butter.
                    minTime[lap] = Math.min(minTime[lap], minTime[smallerLap] + changeTime + minTime[lap - smallerLap]);
                }
            }
        }

        static Solution2 newSolver(int[][] tires, int changeTime, int numLaps) {
            return new Solution2(tires, changeTime, numLaps);
        }

        Solution2(int[][] tires, int changeTime, int numLaps) {
            this.tires = tires;
            this.changeTime = changeTime;
            this.numLaps = numLaps;
        }
    }
}
