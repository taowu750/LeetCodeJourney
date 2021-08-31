package training.math;

import org.junit.jupiter.api.Test;
import util.CollectionUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 89. 格雷编码: https://leetcode-cn.com/problems/gray-code/
 *
 * 格雷编码是一个二进制数字系统，在该系统中，两个连续的数值仅有一个位数的差异。
 * 给定一个代表编码总位数的非负整数 n，打印其格雷编码序列。即使有多个不同答案，你也只需要返回其中一种。
 *
 * 格雷编码序列必须以 0 开头。
 *
 * 例 1：
 * 输入: 2
 * 输出: [0,1,3,2]
 * 解释:
 * 00 - 0
 * 01 - 1
 * 11 - 3
 * 10 - 2
 *
 * 对于给定的 n，其格雷编码序列并不唯一。
 * 例如，[0,2,3,1] 也是一个有效的格雷编码序列。
 * 00 - 0
 * 10 - 2
 * 11 - 3
 * 01 - 1
 *
 * 例 2：
 * 输入: 0
 * 输出: [0]
 * 解释: 我们定义格雷编码序列必须以 0 开头。
 *      给定编码总位数为 n 的格雷编码序列，其长度为 2n。当 n = 0 时，长度为 20 = 1。
 *      因此，当 n = 0 时，其格雷编码序列为 [0]。
 */
public class E89_Medium_GrayCode {

    static void test(IntFunction<List<Integer>> method) {
        //noinspection unchecked
        CollectionUtil.in(method.apply(2), Arrays.asList(0, 1, 3, 2), Arrays.asList(0, 2, 3, 1));
        assertEquals(Collections.singletonList(0), method.apply(0));
        assertEquals(Arrays.asList(0, 1, 3, 2, 6, 7, 5, 4), method.apply(3));
    }

    /**
     * 0 1
     * 00 01 11 10
     * 000 001 011 010 110 111 101 100
     *
     * 注意到 n 位的格雷码就是 n - 1 位格雷码倒过来排列并且每个数最高位加 1。
     *
     * LeetCode 耗时：4 ms - 97.96%
     *          内存消耗：44.2 MB - 98.53%
     */
    public List<Integer> grayCode(int n) {
        if (n == 0) {
            return Collections.singletonList(0);
        }

        Integer[] result = new Integer[1 << n];
        result[0] = 0;
        result[1] = 1;
        for (int len = 2, shift = 1; len < result.length; len *= 2, shift++) {
            for (int i = 0; i < len; i++) {
                result[i + len] = result[len - 1 - i] + (1 << shift);
            }
        }

        return Arrays.asList(result);
    }

    @Test
    public void testGrayCode() {
        test(this::grayCode);
    }
}
