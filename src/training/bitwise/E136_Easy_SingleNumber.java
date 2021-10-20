package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 *
 * 你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 *
 * 例 1：
 * 输入: [2,2,1]
 * 输出: 1
 *
 * 例 2：
 * 输入: [4,1,2,1,2]
 * 输出: 4
 */
public class E136_Easy_SingleNumber {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{2,2,1}), 1);
        assertEquals(method.applyAsInt(new int[]{4,1,2,1,2}), 4);
    }

    /**
     * a ^ a = 0、a ^ 0 = a
     *
     * 异或运算满足交换律：
     * a ^ b ^ a = a ^ a ^ b = b
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.8 MB - 36.99%
     */
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int num : nums)
            res ^= num;

        return res;
    }

    @Test
    public void testSingleNumber() {
        test(this::singleNumber);
    }
}
