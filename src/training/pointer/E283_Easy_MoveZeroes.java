package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 283. 移动零：https://leetcode-cn.com/problems/move-zeroes/
 *
 * 给定一个数组 nums，将所有 0 移到末尾，同时保持非零元素的相对顺序。
 *
 * 请注意 ，必须在不复制数组的情况下原地对数组进行操作。
 *
 * 例 1：
 * Input: [0,1,0,3,12]
 * Output: [1,3,12,0,0]
 *
 * 说明：
 * - 1 <= nums.length <= 10^4
 * - -2^31 <= nums[i] <= 2^31 - 1
 */
public class E283_Easy_MoveZeroes {

    public static void test(Consumer<int[]> method) {
        int[] nums = {0, 1, 0, 3, 12};
        method.accept(nums);
        assertArrayEquals(nums, new int[]{1, 3, 12, 0, 0});
    }

    /**
     * 类似于 {@link E27_Easy_RemoveElement}
     */
    public void moveZeroes(int[] nums) {
        int len = nums.length, retain = 0;
        for (int i = 0; i < len; i++) {
            if (nums[i] != 0)
                nums[retain++] = nums[i];
        }
        for (; retain < len; retain++)
            nums[retain] = 0;
    }

    @Test
    public void testMoveZeroes() {
        test(this::moveZeroes);
    }
}
