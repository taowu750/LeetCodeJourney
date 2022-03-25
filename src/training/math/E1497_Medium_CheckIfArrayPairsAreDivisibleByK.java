package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 1497. 检查数组对是否可以被 k 整除: https://leetcode-cn.com/problems/check-if-array-pairs-are-divisible-by-k/
 *
 * 给你一个整数数组 arr 和一个整数 k ，其中数组长度是偶数，值为 n 。
 *
 * 现在需要把数组恰好分成 n/2 对，以使每对数字的和都能够被 k 整除。
 *
 * 如果存在这样的分法，请返回 True ；否则，返回 False 。
 *
 * 例 1：
 * 输入：arr = [1,2,3,4,5,10,6,7,8,9], k = 5
 * 输出：true
 * 解释：划分后的数字对为 (1,9),(2,8),(3,7),(4,6) 以及 (5,10) 。
 *
 * 例 2：
 * 输入：arr = [1,2,3,4,5,6], k = 7
 * 输出：true
 * 解释：划分后的数字对为 (1,6),(2,5) 以及 (3,4) 。
 *
 * 例 3：
 * 输入：arr = [1,2,3,4,5,6], k = 10
 * 输出：false
 * 解释：无法在将数组中的数字分为三对的同时满足每对数字和能够被 10 整除的条件。
 *
 * 例 4：
 * 输入：arr = [-10,10], k = 2
 * 输出：true
 *
 * 例 5：
 * 输入：arr = [-1,1,-2,2,-3,3,-4,4], k = 3
 * 输出：true
 *
 * 说明：
 * - arr.length == n
 * - 1 <= n <= 10^5
 * - n 为偶数
 * - -10^9 <= arr[i] <= 10^9
 * - 1 <= k <= 10^5
 */
public class E1497_Medium_CheckIfArrayPairsAreDivisibleByK {

    public static void test(BiPredicate<int[], Integer> method) {
        assertTrue(method.test(new int[]{1,2,3,4,5,10,6,7,8,9}, 5));
        assertTrue(method.test(new int[]{1,2,3,4,5,6}, 7));
        assertFalse(method.test(new int[]{1,2,3,4,5,6}, 10));
        assertTrue(method.test(new int[]{-10,10}, 2));
        assertTrue(method.test(new int[]{-1,1,-2,2,-3,3,-4,4}, 3));
    }

    /**
     * LeetCode 耗时：3 ms - 100.00%
     *          内存消耗：56.2 MB - 11.56%
     */
    public boolean canArrange(int[] arr, int k) {
        long sum = 0;
        for (int i : arr) {
            sum += i;
        }
        if (sum % k != 0) {
            return false;
        }

        // 利用模运算对数字分组
        int[] remain2cnt = new int[k];
        for (int i : arr) {
            // 对于正数，((i % k) + k) % k 等同于 i % k；而对于负数，此公式会计算为正数
            remain2cnt[((i % k) + k) % k]++;
        }
        /*
        例如 k 等于 5，那么取模后，就只会有 0、1、2、3、4
        其中 0 只能和 0 配对，1 和 4 配对，2 和 3 配对

        注意 k 为偶数的情况，例如 k 等于 4，那么取模后，就只会有 0、1、2、3
        其中 0 只能和 0 配对，1 和 3 配对，2 和 2 配对
         */
        if (remain2cnt[0] % 2 != 0) {
            return false;
        }
        for (int i = 1; i <= k / 2; i++) {
            if (remain2cnt[i] != remain2cnt[k - i]) {
                return false;
            }
        }
        return k % 2 != 0 || remain2cnt[k / 2] % 2 == 0;
    }

    @Test
    public void testCanArrange() {
        test(this::canArrange);
    }
}
