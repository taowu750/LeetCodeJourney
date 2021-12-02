package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 306. 累加数: https://leetcode-cn.com/problems/additive-number/
 *
 * 累加数是一个字符串，组成它的数字可以形成累加序列。
 * 一个有效的累加序列必须「至少」包含 3 个数。除了最开始的两个数以外，字符串中的其他数都等于它之前两个数相加的和。
 *
 * 给定一个只包含数字 '0'-'9' 的字符串，编写一个算法来判断给定输入是否是累加数。
 *
 * 说明: 累加序列里的数不会以 0 开头，所以不会出现 1, 2, 03 或者 1, 02, 3 的情况。
 *
 * 进阶：你如何处理一个溢出的过大的整数输入?
 *
 * 例 1：
 * 输入: "112358"
 * 输出: true
 * 解释: 累加序列为: 1, 1, 2, 3, 5, 8 。1 + 1 = 2, 1 + 2 = 3, 2 + 3 = 5, 3 + 5 = 8
 *
 * 例 2：
 * 输入: "199100199"
 * 输出: true
 * 解释: 累加序列为: 1, 99, 100, 199。1 + 99 = 100, 99 + 100 = 199
 */
public class E306_Medium_AdditiveNumber {

    public static void test(Predicate<String> method) {
        assertTrue(method.test("112358"));
        assertTrue(method.test("199100199"));
        assertTrue(method.test("011"));
        assertFalse(method.test("10"));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.1 MB - 77.44%
     */
    public boolean isAdditiveNumber(String num) {
        /*
        确定累加数分为两步：
        1. 先判断出起始的 a、b、c 三个数，其中 a + b = c
        2. 然后不断判断 c 的后面有没有等于 b + c 的数
        需要注意，如果一个数是 0，那么它只能单独做一个数字或者拼到前面的数字后面
         */
        char[] numChars = num.toCharArray();
        int ale = 0, ari = 0;
        while (ari < numChars.length){
            int ble = ari + 1, bri = ble;
            while (bri < numChars.length){
                if (isValid(numChars, ale, ari, ble, bri)) {
                    return true;
                }
                if (numChars[ble] == '0' || numChars.length - bri < Math.max(ari - ale, bri - ble) + 1) {
                    break;
                }
                bri++;
            }
            if (numChars[ale] == '0') {
                break;
            }
            ari++;
        }

        return false;
    }

    /**
     * 判断 a、b 两数是否可以作为合法结果的开头
     */
    private boolean isValid(char[] numChars, int ale, int ari, int ble, int bri) {
        int sumLen = eq(numChars, ale, ari, ble, bri);
        while (sumLen > 0) {
            ale = ble;
            ari = bri;
            ble = bri + 1;
            bri = bri + sumLen;
            if (bri == numChars.length - 1) {
                return true;
            }
            sumLen = eq(numChars, ale, ari, ble, bri);
        }

        return false;
    }

    /**
     * 计算 ale 到 ari 表示的数和 ble 到 bri 表示的数相加是否等于后面的数。
     * 如果等于，返回和的长度，否则返回 0
     */
    private int eq(char[] numChars, int ale, int ari, int ble, int bri) {
        // sum 保存 a + b 和的倒序
        char[] sum = new char[Math.max(ari - ale, bri - ble) + 2];
        int si = 0;
        int carry = 0;
        for (int ai = ari, bi = bri; ai >= ale || bi >= ble;) {
            int add;
            if (ai < ale) {
                add = carry + (numChars[bi--] - '0');
            } else if (bi < ble) {
                add = carry + (numChars[ai--] - '0');
            } else {
                add = carry + (numChars[ai--] - '0') + (numChars[bi--] - '0');
            }
            sum[si++] = (char) ('0' + add % 10);
            carry = add / 10;
        }
        if (carry != 0) {
            sum[si++] = (char) (carry + '0');
        }

        if (bri + si >= numChars.length) {
            return 0;
        }

        for (int i = bri + 1, j = si - 1; j >= 0; i++, j--) {
            if (numChars[i] != sum[j]) {
                return 0;
            }
        }

        return si;
    }

    @Test
    public void testIsAdditiveNumber() {
        test(this::isAdditiveNumber);
    }
}
