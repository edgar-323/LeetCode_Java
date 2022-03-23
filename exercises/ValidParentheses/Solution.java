class Solution {
    public boolean isValid(String s) {
        return Solution1.isValid(s);
    }

    static class Solution1 {
        static boolean isValid(String s) {
            /**
             * TimeComplexity:  O(N)
             * SpaceComplexity: O(N)
             */

            Stack<Character> stack = new Stack<>();

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(' || c == '{' || c == '[') {
                    stack.push(c);
                    continue;
                }
                if (stack.isEmpty()) {
                    return false;
                }
                if (c == ')' && stack.pop() != '(') {
                    return false;
                }
                if (c == '}' && stack.pop() != '{') {
                    return false;
                }
                if (c == ']' && stack.pop() != '[') {
                    return false;
                }
            }

            return stack.isEmpty();
        }
    }
}
