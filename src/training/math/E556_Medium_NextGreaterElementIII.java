package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 556. 下一个更大元素 III: https://leetcode-cn.com/problems/next-greater-element-iii/
 *
 * 给你一个正整数 n ，请你找出符合条件的最小整数，其由重新排列 n 中存在的每位数字组成，并且其值大于 n 。
 * 如果不存在这样的正整数，则返回 -1 。
 *
 * 注意 ，返回的整数应当是一个 32 位整数 ，如果存在满足题意的答案，但不是 32 位整数 ，同样返回 -1 。
 *
 * 例 1：
 * 输入：n = 12
 * 输出：21
 *
 * 例 2：
 * 输入：n = 21
 * 输出：-1
 *
 * 说明：
 * - 1 <= n <= 2^31 - 1
 */
public class E556_Medium_NextGreaterElementIII {

    public static void test(IntUnaryOperator method) {
        assertEquals(21, method.applyAsInt(12));
        assertEquals(-1, method.applyAsInt(21));
        assertEquals(-1, method.applyAsInt(Integer.MAX_VALUE));
        assertEquals(-1, method.applyAsInt(11));
        assertEquals(13124, method.applyAsInt(12431));
    }

    /**
     * 参见 {@link E31_Medium_NextPermutation}。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.3 MB - 40.63%
     */
    public int nextGreaterElement(int n) {
        if (n <= 9) {
            return -1;
        }

        char[] s = Integer.toString(n).toCharArray();
        int i = s.length - 1;
        // 跳过降序的序列
        for (; i > 0 && s[i - 1] >= s[i]; i--);

        // 整个序列都是降序
        if (i == 0) {
            return -1;
        } else {
            // 反转后面的降序序列
            for (int j = i, k = s.length - 1; j < k; j++, k--) {
                char tmp = s[j];
                s[j] = s[k];
                s[k] = tmp;
            }
            // 找到刚好大于 s[i - 1] 的数
            int lo = i, hi = s.length;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (s[mid] <= s[i - 1]) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            // 交换
            char tmp = s[i - 1];
            s[i - 1] = s[lo];
            s[lo] = tmp;

            long result = Long.parseLong(String.valueOf(s));
            return result <= Integer.MAX_VALUE ? (int) result : -1;
        }
    }

    @Test
    public void testNextGreaterElement() {
        test(this::nextGreaterElement);
    }
}
