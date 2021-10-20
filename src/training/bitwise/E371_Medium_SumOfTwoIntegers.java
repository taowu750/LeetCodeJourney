package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 371. 两整数之和: https://leetcode-cn.com/problems/sum-of-two-integers/
 *
 * 给你两个整数 a 和 b ，不使用运算符 + 和 -，计算并返回两整数之和。
 *
 * 例 1：
 * 输入：a = 1, b = 2
 * 输出：3
 *
 * 例 2：
 * 输入：a = 2, b = 3
 * 输出：5
 *
 * 提示：
 * - -1000 <= a, b <= 1000
 */
public class E371_Medium_SumOfTwoIntegers {

    public static void test(IntBinaryOperator method) {
        assertEquals(3, method.applyAsInt(1, 2));
        assertEquals(5, method.applyAsInt(2, 3));
        assertEquals(-74, method.applyAsInt(-101, 27));
    }

    /**
     * 考虑两个二进制位相加的四种情况如下：
     *      0 + 0 = 0
     *      0 + 1 = 1
     *      1 + 0 = 1
     *      1 + 1 = 0 (进位)
     *
     * 可以发现，对于整数 aa 和 bb：
     * - 在不考虑进位的情况下，其无进位加法结果为 a^b。
     * - 而所有需要进位的位为 a&b，进位后的进位结果为 (a&b)<<1。
     *
     * 于是，我们可以将整数 a 和 b 的和，拆分为 a 和 b 的无进位加法结果与进位结果的和。
     * 因为每一次拆分都可以让需要进位的最低位至少左移一位，又因为 a 和 b 可以取到负数，
     * 所以我们最多需要 log(max_int) 次拆分即可完成运算。
     */
    public int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }

        return a;
    }

    @Test
    public void testGetSum() {
        test(this::getSum);
    }
}
