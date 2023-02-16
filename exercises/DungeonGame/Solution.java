class Solution {
    public int calculateMinimumHP(int[][] dungeon) {
        return Solution1.solve(dungeon);
    }

    static class Solution1 {
        /**
         * THOUGHTS
         * State-representation:
         *      minHealth(i,j) := the minimum initial health required to reach
         *                        entry (i,j) starting from entry (0,0).
         * Let:
         *      minHealth(i,j) := (health, remaining)
         *                        where health is the min-health to reach
         *                        entry (i,j) while `remaining` refers to
         *                        the remaining health after leaving entry
         *                        (i,j).
         *
         * WRONG! IT'S NATURAL TO ASSUME WE SHOULD SOLVE IT THIS WAY BUT
         * IT IS ACTUALLY AN INCRORRECT APPROACH.
         *
         * I had a feeling the above representation would be difficult to
         * acquire a correct expansion for. And it was true; I noted down
         * a counter argument where the expansion I though would work broke
         * down. I think the right way to use the above representation would
         * require propagation of all path info which is nasty and likely
         * intractable.
         * Then I thought about going from the bottom-right room to the to
         * the top-left room and realized that we didn't need to think about
         * how much we have left over if we do it this way.
         * I think there is a more formal explanation in the solutions section
         * as why it's not possible to go from the top-left to the bottom-right.
         *
         * That said, we will use the representation:
         *      minHealth(i,j) := minimum health needed to EXIT entry (M-1,N-1)
         *                        by ENTERING entry (i,j).
         * Expansion details will be in the code itself -- I left plenty of
         * comments for explanations.
         */
        static int solve(int[][] dungeon) {
            final int M = dungeon.length;
            final int N = dungeon[0].length;
            // This is our dynamic programming table.
            int[][] minHealth = new int[M][N];
            // Bottom-right entry.
            minHealth[M-1][N-1] = 1;
            if (dungeon[M-1][N-1] < 0) {
                minHealth[M-1][N-1] += -dungeon[M-1][N-1];
            }
            // Bottom row.
            // We can only look towards the right entry.
            for (int j = N-2; j >= 0; j--) {
                minHealth[M-1][j] =
                    minHealthRequiredToEnterRoom(
                            /* dungeonPoints= */ dungeon[M-1][j],
                            /* minNextEntryHealth= */ minHealth[M-1][j+1]);
            }
            // Right column.
            // We can only look towards the bottom entry.
            for (int i = M-2; i >= 0; i--) {
                minHealth[i][N-1] =
                    minHealthRequiredToEnterRoom(
                            /* dungeonPoints= */ dungeon[i][N-1],
                            /* minNextEntryHealth= */ minHealth[i+1][N-1]);
            }
            // Entries excluding bottom-row and right-column.
            // We can look at both the right and the bottom entry so we
            // minimize over both possibilities.
            for (int i = M-2; i >= 0; i--) {
                for (int j = N-2; j >= 0; j--) {
                    minHealth[i][j] = Math.min(
                            minHealthRequiredToEnterRoom(
                                /* dungeonPoints= */ dungeon[i][j],
                                /* minNextEntryHealth= */ minHealth[i+1][j]),
                            minHealthRequiredToEnterRoom(
                                /* dungeonPoints= */ dungeon[i][j],
                                /* minNextEntryHealth= */ minHealth[i][j+1]));
                }
            }
            // We are ultimately interested on entry (0,0).
            return minHealth[0][0];
        }

        /**
         * The following function can be implemented in a single statement as:
         *
         *      return (dungeonPoints <  minNextEntryHealth) ? (minNextEntryHealth - dungeonPoints) : 1;
         *
         * However, I am leaving the current, larger implementation as it shows
         * my train of thought at the time that I was solving this problem.
         */
        static int minHealthRequiredToEnterRoom(int dungeonPoints, int minNextEntryHealth) {
            if (dungeonPoints >= minNextEntryHealth) {
                // There is enough points in the current room to cover health for the next room.
                // This means we only need to have 1 point to enter this room.
                return 1;
            }
            if (dungeonPoints < 0) {
                // There is a negative number of points in the current room which means that in
                // order to enter it, we will need to have at least as many negative points plus
                // the points needed to enter the next room (which is strictly greater than the
                // number of required points in that next room).
                return minNextEntryHealth - dungeonPoints;
            }
            return minNextEntryHealth - dungeonPoints;
        }
    }
}
