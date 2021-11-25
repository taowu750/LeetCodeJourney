package training.math;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 60. 排列序列: https://leetcode-cn.com/problems/permutation-sequence/
 *
 * 给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
 * 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下：
 * 1. "123"
 * 2. "132"
 * 3. "213"
 * 4. "231"
 * 5. "312"
 * 6. "321"
 *
 * 给定 n 和 k，返回第 k 个排列。
 *
 * 例 1：
 * 输入：n = 3, k = 3
 * 输出："213"
 *
 * 例 2：
 * 输入：n = 4, k = 9
 * 输出："2314"
 *
 * 例 3：
 * 输入：n = 3, k = 1
 * 输出："123"
 *
 * 说明：
 * - 1 <= n <= 9
 * - 1 <= k <= n!
 */
public class E60_Hard_PermutationSequence {

    public static void test(BiFunction<Integer, Integer, String> method) {
        assertEquals("213", method.apply(3, 3));
        assertEquals("2314", method.apply(4, 9));
        assertEquals("123", method.apply(3, 1));
        assertEquals("1", method.apply(1, 1));
        assertEquals("132", method.apply(3, 2));
    }

    /**
     * n 个数，以每个数开头的子序列有 n 种，每种有 (n-1)! 个；在递归下去就是 n - 1 种子序列，每种有 (n-2)! 个。
     *
     * LeetCode 耗时：1 ms - 97.34%
     *          内存消耗：35.9 MB - 46.31%
     */
    public String getPermutation(int n, int k) {
        StringBuilder result = new StringBuilder();
        // (n-1)!
        int factorial = 1, base = n - 1;
        for (int i = 2; i < n; i++) {
            factorial *= i;
        }

        // 待选的数字，一个数字被使用了，以后就不能再用了
        List<Character> digits = new ArrayList<>(n + 1);
        for (int i = 0; i < n; i++) {
            digits.add((char) ('1' + i));
        }
        while (k > 0 && base > 0) {
            // 商作为选择数字的下标，remainder 作为下一次选择的 k
            int quotient = k / factorial, remainder = k % factorial;
            // 需要注意的是，当 remainder 等于 0，表示 k 刚好为 factorial，要做特殊处理
            if (remainder == 0) {
                quotient--;
                remainder = factorial;
            }
            // 添加数字
            result.append(digits.remove(quotient));
            // 下一序列
            factorial /= base;
            base--;
            k = remainder;
        }
        // 注意还需要要添加最后一个剩余的数字
        result.append(digits.remove(0));

        return result.toString();
    }

    @Test
    public void testGetPermutation() {
        test(this::getPermutation);
    }
}
