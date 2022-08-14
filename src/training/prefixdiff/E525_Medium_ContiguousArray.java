package training.prefixdiff;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 525. 连续数组: https://leetcode-cn.com/problems/contiguous-array/
 *
 * 给定一个二进制数组 nums , 找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
 *
 * 例 1：
 * 输入: nums = [0,1]
 * 输出: 2
 * 说明: [0, 1] 是具有相同数量 0 和 1 的最长连续子数组。
 *
 * 例 2：
 * 输入: nums = [0,1,0]
 * 输出: 2
 * 说明: [0, 1] (或 [1, 0]) 是具有相同数量0和1的最长连续子数组。
 *
 * 说明：
 * - 1 <= nums.length <= 10^5
 * - nums[i] 不是 0 就是 1
 */
public class E525_Medium_ContiguousArray {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(2, method.applyAsInt(new int[]{0, 1}));
        assertEquals(2, method.applyAsInt(new int[]{0, 1, 0}));
    }

    /**
     * 前缀和+HashMap。
     *
     * LeetCode 耗时：22 ms - 77.68%
     *          内存消耗：48 MB - 67.16%
     */
    public int findMaxLength(int[] nums) {
        int ans = 0;
        // diff 记录1的数量减去0的数量
        // prefix 记录不同diff值及其对应的最小下标
        Map<Integer, Integer> prefix = new HashMap<>();
        prefix.put(0, 0);
        for (int i = 0, diff = 0; i < nums.length; i++) {
            diff += nums[i] == 1 ? 1 : -1;
            if (prefix.containsKey(diff)) {
                ans = Math.max(ans, i + 1 - prefix.get(diff));
            } else {
                prefix.put(diff, i + 1);
            }
        }

        return ans;
    }

    @Test
    public void testFindMaxLength() {
        test(this::findMaxLength);
    }
}
