class Solution {
    public int minDistance(String word1, String word2) {
        return Solution1.minDistance(word1, word2);
    }

    private static class Solution1 {
        // Thoughts:
        // Let's work with word1[i:] and word2[j:].
        // Define:
        //  cost[i,j] = cost to transform word1[i:] into word2[j:].
        // Well first thing we want to do is the character transformation:
        //  word1[i] --> word2[j]
        // We have the following options:
        //  1. Insert word2[j] BEFORE word1[i]
        //     In this case, we now have to still account for word1[i:] and word2[(j+1):].
        //  2. Delete word1[i].
        //     Hmm, we no have to account for word1[(i+1):] and word2[(j+1):]
        //  3. Replace word1[i] with word2[j].
        //     We have to account for word1[(i+1):] and word2[(j+1):]
        // In the three above cases we must add 1 to each case since each operation has
        // unit cost.
        // There is one more case that is implicit:
        //  4. If word1[i] == word2[j]
        //     We have to account for word1[(i+1):] and word2[(j+1):]
        //     This has a zero cost because we didn't have to do any operations!
        //
        // More additional corner cases:
        //  5. If i == word1.length() and j == word2.length()
        //     Then cost is 0 (no additonal transformations needed)
        //  6. If i == word1.length() and j < word2.length()
        //     We only have the option to INSERT word2[j] and then have to account for
        //     word1[i:] AND word2[(j+1):]
        //  7. If i < word1.length() and j == word2.length()
        //     We only have option to DELETE word1[i] and then have to account for
        //     word1[(i+1):] and word2[j:]

        private final String word1;
        private final String word2;

        static int minDistance(String word1, String word2) {
            throw new RuntimeException("NOT IMPLEMENTED");
        }

        private static Solution1 newSolver(String word1, String word2) {
            return new Solution1(word1, word2);
        }

        private Solution1(String word1, String word2) {
            this.word1 = word1;
            this.word2 = word2;
        }
    }
}
