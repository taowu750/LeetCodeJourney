package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 670. 最大交换: https://leetcode-cn.com/problems/maximum-swap/
 *
 * 给定一个非负整数，你「至多」可以交换一次数字中的任意两位。返回你能得到的最大值。
 *
 * 例 1：
 * 输入: 2736
 * 输出: 7236
 * 解释: 交换数字2和数字7。
 *
 * 例 2：
 * 输入: 9973
 * 输出: 9973
 * 解释: 不需要交换。
 *
 * 说明：
 * - 给定数字的范围是 [0, 10^8]
 */
public class E670_Medium_MaximumSwap {

    public static void test(IntUnaryOperator method) {
        assertEquals(7236, method.applyAsInt(2736));
        assertEquals(9973, method.applyAsInt(9973));
        assertEquals(9723, method.applyAsInt(3729));
        assertEquals(9923, method.applyAsInt(3929));
        assertEquals(10, method.applyAsInt(10));
        assertEquals(7, method.applyAsInt(7));
        assertEquals(98863, method.applyAsInt(98368));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.2 MB - 63.90%
     */
    public int maximumSwap(int num) {
        if (num <= 11) {
            return num;
        }

        char[] s = Integer.toString(num).toCharArray();
        int exchIdx = -1, maxIdx = -1;
        // 从右往左遍历，找到每个下标 i 和它后面的最大值，并判断它们是否需要交换。
        // 最终我们需要找到最左边可以交换的数字对
        for (int i = s.length - 2, mx = s.length - 1; i >= 0; i--) {
            if (s[i] < s[mx]) {
                exchIdx = i;
                maxIdx = mx;
            } else if (s[i] > s[mx]) {
                mx = i;
            }
        }

        if (exchIdx != -1) {
            int diff = s[maxIdx] - s[exchIdx];
            return num + diff * (int) Math.pow(10, s.length - exchIdx - 1)
                    - diff * (int) Math.pow(10, s.length - maxIdx - 1);
        } else {
            return num;
        }
    }

    @Test
    public void testMaximumSwap() {
        test(this::maximumSwap);
    }
}
