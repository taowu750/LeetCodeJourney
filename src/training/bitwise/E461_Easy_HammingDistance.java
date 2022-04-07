package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 461. 汉明距离: https://leetcode-cn.com/problems/hamming-distance/
 *
 * 两个整数之间的「汉明距离」指的是这两个数字对应二进制位不同的位置的数目。
 * 给你两个整数 x 和 y，计算并返回它们之间的汉明距离。
 *
 * 例 1：
 * 输入：x = 1, y = 4
 * 输出：2
 * 解释：
 * 1   (0 0 0 1)
 * 4   (0 1 0 0)
 *        ↑   ↑
 * 上面的箭头指出了对应二进制位不同的位置。
 *
 * 例 2：
 * 输入：x = 3, y = 1
 * 输出：1
 *
 * 约束：
 * - 0 <= x, y <= 2^31 - 1
 */
public class E461_Easy_HammingDistance {

    public static void test(IntBinaryOperator method) {
        assertEquals(2, method.applyAsInt(1, 4));
        assertEquals(1, method.applyAsInt(3, 1));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.3 MB - 34.83%
     */
    public int hammingDistance(int x, int y) {
        int n = x ^ y;

        n = n - ((n >>> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        n = (n + (n >>> 4)) & 0x0f0f0f0f;
        n += n >>> 8;
        n += n >>> 16;

        return n & 0x3f;
    }

    @Test
    public void testHammingDistance() {
        test(this::hammingDistance);
    }
}
