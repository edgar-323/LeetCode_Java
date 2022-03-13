package playground.expressionaddoperators;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.lang.Character;

class Solution {
    public List<String> addOperators(String num, int target) {
        return Solution1.addOperators(num, target);
    }

    private static class Solution1 {
        // Naive (1st attempt) implementation.
        // Possible operators: {+, -, *}.
        // Say you have the sequence (string) of digits:
        //      d_0, d_1, ..., d_(n-1).
        // Then you only assign an operator to all except the first digit (d_0).
        //
        // at any given time (ie, when you are looking at digit d_i), you have a
        // current_sum and a current_factor.
        // What options do we have with d_i ?
        //
        //   PLUS(+):
        //      (current_sum + current_factor, + d_i)
        //      --> Rationale: as soon as you assign a non-multiplier op, you
        //          "break" the factor stream (and thus it "ends" -- must be
        //          added to current_sum).
        //  MINUS(-):
        //      (current_sum + current_factor, - d_i)
        //      --> Rationale: Same as the PLUS rationale.
        //  MULTIPLY(*):
        //      (current_sum, current_factor * d_i)
        //      --> Rationale: You are extending the multiplication stream.
        //
        // At each step, you consume the digit (ie, increment the index).
        //
        // At d_0, there is no concept of (current_sum, current_factor).
        // Well, actually, there is: (0, 1)
        // However, you cannot assign the MINUS(-) operator at d_0.
        // So at d_0, you can only generate: {(d_0, 1), (0, +d_0)}
        //
        // At index N, there are no more digits, which indicates that you must
        // consolidate the existing tuple:
        //      (current_sum, current_factor) --> current_sum + current_factor
        // Then, compare this value with `target`:
        //      if (i == N && current_sum + current_factor == target) {
        //          possibilities.add(current_sting);
        //          return;
        //      }
        //
        // We've been ignoring the construction of the final string:
        //      OP: (current_sting + "${OP}" + d_i), for OP in {+, -, *}.
        //

        private final String num;
        private final int target;
        private final List<String> possibilities;

        static List<String> addOperators(String num, int target) {
            return new Solution1(num, target).generatePossibilities();
        }

        List<String> generatePossibilities() {
            // Initial states:
            int initialIndex = 0; 
            char digitAsChar = getDigitAsChar(0);
            int digitAsInt = getDigitAsInt(0);

            generatePossibilities(
                    /* index= */ 1,
                    /* sum= */ 0,
                    /* factor= */ digitAsInt,
                    /* possibility= */ Character.toString(digitAsChar));

            return possibilities;
        }

        void generatePossibilities(
                int index, int sum, int factor, String possibility) {
            if (index == num.length()) {
                if (sum + factor == target) {
                    possibilities.add(possibility);
                }
                return;
            }

            char digitAsChar = getDigitAsChar(index);
            int digitAsInt = getDigitAsInt(index);

            // PLUS(+):
            generatePossibilities(
                    index + 1,
                    sum + factor,
                    digitAsInt,
                    String.format("%s+%s", possibility, digitAsChar));
            // MINUS(-):
            generatePossibilities(
                    index + 1,
                    sum + factor,
                    -digitAsInt,
                    String.format("%s-%s", possibility, digitAsChar));
            // MULTIPLY(*):
            generatePossibilities(
                    index + 1,
                    sum,
                    factor * digitAsInt,
                    String.format("%s*%s", possibility, digitAsChar));
            // APPEND(factor):
            generatePossibilities(
                    index + 1,
                    sum,
                    factor * 10 + digitAsInt,
                    String.format("%s%s", possibility, digitAsChar));
        }

        char getDigitAsChar(int index) {
            return num.charAt(index);
        }

        int getDigitAsInt(int index) {
            return Character.getNumericValue(getDigitAsChar(index));
        }

        Solution1(String _num, int _target) {
            num = _num;
            target = _target;
            possibilities = new ArrayList<>();
        }
    }
}
