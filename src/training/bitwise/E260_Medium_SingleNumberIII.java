package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static util.ArrayUtil.equalsIgnoreOrder;

/**
 * 260. 只出现一次的数字 III: https://leetcode-cn.com/problems/single-number-iii/
 *
 * 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素。
 * 你可以按「任意顺序」返回答案。
 *
 * 进阶：你的算法应该具有线性时间复杂度。你能否仅使用常数空间复杂度来实现？
 *
 * 例 1：
 * 输入：nums = [1,2,1,3,2,5]
 * 输出：[3,5]
 * 解释：[5, 3] 也是有效的答案。
 *
 * 例 2：
 * 输入：nums = [-1,0]
 * 输出：[-1,0]
 *
 * 例 3：
 * 输入：nums = [0,1]
 * 输出：[1,0]
 *
 * 说明：
 * - 2 <= nums.length <= 3 * 10^4
 * - -2^31 <= nums[i] <= 2^31 - 1
 * - 除两个只出现一次的整数外，nums 中的其他数字都出现两次
 */
public class E260_Medium_SingleNumberIII {

    public static void test(UnaryOperator<int[]> method) {
        equalsIgnoreOrder(new int[]{3,5}, method.apply(new int[]{1,2,1,3,2,5}));
        equalsIgnoreOrder(new int[]{-1,0}, method.apply(new int[]{-1,0}));
        equalsIgnoreOrder(new int[]{0,1}, method.apply(new int[]{0,1}));
    }

    /**
     * 分组异或法，和 {@link Offer56_Medium_NumberOfOccurrencesInArrayI} 一模一样。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.7 MB - 47.25%
     */
    public int[] singleNumber(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }

        int diffBit = 1;
        while ((diffBit & xor) == 0) {
            diffBit <<= 1;
        }

        int n1 = 0, n2 = 0;
        for (int num : nums) {
            if ((num & diffBit) == 0) {
                n1 ^= num;
            } else {
                n2 ^= num;
            }
        }

        return new int[]{n1, n2};
    }

    @Test
    public void testSingleNumber() {
        test(this::singleNumber);
    }
}
