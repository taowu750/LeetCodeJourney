package training.stack;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 321. 拼接最大数: https://leetcode-cn.com/problems/create-maximum-number/
 *
 * 给定长度分别为 m 和 n 的两个数组，其元素由 0-9 构成，表示两个自然数各位上的数字。
 * 现在从这两个数组中选出 k (k <= m + n) 个数字拼接成一个新的数，要求从同一个数组中取出的数字保持其在原数组中的相对顺序。
 *
 * 求满足该条件的最大数。结果返回一个表示该最大数的长度为 k 的数组。
 *
 * 例 1：
 * 输入:
 * nums1 = [3, 4, 6, 5]
 * nums2 = [9, 1, 2, 5, 8, 3]
 * k = 5
 * 输出:
 * [9, 8, 6, 5, 3]
 *
 * 例 2：
 * 输入:
 * nums1 = [6, 7]
 * nums2 = [6, 0, 4]
 * k = 5
 * 输出:
 * [6, 7, 6, 0, 4]
 *
 * 例 3：
 * 输入:
 * nums1 = [3, 9]
 * nums2 = [8, 9]
 * k = 3
 * 输出:
 * [9, 8, 9]
 */
public class E321_Hard_CreateMaximumNumber {

    public static void test(TriFunction<int[], int[], Integer, int[]> method) {
        assertArrayEquals(new int[]{9, 8, 9}, method.apply(new int[]{3, 9}, new int[]{8, 9}, 3));
        assertArrayEquals(new int[]{6, 7, 6, 0, 4}, method.apply(new int[]{6, 7}, new int[]{6, 0, 4}, 5));
        assertArrayEquals(new int[]{9, 8, 6, 5, 3}, method.apply(new int[]{3, 4, 6, 5}, new int[]{9, 1, 2, 5, 8, 3}, 5));
        assertArrayEquals(new int[]{7, 4, 3}, method.apply(new int[]{5, 7, 3}, new int[]{4, 2, 3}, 3));
        assertArrayEquals(new int[]{8, 8, 8, 5, 4}, method.apply(new int[]{3, 8, 5, 3, 4}, new int[]{8, 7, 3, 6, 8}, 5));
        assertArrayEquals(new int[]{9, 8, 9}, method.apply(new int[]{8, 9}, new int[]{3, 9}, 3));
    }

    /**
     * 超时。
     */
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        if (k == 0) {
            return new int[0];
        }

        // maxs[i] 表示前 i+1 位数字中的最大数
        int[][] maxs = new int[k][];
        for (int i = 0; i < k; i++) {
            maxs[i] = new int[i + 1];
        }
        dfs(nums1, nums2, k, maxs, new int[k], 0, 0, 0);

        return maxs[k - 1];
    }

    /**
     * 在 i1、i2 中选择，有如下情况：
     * - 选择 0 个：两个都丢弃
     * - 选择 0 个：丢弃其中一个，保留另一个
     * - 选择 1 个：选择其中较大的，但仍保留较小的
     * - 选择 2 个：两个都选择
     */
    private void dfs(int[] nums1, int[] nums2, int k,
                     int[][] maxs, int[] num, int i1, int i2, int i3) {
        if (i3 == k) {
            return;
        }

        final int m = nums1.length, n = nums2.length;
        final int remainCnt = nums1.length - (i1 < m ? i1 : m - 1) - 1 + nums2.length - (i2 < n ? i2 : n - 1) - 1;
        final int waitCnt = k - i3;
        // 如果后面的数字数量大于等于待选数量 k - i3，则尝试两个数组都不选
        if (remainCnt >= waitCnt) {
            dfs(nums1, nums2, k, maxs, num, i1 + 1, i2 + 1, i3);
        }
        // nums1 用完了，选 nums2 的数字
        if (i1 >= nums1.length) {
            num[i3] = nums2[i2];
            // 如果选择的数字大于等于 maxs[k]，则可以继续下去
            if (cmp(num, maxs[i3], i3 + 1) >= 0) {
                System.arraycopy(num, 0, maxs[i3], 0, i3 + 1);
                dfs(nums1, nums2, k, maxs, num, i1, i2 + 1, i3 + 1);
            }
        } else if (i2 >= nums2.length) {  // nums2 用完了，选 nums1 的数字
            num[i3] = nums1[i1];
            if (cmp(num, maxs[i3], i3 + 1) >= 0) {
                System.arraycopy(num, 0, maxs[i3], 0, i3 + 1);
                dfs(nums1, nums2, k, maxs, num, i1 + 1, i2, i3 + 1);
            }
        } else {
            // 如果待选数字数量大于大于2，就先两个数字都选，让大的排在前面
            if (waitCnt >= 2) {
                num[i3] = Math.max(nums1[i1], nums2[i2]);
                num[i3 + 1] = Math.min(nums1[i1], nums2[i2]);
                if (cmp(num, maxs[i3 + 1], i3 + 2) >= 0) {
                    System.arraycopy(num, 0, maxs[i3 + 1], 0, i3 + 2);
                    dfs(nums1, nums2, k, maxs, num, i1 + 1, i2 + 1, i3 + 2);
                }
            }
            // 如果丢弃其中一个，剩余元素仍然大于待选数字数量
            if (remainCnt + 1 >= waitCnt) {
                dfs(nums1, nums2, k, maxs, num, i1, i2 + 1, i3);
                dfs(nums1, nums2, k, maxs, num, i1 + 1, i2, i3);
            }
            // 两个中选择一个大的
            num[i3] = Math.max(nums1[i1], nums2[i2]);
            if (cmp(num, maxs[i3], i3 + 1) >= 0) {
                System.arraycopy(num, 0, maxs[i3], 0, i3 + 1);
                if (nums1[i1] >= nums2[i2]) {
                    dfs(nums1, nums2, k, maxs, num, i1 + 1, i2, i3 + 1);
                }
                if (nums2[i2] >= nums1[i1]) {
                    dfs(nums1, nums2, k, maxs, num, i1, i2 + 1, i3 + 1);
                }
            }
        }
    }

    private int cmp(int[] nums1, int[] nums2, int len) {
        for (int i = 0; i < len; i++) {
            int cmp = Integer.compare(nums1[i], nums2[i]);
            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }

    @Test
    public void testMaxNumber() {
        test(this::maxNumber);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/create-maximum-number/solution/pin-jie-zui-da-shu-by-leetcode-solution/
     *
     * 为了找到长度为 k 的最大数，需要从两个数组中分别选出最大的子序列，这两个子序列的长度之和为 k，然后将这两个子序列合并得到最大数。
     * 两个子序列的长度最小为 0，最大不能超过 k 且不能超过对应的数组长度。
     *
     * 令数组 nums1 的长度为 m，数组 nums2的长度为 n，则需要从数组 nums1中选出长度为 x 的子序列，
     * 以及从数组 nums2中选出长度为 y 的子序列，其中 x+y=k，且满足 0≤x≤m 和 0≤y≤n。
     * 需要遍历所有可能的 x 和 y 的值，对于每一组 x 和 y 的值，得到最大数。在整个过程中维护可以通过拼接得到的最大数。
     *
     * 对于每一组 x 和 y 的值，得到最大数的过程分成两步，第一步是分别从两个数组中得到指定长度的最大子序列，
     * 第二步是将两个最大子序列合并。
     *
     * 第一步可以通过单调栈实现。单调栈满足从栈底到栈顶的元素单调递减，从左到右遍历数组，遍历过程中维护单调栈内的元素，
     * 需要保证遍历结束之后单调栈内的元素个数等于指定的最大子序列的长度。遍历结束之后，将从栈底到栈顶的元素依次拼接，
     * 即得到最大子序列。
     *
     * 第二步需要自定义比较方法。首先比较两个子序列的当前元素，如果两个当前元素不同，则选其中较大的元素作为下一个合并的元素，
     * 否则需要比较后面的所有元素才能决定选哪个元素作为下一个合并的元素。
     *
     * LeetCode 耗时：6 ms - 93.51%
     *          内存消耗：41.4 MB - 56.70%
     */
    public int[] stackMethod(int[] nums1, int[] nums2, int k) {
        if (k == 0) {
            return new int[0];
        }

        int[] best = new int[k], cur = new int[k];
        final int m = nums1.length, n = nums2.length;
        int min1len = Math.max(0, k - n), max1len = Math.min(m, k);
        for (int x = min1len, y = k - x; x <= max1len; x++, y--) {
            int[] max1 = maxNumber(nums1, x);
            int[] max2 = maxNumber(nums2, y);
            merge(max1, max2, cur);
            if (compare(cur, 0, best, 0) > 0) {
                int[] tmp = cur;
                cur = best;
                best = tmp;
            }
        }

        return best;
    }

    public int[] maxNumber(int[] nums, int len) {
        final int n = nums.length;
        int[] stack = new int[len];
        int top = -1;
        for (int i = 0; i < n; i++) {
            while (top >= 0 && stack[top] < nums[i] && top + n - i >= len) {
                top--;
            }
            if (top < len - 1) {
                stack[++top] = nums[i];
            }
        }

        return stack;
    }

    public void merge(int[] nums1, int[] nums2, int[] dst) {
        for (int i = 0, j = 0, k = 0; k < dst.length;) {
            if (compare(nums1, i, nums2, j) > 0) {
                dst[k++] = nums1[i++];
            } else {
                dst[k++] = nums2[j++];
            }
        }
    }

    public int compare(int[] nums1, int i1, int[] nums2, int i2) {
        int x = nums1.length, y = nums2.length;
        while (i1 < x && i2 < y) {
            int cmp = nums1[i1] - nums2[i2];
            if (cmp != 0) {
                return cmp;
            }
            i1++;
            i2++;
        }
        // 前面都相同，长的序列更优先
        return (x - i1) - (y - i2);
    }

    @Test
    public void testStackMethod() {
        test(this::stackMethod);
    }
}
