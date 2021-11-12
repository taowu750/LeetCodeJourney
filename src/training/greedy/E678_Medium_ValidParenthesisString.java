package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 678. 有效的括号字符串: https://leetcode-cn.com/problems/valid-parenthesis-string/
 *
 * 给定一个只包含三种字符的字符串：（ ，） 和 *，写一个函数来检验这个字符串是否为有效字符串。有效字符串具有如下规则：
 * - 任何左括号 ( 必须有相应的右括号 )。
 * - 任何右括号 ) 必须有相应的左括号 (。
 * - 左括号 ( 必须在对应的右括号之前 )。
 * - * 可以被视为单个右括号 )，或单个左括号 (，或一个空字符串。
 * - 一个空字符串也被视为有效字符串。
 *
 * 例 1：
 * 输入: "()"
 * 输出: True
 *
 * 例 2：
 * 输入: "(*)"
 * 输出: True
 *
 * 例 3：
 * 输入: "(*))"
 * 输出: True
 *
 * 说明：
 * - 字符串大小将在 [1，100] 范围内。
 */
public class E678_Medium_ValidParenthesisString {

    public static void test(Predicate<String> method) {
        assertTrue(method.test("()"));
        assertTrue(method.test("(*)"));
        assertTrue(method.test("(*))"));
        assertFalse(method.test(")*("));
        assertFalse(method.test(")("));
        assertFalse(method.test("())"));
        assertFalse(method.test("(()"));
        assertFalse(method.test("(((((*(()((((*((**(((()()*)()()()*((((**)())*)*)))))))(())(()))())((*()()(((()((()*(())*(()**)()(())"));
        assertTrue(method.test("((((()(()()()*()(((((*)()*(**(())))))(())()())(((())())())))))))(((((())*)))()))(()((*()*(*)))(*)()"));
    }

    /**
     * 栈+TreeSet。
     *
     * LeetCode 耗时：3 ms - 15.37%
     *          内存消耗：36.5 MB - 22.60%
     */
    public boolean checkValidString(String s) {
        // 栈中装的是未匹配的 ( 下标
        Deque<Integer> stack = new LinkedList<>();
        // signs 装的是 * 下标
        TreeSet<Integer> signs = new TreeSet<>();
        // noMatchRights 装的是未匹配的 ) 下标
        List<Integer> noMatchRights = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else if (c == ')') {
                if (stack.isEmpty()) {
                    noMatchRights.add(i);
                } else {
                    stack.pop();
                }
            } else {
                signs.add(i);
            }
        }
        // 给未匹配的 ) 找前面的 * 匹配，没找到则序列不合法
        for (Integer ri : noMatchRights) {
            Integer signIdx = signs.lower(ri);
            if (signIdx == null) {
                return false;
            }
            signs.remove(signIdx);
        }
        // 给未匹配的 ( 找后面的 * 匹配，没找到则序列不合法
        for (Integer li : stack) {
            Integer signIdx = signs.higher(li);
            if (signIdx == null) {
                return false;
            }
            signs.remove(signIdx);
        }

        return true;
    }

    @Test
    public void testCheckValidString() {
        test(this::checkValidString);
    }


    /**
     * 双栈法。参见：
     * https://leetcode-cn.com/problems/valid-parenthesis-string/solution/you-xiao-de-gua-hao-zi-fu-chuan-by-leetc-osi3/
     *
     * 如果字符串中没有星号，则只需要一个栈存储左括号，在从左到右遍历字符串的过程中检查括号是否匹配。
     * 在有星号的情况下，需要两个栈分别存储左括号和星号。从左到右遍历字符串，进行如下操作。
     * - 如果遇到左括号，则将当前下标存入左括号栈。
     * - 如果遇到星号，则将当前下标存入星号栈。
     * - 如果遇到右括号，则需要有一个左括号或星号和右括号匹配，由于星号也可以看成右括号或者空字符串，因此当前的右括号应优先和左括号匹配，没有左括号时和星号匹配：
     *   - 如果左括号栈不为空，则从左括号栈弹出栈顶元素；
     *   - 如果左括号栈为空且星号栈不为空，则从星号栈弹出栈顶元素；
     *   - 如果左括号栈和星号栈都为空，则没有字符可以和当前的右括号匹配，返回 false。
     *
     * 遍历结束之后，左括号栈和星号栈可能还有元素。为了将每个左括号匹配，需要将星号看成右括号，
     * 且每个左括号必须出现在其匹配的星号之前。当两个栈都不为空时，每次从左括号栈和星号栈分别弹出栈顶元素，
     * 对应左括号下标和星号下标，判断是否可以匹配，匹配的条件是左括号下标小于星号下标，
     * 如果左括号下标大于星号下标则返回 false。
     *
     * LeetCode 耗时：1 ms - 58.78%
     *          内存消耗：36.1 MB - 79.26%
     */
    public boolean doubleStackMethod(String s) {
        Deque<Integer> leftStack = new LinkedList<>();
        Deque<Integer> signStack = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                leftStack.push(i);
            } else if (c == ')') {
                if (!leftStack.isEmpty()) {
                    leftStack.pop();
                } else if (!signStack.isEmpty()) {
                    signStack.pop();
                } else {
                    return false;
                }
            } else {
                signStack.push(i);
            }
        }
        while (!leftStack.isEmpty()) {
            int leftIdx = leftStack.pop();
            if (signStack.isEmpty() || signStack.pop() < leftIdx) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testDoubleStackMethod() {
        test(this::doubleStackMethod);
    }


    /**
     * 动态规划法，参见：
     * https://leetcode-cn.com/problems/valid-parenthesis-string/solution/you-xiao-de-gua-hao-zi-fu-chuan-by-leetc-osi3/
     *
     * LeetCode 耗时：10 ms - 10.64%
     *          内存消耗：36.7 MB - 8.17%
     */
    public boolean dpMethod(String s) {
        int n = s.length();
        // dp[i][j] 表示 s[i..j] 是不是合法序列
        boolean[][] dp = new boolean[n][n];
        dp[0][0] = s.charAt(0) == '*';
        for (int i = 1; i < n; i++) {
            dp[i][i] = s.charAt(i) == '*';
            dp[i][i - 1] = true;
        }

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                char l = s.charAt(i), r = s.charAt(j);
                // 如果 i、j 匹配，且 dp[i+1][j-1] 也匹配，则 dp[i][j] 匹配
                dp[i][j] = (l == '(' || l == '*') && (r == ')' || r == '*')
                        && dp[i + 1][j - 1];
                if (!dp[i][j]) {
                    // 否则就需要看下标范围 [i,k] 和 [k+1,j] 的子串是不是分别为有效的括号字符串，是的话则 dp[i][j] 匹配
                    for (int k = i; k < j; k++) {
                        dp[i][j] = dp[i][k] && dp[k + 1][j];
                        if (dp[i][j]) {
                            break;
                        }
                    }
                }
            }
        }

        return dp[0][n - 1];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * 贪心法，参见：
     * https://leetcode-cn.com/problems/valid-parenthesis-string/solution/you-xiao-de-gua-hao-zi-fu-chuan-by-leetc-osi3/
     *
     *
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.3 MB - 40.25%
     */
    public boolean greedyMethod(String s) {
        int minCnt = 0, maxCnt = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                minCnt++;
                maxCnt++;
            } else if (c == ')') {
                minCnt = Math.max(minCnt - 1, 0);
                maxCnt--;
                if (maxCnt < 0) {
                    return false;
                }
            } else {
                minCnt = Math.max(minCnt - 1, 0);
                maxCnt++;
            }
        }

        return minCnt == 0;
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
