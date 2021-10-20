package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static util.ArrayUtil.equalsIgnoreOrder;

/**
 * 剑指 Offer 56 - I. 数组中数字出现的次数: https://leetcode-cn.com/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-lcof/
 *
 * 一个整型数组 nums 里除两个数字之外，其他数字都出现了两次。请写程序找出这两个只出现一次的数字。
 * 要求时间复杂度是O(n)，空间复杂度是O(1)。
 *
 * 例 1：
 * 输入：nums = [4,1,4,6]
 * 输出：[1,6] 或 [6,1]
 *
 * 例 2：
 * 输入：nums = [1,2,10,4,1,4,3,3]
 * 输出：[2,10] 或 [10,2]
 *
 * 约束：
 * - 2 <= nums.length <= 10000
 */
public class Offer56_Medium_NumberOfOccurrencesInArrayI {

    static void test(UnaryOperator<int[]> method) {
        equalsIgnoreOrder(new int[]{1,6}, method.apply(new int[]{4,1,4,6}));
        equalsIgnoreOrder(new int[]{2,10}, method.apply(new int[]{1,2,10,4,1,4,3,3}));
    }

    /**
     * 分组异或法，参见：
     * https://leetcode-cn.com/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-lcof/solution/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-by-leetcode/
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：40 MB - 76.12%
     */
    public int[] singleNumbers(int[] nums) {
        // 全部异或
        int xor = nums[0];
        for (int i = 1; i < nums.length; i++) {
            xor ^= nums[i];
        }

        // 找到一个为 1 的位
        int bit = 1;
        while ((xor & bit) == 0) {
            bit <<= 1;
        }

        // 进行划分，找到两个数
        int a = 0, b = 0;
        for (int num : nums) {
            if ((num & bit) == 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }

        return new int[]{a, b};
    }

    @Test
    public void testSingleNumbers() {
        test(this::singleNumbers);
    }
}
