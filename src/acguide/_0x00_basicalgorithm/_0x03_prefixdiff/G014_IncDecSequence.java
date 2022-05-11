package acguide._0x00_basicalgorithm._0x03_prefixdiff;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * IncDec Sequence: https://ac.nowcoder.com/acm/contest/999/B
 *
 * 给定一个长度为 n(n ≤ 10^5) 的数列 a_1,a_2,···,a_n, 每次可以选择一个区间 [l,r]，
 * 使下标在这个区内间的数都加1或者都减1。
 *
 * 求至少需要多少次操作才能使数列中的所有数都一样，并求出在保证最少次数的前提下，最终得到的数列可能有多少种。
 *
 * 例 1：
 * 输入：
 * [1,1,2,2]
 * 输出：
 * 1 2
 * 解释：
 * 输出两个数字，分别是最少操作次数和最终能得到多少种结果
 *
 * 说明：
 * - 0 ≤ a_i < 2147483648
 */
public class G014_IncDecSequence {

    public static void test(Function<int[], long[]> method) {
        assertArrayEquals(new long[]{1, 2}, method.apply(new int[]{1,1,2,2}));
        assertArrayEquals(new long[]{4, 4}, method.apply(new int[]{1,4,3,4}));
        assertArrayEquals(new long[]{2147483668L, 2147483617}, method.apply(new int[]{31, 87, 35, 74, 78, 2147483647}));
    }

    public long[] operation(int[] nums) {
        final int n = nums.length;
        /*
        这个问题可以转化为：对 nums 的差分数组 diff，至少多少次可以将 diff 变成 [num, 0, 0, ..., 0] 的序列。
        这样原来的区间操作转化为：
        1. [l,r]+1，相当于 diff[l]++，diff[r+1]--（r < n-1）；
        2. [l,r]-1，相当于 diff[l]--，diff[r+1]++（r < n-1）。

        先要找到一正一负的两个数（除开头），这样可以一次操作改两个数；
        等到除了 diff[0] 之外都是正数或都是负数，则最终结果数为 sum(abs(nonzero))+1
         */
        int[] diff = new int[n];
        diff[0] = nums[0];
        for (int i = 1; i < n; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }

        long[] ans = {0, 1};
        for (int i = 1, j = 1;;) {
            // 找到第一个不等于 0 的数
            for (; i < n && diff[i] == 0; i++) {}
            if (i == n) {
                break;
            }
            // 找到第一个和 diff[i] 异号的数
            for (int negSignI = Integer.compare(0, diff[i]); j < n && Integer.compare(diff[j], 0) != negSignI; j++) {}
            if (j == n) {
                break;
            }
            // 将其中一个数变成 0，如果两个数绝对值相等，则都变成 0
            int change = Math.min(Math.abs(diff[i]), Math.abs(diff[j]));
            ans[0] += change;
            diff[i] = diff[i] > 0 ? diff[i] - change : diff[i] + change;
            diff[j] = diff[j] > 0 ? diff[j] - change : diff[j] + change;
            // 为下一次循环做准备
            if (diff[i] == 0 && diff[j] == 0) {  // 都等于 0，都重新找
                i++;
                j++;
            } else if (diff[i] == 0) {  // diff[i] 等于 0，把它和 j 换一下，j 重新找
                int tmp = i;
                i = j;
                j = tmp + 1;
            } else {  // diff[j] 等于 0，j 重新找
                j++;
            }
        }
        // 最终结果数为 sum(abs(nonzero))+1
        for (int i = 1; i < n; i++) {
            ans[0] += Math.abs(diff[i]);
            ans[1] += Math.abs(diff[i]);
        }

        return ans;
    }

    @Test
    public void testOperation() {
        test(this::operation);
    }


    public long[] betterMethod(int[] nums) {
        final int n = nums.length;
        int[] diff = new int[n];
        diff[0] = nums[0];
        for (int i = 1; i < n; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }

        long p = 0, q = 0;
        for (int i = 1; i < n; i++) {
            if (diff[i] >= 0) {
                p += diff[i];
            } else {
                q += -diff[i];
            }
        }

        return new long[]{Math.max(p, q), Math.abs(p - q) + 1};
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
