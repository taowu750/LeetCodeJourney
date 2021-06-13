package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 137. 只出现一次的数字 II: https://leetcode-cn.com/problems/single-number-ii/
 *
 * 给你一个整数数组 nums ，除某个元素仅出现「一次」外，其余每个元素都恰出现「三次」。
 * 请你找出并返回那个只出现了一次的元素。
 *
 * 例 1：
 * 输入：nums = [2,2,3,2]
 * 输出：3
 *
 * 例 2：
 * 输入：nums = [0,1,0,1,0,1,99]
 * 输出：99
 *
 * 约束：
 * - 1 <= nums.length <= 3 * 10**4
 * - -2**31 <= nums[i] <= 2**31 - 1
 */
public class E137_Medium_SingleNumberII {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(3, method.applyAsInt(new int[]{2,2,3,2}));
        assertEquals(99, method.applyAsInt(new int[]{0,1,0,1,0,1,99}));
    }

    /**
     * LeetCode 耗时：3 ms - 54.85%
     *          内存消耗：37.8 MB - 98.46%
     */
    public int singleNumber(int[] nums) {
        int[] bitNums = new int[32];

        // 统计每个二进制位上的 bit 个数
        for (int num : nums) {
            for (int i = 31; i >= 0; i--) {
                if ((num & 1) == 1) {
                    bitNums[i] += 1;
                }
                num >>>= 1;
            }
        }

        // 对 3 取模，这样就只剩下所需的数字位
        for (int i = 0; i < 32; i++) {
            bitNums[i] %= 3;
        }

        // 求出数字
        int result = 0;
        for (int i = 31, shift = 0; i >= 0; i--, shift += 1) {
            result |= bitNums[i] << shift;
        }

        return result;
    }

    @Test
    public void testSingleNumber() {
        test(this::singleNumber);
    }


    /**
     * 有限状态自动机，参见：
     * https://leetcode-cn.com/problems/single-number-ii/solution/single-number-ii-mo-ni-san-jin-zhi-fa-by-jin407891/
     *
     * LeetCode 耗时：0ms - 100%
     *          内存消耗：38.3 MB - 43.13%
     */
    public int fsm(int[] nums) {
        int ones = 0, twos = 0;
        for (int num : nums) {
            ones = ones ^ num & ~twos;
            twos = twos ^ num & ~ones;
        }

        return ones;
    }

    @Test
    public void testFsm() {
        test(this::fsm);
    }
}
