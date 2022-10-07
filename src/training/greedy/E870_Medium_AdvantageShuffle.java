package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 * 870. 优势洗牌: https://leetcode-cn.com/problems/advantage-shuffle/
 *
 * 给定两个大小相等的数组 A 和 B，A 相对于 B 的「优势」可以用满足 A[i] > B[i] 的索引 i 的数目来描述。
 *
 * 返回 A 的任意排列，使其相对于 B 的优势最大化。
 *
 * 例 1：
 * 输入：A = [2,7,11,15], B = [1,10,4,11]
 * 输出：[2,11,7,15]
 *
 * 例 2：
 * 输入：A = [12,24,8,32], B = [13,25,32,11]
 * 输出：[24,32,8,12]
 *
 * 说明：
 * - 1 <= A.length = B.length <= 10000
 * - 0 <= A[i] <= 10^9
 * - 0 <= B[i] <= 10^9
 */
public class E870_Medium_AdvantageShuffle {

    public static void assertAdvantage(int expected, BinaryOperator<int[]> method, int[] nums1, int[] nums2) {
        int[] nums = nums2.clone();
        int[] result = method.apply(nums1, nums2);
        int advantage = 0;
        for (int i = 0; i < nums.length; i++) {
            if (result[i] > nums[i]) {
                advantage++;
            }
        }
        if (expected != advantage) {
            throw new AssertionError("wrong advantage " + advantage + ", actual=" + Arrays.toString(result));
        }
    }

    public static void test(BinaryOperator<int[]> method) {
        assertAdvantage(4, method, new int[]{2,7,11,15}, new int[]{1,10,4,11});
        assertAdvantage(3, method, new int[]{12,24,8,32}, new int[]{13,25,32,11});
        assertAdvantage(8, method, new int[]{8,2,4,4,5,6,6,0,4,7}, new int[]{0,8,7,4,4,2,8,5,2,0});
    }

    /**
     * 田忌赛马，贪心解法。
     *
     * LeetCode 耗时：108 ms - 5.21%
     *          内存消耗：59.5 MB - 27.06%
     */
    public int[] advantageCount(int[] nums1, int[] nums2) {
        int[] sorted = nums1.clone();
        Arrays.sort(sorted);
        // 将 nums1 填充 -1，表示还没有安排数字
        Arrays.fill(nums1, -1);

        final int n = nums1.length;
        // 按照 nums2 中数字在 sorted 中的大于下标进行排序
        PriorityQueue<int[]> pq = new PriorityQueue<>(n, (a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 0; i < n; i++) {
            pq.add(new int[]{bs(sorted, nums2[i]), i});
        }
        // 记录 sorted 中还没有使用的下标
        Set<Integer> unusedIndices = new HashSet<>((int) (n / 0.75) + 1);
        for (int i = 0; i < n; i++) {
            unusedIndices.add(i);
        }
        int lastUseIdx = -1;
        while (!pq.isEmpty()) {
            int[] elem = pq.remove();
            // 如果 nums2 中的数字大于所有 nums1 中数字
            if (elem[0] == n) {
                break;
            }
            // elem[0] 还未被使用
            if (elem[0] > lastUseIdx) {
                // 安排一个数字
                nums1[elem[1]] = sorted[elem[0]];
                lastUseIdx = elem[0];
                unusedIndices.remove(elem[0]);
            } else {  // 这个位置已经被用了
                if (lastUseIdx + 1 < n) {
                    lastUseIdx++;
                    nums1[elem[1]] = sorted[lastUseIdx];
                    unusedIndices.remove(lastUseIdx);
                } else {
                    break;
                }
            }
        }

        int i = 0;
        for (Integer unusedIdx : unusedIndices) {
            while (nums1[i] != -1) {
                i++;
            }
            nums1[i] = sorted[unusedIdx];
        }

        return nums1;
    }

    /**
     * 找到最小的大于 target 的下标
     */
    private int bs(int[] nums, int target) {
        int lo = 0, hi = nums.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (nums[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    @Test
    public void testAdvantageCount() {
        test(this::advantageCount);
    }


    /**
     * 双指针+贪心，参见：
     * https://leetcode-cn.com/problems/advantage-shuffle/solution/java-pai-xu-shuang-zhi-zhen-by-programme-wszf/
     *
     * 核心思路是将两个数组都进行排序，然后每次用nums1中未被排列的最大值去和nums2中未被比较的最大值进行比较，
     * 赢了就放在相应位置上，输了就把nums1中未被排列的最小值放在相应位置上。
     *
     * 怎样知道nums1中未被排列的最大值和最小值？
     * - 因为数组nums1经过排序，对于有序数组，只需要使用双指针来记录未被排列的边界即可。
     *
     * 怎样知道相应位置的索引？
     * - 相应位置就是nums2中未被比较过的最大值对应的索引，所以不能直接对nums2中的元素进行排序，需要带着索引进行排序。
     *   另外，每次需要取出最大值，自然而然想到了大根堆。因此利用PriorityQueue的定制排序可以轻松实现。
     *
     * LeetCode 耗时：70 ms - 66.63%
     *          内存消耗：54 MB - 85.24%
     */
    public int[] pointMethod(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        final int n = nums1.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>(n, (a, b) -> b[0] - a[0]);
        for (int i = 0; i < n; i++) {
            pq.add(new int[]{nums2[i], i});
        }

        int[] result = new int[n];
        int left = 0, right = n - 1;
        while (!pq.isEmpty()) {
            int[] elem = pq.remove();
            if (elem[0] < nums1[right]) {
                result[elem[1]] = nums1[right--];
            } else {
                result[elem[1]] = nums1[left++];
            }
        }

        return result;
    }

    @Test
    public void testPointMethod() {
        test(this::pointMethod);
    }


    /**
     * 和上面的方法思想类似，只不过用排序替代了优先队列。
     *
     * LeetCode 耗时：65 ms - 72.51%
     *          内存消耗：58.8 MB - 34.69%
     */
    public int[] betterGreedyMethod(int[] nums1, int[] nums2) {
        final int n = nums1.length;
        int[][] nums2Idx = new int[n][2];
        for (int i = 0; i < nums2.length; i++) {
            nums2Idx[i][0] = i;
            nums2Idx[i][1] = nums2[i];
        }
        Arrays.sort(nums1);
        Arrays.sort(nums2Idx, Comparator.comparingInt(a -> a[1]));

        int[] ans = new int[n];
        for (int i = 0, j = n - 1, k = n - 1; i <= j; k--) {
            if (nums1[j] > nums2Idx[k][1]) {
                ans[nums2Idx[k][0]] = nums1[j--];
            } else {
                ans[nums2Idx[k][0]] = nums1[i++];
            }
        }

        return ans;
    }

    @Test
    public void testBetterGreedyMethod() {
        test(this::betterGreedyMethod);
    }


    /**
     * 类似于田忌赛马的思路，如果 nums1 最小值大于 nums2 最小值则配对，否则 nums1 最小值和 nums2 最大值配对。
     * 其中用到了排序下标和原地赋值的技巧。
     *
     * 参见：https://leetcode.cn/problems/advantage-shuffle/solution/you-shi-xi-pai-by-leetcode-solution-sqsf/
     *
     * LeetCode 耗时：60 ms - 83.53%
     *          内存消耗：55.6 MB - 92.29%
     */
    public int[] bestGreedyMethod(int[] nums1, int[] nums2) {
        final int n = nums1.length;
        Integer[] indices = new Integer[n];
        Arrays.setAll(indices, i -> i);
        Arrays.sort(nums1);
        // 排序下标
        Arrays.sort(indices, (i, j) -> nums2[i] - nums2[j]);

        for (int i = 0, l = 0, r = n - 1; i < n; i++) {
            if (nums1[i] > nums2[indices[l]]) {
                nums2[indices[l++]] = nums1[i];
            } else {
                nums2[indices[r--]] = nums1[i];
            }
        }

        return nums2;
    }

    @Test
    public void testBestGreedyMethod() {
        test(this::bestGreedyMethod);
    }
}
