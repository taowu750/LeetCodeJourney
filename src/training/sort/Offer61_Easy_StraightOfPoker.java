package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 剑指 Offer 61. 扑克牌中的顺子: https://leetcode-cn.com/problems/bu-ke-pai-zhong-de-shun-zi-lcof/
 *
 * 从若干副扑克牌中随机抽 5 张牌，判断是不是一个顺子，即这5张牌是不是连续的。2～10为数字本身，
 * A为1，J为11，Q为12，K为13，而大、小王为 0 ，可以看成任意数字。A 不能视为 14。
 *
 * 例 1：
 * 输入: [1,2,3,4,5]
 * 输出: True
 *
 * 例 2：
 * 输入: [0,0,1,2,5]
 * 输出: True
 *
 * 说明：
 * - 数组长度为 5
 * - 数组的数取值为 [0, 13]
 */
public class Offer61_Easy_StraightOfPoker {

    public static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{1,2,3,4,5}));
        assertTrue(method.test(new int[]{0,0,1,2,5}));
        assertFalse(method.test(new int[]{1,2,3,4,4}));
        assertFalse(method.test(new int[]{0,0,1,2,6}));
        assertTrue(method.test(new int[]{0,0,4,5,8}));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.8 MB - 50.71%
     */
    public boolean isStraight(int[] nums) {
        // 使用 order 记录每一张扑克牌
        int[] order = new int[14];
        // 记录 0 的数量，已经扑克牌中的最小（除 0 外）、最大值
        int zeros = 0, min = 14, max = 0;
        for (int num : nums) {
            // 当出现重复的扑克牌，则返回 false
            if (order[num] != 0) {
                return false;
            }
            order[num] = num;
            if (num == 0) {
                zeros++;
            } else {
                min = Math.min(min, num);
            }
            max = Math.max(max, num);
        }

        // 从 min 到 max，使用 0 不起空缺的部分，当不够补时，返回 false
        for (int i = min + 1; i < max; i++) {
            if (order[i] == 0) {
                if (zeros == 0) {
                    return false;
                }
                zeros--;
            }
        }

        return true;
    }

    @Test
    public void testIsStraight() {
        test(this::isStraight);
    }


    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.6 MB - 84.33%
     */
    public boolean sortMethod(int[] nums) {
        Arrays.sort(nums);
        int zeros = nums[0] == 0 ? 1 : 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == 0) {
                zeros++;
            } else if (nums[i] == nums[i - 1]) {
                return false;
            }
        }
        int min = zeros < nums.length ? nums[zeros] : 0, max = nums[nums.length - 1];
        /*
        max - min - (nums.length - zeros) + 1 <= zeros
        max - min - nums.length + zeros + 1 <= zeros
        max - min <= nums.length - 1
         */
        return max - min <= nums.length - 1;
    }

    @Test
    public void test() {
        test(this::sortMethod);
    }
}
