package training.prefixdiff;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    }

    /**
     * LeetCode 耗时：15 ms - 10.14%
     *          内存消耗：44.6 MB - 5.01%
     */
    public int longestOnes(int[] nums, int k) {
        // zeroPrefix[0] 存放每段连续 0 的个数，以前缀和的形式；
        // zeroPrefix[1] 存放该段连续 0 的起始下标；zeroPrefix
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

        if (zeroPrefix.isEmpty() || k >= zeroPrefix.get(zeroPrefix.size() - 1)[0]) {
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
}
