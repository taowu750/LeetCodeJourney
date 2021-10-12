package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToDoubleBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 4. 寻找两个正序数组的中位数: https://leetcode-cn.com/problems/median-of-two-sorted-arrays/
 *
 * 给定两个分别大小为 m 和 n 的排序数组 nums1 和 nums2，返回两个排序数组的中位数。
 * 写出时间复杂度 O(log(m+n)) 的实现。
 * <p>
 * 例 1：
 * Input: nums1 = [1,3], nums2 = [2]
 * Output: 2.00000
 * Explanation: merged array = [1,2,3] and median is 2.
 * <p>
 * 例 2：
 * Input: nums1 = [1,2], nums2 = [3,4]
 * Output: 2.50000
 * Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
 * <p>
 * 例 3：
 * Input: nums1 = [0,0], nums2 = [0,0]
 * Output: 0.00000
 * <p>
 * 例 4：
 * Input: nums1 = [], nums2 = [1]
 * Output: 1.00000
 * <p>
 * 例 5：
 * Input: nums1 = [2], nums2 = []
 * Output: 2.00000
 * <p>
 * 约束：
 * - nums1.length == m
 * - nums2.length == n
 * - 0 <= m <= 1000
 * - 0 <= n <= 1000
 * - 1 <= m + n <= 2000
 * - -10**6 <= nums1[i], nums2[i] <= 10**6
 */
public class E4_Hard_MedianOfTwoSortedArrays {

    static void test(ToDoubleBiFunction<int[], int[]> method) {
        assertEquals(method.applyAsDouble(new int[]{1, 3}, new int[]{2}), 2.0);

        assertEquals(method.applyAsDouble(new int[]{1, 2}, new int[]{3, 4}), 2.5);

        assertEquals(method.applyAsDouble(new int[]{0, 0}, new int[]{0, 0}), 0.0);

        assertEquals(method.applyAsDouble(new int[]{}, new int[]{1}), 1.0);

        assertEquals(method.applyAsDouble(new int[]{2}, new int[]{}), 2.0);
    }

    /**
     * 这里有两个数组，如果采用传统方式，意味着要同时分析至少两个变量，
     * 这会导致很复杂，所以我们应该试着找出这两个变量的关系。
     *
     * 在统计中，中位数用于将集合划分为两个相等长度的子集，其中一个子集始终大于另一个子集。
     *
     * 假设有两个数组 A，B。A 的长度为 m，B 的长度为 n。
     * 首先，让我们在任意位置 i 将 A 分为两部分：
     *       left_A             |        right_A
     * A[0], A[1], ..., A[i-1]  |  A[i], A[i+1], ..., A[m-1]
     * 由于 A 有 m 个元素，因此有 m+1 种切法(i=0〜m)。我们知道：len(left_A)=i，len(right_A)=m-i。
     * 注意：当 i=0 时，left_A 为空，而当 i=m 时，right_A 为空。
     *
     * 用相同的方法，在任意位置 j 处将 B 切成两部分：
     *       left_B             |        right_B
     * B[0], B[1], ..., B[j-1]  |  B[j], B[j+1], ..., B[n-1]
     *
     * 将 left_A 和 left_B 放入一组，然后将 right_A 和 right_B 放入另一组。
     * 让我们将它们命名为 left_part 和 right_part：
     *       left_part          |        right_part
     * A[0], A[1], ..., A[i-1]  |  A[i], A[i+1], ..., A[m-1]
     * B[0], B[1], ..., B[j-1]  |  B[j], B[j+1], ..., B[n-1]
     *
     * 当 A 和 B 的长度是偶数时，如果我们能够保证：
     * 1) len(left_part) == len(right_part)
     * 2) max(left_part) <= min(right_part)
     * 就有 median = (max(left_part)+ min(right_part))/ 2。
     *
     * 当 A 和 B 的长度是奇数时，如果我们能够保证：
     * 1) len(left_part) == len(right_part) + 1
     * 2) max(left_part) <= min(right_part)
     * 就有 median = max(left_part)。
     *
     * 为了确保这两个条件，我们只需要确保：
     * (1) i + j == m - i + n - j (当 m + n 是偶数) 或 i + j == m - i + n - j + 1 (当 m + n 是奇数)
     *     于是有 i + j = (m + n + 1) / 2，这里的分数结果只保留整数部分。
     * (2) 如果我们规定 m <= n，则有 i ∈ [0, m], j = (m + n + 1)/2 - i ∈ [0, n]。
     *     那么我们在 [0, m] 的范围内枚举 i 并得到 j，就不需要额外的性质了。
     * (3) 如果 A 的长度较大，那么我们只要交换 A 和 B 即可。
     *
     * 为了简化分析，假设 A[i−1],B[j−1],A[i],B[j] 总是存在。对于 i=0、i=m、j=0、j=n 这样的临界条件，
     * 我们只需要规定 A[−1]=B[−1]=−∞，A[m]=B[n]=∞ 即可。这也是比较直观的：当一个数组不出现在前一部分时，
     * 对应的值为负无穷，就不会对前一部分的最大值产生影响；当一个数组不出现在后一部分时，对应的值为正无穷，
     * 就不会对后一部分的最小值产生影响。
     *
     * 因此，我们需要做的是：
     * 在 [0，m] 中搜索 i，以找到元素 i，使得：
     *      B[j-1] <= A[i] && A[i-1] <= B[j]（其中 j=(m + n + 1)/ 2-i）。
     *
     * 我们证明它等价于：
     * 在 [0, m] 中找到「最大的 i」，使得：
     *      A[i-1] <= B[j]（其中 j=(m + n + 1)/ 2-i）
     *
     * 这是因为：
     * - 当 i 从 0∼m 递增时，A[i−1] 递增，B[j] 递减，所以一定存在一个「最大的 i」满足 A[i−1]≤B[j]；
     * - 如果 i 是最大的，那么说明 i+1 不满足。将 i+1 带入可以得到 A[i]>B[j−1]，也就是 B[j−1] < A[i]，
     *   就和我们进行等价变换前 i 的性质一致了（甚至还要更强）。
     *
     * 因此我们可以在 [0, m] 上进行二分查找
     *
     * LeetCode 耗时：2ms - 99.81%
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            int[] tmp = nums1;
            nums1 = nums2;
            nums2 = tmp;
        }

        int m = nums1.length, n = nums2.length;
        int iMin = 0, iMax = nums1.length;
        // median1：前一部分的最大值。median2：后一部分的最小值
        int median1 = 0, median2 = 0;

        while (iMin <= iMax) {
            // 前一部分包含 nums1[0 .. i-1] 和 nums2[0 .. j-1]
            // 后一部分包含 nums1[i .. m-1] 和 nums2[j .. n-1]
            int i = (iMin + iMax) / 2;
            int j = (m + n + 1) / 2 - i;

            // nums1_im1, nums1_i, nums2_jm1, nums2_j 分别表示 nums1[i-1], nums1[i], nums2[j-1], nums2[j]
            int nums1_im1 = (i == 0 ? Integer.MIN_VALUE : nums1[i - 1]);
            int nums1_i = (i == m ? Integer.MAX_VALUE : nums1[i]);
            int nums2_jm1 = (j == 0 ? Integer.MIN_VALUE : nums2[j - 1]);
            int nums2_j = (j == n ? Integer.MAX_VALUE : nums2[j]);

            if (nums1_im1 <= nums2_j) {
                median1 = Math.max(nums1_im1, nums2_jm1);
                median2 = Math.min(nums1_i, nums2_j);
                iMin = i + 1;
            } else {
                iMax = i - 1;
            }
        }

        return (m + n) % 2 == 0 ? (median1 + median2) / 2.0 : median1;
    }

    @Test
    public void testFindMedianSortedArrays() {
        test(this::findMedianSortedArrays);
    }
}
