class Solution {
    public int minimumFinishTime(int[][] tires, int changeTime, int numLaps) {
        return Solution1.newSolver(tires, changeTime, numLaps).solve();
    }

    static class Solution1 {
        int solve() {
            throw new RuntimeException("NOT_IMPLEMENTED");
        }
        static Solution1 newSolver(int[][] tires, int changeTime, int numLaps) {
            return new Solution1(tires, changeTime, numLaps);
        }
        Solution1(int[][] tires, int changeTime, int numLaps) {}
    }
}
