package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。
 * <p>
 * 例 1：
 * 输入：s = "(()"
 * 输出：2
 * 解释：最长有效括号子串是 "()"
 * <p>
 * 例 2：
 * 输入：s = ")()())"
 * 输出：4
 * 解释：最长有效括号子串是 "()()"
 * <p>
 * 例 3：
 * 输入：s = ""
 * 输出：0
 * <p>
 * 约束：
 * - 0 <= s.length <= 3 * 10**4
 * - s[i] 为 '(' 或 ')'
 */
public class E32_Hard_LongestValidParentheses {

    static void test(ToIntFunction<String> method) {
        assertEquals(2, method.applyAsInt("(()"));
        assertEquals(4, method.applyAsInt(")()())"));
        assertEquals(0, method.applyAsInt(""));
        assertEquals(2, method.applyAsInt("()(()"));
        assertEquals(2, method.applyAsInt("(()(((()"));
        assertEquals(4, method.applyAsInt("(()()(((()"));
        assertEquals(6, method.applyAsInt("(())()(()(("));
        assertEquals(22, method.applyAsInt(")(((((()())()()))()(()))("));
        assertEquals(6, method.applyAsInt("()(())"));
    }

    static final class State {
        boolean valid;
        int diff;

        public State(boolean valid, int diff) {
            this.valid = valid;
            this.diff = diff;
        }
    }

    // 超出内存限制
    public int longestValidParentheses(String s) {
        final int n = s.length();
        if (n <= 1)
            return 0;
        // dp[i][j] 表示 [i..j] 是否有效，以及范围内 "(" 和 ")" 数量的差值
        // 有效表示 [i..j]，以及它的前缀 [i..j-1]、[i..j-2] 等等中的 "(" 数量大于等于 ")" 数量
        State[][] dp = new State[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = new State(false, 0);
            }
        }
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            dp[i][i].valid = c == '(';
            dp[i][i].diff = s.charAt(i) == '(' ? 1 : -1;
        }

        int maxLen = 0;
        for (int i = 0; i <= n - 2; i++) {
            for (int j = i + 1; j < n; j++) {
                char cj = s.charAt(j);
                dp[i][j].diff = dp[i][j - 1].diff;
                dp[i][j].diff += cj == '(' ? 1 : -1;
                dp[i][j].valid = dp[i][j - 1].valid && dp[i][j].diff >= 0;
                if (dp[i][j].valid && dp[i][j].diff == 0) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }

        return maxLen;
    }

    @Test
    public void testLongestValidParentheses() {
        test(this::longestValidParentheses);
    }


    /**
     * LeetCode 耗时：2332 ms - 5.06%
     * 内存消耗：38.6 MB - 28.84%
     */
    public int compressMethod(String s) {
        final int n = s.length();
        if (n <= 1)
            return 0;

        State prev = new State(false, 0), cur = new State(false, 0);
        int maxLen = 0;
        for (int i = 0; i <= n - 2; i++) {
            prev.valid = s.charAt(i) == '(';
            prev.diff = s.charAt(i) == '(' ? 1 : -1;
            for (int j = i + 1; j < n; j++) {
                cur.diff = prev.diff;
                cur.diff += s.charAt(j) == '(' ? 1 : -1;
                cur.valid = prev.valid && cur.diff >= 0;
                if (cur.valid && cur.diff == 0)
                    maxLen = Math.max(maxLen, j - i + 1);
                prev.valid = cur.valid;
                prev.diff = cur.diff;
            }
        }

        return maxLen;
    }

    @Test
    public void testBetterMethod() {
        test(this::compressMethod);
    }


    /**
     * LeetCode 耗时：3 ms - 45.45%
     *          内存消耗：38.7 MB - 13.62%
     */
    public int stackMethod(String s) {
        final int n = s.length();
        int maxLen = 0;
        // 使用 leftQuoteStack 保存每个 '(' 的位置，遇到 ')' 就弹出
        int[] leftQuoteStack = new int[n];
        int lsi = -1;
        // 使用 validSeqStack 保存每个有效序列的开始和结束下标，在合适的时候合并序列
        int[][] validSeqStack = new int[n / 2][2];
        int vsi = -1;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c == '(')
                leftQuoteStack[++lsi] = i;
            else {
                if (lsi != -1) {
                    int leftStart = leftQuoteStack[lsi--];
                    // 先保存当前有效序列
                    validSeqStack[++vsi][0] = leftStart;
                    validSeqStack[vsi][1] = i;
                    // 尝试循环合并序列。记住了，这种合并栈的算法一般都需要循环合并
                    for (; vsi > 0; vsi--) {
                        int curStart = validSeqStack[vsi][0],
                                curEnd = validSeqStack[vsi][1];
                        int lastStart = validSeqStack[vsi - 1][0],
                                lastEnd = validSeqStack[vsi - 1][1];
                        // 如果当前有效序列包含上一个序列，就将它们连接起来
                        if (curStart < lastStart) {
                            validSeqStack[vsi - 1][0] = curStart;
                            validSeqStack[vsi - 1][1] = curEnd;
                        }
                        // 如果当前有效序列和上一个序列紧挨着，就将它们连接起来
                        else if (lastEnd == curStart - 1)
                            validSeqStack[vsi - 1][1] = curEnd;
                        else
                            break;
                    }
                    maxLen = Math.max(maxLen, validSeqStack[vsi][1] - validSeqStack[vsi][0] + 1);
                }
            }
        }

        return maxLen;
    }

    @Test
    public void testStackMethod() {
        test(this::stackMethod);
    }


    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.7 MB - 9.42%
     */
    public int betterDpMethod(String s) {
        // 可以利用 dp 数组中以前的信息
        final int n = s.length();
        // dp[i] 表示以 i 结尾的最长子序列长度
        final int[] dp = new int[n];

        int maxLen = 0;
        for (int i = 1; i < n; i++) {
            char c = s.charAt(i);
            // 等于 )，就可能和之前的 ( 配对
            if (c == ')') {
                // 前一个字符是 (，则可以配对
                if (s.charAt(i - 1) == '(')
                    // 除了配对这对括号，还要看看再前面是不是也是个有效序列
                    dp[i] = 2 + (i >= 2 ? dp[i - 2] : 0);
                // 如果前面是一个合法序列，并且这个合法序列之前有个 (，则也可以配对
                else if (dp[i - 1] > 0 && dp[i - 1] <= i - 1 && s.charAt(i - 1 - dp[i - 1]) == '(') {
                    dp[i] = 2 + dp[i - 1];
                    // 除了配对这对括号，还要看看再前面是不是也是个有效序列
                    int prev = i - 1 - dp[i - 1] - 1;
                    if (prev >= 0)
                        dp[i] += dp[prev];
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }

        return maxLen;
    }

    @Test
    public void testBetterDpMethod() {
        test(this::betterDpMethod);
    }
}
