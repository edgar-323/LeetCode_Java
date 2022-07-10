class Solution {

    // The following limits are given in the description of the problem.
    // Since they are small numbers (computationally speaking), we can build
    // data-structures with these limits in place, if needed.
    private static final int MAX_CHOOSABLE_INTEGER = 20;
    private static final int MAX_DESIRED_TOTAL = 300;

    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        return Solution3.canIWin(maxChoosableInteger, desiredTotal);
    }

    private static int maxSum(int n) {
        return (n * (n + 1)) / 2;
    }

    private static class Solution1 {
        // NOTE: THIS SOLUTION TIMES OUT.
        //
        // Naive approach.
        // Whatever player1 does, we have to assume that player2 will react optimally.
        // With that said, we can also assume that player1 will play optimally.
        // In order for player1 to play optimally, we will employ a strategy that searches
        // the entire solution space for the next best move.
        // Since this strategy exhausts all possibilities (and picks the best one), it must be optimal.
        // This means that we can assume that player2 also uses this strategy.
        // What does a "state" look like in this search space?
        // Let's try:
        //      currentPlayerCanWin(availableIntegers, runningTotal) := true if current player can win, given that
        //                                                              runningTotal < desiredTotal and that we can
        //                                                              choose numbers from availableIntegers and
        //                                                              that it is the current player's turn to choose
        //                                                              the next integer.

        static boolean canIWin(int maxChoosableInteger, int desiredTotal) {
            if (maxSum(maxChoosableInteger) < desiredTotal) {
                return false;
            }
            boolean[] availableIntegers = new boolean[maxChoosableInteger + 1];
            for (int i = 1; i <= maxChoosableInteger; i++) {
                availableIntegers[i] = true;
            }

            return currentPlayerCanWin(availableIntegers, 0, desiredTotal);
        }

        static boolean currentPlayerCanWin(
                boolean[] availableIntegers,
                int runningTotal,
                int desiredTotal) {
            // Let's try to see if current player can pick an integer than can force a win.
            for (int i = 1; i < availableIntegers.length; i++) {
                if (availableIntegers[i]) {
                    // Let's choose i.
                    //
                    // If we reached our goal (by choosing i), then current player won.
                    if (runningTotal + i >= desiredTotal) {
                        return true;
                    }
                    // If, by choosing i, we caused the other player NOT to win, then it must mean
                    // that the current player won.
                    availableIntegers[i] = false;
                    boolean otherPlayerWon =
                        currentPlayerCanWin(availableIntegers, runningTotal + i, desiredTotal);
                    availableIntegers[i] = true;
                    if (!otherPlayerWon) {
                        return true;
                    }
                }
            }
            // None of the available integers can force a win for current player.
            return false;
        }
    }

    private static class Solution2 {
        // NOTE: THIS SOLUTION IS CORRECT BUT IT ALSO TIMES OUT (MADNESS)!
        //
        //
        // Notice that while Solution1 appears to be algorithmically correct, we cannot "cache" the results
        // of a given state due to the object type of the `availableIntegers` (an array).
        // How can we address this?
        // We know that MAX_CHOOSABLE_INTEGER is computationally small.
        // This means that we could potentially convert this array into a string representation and cache it
        // so that we can "remember" a given state.
        // But an even neater trick is that since MAX_CHOOSABLE_INTEGER < 32, so we can actually use an integer
        // to represent the available chosen numbers!
        // I.e., the integer will have the i-th bit set iff `i` is available.
        // This is a lot more efficient than converting an array into a hashable representation.
        // With this, a state will be represented by two integers (x,y) where if we define
        // k := MAX_CHOOSABLE_INTEGER, then we have:
        //      x <= 2^k - 1
        //      y <= 1 + 2 + ... + k = k * (k + 1) / 2
        // If we use arrays to cache our states, then it will be a (2^k - 1) X (k*(k+1)/2) sized array.
        // Given the parameters of this problem, this could get as big as a 1048575 X 210 array which is
        // upto 220200750 units of memory (not a small number)!
        // Instead, let's use a hashtable<int,int, boolean> to cache a state so that, in the cases where we find a
        // solution fairly quick, we will hopefully have searched a very small area of the search space,
        // meaning we would save some memory (though we should put a number on that -- we should also investigate
        // whether there exist hueristics that can drive us to a solution in a quicker fashion).

        private final int maxChoosableInteger;
        private final int desiredTotal;
        private final Map<Integer, Map<Integer, Boolean>> cache;

        /**
         * TimeComplexity:  O(N*(N+1)/2 * (2^N - 1)) ==> O(N^2 * 2^N)
         * SpaceComplexity: O(N^2 * 2^N)
         *
         * Where:
         *      N = maxChoosableInteger
         */
        static boolean canIWin(int maxChoosableInteger, int desiredTotal) {
            if (maxSum(maxChoosableInteger) < desiredTotal) {
                return false;
            }
            Solution2 solution = new Solution2(maxChoosableInteger, desiredTotal);

            return solution.player1CanWin();
        }

        private boolean player1CanWin() {
            int availableIntegers = 0;
            for (int i = 1; i <= maxChoosableInteger; i++) {
                availableIntegers = makeIntegerAvailable(availableIntegers, i);
            }

            return currentPlayerCanWin(availableIntegers, /* runningTotal= */ 0); // Can player1 win.
        }

        private boolean currentPlayerCanWin(int availableIntegers, int runningTotal) {
            if (inCache(availableIntegers, runningTotal)) {
                return getCacheValue(availableIntegers, runningTotal);
            }

            for (int i = 1; i <= maxChoosableInteger; i++) {
                if (integerIsAvailable(availableIntegers, i)) {
                    if (runningTotal + i >= desiredTotal) {
                        return setCacheValue(availableIntegers, runningTotal, true);
                    }
                    boolean nextPlayerWon =
                        currentPlayerCanWin(chooseInteger(availableIntegers, i), runningTotal + i);
                    if (!nextPlayerWon) {
                        return setCacheValue(availableIntegers, runningTotal, true);
                    }
                }
            }

            return setCacheValue(availableIntegers, runningTotal, false);
        }

        private Solution2(int maxChoosableInteger, int desiredTotal) {
            this.maxChoosableInteger = maxChoosableInteger;
            this.desiredTotal = desiredTotal;
            this.cache = new HashMap<>();
        }

        private boolean inCache(int availableIntegers, int runningTotal) {
            return cache.containsKey(availableIntegers)
                && cache.get(availableIntegers).containsKey(runningTotal);
        }

        private boolean setCacheValue(int availableIntegers, int runningTotal, boolean result) {
            if (!cache.containsKey(availableIntegers)) {
                cache.put(availableIntegers, new HashMap<>());
            }
            cache.get(availableIntegers).put(runningTotal, result);

            return result;
        }

        private boolean getCacheValue(int availableIntegers, int runningTotal) {
            return cache.get(availableIntegers).get(runningTotal);  // Assumes inCache(availableIntegers, runningTotal) is true.
        }

        private static boolean integerIsAvailable(int availableIntegers, int bit) {
            int value = availableIntegers & (1 << bit);
            return value != 0;
        }

        private static int chooseInteger(int availableIntegers, int bit) {
            return availableIntegers ^ (1 << bit);
        }

        private static int makeIntegerAvailable(int value, int bit) {
            return value | (1 << bit);
        }
    }

    static private class Solution3 {
        // Why does Solution2 time out?!
        // Because the second dimension that we were caching (runningTotal) was redundant!
        // Remember, we were caching (availableIntegers, runningTotal).
        // At any point in the computation, you are able to derive runningTotal from availableIntegers.
        // To do this, you run through the availableIntegers and add up the integers that have already
        // been chosen!
        // Therefore, for some value of availableIntegers`, you will always arrive at the same value
        // of runningTotal`.
        // Let's rewrite the solution without the second caching-parameter.
        // At this point it may be easier to use a fixed sized array.

        private final int maxChoosableInteger;
        private final int desiredTotal;
        private final Boolean[] cache;

        private static boolean canIWin(int maxChoosableInteger, int desiredTotal) {
            if (maxSum(maxChoosableInteger) < desiredTotal) {
                return false;
            }
            Solution3 solution = new Solution3(maxChoosableInteger,  desiredTotal);

            return solution.player1CanWin();
        }

        private boolean player1CanWin() {
            int availableIntegers = 0;
            for (int i = 1; i <= maxChoosableInteger; i++) {
                availableIntegers = makeIntegerAvailable(availableIntegers, i);
            }

            return currentPlayerCanWin(availableIntegers, /* runningTotal= */ 0); // Can player1 win.
        }

        private boolean currentPlayerCanWin(int availableIntegers, int runningTotal) {
            if (inCache(availableIntegers)) {
                return getCacheValue(availableIntegers);
            }

            for (int i = 1; i <= maxChoosableInteger; i++) {
                if (integerIsAvailable(availableIntegers, i)) {
                    if (runningTotal + i >= desiredTotal) {
                        return setCacheValue(availableIntegers, true);
                    }
                    boolean nextPlayerWon =
                        currentPlayerCanWin(chooseInteger(availableIntegers, i), runningTotal + i);
                    if (!nextPlayerWon) {
                        return setCacheValue(availableIntegers, true);
                    }
                }
            }

            return setCacheValue(availableIntegers, false);
        }

        private boolean inCache(int availableIntegers) {
            return cache[availableIntegers] != null;
        }

        private boolean setCacheValue(int availableIntegers, boolean result) {
            cache[availableIntegers] = result;
            return result;
        }

        private boolean getCacheValue(int availableIntegers) {
            return cache[availableIntegers];
        }

        private static boolean integerIsAvailable(int availableIntegers, int bit) {
            int value = availableIntegers & (1 << bit);
            return value != 0;
        }

        private static int chooseInteger(int availableIntegers, int bit) {
            return availableIntegers ^ (1 << bit);
        }

        private static int makeIntegerAvailable(int value, int bit) {
            return value | (1 << bit);
        }

        private Solution3(int maxChoosableInteger, int desiredTotal) {
            this.maxChoosableInteger = maxChoosableInteger;
            this.desiredTotal = desiredTotal;
            cache = new Boolean[1 << (maxChoosableInteger + 1)];
            for (int i = 0; i < cache.length; i++) {
                cache[i] = null;
            }
        }
    }
}
