package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 190. 颠倒二进制位: https://leetcode-cn.com/problems/reverse-bits/
 *
 * 颠倒给定的 32 位无符号整数的二进制位。
 * 提示：
 * - 请注意，在某些语言（如 Java）中，没有无符号整数类型。在这种情况下，输入和输出都将被指定为有符号整数类型，
 *   并且不应影响您的实现，因为无论整数是有符号的还是无符号的，其内部的二进制表示形式都是相同的。
 * - 在 Java 中，编译器使用二进制补码记法来表示有符号整数。因此，在 示例 2 中，输入表示有符号整数 -3，
 *   输出表示有符号整数 -1073741825。
 *
 * 进阶: 如果多次调用这个函数，你将如何优化你的算法？
 *
 * 例 1：
 * 输入：n = 00000010100101000001111010011100
 * 输出：964176192 (00111001011110000010100101000000)
 * 解释：输入的二进制串 00000010100101000001111010011100 表示无符号整数 43261596，
 *      因此返回 964176192，其二进制表示形式为 00111001011110000010100101000000。
 *
 * 例 2：
 * 输入：n = 11111111111111111111111111111101
 * 输出：3221225471 (10111111111111111111111111111111)
 * 解释：输入的二进制串 11111111111111111111111111111101 表示无符号整数 4294967293，
 *      因此返回 3221225471 其二进制表示形式为 10111111111111111111111111111111 。
 */
public class E190_Easy_ReverseBits {

    public static void test(IntUnaryOperator method) {
        assertEquals(964176192, method.applyAsInt(43261596));
        assertEquals(-1073741825, method.applyAsInt(-3));
    }

    /**
     * 使用 lowbit 和计算2次幂指数的技巧。
     *
     * LeetCode 耗时：1 ms - 21.77%
     *          内存消耗：40.5 MB - 63.26%
     */
    public int reverseBits(int n) {
        int ans = 0;
        for (; n != 0; n &= n - 1) {
            int lowbit = n & -n;
            ans += 1 << (31 - log2(Integer.toUnsignedLong(lowbit)));
        }

        return ans;
    }

    private static final int[] H = new int[37];
    static {
        for (int i = 0; i < 36; i++) {
            H[(int) ((1L << i) % 37)] = i;
        }
    }

    public static int log2(long n) {
        return H[(int) (n % 37)];
    }

    @Test
    public void testReverseBits() {
        test(this::reverseBits);
    }


    /**
     * jdk 归并算法。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：40.5 MB - 63.26%
     */
    public int jdkMethod(int n) {
        n = (n & 0x55555555) << 1 | (n >>> 1 & 0x55555555);
        n = (n & 0x33333333) << 2 | (n >>> 2 & 0x33333333);
        n = (n & 0x0f0f0f0f) << 4 | (n >>> 4 & 0x0f0f0f0f);
        return (n << 24) | ((n & 0xff00) << 8) | ((n & 0xff0000) >>> 8) | (n >>> 24);
    }

    @Test
    public void testJdkMethod() {
        test(this::jdkMethod);
    }
}
