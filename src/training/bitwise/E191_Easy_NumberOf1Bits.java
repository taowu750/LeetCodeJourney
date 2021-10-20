package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明权重）。
 *
 * 请注意，在某些语言（如 Java）中，没有无符号整数类型。在这种情况下，输入和输出都将被指定为有符号整数类型，
 * 并且不应影响您的实现，因为无论整数是有符号的还是无符号的，其内部的二进制表示形式都是相同的。
 *
 * 在 Java 中，编译器使用二进制补码记法来表示有符号整数。因此，在下面的「示例 3」中，输入表示有符号整数 -3。
 *
 * 例 1：
 * 输入：00000000000000000000000000001011
 * 输出：3
 * 解释：输入的二进制串 00000000000000000000000000001011 中，共有三位为 '1'。
 *
 * 例 2：
 * 输入：00000000000000000000000010000000
 * 输出：1
 * 解释：输入的二进制串 00000000000000000000000010000000 中，共有一位为 '1'。
 *
 * 例 3：
 * 输入：11111111111111111111111111111101
 * 输出：31
 * 解释：输入的二进制串 11111111111111111111111111111101 中，共有 31 位为 '1'。
 */
public class E191_Easy_NumberOf1Bits {

    static void test(IntUnaryOperator method) {
        assertEquals(method.applyAsInt(0b00000000000000000000000000001011), 3);
        assertEquals(method.applyAsInt(0b00000000000000000000000010000000), 1);
        assertEquals(method.applyAsInt(0b11111111111111111111111111111101), 31);
    }

    /**
     * LeetCode 耗时：1 ms - 95.92%
     *          内存消耗：35.4 MB - 50.07%
     */
    public int hammingWeight(int n) {
        int cnt = 0;
        // 此方法利用了 n & (n - 1) 会去掉 n 最后一位 1 的位运算特性
        while (n != 0) {
            n &= n - 1;
            cnt++;
        }
        return cnt;
    }

    @Test
    public void testHammingWeight() {
        test(this::hammingWeight);
    }


    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.4 MB - 31.80%
     */
    public int jdkMethod(int n) {
        n = n - ((n >>> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        n = (n + (n >>> 4)) & 0x0f0f0f0f;
        n += n >>> 8;
        n += n >>> 16;

        return n & 0x3f;
    }

    @Test
    public void testJdkMethod() {
        test(this::jdkMethod);
    }
}
