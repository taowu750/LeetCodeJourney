package training.stack;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.stack.E224_Hard_BasicCalculator.evaluateSuffix;
import static training.stack.E224_Hard_BasicCalculator.infixToSuffix;

/**
 * 772. 基本计算器 III: https://leetcode-cn.com/problems/basic-calculator-iii/
 *
 * 实现一个基本的计算器来计算简单的表达式字符串。
 * 表达式字符串只包含非负整数，算符 +、-、*、/ ，左括号 ( 和右括号 ) 。整数除法需要「向下截断」。
 * 你可以假定给定的表达式总是有效的。所有的中间结果的范围为 [-2**31, 2**31 - 1] 。
 *
 * 例 1：
 * 输入：s = "1+1"
 * 输出：2
 *
 * 例 2：
 * 输入：s = "6-4/2"
 * 输出：4
 *
 * 例 3：
 * 输入：s = "2*(5+5*2)/3+(6/2+8)"
 * 输出：21
 *
 * 例 4：
 * 输入：s = "(2+6*3+5-(3*14/7+2)*5)+3"
 * 输出：-12
 *
 * 例 5：
 * 输入：s = "0"
 * 输出：0
 *
 * 约束：
 * - 1 <= s <= 10**4
 * - s 由整数、'+'、'-'、'*'、'/'、'(' 和 ')' 组成
 * - s 是一个有效的表达式
 */
public class E772_Hard_BasicCalculatorIII {

    static void test(ToIntFunction<String> method) {
        assertEquals(2, method.applyAsInt("1+1"));
        assertEquals(4, method.applyAsInt("6-4/2"));
        assertEquals(21, method.applyAsInt("2*(5+5*2)/3+(6/2+8)"));
        assertEquals(-12, method.applyAsInt("(2+6*3+5-(3*14/7+2)*5)+3"));
        assertEquals(0, method.applyAsInt("0"));
    }

    /**
     * LeetCode 耗时：6 ms - 51.96%
     *          内存消耗：36.9 MB - 89.35%
     */
    public int calculate(String s) {
        return evaluateSuffix(infixToSuffix(s));
    }

    @Test
    public void testCalculate() {
        test(this::calculate);
    }
}
