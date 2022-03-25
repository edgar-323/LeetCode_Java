class Solution {
    public boolean isMatch(String s, String p) {
        return Solution1.isMatch(s, p);
    }

    static class Solution1 {
        private static final char ANY = '.';
        private static final char WILDCARD = '*';

        private final String text;
        private final String pattern;

        private final List<List<Boolean>> cache;

        static boolean isMatch(String s, String p) {
            return new Solution1(s, p).matches(
                    /* textIndex= */ 0,
                    /* patternIndex= */ 0);
        }

        boolean matches(int textIndex, int patternIndex) {
            if (cacheHit(textIndex, patternIndex)) {
                return getCacheValue(textIndex, patternIndex);
            }
            if (consumedText(textIndex)) {
                // Matched all text characters but there are still pattern characters left.
                // The remaining pattern characters must be wildcards so that we only match
                // if we don't use any wild cards.
                return setCacheValue(
                        textIndex,
                        patternIndex,
                        consumedPattern(patternIndex)
                        || (precedesWildCard(patternIndex) && matches(textIndex, patternIndex + 2)));
            }
            if (consumedPattern(patternIndex)) {
                // Used up all pattern characters but there are still text characters
                // left to match. Ensure we check that:
                return setCacheValue(
                        textIndex,
                        patternIndex,
                        consumedText(textIndex));
            }
            if (precedesWildCard(patternIndex)) {
                if (charsMatch(textIndex, patternIndex)) {
                    return setCacheValue(
                            textIndex,
                            patternIndex,
                            matches(textIndex + 1, patternIndex + 2)  // Match text & use up wildcard.
                            || matches(textIndex + 1, patternIndex)  // Match text but don't use up wildcard.
                            || matches(textIndex, patternIndex + 2));  // Don't match text but use up wildcard.
                }
                // We can check to see if choosing to use zero patternIndex chars will
                // still result in a match.
                return setCacheValue(
                        textIndex,
                        patternIndex,
                        matches(textIndex, patternIndex + 2));  // Cannot match text, must use up wildcard.
            }
            // No wild card follows patternIndex.
            // So there must be a match at the current possition.
            return setCacheValue(
                    textIndex,
                    patternIndex,
                    charsMatch(textIndex, patternIndex)  // text must match pattern
                    && matches(textIndex + 1, patternIndex + 1));
        }

        boolean consumedText(int textIndex) {
            return textIndex == text.length();
        }

        boolean consumedPattern(int patternIndex) {
            return patternIndex == pattern.length();
        }

        boolean precedesWildCard(int patternIndex) {
            return patternIndex < pattern.length() - 1
                && pattern.charAt(patternIndex + 1) == WILDCARD;
        }

        boolean charsMatch(int textIndex, int patternIndex) {
            return pattern.charAt(patternIndex) == ANY
                || pattern.charAt(patternIndex) == text.charAt(textIndex);
        }

        boolean getCacheValue(int textIndex, int patternIndex) {
            return cache.get(textIndex).get(patternIndex);
        }

        boolean setCacheValue(int textIndex, int patternIndex, boolean value) {
            cache.get(textIndex).set(patternIndex, value);
            return value;
        }

        boolean cacheHit(int textIndex, int patternIndex) {
            return cache.get(textIndex).get(patternIndex) != null;
        }

        Solution1(String s, String p) {
            text = s;
            pattern = p;
            // Init cache.
            cache = new ArrayList<>(text.length() + 1);
            for (int i = 0; i <= text.length(); i++) {
                List<Boolean> subCache = new ArrayList<>(pattern.length() + 1);
                for (int j = 0; j <= pattern.length(); j++) {
                    subCache.add(null);
                }
                cache.add(subCache);
            }
        }
    }
}
