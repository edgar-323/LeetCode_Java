class Solution {
    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        return Solution1.newSolver(target, startFuel, stations).solve();
    }

    static class Solution1 {

        static Solution1 newSolver(int target, int startFuel, int[][] stations) {
            return new Solution1(target, startFuel, stations);
        }

        int solve() {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        Solution1(int target, int startFuel, int[][] stations) {}
    }
}
