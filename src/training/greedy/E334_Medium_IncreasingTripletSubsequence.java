package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 334. 递增的三元子序列: https://leetcode-cn.com/problems/increasing-triplet-subsequence/
 *
 * 给你一个整数数组 nums，判断这个数组中是否存在长度为 3 的递增「子序列」。
 *
 * 如果存在这样的三元组下标 (i, j, k) 且满足 i < j < k ，使得 nums[i] < nums[j] < nums[k] ，
 * 返回 true ；否则，返回 false 。
 *
 * 例 1：
 * 输入：nums = [1,2,3,4,5]
 * 输出：true
 * 解释：任何 i < j < k 的三元组都满足题意
 *
 * 例 2：
 * 输入：nums = [5,4,3,2,1]
 * 输出：false
 * 解释：不存在满足题意的三元组
 *
 * 例 3：
 * 输入：nums = [2,1,5,0,4,6]
 * 输出：true
 * 解释：三元组 (3, 4, 5) 满足题意，因为 nums[3] == 0 < nums[4] == 4 < nums[5] == 6
 *
 * 约束：
 * - 1 <= nums.length <= 10^5
 * - -2^31 <= nums[i] <= 2^31 - 1
 */
public class E334_Medium_IncreasingTripletSubsequence {

    public static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{1,2,3,4,5}));
        assertFalse(method.test(new int[]{5,4,3,2,1}));
        assertTrue(method.test(new int[]{2,1,5,0,4,6}));
    }

    /**
     * 和 {@link training.dynamicprogramming.E300_Medium_LongestIncreasingSubsequence} 类似。
     *
     * LeetCode 耗时：3 ms - 22.70%
     *          内存消耗：79.1 MB - 49.47%
     */
    public boolean increasingTriplet(int[] nums) {
        int[] piles = new int[3];
        int top = 0;

        for (int num : nums) {
            int i = 0;
            for (; i < top; i++) {
                if (piles[i] >= num) {
                    break;
                }
            }
            if (i == top) {
                top++;
                if (top == 3) {
                    return true;
                }
            }
            piles[i] = num;
        }

        return false;
    }

    @Test
    public void testIncreasingTriplet() {
        test(this::increasingTriplet);
    }


    /**
     * 贪心双指针法，参见：
     * https://leetcode-cn.com/problems/increasing-triplet-subsequence/solution/c-xian-xing-shi-jian-fu-za-du-xiang-xi-jie-xi-da-b/
     *
     * 首先，新建两个变量 small 和 mid ，分别用来保存题目要我们求的长度为 3 的递增子序列的最小值和中间值。
     *
     * 接着，我们遍历数组，每遇到一个数字，我们将它和 small 和 mid 相比，若小于等于 small ，则替换 small；否则，
     * 若小于等于 mid，则替换 mid；否则，若大于 mid，则说明我们找到了长度为 3 的递增数组.
     *
     * 上面的求解过程中有个问题：当已经找到了长度为 2 的递增序列，这时又来了一个比 small 还小的数字，为什么可以
     * 直接替换 small 呢，这样 small 和 mid 在原数组中并不是按照索引递增的关系呀？
     *
     * 因为我们更新了 small ，这个 small 在 mid 后面，没有严格遵守递增顺序，但它隐含着的真相是，有一个比
     * 更新后的 small 大比 mid 小的「前·最小值」出现在 mid 之前。因此，当后续出现比 mid 大的值的时候，
     * 我们一样可以通过当前 small 和 mid 推断的确存在着长度为 3 的递增序列。所以，这样的替换并不会干扰我们后续的计算！
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：78.9 MB - 77.26%
     */
    public boolean pointerMethod(int[] nums) {
        if (nums.length < 3) {
            return false;
        }

        int small = Integer.MAX_VALUE, mid = Integer.MAX_VALUE;
        for (int num : nums) {
            if (num <= small) {
                small = num;
            } else if (num <= mid) {
                mid = num;
            } else {
                return true;
            }
        }

        return false;
    }

    @Test
    public void testPointerMethod() {
        test(this::pointerMethod);
    }
}
