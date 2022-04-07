package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 338. 比特位计数: https://leetcode-cn.com/problems/counting-bits/
 *
 * 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回。
 *
 * 例 1：
 * 输入: 2
 * 输出: [0,1,1]
 *
 * 例 2：
 * 输入: 5
 * 输出: [0,1,1,2,1,2]
 *
 * 约束：
 * - 给出时间复杂度为 O(n*sizeof(integer)) 的解答非常容易。但你可以在线性时间 O(n) 内用一趟扫描做到吗？
 * - 要求算法的空间复杂度为O(n)。
 * - 你能进一步完善解法吗？要求在C++或任何其他语言中不使用任何内置函数。
 */
public class E338_Easy_CountingBits {

    public static void test(IntFunction<int[]> method) {
        assertArrayEquals(new int[]{0, 1, 1}, method.apply(2));
        assertArrayEquals(new int[]{0,1,1,2,1,2}, method.apply(5));
    }

    /**
     * 动态规划-最高有效位。参见：
     * https://leetcode-cn.com/problems/counting-bits/solution/bi-te-wei-ji-shu-by-leetcode-solution-0t1i/
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：42.6 MB - 19.49%
     */
    public int[] countBits(int n) {
        int[] result = new int[n + 1];
        int mostSignificantBit = 0;

        for (int i = 1; i <= n; i++) {
            if ((i & (i - 1)) == 0) {
                mostSignificantBit = i;
                result[i] = 1;
            } else {
                result[i] = result[i - mostSignificantBit] + 1;
            }
        }

        return result;
    }

    @Test
    public void testCountBits() {
        test(this::countBits);
    }


    /**
     * 动态规划-最低有效位。参见：
     * https://leetcode-cn.com/problems/counting-bits/solution/bi-te-wei-ji-shu-by-leetcode-solution-0t1i/
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：42.6 MB - 19.49%
     */
    public int[] dpLowerSignificantBit(int n) {
        int[] result = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            result[i] = result[i >>> 1] + (i & 1);
        }

        return result;
    }

    @Test
    public void testDpLeastSignificantBit() {
        test(this::dpLowerSignificantBit);
    }


    /**
     * 动态规划-最低设置位。参见：
     * https://leetcode-cn.com/problems/counting-bits/solution/bi-te-wei-ji-shu-by-leetcode-solution-0t1i/
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：42.6 MB - 19.49%
     */
    public int[] dpLowerSettingBit(int n) {
        int[] result = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            result[i] = result[i & (i - 1)] + 1;
        }

        return result;
    }

    @Test
    public void testDpLowerSettingBit() {
        test(this::dpLowerSettingBit);
    }
}
