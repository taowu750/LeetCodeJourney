package training.array;

import org.junit.jupiter.api.Test;
import util.ArrayUtil;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给你一个整数数组 nums，请你将该数组升序排列。
 * <p>
 * 例 1：
 * Input：nums = [5,2,3,1]
 * Output：[1,2,3,5]
 * <p>
 * 例 2：
 * Input：nums = [5,1,1,2,0,0]
 * Output：[0,0,1,1,2,5]
 * <p>
 * 约束：
 * - 1 <= nums.length <= 50000
 * - -50000 <= nums[i] <= 50000
 */
public class E912_Medium_SortAnArray {

    static void test(Function<int[], int[]> method) {
        assertArrayEquals(method.apply(new int[]{5, 2, 3, 1}), new int[]{1, 2, 3, 5});
        assertArrayEquals(method.apply(new int[]{5, 1, 1, 2, 0, 0}), new int[]{0, 0, 1, 1, 2, 5});

        assertTrue(ArrayUtil.isAscending(method.apply(
                new Random().ints(10000, -50000, 50000)
                        .toArray())));
    }

    /**
     * 快速排序。
     *
     * LeetCode 耗时：5 ms - 93.76%
     *          内存消耗：45.8 MB - 68.17%
     */
    public int[] sortArray(int[] nums) {
        qsort(nums, 0, nums.length - 1);
        return nums;
    }

    private void qsort(int[] nums, int lo, int hi) {
        if (hi - lo > 14) {
            int pivot = median(nums, lo, hi);
            int i = lo, j = hi + 1;
            for (; ; ) {
                while (nums[++i] < pivot) ;
                while (nums[--j] > pivot) ;

                if (i >= j)
                    break;
                exch(nums, i, j);
            }
            exch(nums, lo, j);

            qsort(nums, lo, j - 1);
            qsort(nums, j + 1, hi);
        } else {
            Arrays.sort(nums, lo, hi + 1);
        }
    }

    private int median(int[] nums, int lo, int hi) {
        int mid = (lo + hi) >>> 1;
        if (nums[lo] > nums[mid])
            exch(nums, lo, mid);
        if (nums[mid] > nums[hi])
            exch(nums, mid, hi);
        if (nums[lo] > nums[mid])
            exch(nums, lo, hi);
        exch(nums, lo, mid);

        return nums[lo];
    }

    private void exch(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    @Test
    public void testSortArray() {
        test(this::sortArray);
    }
}
