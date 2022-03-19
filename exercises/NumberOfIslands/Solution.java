class Solution {
    public int numIslands(char[][] grid) {        
        return Solution1.numIslands(grid);
    }

    static class Solution1 {
        private final char[][] grid;
        private final int M;
        private final int N;

        static int numIslands(char[][] grid) {
            return new Solution1(grid).computeNumIslands();
        }

        int computeNumIslands() {
            int numIslands = 0;

            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    if (isLand(i, j)) {
                        numIslands++;
                        visit(i, j);
                    }
                }
            }

            return numIslands;
        }

        void visit(int i, int j) {
            if (!isValid(i, j) || !isLand(i, j)) {
                return;
            }
            grid[i][j] = '0';
            // TOP:
            visit(i-1, j);
            // BOTTOM:
            visit(i+1, j);
            // LEFT:
            visit(i, j-1);
            // RIGHT:
            visit(i, j+1);
        }

        boolean isValid(int i, int j) {
            return 0 <= i && i < M && 0 <= j && j < N;
        }

        boolean isLand(int i, int j) {
            return grid[i][j] == '1';
        }

        Solution1(char[][] grid) {
            this.grid = grid;
            this.M = grid.length;
            this.N = grid[0].length;
        }
    }
}
