package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 201. 数字范围按位与: https://leetcode-cn.com/problems/bitwise-and-of-numbers-range/
 *
 * 给你两个整数 left 和 right ，表示区间 [left, right] ，返回此区间内所有数字「按位与」的结果（包含 left 、right 端点）。
 *
 * 例 1：
 * 输入：left = 5, right = 7
 * 输出：4
 *
 * 例 2：
 * 输入：left = 0, right = 0
 * 输出：0
 *
 * 例 3：
 * 输入：left = 1, right = 2147483647
 * 输出：0
 *
 * 说明：
 * - 0 <= left <= right <= 2^31 - 1
 */
public class E201_Medium_BitwiseANDOfNumbersRange {

    public static void test(IntBinaryOperator method) {
        assertEquals(4, method.applyAsInt(5, 7));
        assertEquals(0, method.applyAsInt(0, 0));
        assertEquals(0, method.applyAsInt(1, 2147483647));
    }

    /**
     * 参见：https://leetcode-cn.com/problems/bitwise-and-of-numbers-range/solution/shu-zi-fan-wei-an-wei-yu-by-leetcode-solution/
     *
     * 我们可以发现，对所有数字执行按位与运算的结果是所有对应二进制字符串的公共前缀再用零补上后面的剩余位。
     * 假如没有公共前缀，例如有一个数字 i 多了一个进位，因为数字是连续的，i 后面的位数就都是 0，那么与运算后就全为 0。
     *
     * 那么这个规律是否正确呢？我们可以进行简单的证明。假设对于所有这些二进制串，前 i 位均相同，第 i+1 位开始不同，
     * 由于 [m,n] 连续，所以第 i+1 位在 [m,n] 的数字范围从小到大列举出来一定是前面全部是 0，后面全部是 1（注意 1000>0111），
     * 在上图中对应 [9,11] 均为 0，[12,12] 均为 1。并且一定存在连续的两个数 x 和 x+1，满足 x 的第 i+1 位为 0，
     * 后面全为 1，x+1 的第 i+1 位为 1，后面全为 0，对应上图中的例子即为 11 和 12。
     * 这种形如 0111… 和 1000… 的二进制串的按位与的结果一定为 0000…，因此第 i+1 位开始的剩余位均为 0，
     * 前 i 位由于均相同，因此按位与结果不变。最后的答案即为二进制字符串的公共前缀再用零补上后面的剩余位。
     *
     * LeetCode 耗时：3 ms - 100.00%
     *          内存消耗：40.8 MB - 23.31%
     */
    public int rangeBitwiseAnd(int left, int right) {
        // 逆向思维，缩减 right 来逼近前缀
        while (left < right) {
            right &= right - 1;
        }
        return right;
    }

    @Test
    public void testRangeBitwiseAnd() {
        test(this::rangeBitwiseAnd);
    }
}
