class Solution {
    public List<String> addOperators(String num, int target) {
        return Solution1.newSolver(num, target).solve();
    }


    static class ValueAndIndex {
        private final long value;
        private final int index;

        ValueAndIndex(long value, int index) {
            this.value = value;
            this.index = index;
        }

        long getValue() {
            return value;
        }

        int getIndex() {
            return index;
        }

        static ValueAndIndex create(long value, int index) {
            return new ValueAndIndex(value, index);
        }
    }

    static class Solution1 {
        // THIS IS MY MOST NAIVE ATTEMPT (BRUTE FORCE!).

        private static final char ADDITION = '+';
        private static final char SUBTRACTION = '-';
        private static final char MULTIPLICATION = '*';
        private static final char CONCAT = '#';
        private static final char[] OPS = {
            ADDITION,
            SUBTRACTION,
            MULTIPLICATION,
            CONCAT
        };

        private final int N;
        private final int target;
        private final char[] combo;

        static Solution1 newSolver(String num, int target) {
            return new Solution1(num, target);
        }

        List<String> solve() {
            List<String> combinations = new ArrayList<>();
            solve(0, toDigit(0) == 0, combinations);

            return combinations;
        }

        /**
         * Assumption: combo[i] is a digit that belongs to the original `num` string.
         * Objective: This method attempts to place an operator in front of the i-th
         *            digit. It will avoid placing the concatenation operator if the
         *            i-th digit is a zero. Furthermore, if there are no more digits
         *            after the i-th digit, then we must stop this assignment, which
         *            also means that we will evaluate the value of the current combo
         *            and if it matches our target, we will add it to our solution
         *            combinations.
         */
        void solve(int i, boolean isLeadingZero, List<String> combinations) {
            if (i >= (N-1)) {
                if (evalCombo() == target) {
                    combinations.add(comboToString());
                }
                return;
            }

            for (char op : OPS) {
                if (isLeadingZero && op == CONCAT) {
                    continue;
                }
                combo[i+1] = op;
                solve(i+2, (op != CONCAT) && (toDigit(i+2) == 0), combinations);
            }
        }

        // Convert the current combo to a string (we just need to strip out the
        // CONCAT operator occurrences).
        String comboToString() {
            int numConcats = 0;
            for (char c : combo) {
                if (c == CONCAT) {
                    numConcats++;
                }
            }
            StringBuilder strBuilder = new StringBuilder(N - numConcats);
            for (char c : combo) {
                if (c != CONCAT) {
                    strBuilder.append(c);
                }
            }

            return strBuilder.toString();
        }

        // This method evaluates the value of the current combo.
        // At this level, we group blocks by multiplication, and
        // we then apply ADDITION and SUBTRACTION operators over
        // these blocks; this is because the multiplication blocks
        // have precedence over the addition/substraction operators.
        long evalCombo() {
            ValueAndIndex valueAndIndex = evalMultiplication(0);
            long value = valueAndIndex.getValue();
            int i = valueAndIndex.getIndex();

            while ((i+1) < N) {
                char op = combo[i+1];
                valueAndIndex = evalMultiplication(i+2);
                value += (op == SUBTRACTION ? -1 : 1)*valueAndIndex.getValue();
                i = valueAndIndex.getIndex();
            }

            return value;
        }

        // This method groups blocks by the CONCAT operator and performs
        // multiplication operations on them.
        ValueAndIndex evalMultiplication(int i) {
            ValueAndIndex valueAndIndex = evalConcat(i);
            long value = valueAndIndex.getValue();
            i = valueAndIndex.getIndex();

            while ((i+1) < N && combo[i+1] == MULTIPLICATION) {
                valueAndIndex = evalConcat(i+2);
                value *= valueAndIndex.getValue();
                i = valueAndIndex.getIndex();
            }

            return ValueAndIndex.create(value, i);
        }

        // This method forms a single group via the CONCAT operator.
        ValueAndIndex evalConcat(int i) {
            long value = toDigit(i);
            while ((i+1) < N && combo[i+1] == CONCAT) {
                value = 10*value + toDigit(i+2);
                i += 2;
            }

            return ValueAndIndex.create(value, i);
        }

        long toDigit(int i) {
            return combo[i] - '0';
        }

        Solution1(String num, int target) {
            N = 2*num.length() - 1;
            combo = new char[N];

            for (int j = 0; j < N; j += 2) {
                combo[j] = num.charAt(j/2);
            }

            this.target = target;
        }
    }
}
