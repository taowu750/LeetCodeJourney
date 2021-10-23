package training.hash_pq;

import org.junit.jupiter.api.Test;
import util.datastructure.function.ToIntQuaFunction;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 454. 四数相加 II：https://leetcode-cn.com/problems/4sum-ii/
 *
 * 给你四个整数数组 nums1、nums2、nums3 和 nums4 ，数组长度都是 n ，请你计算有多少个元组 (i, j, k, l) 能满足：
 * - 0 <= i, j, k, l < n
 * - nums1[i] + nums2[j] + nums3[k] + nums4[l] == 0
 *
 * 例 1：
 * 输入：nums1 = [1,2], nums2 = [-2,-1], nums3 = [-1,2], nums4 = [0,2]
 * 输出：2
 * 解释：
 * 两个元组如下：
 * 1. (0, 0, 0, 1) -> nums1[0] + nums2[0] + nums3[0] + nums4[1] = 1 + (-2) + (-1) + 2 = 0
 * 2. (1, 1, 0, 0) -> nums1[1] + nums2[1] + nums3[0] + nums4[0] = 2 + (-1) + (-1) + 0 = 0
 *
 * 例 2：
 * 输入：nums1 = [0], nums2 = [0], nums3 = [0], nums4 = [0]
 * 输出：1
 *
 * 说明：
 * n == nums1.length
 * n == nums2.length
 * n == nums3.length
 * n == nums4.length
 * 1 <= n <= 200
 * -2^28 <= nums1[i], nums2[i], nums3[i], nums4[i] <= 2^28
 */
public class E454_Medium_4SumII {

    public static void test(ToIntQuaFunction<int[], int[], int[], int[]> method) {
        assertEquals(2, method.applyAsInt(new int[]{1,2}, new int[]{-2,-1}, new int[]{-1,2}, new int[]{0,2}));
        assertEquals(1, method.applyAsInt(new int[]{0}, new int[]{0}, new int[]{0}, new int[]{0}));
    }

    /**
     * 参见：
     * https://leetcode-cn.com/problems/4sum-ii/solution/si-shu-xiang-jia-ii-by-leetcode-solution/
     *
     * 总结，看到形如：A+B....+N=0的式子，要转换为(A+...T)=-((T+1)...+N)再计算，这个T的分割点一般是一半，
     * 特殊情况下需要自行判断。定T是解题的关键。
     *
     * LeetCode 耗时：103 ms - 99.42%
     *          内存消耗：38.4 MB - 82.91%
     */
    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        int n = nums1.length;
        Map<Integer, Integer> map = new HashMap<>((int) (n * n / 0.75) + 1);
        for (int i : nums1) {
            for (int j : nums2) {
                map.merge(i + j, 1, Integer::sum);
            }
        }

        int result = 0;
        for (int i : nums3) {
            for (int j : nums4) {
                result += map.getOrDefault(-(i + j), 0);
            }
        }

        return result;
    }

    @Test
    public void testFourSumCount() {
        test(this::fourSumCount);
    }
}
