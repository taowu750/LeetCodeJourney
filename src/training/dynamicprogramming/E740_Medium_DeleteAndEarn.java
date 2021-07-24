package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 740. 删除并获得点数: https://leetcode-cn.com/problems/delete-and-earn/
 *
 * 给你一个整数数组 nums，你可以对它进行一些操作。
 * 每次操作中，选择任意一个 nums[i]，删除它并获得 nums[i] 的点数。之后，你必须删除「所有」等于 nums[i] - 1 和
 * nums[i] + 1 的元素。
 *
 * 开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
 *
 * 例 1：
 * 输入：nums = [3,4,2]
 * 输出：6
 * 解释：
 * 删除 4 获得 4 个点数，因此 3 也被删除。
 * 之后，删除 2 获得 2 个点数。总共获得 6 个点数。
 *
 * 例 2：
 * 输入：nums = [2,2,3,3,3,4]
 * 输出：9
 * 解释：
 * 删除 3 获得 3 个点数，接着要删除两个 2 和 4 。
 * 之后，再次删除 3 获得 3 个点数，再次删除 3 获得 3 个点数。
 * 总共获得 9 个点数。
 *
 * 约束：
 * - 1 <= nums.length <= 2 * 10^4
 * - 1 <= nums[i] <= 10^4
 */
public class E740_Medium_DeleteAndEarn {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(6, method.applyAsInt(new int[]{3,4,2}));
        assertEquals(9, method.applyAsInt(new int[]{2,2,3,3,3,4}));
        assertEquals(52, method.applyAsInt(new int[]{8,7,3,8,1,4,10,10,10,2}));
        assertEquals(4, method.applyAsInt(new int[]{3,1}));
    }

    /**
     * 排序之后，类似于 {@link E198_Medium_HouseRobber}。
     *
     * LeetCode 耗时：8 ms - 10.08%
     *          内存消耗：38.1 MB - 52.56%
     */
    public int deleteAndEarn(int[] nums) {
        // 分组
        TreeMap<Integer, Integer> num2cnt = new TreeMap<>();
        for (int num: nums) {
            num2cnt.merge(num, 1, Integer::sum);
        }

        int[][] numCnts = new int[num2cnt.size()][2];
        Iterator<Map.Entry<Integer, Integer>> iter = num2cnt.entrySet().iterator();
        for (int i = 0; i < numCnts.length; i++) {
            Map.Entry<Integer, Integer> entry = iter.next();
            numCnts[i][0] = entry.getKey();
            numCnts[i][1] = entry.getValue();
        }

        int dp1 = 0, dp2 = 0;
        for (int i = 0; i < numCnts.length; i++) {
            int tmp = dp1;
            // 相邻则类似于打家劫舍
            if (i > 0 && numCnts[i][0] == numCnts[i - 1][0] + 1) {
                dp1 = Math.max(dp1, dp2 + numCnts[i][0] * numCnts[i][1]);
            } else {  // 不相邻则可以直接选取 i
                dp1 += numCnts[i][0] * numCnts[i][1];
            }
            dp2 = tmp;
        }

        return dp1;
    }

    @Test
    public void testDeleteAndEarn() {
        test(this::deleteAndEarn);
    }
}
