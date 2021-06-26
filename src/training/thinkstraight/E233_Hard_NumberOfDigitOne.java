package training.thinkstraight;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 233. 数字 1 的个数: https://leetcode-cn.com/problems/number-of-digit-one/
 *
 * 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
 *
 * 例 1：
 * 输入：n = 13
 * 输出：6
 *
 * 例 2：
 * 输入：n = 0
 * 输出：0
 *
 * 约束：
 * - 0 <= n <= 2 * 10**9
 */
public class E233_Hard_NumberOfDigitOne {

    static void test(IntUnaryOperator method) {
        assertEquals(6, method.applyAsInt(13));
        assertEquals(0, method.applyAsInt(0));
        assertEquals(316, method.applyAsInt(1011));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.2 MB - 45.66%
     */
    public int countDigitOne(int n) {
        return recur(n, calcTopTen(n), new HashMap<>());
    }

    int recur(int n, int topTen, Map<Integer, Integer> memory) {
        if (n == 0) {
            return 0;
        } else if (n <= 9) {
            return 1;
        }

        /*
        计算 n 的下级数字的 1 的个数。这个计算结果会被多次用到，所以存起来避免重复计算。
        例如：
        132 的下级数字是 99
        2341 的下级数字是 999
         */
        int subOneNums;
        if (memory.containsKey(topTen)) {
            subOneNums = memory.get(topTen);
        } else {
            subOneNums = recur(topTen - 1, topTen / 10, memory);
            memory.put(topTen, subOneNums);
        }

        int topNum = n / topTen, modNum = n % topTen;
        // n 的最高位数字 topNum 乘以下级数字的 1 的个数
        int totalOneNums = topNum * subOneNums;
        // 如果 topNum 大于 1，还需要加上 topTen，否则加上余数 + 1
        if (topNum > 1) {
            totalOneNums += topTen;
        } else {
            totalOneNums += modNum + 1;
        }
        // 递归计算余数的 1 的个数
        totalOneNums += recur(modNum, calcTopTen(modNum), memory);

        return totalOneNums;
    }

    /**
     * 计算和 n 相同位数的 10 次幂。例如：
     * - 13: 10
     * - 341: 100
     * - 2453: 1000
     */
    private int calcTopTen(int n) {
        int topTen = 1, temp = n;
        while (temp / 10 > 0) {
            topTen *= 10;
            temp /= 10;
        }

        return topTen;
    }

    @Test
    public void testCountDigitOne() {
        test(this::countDigitOne);
    }
}
