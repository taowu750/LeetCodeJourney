package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 剑指 Offer 46. 把数字翻译成字符串: https://leetcode-cn.com/problems/ba-shu-zi-fan-yi-cheng-zi-fu-chuan-lcof/
 *
 * 给定一个数字，我们按照如下规则把它翻译为字符串：0 翻译成 “a” ，1 翻译成 “b”，……，11 翻译成 “l”，……，25 翻译成 “z”。
 * 一个数字可能有多个翻译。请编程实现一个函数，用来计算一个数字有多少种不同的翻译方法。
 *
 * 例 1：
 * 输入: 12258
 * 输出: 5
 * 解释: 12258有5种不同的翻译，分别是"bccfi", "bwfi", "bczi", "mcfi"和"mzi"
 *
 * 约束：
 * - 0 <= num < 2**31
 */
public class Offer46_Medium_TranslateNumbersIntoStrings {

    public static void test(IntUnaryOperator method) {
        assertEquals(5, method.applyAsInt(12258));
        assertEquals(2, method.applyAsInt(18580));
        assertEquals(1, method.applyAsInt(506));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.4 MB - 17.60%
     */
    public int translateNum(int num) {
        count = 0;
        dfs(num);

        return count;
    }

    private int count;

    private void dfs(int num) {
        if (num == 0) {
            count++;
            return;
        }

        int quotient = num / 10;
        dfs(quotient);
        // 只有当最后两位数小于等于 25 且不等于最后一位数时，才继续递归
        if (quotient > 0 && num % 100 <= 25 && (num % 100 != num % 10)) {
            dfs(quotient / 10);
        }
    }

    @Test
    public void testTranslateNum() {
        test(this::translateNum);
    }
}
