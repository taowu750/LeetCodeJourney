package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，
 * 求出这个数组中的逆序对的总数。
 *
 * 例 1：
 * Input: [7,5,6,4]
 * Output: 5
 *
 * 约束：
 * - 0 <= 数组长度 <= 50000
 */
public class Offer51_Hard_InverseOrderPairsInArrays {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{7,5,6,4}), 5);
    }

    /**
     * 在归并排序中计算逆序对数量。
     *
     * LeetCode 耗时：30 ms - 99.29%
     *          内存消耗：48.9 MB - 5.03%
     */
    public int reversePairs(int[] nums) {
        return mergeSort(nums.clone(), nums, 0, nums.length - 1);
    }

    /**
     * 将 [lo, hi] 范围内的 src 元素排序到 dst 中，返回逆序对数量。
     *
     * 利用递归交换参数的技巧，避免数组复制。
     */
    private int mergeSort(int[] src, int[] dst, int lo, int hi) {
        if (lo < hi) {
            int mid = (lo + hi) >>> 1;
            // 分别将 [lo, mid]、[mid + 1, hi] 范围内的 dst 排序到 src 中
            int leftReversePair = mergeSort(dst, src, lo, mid);
            int rightReversePair = mergeSort(dst, src, mid + 1, hi);
            // 然后将 src 归并到 dst 中，完成 src 到 dst 的排序
            return leftReversePair + rightReversePair + merge(src, dst, lo, mid, hi);
        }

        return 0;
    }

    private int merge(int[] src, int[] dst, int lo, int mid, int hi) {
        // 计算左半边元素和右半边元素之间的逆序对（它们内部的逆序对数量不需要计算）
        int reversePair = 0;
        for (int i = lo, left = lo, right = mid + 1; i <= hi; i++) {
            if (left > mid)
                dst[i] = src[right++];
            else if (right > hi)
                dst[i] = src[left++];
            else if (src[left] <= src[right])
                dst[i] = src[left++];
            else {
                dst[i] = src[right++];
                // 当 src[left] > src[right] 时，就有逆序对
                reversePair += mid - left + 1;
            }
        }
        return reversePair;
    }

    @Test
    public void testReversePairs() {
        test(this::reversePairs);
    }
}
