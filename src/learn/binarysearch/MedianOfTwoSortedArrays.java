package learn.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToDoubleBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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
public class MedianOfTwoSortedArrays {

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
     * 如果我们能够保证：
     * 1) len(left_part) == len(right_part)
     * 2) max(left_part) <= min(right_part)
     * 就有 median = (max(left_part)+ min(right_part))/ 2。
     *
     * 为了确保这两个条件，我们只需要确保：
     * (1) i + j == m - i + n - j (or: m - i + n - j + 1)
     *     if n >= m, 则有 i = 0 ~ m, j = (m + n + 1)/2 - i
     * (2) B[j-1] <= A[i] and A[i-1] <= B[j]
     * 注意，必须 n >= m，因为我必须确保 j 为非负数，因为 0 <= i <= m 并且 j=(m+n+1)/2 - i。
     * 如果 n < m，则 j 可能为负，这将导致错误的结果。
     * 其他边界情况后面会讲到。
     *
     * 因此，我们需要做的是：
     * 在 [0，m] 中搜索 i，以找到元素`i`使得，B[j-1] <= A[i] && A[i-1] <= B[j]
     * （其中 j=(m + n + 1)/ 2-i）。
     *
     * 我们可以按照以下步骤进行二分查找：
     * (1) 令 imin = 0，imax = m，然后开始在 [imin，imax] 中搜索
     * (2) 令 i = (imin + imax)/2, j = (m + n + 1)/2 - i
     * (3) 现在我们有 len(left_part)==len(right_part)。现在会有下面三种情况：
     *     (a) B[j-1] <= A[i] && A[i-1] <= B[j]
     *         意味着我们已经找到了元素“i”，因此停止搜索。
     *     (b) B[j-1] > A[i]
     *         表示 A[i] 太小。我们必须调整 i 才能得到 B[j-1] <= A[i]。
     *         我们必须增加 i。也就是说，我们必须将搜索范围调整为
     *         [i + 1，imax]。因此，设置 imin = i+1，然后转到 (2)。
     *     (c) A[i-1] > B[j]
     *         表示 A[i-1] 太大。我们必须减小 i 以使得 A[i-1] <= B[j]。
     *         也就是说，我们必须将搜索范围调整为 [imin, i-1]。
     *         因此，设置 imax = i-1，然后转到 (2)。
     *
     * 找到元素 i 时，中位数为：
     * max(A[i-1], B[j-1]) (当 m + n 是奇数)
     * 或者 (max(A[i-1], B[j-1]) + min(A[i], B[j]))/2 (当 m + n 是偶数)
     *
     * 现在让我们考虑边界条件 i=0，i=m，j=0，j=n，这会使 A[i-1]，B[j-1]，A[i]，B[j]
     * 可能不存在。
     *
     * 我们需要确保 max(left_part) <= min(right_part)。
     * 当 i，j 不是边界值时，只要检查 B[j-1] <= A[i] && A[i-1] <= B[j]。
     * 当 i, j 是边界值时，我们只需要检查其中一个条件。
     * 因此，我们需要做的是：
     * 在 [0, m] 中查找元素`i`使得:
     *     (j == 0 or i == m or B[j-1] <= A[i]) and
     *     (i == 0 or j == n or A[i-1] <= B[j])
     *     其中 j = (m + n + 1)/2 - i
     *
     * 在搜索循环中，我们只会遇到三种情况：
     * (1) (j == 0 or i == m or B[j-1] <= A[i]) and
     *     (i == 0 or j = n or A[i-1] <= B[j])
     *     此时 i 是要查找的值，终止循环
     * (2) j > 0 and i < m and B[j - 1] > A[i]
     *     表示 i 太小，我们必须增加它。
     * (3) i > 0 and j < n and A[i - 1] > B[j]
     *     表示 i 太大，我们必须减小它。
     *
     * 其中，由 i < m 可推得 j > 0；i > 0 可推得 j < n，因为
     *   m <= n, i < m ==> j = (m+n+1)/2 - i > (m+n+1)/2 - m >= (2*m+1)/2 - m >= 0
     *   m <= n, i > 0 ==> j = (m+n+1)/2 - i < (m+n+1)/2 <= (2*n+1)/2 <= n
     * 所以在 (2)、(3) 中我们其实可以不检查 j > 0 和 j < n。
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
        int i, j;
        for (;;) {
            i = (iMin + iMax) >>> 1;
            j = (m + n + 1) / 2 - i;
            if ((j == 0 || i == m || nums2[j - 1] <= nums1[i])
                    && (i == 0 || j == n || nums1[i - 1] <= nums2[j]))
                break;
            else if (i < m && nums2[j - 1] > nums1[i])
                iMin = i + 1;
            else
                iMax = i - 1;
        }

        /*
        max(A[i-1], B[j-1])

        i = 0 时，j = (m + n + 1) / 2 - i >= 1
        j = 0 时，i = (m + n + 1) / 2 >= 1
         */
        int max = i == 0
                ? nums2[j - 1]
                : j == 0 ? nums1[i - 1] : Math.max(nums1[i - 1], nums2[j - 1]);
        if ((m + n) % 2 == 1)
            return max;
        else {
            // (max(A[i-1], B[j-1]) + min(A[i], B[j]))/2
            int min = 0;
            if (i != m && j != n)
                min = Math.min(nums1[i], nums2[j]);
            else if (i != m)
                min = nums1[i];
            else if (j != n)
                min = nums2[j];
            return (min + max) / 2.0;
        }
    }

    @Test
    public void testFindMedianSortedArrays() {
        test(this::findMedianSortedArrays);
    }
}
