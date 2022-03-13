package playground.expressionaddoperators;


// TODO: Add the necessary imports to Solution.java

/********************************************************************
TO RUN THIS CLASS::

$ cd ~/GitHub && \
javac playground/expressionaddoperators/Tester.java && \
java playground.expressionaddoperators.Tester;

********************************************************************/


public class Tester {

    private Solution solution;

    Tester() {
         solution = new Solution();
    }

    public static void main(String[] unusedArgs) {
         Tester tester = new Tester();
         tester.runTests();
    }

    private void runTests() {
         // TODO: Implement.
         System.out.println("Initializing all tests ...");
         System.out.println("Testing complete.");
    }
}
