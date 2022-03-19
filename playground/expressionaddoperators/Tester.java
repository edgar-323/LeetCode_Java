package playground.expressionaddoperators;

import java.lang.Character;
import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

// TODO: Add the necessary imports to Solution.java

/********************************************************************

TO RUN THIS CLASS:

$ cd ~/GitHub && \
javac -g playground/expressionaddoperators/Tester.java && \
java playground.expressionaddoperators.Tester;


TO RUN DEBUGGER:

$ cd ~/GitHub && \
javac -g playground/expressionaddoperators/Tester.java && \
jdb playground.expressionaddoperators.Tester;

********************************************************************/

public class Tester {

    private static final Set<Character> VALID_OPERATORS =
        new HashSet<>(Arrays.asList('+', '-', '*'));

    private Solution solution;
    private ScriptEngine scriptEngine;
 
    Tester() {
         solution = new Solution();
         ScriptEngineManager scriptEngineManager =
             new ScriptEngineManager();
         scriptEngine =
             scriptEngineManager.getEngineByName("JavaScript");
    }

    public static void main(String[] unusedArgs) {
         Tester tester = new Tester();
         tester.runTests();
    }

    private static class Test {

        private final String num;
        private final int target;
        private final List<String> expected;

        private Test(String num, int target, List<String> expected) {
            this.num = num;
            this.target = target;
            this.expected = expected;
        }

        public static Test create(
                String num, int target, List<String> expected) {
            return new Test(num, target, expected);
        }

        public String getNum() { return num; }

        public int getTarget() { return target; }

        public List<String> getExpected() { return expected; }
    }

    private void runTests() {
         System.out.println("Initializing all tests ...\n");

         List<Test> tests =
             Arrays.asList(
                 Test.create("123", 6, Arrays.asList("1*2*3","1+2+3")),
                 Test.create("232", 8, Arrays.asList("2*3+2","2+3*2")),
                 Test.create("3456237490", 9191, Arrays.asList()),
                 Test.create("105", 5, Arrays.asList("1*0+5","10-5")));

         for (Test test : tests) { outputTestResults(test); }

         System.out.println("\nTesting complete.\n");
    }

    private void outputTestResults(Test test) {
        // Get actual output.
        List<String> actual =
            solution.addOperators(test.getNum(), test.getTarget());
        // Sort arrays for better visual comparisons.
        Collections.sort(test.getExpected());
        Collections.sort(actual);
        // Output results.
        System.out.println(String.format(
                    "INPUT:\t\t\t{ num=\"%s\", target=%d }",
                    test.getNum(), test.getTarget()));
        System.out.println(String.format(
                    "EXPECTED:\t\t%s",
                    serializePossibilities(test.getExpected())));
        System.out.println(String.format(
                    "ACTUAL:\t\t\t%s",
                    serializePossibilities(actual)));
        outputIncorrectMathExpressions(
                test.getTarget(), actual);
        System.out.println();
    }

    private static String serializePossibilities(List<String> possibilities) {
        if (possibilities.isEmpty()) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[ ");
        for (int i = 0; i < possibilities.size(); i++) {
            stringBuilder.append(String.format("\"%s\"", possibilities.get(i)));
            if (i < possibilities.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(" ]");

        return stringBuilder.toString();
    }

    private void outputIncorrectMathExpressions(
            int target, List<String> expressions) {
        expressions
            .stream()
            .filter(expr -> {
                try {
                    return eval(expr) != target;
                } catch (ScriptException e) {
                    throw new RuntimeException(String.format(
                                "Eval error occurred: expr=\"%s\".\n%s", expr, e));
                }
            })
            .map(expr -> 
                    String.format(
                        "INCORRECT_EXPR:\t\t\"%s\"",
                        expr))
            .forEach(System.out::println);

    }

    private int eval(String expr) throws ScriptException {
        validateSimpleMathExpr(expr);
        return (Integer) scriptEngine.eval(expr);
    }

    /**
     * Validates a simple math expression.
     */
    private void validateSimpleMathExpr(String expr) {
        // Input expression must comply with the following requirements:
        //      1.  Cannot be null (or empty). Otherwise, mathematical evaluation
        //          of `expr` will be meaningless.
        //      2.  Must start and end with a series of digits.
        //          This is because mathematical operators can only exist between
        //          digits.
        //      4.  Only digits and allowed mathematical operators are valid.
        //      3.  Mathematical operators must be followed up with a digit.
        if (expr == null || expr.isEmpty()) {
            throw new RuntimeException(String.format(
                        "Recieved null (or empty) math expression: %s", expr));
        }
        final int N = expr.length();
        char firstChar = expr.charAt(0);
        char lastChar = expr.charAt(N - 1);
        if (!Character.isDigit(firstChar)) {
            throw new RuntimeException(String.format(
                        "Initial char of math expr must be a digit: %s", expr));
        }
        if (!Character.isDigit(lastChar)) {
            throw new RuntimeException(String.format(
                        "Final char of math expr must be a digit: %s", expr));
        }
        for (int i = 1; i < N - 1; i++) {
            char currChar = expr.charAt(i);
            if (Character.isDigit(currChar)) {
                continue;
            } 
            if (!VALID_OPERATORS.contains(currChar)) {
                throw new RuntimeException(String.format(
                            "Math expr contains illegal character(s): %s", expr));
            }
            char nextChar = expr.charAt(i+1);
            if (!Character.isDigit(nextChar)) {
                throw new RuntimeException(String.format(
                            "Math operator must be followed by a digit: %s", expr));
            }
        }
    }
}
