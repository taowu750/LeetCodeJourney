package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 280. 摆动排序: https://leetcode-cn.com/problems/wiggle-sort/
 *
 * 给你一个整数数组 nums, 将该数组重新排序后使 nums[0] <= nums[1] >= nums[2] <= nums[3]...
 *
 * 输入数组总是有一个有效的答案。你能在 O(n) 时间复杂度下解决这个问题吗？
 *
 * 例 1：
 * 输入：nums = [3,5,2,1,6,4]
 * 输出：[3,5,1,6,2,4]
 * 解释：[1,6,2,5,3,4]也是有效的答案
 *
 * 例 2：
 * 输入：nums = [6,6,5,6,3,8]
 * 输出：[6,6,5,6,3,8]
 *
 * 说明：
 * - 1 <= nums.length <= 5 * 10^4
 * - 0 <= nums[i] <= 10^4
 * - 输入的 nums 保证至少有一个答案。
 */
public class E280_Medium_WiggleSort {

    public static void assertWiggle(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            if (i % 2 == 0) {
                if (nums[i] > nums[i + 1]) {
                    throw new AssertionError("actual not wiggle array: " + Arrays.toString(nums));
                }
            } else if (nums[i] < nums[i + 1]) {
                throw new AssertionError("actual not wiggle array: " + Arrays.toString(nums));
            }
        }
    }

    public static void test(Consumer<int[]> method) {
        int[] nums = {3,5,2,1,6,4};
        method.accept(nums);
        assertWiggle(nums);

        nums = new int[]{6,6,5,6,3,8};
        method.accept(nums);
        assertWiggle(nums);

        nums = new int[]{1};
        method.accept(nums);
        assertWiggle(nums);
    }

    /**
     * 参见 {@link E324_Medium_WiggleSortII}。
     *
     * LeetCode 耗时：1 ms - 48.41%
     *          内存消耗：42.3 MB - 5.16%
     */
    public void wiggleSort(int[] nums) {
        /*
        不需要完全排序，只需要区分出一半小的、一半大的即可
         */
        quick3waySelect(nums);
        // 将 [mid+1, n) 依次穿插到 [lo, mid] 中
        final int n = nums.length, mid = (n + 1) / 2 - 1;
        // 复制后半段
        int[] after = new int[n / 2];
        System.arraycopy(nums, mid + 1, after, 0, after.length);
        // 安排前半部分的位置
        for (int i = mid; i > 0; i--) {
            /*
            123456
            1_2_3_
             */
            nums[i * 2] = nums[i];
        }
        // 安排后半部分的位置
        for (int i = 0; i < after.length; i++) {
            /*
            123456
            _4_5_6
             */
            nums[(i + 1) * 2 - 1] = after[i];
        }
    }

    /**
     * 三向快速选择，找出 nums 的中位数 m，并将数组排成 [小于m，等于m，大于m] 的形式。
     *
     * 其中三向切分参见 {@link E75_Medium_SortColors}
     */
    private void quick3waySelect(int[] nums) {
        final int n = nums.length, mid = (n + 1) / 2 - 1;
        for (int lo = 0, hi = n - 1;;) {
            int pivot = median3(nums, lo, hi);
            // [lo, i) < pivot, [i, j) == pivot, (k, hi] > pivot
            int i = lo, j = lo, k = hi;
            while (j <= k) {
                if (nums[j] < pivot) {
                    swap(nums, i, j);
                    i++;
                    j++;
                } else if (nums[j] == pivot) {
                    j++;
                } else {
                    swap(nums, j, k);
                    k--;
                }
            }
            if (i <= mid && k >= mid) {
                return;
            } else if (i > mid) {
                hi = i - 1;
            } else {
                lo = k + 1;
            }
        }
    }

    private int median3(int[] nums, int lo, int hi) {
        int mid = (lo + hi) >>> 1;
        return Math.min(nums[lo], Math.max(nums[mid], nums[hi]));
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    @Test
    public void testWiggleSort() {
        test(this::wiggleSort);
    }


    /**
     * 可以只用一遍完成任务。当我们遍历整个数组，比较当前元素与下一个元素。若顺序不正确，则交换之。参见：
     * https://leetcode-cn.com/problems/wiggle-sort/solution/bai-dong-pai-xu-by-leetcode/
     *
     * 用数学归纳法可以证明。假设 [0,1, ..., k] 均已满足摆动排序，而 k+1 不满足，记此时 nums[k-1]，nums[k]，nums[k+1] 分别为 a，b，c
     * - 若不满足的是降序，则可知：c > b，同时 b>=a，此时三个数状态是 a <= b < c，交换 b 和 c 后变成：a < c > b，满足小大小
     * - 若不满足的是升序，则可知：c < b，同时 b<=a，此时三个数状态是 a >= b > c，交换 b 和 c 后变成：a > c < b，满足大小大
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：42.1 MB - 17.86%
     */
    public void greedyMethod(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            if (i % 2 == 0) {
                if (nums[i] > nums[i + 1]) {
                    int tmp = nums[i];
                    nums[i] = nums[i + 1];
                    nums[i + 1] = tmp;
                }
            } else if (nums[i] < nums[i + 1]) {
                int tmp = nums[i];
                nums[i] = nums[i + 1];
                nums[i + 1] = tmp;
            }
        }
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
