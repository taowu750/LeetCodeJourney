package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 324. 摆动排序 II: https://leetcode-cn.com/problems/wiggle-sort-ii/
 *
 * 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序。
 * 你可以假设所有输入数组都可以得到满足题目要求的结果。
 *
 * 你能用 O(n) 时间复杂度和 / 或原地 O(1) 额外空间来实现吗？
 *
 * 例 1：
 * 输入：nums = [1,5,1,1,6,4]
 * 输出：[1,6,1,5,1,4]
 * 解释：[1,4,1,5,1,6] 同样是符合题目要求的结果，可以被判题程序接受。
 *
 * 例 2：
 * 输入：nums = [1,3,2,2,3,1]
 * 输出：[2,3,1,3,1,2]
 *
 * 约束：
 * - 1 <= nums.length <= 5 * 10^4
 * - 0 <= nums[i] <= 5000
 * - 题目数据保证，对于给定的输入 nums ，总能产生满足题目要求的结果
 */
public class E324_Medium_WiggleSortII {

    public static void assertWiggle(int[] nums) {
        boolean top = true;
        for (int i = 1; i < nums.length; i++) {
            if (top) {
                if (nums[i] <= nums[i - 1]) {
                    throw new AssertionError(Arrays.toString(nums));
                }
                if (i < nums.length - 1 && nums[i] <= nums[i + 1]) {
                    throw new AssertionError(Arrays.toString(nums));
                }
            } else {
                if (nums[i] >= nums[i - 1]) {
                    throw new AssertionError(Arrays.toString(nums));
                }
                if (i < nums.length - 1 && nums[i] >= nums[i + 1]) {
                    throw new AssertionError(Arrays.toString(nums));
                }
            }
            top = !top;
        }
    }

    public static void test(Consumer<int[]> method) {
        int[] nums = {1,5,1,1,6,4};
        method.accept(nums);
        assertWiggle(nums);

        nums = new int[]{1,3,2,2,3,1};
        method.accept(nums);
        assertWiggle(nums);

        nums = new int[]{2, 1};
        method.accept(nums);
        assertWiggle(nums);

        nums = new int[]{1,4,3,4,1,2,1,3,1,3,2,3,3};
        method.accept(nums);
        assertWiggle(nums);
    }

    /**
     * 参见：
     * https://leetcode-cn.com/problems/wiggle-sort-ii/solution/yi-bu-yi-bu-jiang-shi-jian-fu-za-du-cong-onlognjia/
     *
     * 将数组进行排序，然后从中间位置进行等分（如果数组长度为奇数，则将中间的元素分到前面），然后将两个数组进行穿插。
     * 但是这一解法有一个问题，例如，对于数组[1, 2, 2, 3]，按照这种做法求得的结果仍为[1, 2, 2, 3]。
     * 如果题目不要求各元素严格大于或小于相邻元素，那么这一解法是符合要求的，但题目要求元素相互严格大于或小于，那么需要稍微做一点改进。
     *
     * 为了方便阅读，我们在下文中定义左半边数组为数组A，右半边数组为数组 B。显然，出现上述现象是因为nums中存在重复元素。
     * 实际上，由于穿插之后，相邻元素必来自不同子数组，所以A或B内部出现重复元素是不会出现上述现象的。所以，
     * 出现上述情况其实是因为数组A和数组B出现了相同元素，我们用r来表示这一元素。而且我们可以很容易发现，如果A和B都存在r，
     * 那么r一定是A的最大值，B的最小值，这意味着r一定出现在A的尾部，B的头部。其实，如果这一数字的个数较少，
     * 不会出现这一现象，只有当这一数字个数达到原数组元素总数的一半，才会在穿插后的出现在相邻位置。
     *
     * 出现这一问题是因为重复数在A和B中的位置决定的，因为r在A尾部，B头部，所以如果r个数太多（大于等于(length(nums) + 1)/2），
     * 就可能在穿插后相邻。要解决这一问题，我们需要使A的r和B的r在穿插后尽可能分开。一种可行的办法是将A和B反序。
     *
     * 当然，这只能解决r的个数等于(length(nums) + 1)/2的情况，如果r的个数大于(length(nums) + 1)/2，还是会出现相邻。
     * 但实际上，这种情况是不存在有效解的，也就是说，这种数组对于本题来说是非法的。
     *
     * LeetCode 耗时：3 ms - 83.20%
     *          内存消耗：41.2 MB - 39.39%
     */
    public void wiggleSort(int[] nums) {
        if (nums.length == 1) {
            return;
        }

        int[] aux = nums.clone();
        Arrays.sort(aux);

        int n = aux.length, mid = (n & 1) == 0 ? n / 2 - 1 : n / 2;
        if (aux[mid] == aux[mid + 1]) {
            for (int i = 0, half = (mid + 1) / 2; i < half; i++) {
                int tmp = aux[i];
                aux[i] = aux[mid - i];
                aux[mid - i] = tmp;
            }
            for (int i = mid + 1, half = n - (n - mid - 1) / 2; i < half; i++) {
                int tmp = aux[i];
                aux[i] = aux[n - i + mid];
                aux[n - i + mid] = tmp;
            }
        }

        for (int i = 0, j = mid + 1, k = 0; j < n; i++, j++, k++) {
            nums[k] = aux[i];
            nums[++k] = aux[j];
        }
        if ((n & 1) == 1) {
            nums[n - 1] = aux[mid];
        }
    }

    @Test
    public void testWiggleSort() {
        test(this::wiggleSort);
    }


    /**
     * 快速选择+三项切分方法。
     *
     * 回顾解法1，我们发现，我们实际上并不关心A和B内部的元素顺序，只需要满足A和B长度相同（或相差1），
     * 且A中的元素小于等于B中的元素，且r出现在A的头部和B的尾部即可。实际上，由于A和B长度相同（或相差1），
     * 所以r实际上是原数组的中位数，下文改用mid来表示。因此，我们第一步其实不需要进行排序，而只需要找到中位数即可。
     *
     * 找到中位数后，我们需要利用3-way-partition算法将中位数放在数组中部，同时将小于中位数的数放在左侧，
     * 大于中位数的数放在右侧。
     *
     * 之后的步骤就和解法1一样了。
     *
     * LeetCode 耗时：4 ms - 29.57%
     *          内存消耗：41.3 MB - 19.83%
     */
    public void quickSelectMethod(int[] nums) {
        if (nums.length == 1) {
            return;
        }

        // 1 4 3 4 1 2 1 3 1 3 2 3 3
        // 1 1 1 1 2 2 3 3 3 3 3 4 4
        int n = nums.length;
        int[] aux = nums.clone();
        // 其实可以在快速选择后马上在找出中位数并移动，会快一点
        int median = quickSelectMedian(aux);
        /*
        三向切分，分成小于、等于、大于中位数的三部分。
        left 指向等于部分的开头，mid 指向等于部分的结尾+1，right 指向大于部分的开头-1
         */
        int left = 0, mid = 0, right = n - 1;
        while (mid < right) {
            if (aux[mid] > median) {
                swap(aux, mid, right);
                right--;
            } else if (aux[mid] < median) {
                swap(aux, left, mid);
                left++;
                mid++;
            } else {
                mid++;
            }
        }

        // 下面的步骤和解法1一样
        mid = (n & 1) == 0 ? n / 2 - 1 : n / 2;
        if (aux[mid] == aux[mid + 1]) {
            for (int i = 0, half = (mid + 1) / 2; i < half; i++) {
                int tmp = aux[i];
                aux[i] = aux[mid - i];
                aux[mid - i] = tmp;
            }
            for (int i = mid + 1, half = n - (n - mid - 1) / 2; i < half; i++) {
                int tmp = aux[i];
                aux[i] = aux[n - i + mid];
                aux[n - i + mid] = tmp;
            }
        }

        for (int i = 0, j = mid + 1, k = 0; j < n; i++, j++, k++) {
            nums[k] = aux[i];
            nums[++k] = aux[j];
        }
        if ((n & 1) == 1) {
            nums[n - 1] = aux[mid];
        }
    }

    /**
     * 快速选择，寻找中位数。
     */
    private int quickSelectMedian(int[] nums) {
        int n = nums.length, median = (n & 1) == 0 ? n / 2 - 1: n / 2;
        int lo = 0, hi = n - 1;
        for (;;) {
            if (lo == hi) {
                return nums[lo];
            }

            int pivot = median3(nums, lo, hi);
            int i = lo, j = hi;
            while (i < j) {
                while (nums[++i] < pivot);
                while (nums[--j] > pivot);

                if (i >= j) {
                    break;
                }
                swap(nums, i, j);
            }
            swap(nums, lo, j);

            if (j == median) {
                return nums[j];
            } else if (j < median) {
                lo = j + 1;
            } else {
                hi = j - 1;
            }
        }
    }

    private int median3(int[] nums, int lo, int hi) {
        int mid = (lo + hi) >>> 1;
        // 下面两步将最大的放在 hi
        if (nums[lo] > nums[mid]) {
            swap(nums, lo, mid);
        }
        if (nums[mid] > nums[hi]) {
            swap(nums, mid, hi);
        }
        // 将中位数放在 lo
        if (nums[lo] < nums[mid]) {
            swap(nums, lo, mid);
        }

        return nums[lo];
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    @Test
    public void testQuickSelectMethod() {
        test(this::quickSelectMethod);
    }
}
