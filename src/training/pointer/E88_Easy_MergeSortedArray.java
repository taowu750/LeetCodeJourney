package training.pointer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定两个已排序的数组 nums1 和 nums2，将 num2 归并到 nums1 中。
 * nums1 和 nums2 的初始元素个数分别是 m 和 n。nums1 中有足够的空间存放 m + n 个元素。
 * <p>
 * 例 1：
 * Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
 * Output: [1,2,2,3,5,6]
 * <p>
 * 例 2：
 * Input: nums1 = [1], m = 1, nums2 = [], n = 0
 * Output: [1]
 * <p>
 * 约束：
 * - 0 <= n, m <= 200
 * - 1 <= n + m <= 200
 * - nums1.length == m + n
 * - nums2.length == n
 * - -10**9 <= nums1[i], nums2[i] <= 10**9
 */
public class E88_Easy_MergeSortedArray {

    interface MergeMethod {
        void merge(int[] nums1, int m, int[] nums2, int n);
    }

    @Test
    public void test(MergeMethod mergeMethod) {
        int[] nums1 = {1, 2, 3, 0, 0, 0}, nums2 = {2, 5, 6};
        mergeMethod.merge(nums1, 3, nums2, 3);
        assertArrayEquals(nums1, new int[]{1, 2, 2, 3, 5, 6});

        nums1 = new int[]{1};
        nums2 = new int[0];
        mergeMethod.merge(nums1, 1, nums2, 0);
        assertArrayEquals(nums1, new int[]{1});
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        for (int p1 = m - 1, p2 = n - 1, pe = m + n - 1; pe >= 0; pe--) {
            if (p1 < 0)
                nums1[pe] = nums2[p2--];
            else if (p2 < 0)
                nums1[pe] = nums1[p1--];
            else if (nums2[p2] >= nums1[p1])
                nums1[pe] = nums2[p2--];
            else
                nums1[pe] = nums1[p1--];
        }
    }

    @Test
    public void testMerge() {
        test(this::merge);
    }
}
