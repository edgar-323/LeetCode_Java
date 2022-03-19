package playground.expressionaddoperators;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.lang.Character;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;


class Solution {
    private final static ScriptEngine SCRIPT_ENGINE =
        new ScriptEngineManager().getEngineByName("JavaScript");

    private static int evalExpr(String expr) {
        try {
            return (Integer) Solution.SCRIPT_ENGINE.eval(expr);
        } catch (ScriptException e) {
            throw new RuntimeException(String.format(
                        "Script Engine Error with expr=\"%s\"\n%s",
                        expr, e));
        }
    }

    public List<String> addOperators(String num, int target) {
        return Solution1.addOperators(num, target);
    }

    static class Eval {
        private final int value;
        private final String stringToAppend;

        Eval(int value, String stringToAppend) {
            this.value = value;
            this.stringToAppend = stringToAppend;
        }

        static Eval create(int value, String stringToAppend) {
            return new Eval(value, stringToAppend);
        }

        int getValue() {
            return value;
        }

        String getStringToAppend() {
            return stringToAppend;
        }
    }

    private static class Solution1 {
        // Naive (1st attempt) implementation.

        private final String num;
        private final List<String> possibilities;

        enum Operation { ADDITION, SUBTRACTION, MULTIPLICATION, APPEND};

        static List<String> addOperators(String num, int target) {
            return new Solution1(num).generatePossibilities(target);
        }

        List<String> generatePossibilities(int target) {

            if (num.length() == 0) {
                return possibilities;
            }
            if (num.length() == 1) {
                if (getDigitAsInt(0) == target) {
                    possibilities.add(num);
                }
                return possibilities;
            }

            generatePossibilities(
                    /* target= */ target,
                    /* x= */ 0,
                    /* y= */ getDigitAsInt(0),
                    /* op= */ Operation.ADDITION,
                    /* index= */ 1,
                    /* possibility= */ String.format("%d", getDigitAsInt(0)));

            return possibilities;
        }

        /*
         * Appends num[index] to y and preserves previous operation (since APPEND is not mathematical).
         */
        void doAppendOp(
                int target, int x, int y, Operation op, int index, String possibility) {
            generatePossibilities(
                    /* target= */ target,
                    /* x= */ x,
                    /* y= */ eval(y, getDigitAsInt(index), Operation.APPEND),
                    /* op= */ op,
                    /* index= */ index + 1,
                    /* possibility= */ String.format("%s%d", possibility, getDigitAsInt(index)));
        }

        /*
         * num[index] will be preceded with a + operation. Since this operator has low precedence,
         * we can apply previous operator first (hence consolidate (x, y) into x_prime).
         */
        void doAdditionOp(
                int target, int x, int y, Operation op, int index, String possibility) {
            generatePossibilities(
                    /* target= */ target,
                    /* x= */ eval(x, y, op),
                    /* y= */ getDigitAsInt(index),
                    /* op= */ Operation.ADDITION,
                    /* index= */ index + 1,
                    /* possibility= */ String.format("%s+%d", possibility, getDigitAsInt(index)));
        }

        void doSubtractionOp(
                int target, int x, int y, Operation op, int index, String possibility) {
            generatePossibilities(
                    /* target= */ target,
                    /* x= */ eval(x, y, op),
                    /* y= */ getDigitAsInt(index),
                    /* op= */ Operation.SUBTRACTION,
                    /* index= */ index + 1,
                    /* possibility= */ String.format("%s-%d", possibility, getDigitAsInt(index)));
        }

        void doMultiplicationOp(
                int target, int x, int y, Operation op, int index, String possibility) {
            switch (op) {
                case MULTIPLICATION:
                    generatePossibilities(
                            /* target= */ target,
                            /* x= */ eval(x, y, Operation.MULTIPLICATION),
                            /* y= */ getDigitAsInt(index),
                            /* op= */ Operation.MULTIPLICATION,
                            /* index= */ index + 1,
                            /* possibility= */ String.format("%s*%d", possibility, getDigitAsInt(index)));
                    break;
                case ADDITION:
                        generatePossibilities(
                            /* target= */ target + (x < 0 ? -x : x),
                            /* x= */ y,
                            /* y= */ getDigitAsInt(index),
                            /* op= */ Operation.MULTIPLICATION,
                            /* index= */ index + 1,
                            /* possibility= */ String.format("%s*%d", possibility, getDigitAsInt(index)));
                        break;
                case SUBTRACTION:
                        generatePossibilities(
                            /* target= */ target + (x < 0 ? -x : x),
                            /* x= */ -y,
                            /* y= */ getDigitAsInt(index),
                            /* op= */ Operation.MULTIPLICATION,
                            /* index= */ index + 1,
                            /* possibility= */ String.format("%s*%d", possibility, getDigitAsInt(index)));
                        break;
                default:
                        throw new RuntimeException(String.format("Illegal previous operation: %s", op));
            }
        }


        void generatePossibilities(
                int target, int x, int y, Operation op, int index, String possibility) {
            /* ASSUMPTIONS:
             * If all numbers have been "consumed" (ie, if you are at the end of `num`),
             * then you must apply the final operation:
             *      if eval(x, y, op) == target:
             *          our possibility is valid.
             *          we must add it to the list.
             *          However, one question we must ask ourselves:
             *          Does `possibility` already include this latest operation?
             *          We should make it so since this operation is final and it
             *          keeps things cleaner (we don't inherit debt from previous call).
             */
            if (index == num.length()) {
                if (eval(x, y, op) == target) {
                    possibilities.add(possibility);
                }
                if (Solution.evalExpr(possibility) != eval(x, y, op)) {
                    throw new RuntimeException(String.format(
                                "Incorrect Expr!\n" +
                                "possibility = %s\n" +
                                "target = %d\n" +
                                "x = %d\n" +
                                "y = %d\n" +
                                "op = %s\n" +
                                "eval(%d, %d, %s) = %d\n" +
                                "evalExpr(\"%s\") = %d\n",
                                possibility,
                                target,
                                x,
                                y,
                                op,
                                x,
                                y,
                                op,
                                eval(x, y, op),
                                possibility,
                                Solution.evalExpr(possibility)));
                }
                return;
            }
            int digitAsInt = getDigitAsInt(index);
            // Now generate strings with (possibly) different operation.
            doAdditionOp(target, x, y, op, index, possibility);
            // ADDITION(+):
            doAdditionOp(target, x, y, op, index, possibility);
            // SUBTRACTION(-):
            doSubtractionOp(target, x, y, op, index, possibility);
            // MULTIPLICATION(*):
            // This will be more involved.
            doMultiplicationOp(target, x, y, op, index, possibility);
        }

        int getDigitAsInt(int index) {
            return Character.getNumericValue(num.charAt(index));
        }

        int eval(int x, int y, Operation op) {
            switch (op) {
                case MULTIPLICATION:
                    return x * y;
                case ADDITION:
                    return x + y;
                case SUBTRACTION:
                    return x - y;
                case APPEND:
                    if (x == 0) {
                        throw new RuntimeException("Cannot append to a leading zero!");
                    }
                    if (y < 0) {
                        throw new RuntimeException(String.format("Cannot append a negative number: %d", y));
                    }
                    if (y % 10 > 0) {
                        throw new RuntimeException(String.format("Appending number must be a single digit: %d", y));
                    }
                    return (Math.abs(x) + y) * (x < 0 ? -1 : 1);
            }
            throw new RuntimeException(String.format("Unknown Operation: %s", op));
        }

        Solution1(String _num) {
            num = _num;
            possibilities = new ArrayList<>();
        }
    }
}
