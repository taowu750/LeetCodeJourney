package training.math;

import learn.array.FindAllNumbersDisappearedInAnArray;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 645. 错误的集合: https://leetcode-cn.com/problems/set-mismatch/
 *
 * 集合 s 包含从 1 到 n 的整数。不幸的是，因为数据错误，导致集合里面某一个数字复制了
 * 成了集合里面的另外一个数字的值，导致集合「丢失了一个数字」并且「有一个数字重复」。
 *
 * 给定一个数组 nums 代表了集合 S 发生错误后的结果。
 *
 * 请你「先找出」重复出现的整数，「再找到」丢失的整数，将它们以数组的形式返回。
 *
 * 例 1：
 * 输入：nums = [1,2,2,4]
 * 输出：[2,3]
 *
 * 例 2：
 * 输入：nums = [1,1]
 * 输出：[1,2]
 *
 * 约束：
 * - 2 <= nums.length <= 10**4
 * - 1 <= nums[i] <= 10**4
 */
public class E645_Easy_SetMismatch {

    static void test(UnaryOperator<int[]> method) {
        assertArrayEquals(new int[]{2,3}, method.apply(new int[]{1,2,2,4}));
        assertArrayEquals(new int[]{1,2}, method.apply(new int[]{1,1}));
        assertArrayEquals(new int[]{2,1}, method.apply(new int[]{2,2}));
        assertArrayEquals(new int[]{3,2}, method.apply(new int[]{3,6,4,5,1,3}));
    }

    /**
     * 此题类似于 {@link FindAllNumbersDisappearedInAnArray}。
     *
     * LeetCode 耗时：2 ms - 90.88%
     *          内存消耗：40.1 MB - 43.71%
     */
    public int[] findErrorNums(int[] nums) {
        int repeat = 0, miss = 0;
        for (int i = 0; i < nums.length; i++) {
            int idx = Math.abs(nums[i]) - 1;
            // 重复的数会被转换两次
            if (nums[idx] < 0)
                repeat = idx + 1;
            nums[idx] = -nums[idx];
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0 && i + 1 != repeat) {
                miss = i + 1;
            }
        }

        return new int[]{repeat, miss};
    }

    @Test
    public void testFindErrorNums() {
        test(this::findErrorNums);
    }
}
