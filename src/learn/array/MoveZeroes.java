package learn.array;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个数组 nums，将所有 0 移到末尾，同时保持非零元素的相对顺序。
 * <p>
 * 例 1：
 * Input: [0,1,0,3,12]
 * Output: [1,3,12,0,0]
 */
public class MoveZeroes {

    static void test(Consumer<int[]> method) {
        int[] nums = {0, 1, 0, 3, 12};
        method.accept(nums);
        assertArrayEquals(nums, new int[]{1, 3, 12, 0, 0});
    }

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
