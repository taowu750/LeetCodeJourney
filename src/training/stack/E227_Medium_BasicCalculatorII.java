package training.stack;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.stack.E224_Hard_BasicCalculator.evaluateSuffix;
import static training.stack.E224_Hard_BasicCalculator.infixToSuffix;

/**
 * 227. 基本计算器 II: https://leetcode-cn.com/problems/basic-calculator-ii/
 *
 * 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
 * 整数除法仅保留整数部分。
 *
 * 例 1：
 * 输入：s = "3+2*2"
 * 输出：7
 *
 * 例 2：
 * 输入：s = " 3/2 "
 * 输出：1
 *
 * 例 3：
 * 输入：s = " 3+5 / 2 "
 * 输出：5
 *
 * 约束：
 * - 1 <= s.length <= 3 * 10**5
 * - s 由整数和算符 ('+', '-', '*', '/') 组成，中间由一些空格隔开
 * - s 表示一个有效表达式
 * - 表达式中的所有整数都是非负整数，且在范围 [0, 2**31 - 1] 内
 * - 题目数据保证答案是一个 32-bit 整数
 */
public class E227_Medium_BasicCalculatorII {

    static void test(ToIntFunction<String> method) {
        assertEquals(7, method.applyAsInt("3+2*2"));
        assertEquals(1, method.applyAsInt(" 3/2 "));
        assertEquals(5, method.applyAsInt(" 3+5 / 2 "));
    }

    /**
     * LeetCode 耗时：21 ms - 23.50%
     *          内存消耗：38.3 MB - 98.04%
     */
    public int calculate(String s) {
        return evaluateSuffix(infixToSuffix(s));
    }

    @Test
    public void testCalculate() {
        test(this::calculate);
    }
}
