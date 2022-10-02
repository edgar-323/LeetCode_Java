class Solution {
    public List<String> generateParenthesis(int n) {
        return Solution1.generateParenthesis(n);
    }

    /**
     * TimeComplexity: O(2^N)
     * SpaceComplexity: O(2^N)
     */
    private static class Solution1 {
        // We should start off with a simple brute-force implementation.
        //
        // Suppose we want to generate n pairs (can be sequential and/or nested)
        // pairs of paranethesis, this means that we will have n open paranthesis
        // symbols as well as n closed paranthesis symbols.
        // Hence, the size of a given pair will be 2*n.
        // 
        // At a given point, we need to know how many open parantheis symbols we've
        // used up.
        // Also, we must ensure that final output is "valid" which means we cannot
        // allow more closed paranthesis than open paranthesis symbols at any given
        // point in time.
        // Do we need two different variables for number of open paranethesis as well
        // as well as number of closed paranthesis used?
        // I think so. You can either use your current index to deduce this or you
        // can have an explicit variable to tell you this.
        //
        // How do we recurse?

        int n;
        Map<Integer, Map<Integer, String>> cache;
        List<String> combinations;
        char[] combo;

        static List<String> generateParenthesis(int n) {
            Solution1 soln = create(n);
            
            soln.generate(0, 0);

            return soln.combinations;
        }

        private void generate(int open, int closed) {
            if (open + closed == 2*n) {
                addCurrentCombination();
            }
            if (open < n) {
                combo[open + closed] = '(';
                generate(open + 1, closed);
            }
            if (closed < open) {
                combo[open + closed] = ')';
                generate(open, closed + 1);
            }
        }

        private void addCurrentCombination() {
            combinations.add(String.copyValueOf(combo));
        } 

        static Solution1 create(int n) {
            return new Solution1(n);
        }

        private Solution1(int n) {
            this.n = n;
            combinations = new ArrayList<>();
            combo = new char[2*n];
        }
    }
}
