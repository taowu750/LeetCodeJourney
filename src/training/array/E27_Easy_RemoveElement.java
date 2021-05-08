package training.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个数组 nums 和一个值 val，就地删除数组中所有 val 并返回新的长度。要求空间复杂度为 O(1)。
 * 数组中剩余元素的顺序任意，只要删除了给定元素即可。
 * <p>
 * 例 1：
 * Input: nums = [3,2,2,3], val = 3
 * Output: 2, nums = [2,2]
 * Explanation: nums 数组前 2 个元素需要是 2,2
 * <p>
 * 例 2：
 * Input: nums = [0,1,2,2,3,0,4,2], val = 2
 * Output: 5, nums = [0,1,4,0,3]
 * Explanation: nums 数组前 5 个元素需要包含 0,1,4,0,3
 * <p>
 * 约束：
 * - 0 <= nums.length <= 100
 * - 0 <= nums[i] <= 50
 * - 0 <= val <= 100
 */
public class E27_Easy_RemoveElement {

    static interface RemoveMethod {
        int removeElement(int[] nums, int val);
    }

    static void test(RemoveMethod method) {
        int[] nums = {3, 2, 2, 3}, result = new int[]{2, 2};
        int val = 3;
        assertEquals(method.removeElement(nums, val), 2);
        for (int i = 0; i < result.length; i++) {
            assertEquals(nums[i], result[i]);
        }

        nums = new int[]{0, 1, 2, 2, 3, 0, 4, 2};
        result = new int[]{0, 1, 3, 0, 4};
        val = 2;
        assertEquals(method.removeElement(nums, val), 5);
        for (int i = 0; i < result.length; i++) {
            assertEquals(nums[i], result[i]);
        }
    }

    public int removeElement(int[] nums, int val) {
        int len = nums.length, j = 0;
        for (int i = 0; i < len; i++) {
            if (nums[i] != val)
                nums[j++] = nums[i];
        }

        return j;
    }

    @Test
    public void testRemoveElement() {
        test(this::removeElement);
    }
}
