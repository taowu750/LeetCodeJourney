package training.bitwise;

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

    public static void test(ToIntFunction<int[]> method) {
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
     * 考虑数字的二进制形式，对于出现三次的数字，各二进制位出现的次数都是 3 的倍数。
     * 因此，统计所有数字的各二进制位中 1 的出现次数，并对 3 求余，结果则为只出现一次的数字。
     *
     * 有限状态自动机:00-(1)>01-(1)>10-(1)>00->...满足(num=1改变,num=0保持原状)
     * 以count[i]%3=0,1,2为状态标定,有三种状态:00,01,10
     * 设高位为twos,低位为ones,设每次加进来的位为num(0,1)
     *
     * ones的分析方法:
     *         if(twos == 0) {
     *             if(num == 0) ones = ones;
     *             if(num == 1) ones = ~ones;
     *             // 可以合并为:ones = ones ^ num
     *         }
     *         if(twos = 1) {
     *             // 两种状态num=0或1均为0
     *             ones = 0;
     *         }
     * 两种进一步可以合并为:ones = ones ^ num & ~twos
     *
     * twos的分析方法类似,不过注意是在ones确定之后进行状态获取的:
     *         if(ones == 0) {
     *             if(num == 0) twos = twos;
     *             if(num == 1) twos = ~twos;
     *             // 可以合并为:twos = twos ^ num
     *         }
     *         if(ones = 1) {
     *             twos = 0;
     *         }
     * 两种进一步可以合并为:twos = twos ^ num & ~ones
     *
     * 遍历完所有数字后，各二进制位都处于状态 00 和状态 01 （取决于 “只出现一次的数字” 的各二进制位是 0 还是 1 ），
     * 而此两状态是由 one 来记录的（此两状态下 twos 恒为 0 ），因此返回 one 即可。
     *
     * LeetCode 耗时：0ms - 100%
     *          内存消耗：38.3 MB - 43.13%
     */
    public int fsm(int[] nums) {
        int ones = 0, twos = 0;
        for (int num : nums) {
            // 注意 & 优先级高于 ^
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
