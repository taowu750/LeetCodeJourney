package learn.array;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 原地删除已排序 int 数组中所有重复的数字，只留下一个。要求空间复杂度 O(1)。
 * <p>
 * 例 1：
 * Input: nums = [1,1,2]
 * Output: 2, nums = [1,2]
 * <p>
 * 例 2：
 * Input: nums = [0,0,1,1,1,2,2,3,3,4]
 * Output: 5, nums = [0,1,2,3,4]
 * <p>
 * 约束：
 * - 0 <= nums.length <= 3 * 10**4
 * - -10**4 <= nums[i] <= 10**4
 * - nums 升序排列
 */
public class RemoveDuplicatesFromSortedArray {

    static void test(ToIntFunction<int[]> method) {
        int[] nums = {1, 1, 2}, result = {1, 2};
        assertEquals(method.applyAsInt(nums), 2);
        for (int i = 0; i < result.length; i++) {
            assertEquals(nums[i], result[i]);
        }

        nums = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        result = new int[]{0, 1, 2, 3, 4};
        assertEquals(method.applyAsInt(nums), 5);
        for (int i = 0; i < result.length; i++) {
            assertEquals(nums[i], result[i]);
        }
    }

    public int removeDuplicates(int[] nums) {
        if (nums.length < 2)
            return nums.length;

        int len = nums.length, j = 1;
        for (int i = 1; i < len; i++) {
            if (nums[i] != nums[i - 1])
                nums[j++] = nums[i];
        }

        return j;
    }

    @Test
    public void testRemoveDuplicates() {
        test(this::removeDuplicates);
    }
}
