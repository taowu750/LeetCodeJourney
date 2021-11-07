package training.sort;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 75. 颜色分类: https://leetcode-cn.com/problems/sort-colors/
 *
 * 给定一个包含红色、白色和蓝色，一共 n 个元素的数组，「原地」对它们进行排序，使得相同颜色的元素相邻，
 * 并按照红色、白色、蓝色顺序排列。
 *
 * 此题中，我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
 *
 * 例 1：
 * 输入：nums = [2,0,2,1,1,0]
 * 输出：[0,0,1,1,2,2]
 *
 * 例 2：
 * 输入：nums = [2,0,1]
 * 输出：[0,1,2]
 *
 * 例 3：
 * 输入：nums = [0]
 * 输出：[0]
 *
 * 例 4：
 * 输入：nums = [1]
 * 输出：[1]
 *
 * 约束：
 * - n == nums.length
 * - 1 <= n <= 300
 * - nums[i] 为 0、1 或 2
 */
public class E75_Medium_SortColors {

    static void test(Consumer<int[]> method) {
        int[] nums = new int[]{2,0,2,1,1,0};
        method.accept(nums);
        assertArrayEquals(new int[]{0,0,1,1,2,2}, nums);

        nums = new int[]{2,0,1};
        method.accept(nums);
        assertArrayEquals(new int[]{0,1,2}, nums);

        nums = new int[]{0};
        method.accept(nums);
        assertArrayEquals(new int[]{0}, nums);

        nums = new int[]{1};
        method.accept(nums);
        assertArrayEquals(new int[]{1}, nums);
    }

    /**
     * 三指针法。
     *
     * LeetCode 耗时：0ms - 100%
     *          内存消耗：37.8MS - 48%
     */
    public void sortColors(int[] nums) {
        // i 表示第一个 1 的位置，j 表示最后一个 1 的位置，k 表示第一个 2 的位置
        for (int i = 0, j = 0, k = nums.length - 1; j <= k;) {
            if (nums[j] == 0) {
                if (j != i) {
                    int tmp = nums[j];
                    nums[j] = nums[i];
                    nums[i] = tmp;
                }
                i++;
                j++;
            } else if (nums[j] == 1) {
                j++;
            } else {
                int tmp = nums[j];
                nums[j] = nums[k];
                nums[k] = tmp;
                k--;
            }
        }
    }

    @Test
    public void testSortColors() {
        test(this::sortColors);
    }
}
