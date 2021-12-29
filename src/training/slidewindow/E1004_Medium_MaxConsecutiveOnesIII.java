package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1004. 最大连续1的个数 III: https://leetcode-cn.com/problems/max-consecutive-ones-iii/
 *
 * 给定一个由若干 0 和 1 组成的数组 A，我们最多可以将 K 个值从 0 变成 1 。
 *
 * 返回仅包含 1 的最长（连续）子数组的长度。
 *
 * 例 1：
 * 输入：A = [1,1,1,0,0,0,1,1,1,1,0], K = 2
 * 输出：6
 * 解释：
 * [1,1,1,0,0,*1*,1,1,1,1,*1*]
 * 粗体数字从 0 翻转到 1，最长的子数组长度为 6。
 *
 * 例 2：
 * 输入：A = [0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], K = 3
 * 输出：10
 * 解释：
 * [0,0,1,1,*1*,*1*,1,1,1,*1*,1,1,0,0,0,1,1,1,1]
 * 粗体数字从 0 翻转到 1，最长的子数组长度为 10。
 *
 * 说明：
 * - 1 <= A.length <= 20000
 * - 0 <= K <= A.length
 * - A[i] 为 0 或 1
 */
public class E1004_Medium_MaxConsecutiveOnesIII {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(6, method.applyAsInt(new int[]{1,1,1,0,0,0,1,1,1,1,0}, 2));
        assertEquals(10, method.applyAsInt(new int[]{0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1}, 3));
        assertEquals(19, method.applyAsInt(new int[]{0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1}, 8));
        assertEquals(4, method.applyAsInt(new int[]{0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1}, 0));
        assertEquals(4, method.applyAsInt(new int[]{0,0,0,1}, 4));
        assertEquals(5, method.applyAsInt(new int[]{1,1,1,1,1}, 0));
        assertEquals(25, method.applyAsInt(new int[]{1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1},
                8));
    }

    /**
     * 前缀和+二分查找。
     *
     * LeetCode 耗时：15 ms - 10.14%
     *          内存消耗：44.6 MB - 5.01%
     */
    public int longestOnes(int[] nums, int k) {
        // zeroPrefix[0] 存放每段连续 0 的个数，以前缀和的形式；
        // zeroPrefix[1] 存放该段连续 0 的起始下标；
        // zeroPrefix[2] 存放该段连续 0 的结束下标（不包含）
        List<int[]> zeroPrefix = new ArrayList<>();
        zeroPrefix.add(new int[]{0, 0, 0});
        int zeroCnt = 0, oneCnt = 0, maxOneCnt = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                zeroCnt++;
                if (oneCnt > 0) {
                    maxOneCnt = Math.max(maxOneCnt, oneCnt);
                    oneCnt = 0;
                }
            } else {
                oneCnt++;
                if (zeroCnt > 0) {
                    zeroPrefix.add(new int[]{zeroPrefix.get(zeroPrefix.size() - 1)[0] + zeroCnt, i - zeroCnt, i});
                    zeroCnt = 0;
                }
            }
        }
        if (zeroCnt > 0) {
            zeroPrefix.add(new int[]{zeroPrefix.get(zeroPrefix.size() - 1)[0] + zeroCnt, nums.length - zeroCnt, nums.length});
        }
        if (oneCnt > 0) {
            maxOneCnt = Math.max(maxOneCnt, oneCnt);
        }

        if (k >= zeroPrefix.get(zeroPrefix.size() - 1)[0]) {
            return nums.length;
        } else if (k == 0) {
            return maxOneCnt;
        }

        // 遍历，看看 k 能填补哪些连续的 0
        int result = 0;
        for (int i = 1; i < zeroPrefix.size(); i++) {
            int[] zeroSegment = zeroPrefix.get(i);
            int prefixCnt = zeroSegment[0], cnt = zeroSegment[2] - zeroSegment[1];

            // 从第 i 个连续 0 片段开始往右找
            int rightIdx = Collections.binarySearch(zeroPrefix, new int[]{prefixCnt - cnt + k}, (a, b) -> a[0] - b[0]);
            // lo 等于前一个0片段的结尾
            int lo = zeroPrefix.get(i - 1)[2], hi;
            if (rightIdx >= 0) {  // 刚好找到能完美填补的
                // 能填充最后一段0片段，那么就能一直到数组结尾；否则就到下一个0片段的开头
                hi = rightIdx == zeroPrefix.size() - 1 ? nums.length : zeroPrefix.get(rightIdx + 1)[1];
            } else {
                rightIdx = -rightIdx - 1;
                hi = rightIdx < zeroPrefix.size()
                        // 计算能填补 rightIdx 处0片段多少个 0
                        ? zeroPrefix.get(rightIdx)[1] + (k - zeroPrefix.get(rightIdx - 1)[0] - zeroPrefix.get(i - 1)[0])
                        // k 覆盖了全部的 0 还多出来
                        : nums.length;
            }
            result = Math.max(result, hi - lo);

            // 从第 i 个连续 0 片段开始往左找
            int leftIdx = Collections.binarySearch(zeroPrefix, new int[]{prefixCnt - k}, (a, b) -> a[0] - b[0]);
            // 最后一个 0 片段则 hi 取数组末尾，否则取下一个 0 片段开头
            hi = i == zeroPrefix.size() - 1 ? nums.length : zeroPrefix.get(i + 1)[1];
            if (leftIdx >= 0) {  // 刚好找到能完美填补的
                // lo 等于 leftIdx 处的末尾
                lo = zeroPrefix.get(leftIdx)[2];
            } else {
                leftIdx = -leftIdx - 1;
                int[] leftSegment = zeroPrefix.get(leftIdx);
                // 注意不能让 lo 减到小于 0
                lo = Math.max(0, leftSegment[2] - (k - (zeroPrefix.get(i)[0] - leftSegment[0])));
            }
            result = Math.max(result, hi - lo);
        }

        return result;
    }

    @Test
    public void testLongestOnes() {
        test(this::longestOnes);
    }


    /**
     * 更好的前缀和+二分查找方法，参见：
     * https://leetcode-cn.com/problems/max-consecutive-ones-iii/solution/zui-da-lian-xu-1de-ge-shu-iii-by-leetcod-hw12/
     *
     * 对于任意的右端点 right，希望找到最小的左端点 left，使得 [left,right] 包含不超过 k 个 0。
     *
     * 我们对数组 A 的 0 的数量求出前缀和，记为数组 P，那么 [left,right] 中包含不超过 k 个 1，当且仅当二者的前缀和之差：
     *      P[right]−P[left−1] ≤ k
     * 这也等价于：
     *      P[left−1] ≥ P[right]−k
     *
     * 因为左侧的下标是 left−1 而不是 left，那么我们在构造前缀和数组时，可以将前缀和整体向右移动一位，空出 P[0] 的位置。
     * 此时，我们在数组 P 上二分查找到的下标即为 left 本身，同时我们也避免了原先 left=0 时 left−1=−1 不在数组合法的下标范围中的边界情况。
     *
     * LeetCode 耗时：15 ms - 10.05%
     *          内存消耗：40.6 MB - 7.74%
     */
    public int betterPrefixMethod(int[] nums, int k) {
        int[] prefix = new int[nums.length + 1];
        for (int i = 1; i < prefix.length; i++) {
            prefix[i] = prefix[i - 1] + 1 - nums[i - 1];
        }

        int result = 0;
        for (int right = 0; right < nums.length; right++) {
            // 有可能 prefix[right + 1] - k 小于 0，所以必须这样做
            int start = binarySearch(prefix, prefix[right + 1] - k);
            result = Math.max(result, right + 1 - start);
        }

        return result;
    }

    /**
     * 找到最左边大于等于 target 的下标
     */
    private int binarySearch(int[] arr, int target) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    @Test
    public void testBetterPrefixMethod() {
        test(this::betterPrefixMethod);
    }


    /**
     * 滑动窗口。
     *
     * LeetCode 耗时：7 ms - 14.09%
     *          内存消耗：41.1 MB - 6.74%
     */
    public int slideWindowMethod(int[] nums, int k) {
        // 存放遇到的 0 的下标
        Deque<Integer> covertZeros = new ArrayDeque<>(k);
        int result = 0, left = 0, right = 0, remain = k;
        while (right < nums.length) {
            int num = nums[right++];
            if (num == 0) {
                // 还有剩余
                if (remain > 0) {
                    remain--;
                    // 添加 0 的下标
                    covertZeros.addLast(right - 1);
                } else {
                    result = Math.max(result, right - left - 1);
                    // 当 k == 0 时，会出现 covertZeros 等于空的情况
                    if (!covertZeros.isEmpty()) {
                        left = covertZeros.removeFirst() + 1;
                        // 这里也要添加 0 的下标
                        covertZeros.addLast(right - 1);
                    } else {
                        left = right;
                    }
                }
            }
        }
        result = Math.max(result, right - left);

        return result;
    }

    @Test
    public void testSlideWindowMethod() {
        test(this::slideWindowMethod);
    }


    /**
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：39.5 MB - 70.90%
     */
    public int betterSlideWindowMethod(int[] nums, int k) {
        int result = 0, left = 0, right = 0;
        while (right < nums.length) {
            int num = nums[right++];
            if (num == 0) {
                // 还有剩余
                if (k > 0) {
                    k--;
                } else {
                    result = Math.max(result, right - left - 1);
                    while (nums[left++] != 0);
                }
            }
        }
        result = Math.max(result, right - left);

        return result;
    }

    @Test
    public void testBetterSlideWindowMethod() {
        test(this::betterSlideWindowMethod);
    }
}
