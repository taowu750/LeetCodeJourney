package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 673. 最长递增子序列的个数: https://leetcode-cn.com/problems/number-of-longest-increasing-subsequence/
 *
 * 给定一个未排序的整数数组，找到最长递增子序列的个数。
 *
 * 例 1：
 * 输入: [1,3,5,4,7]
 * 输出: 2
 * 解释: 有两个最长递增子序列，分别是 [1, 3, 4, 7] 和 [1, 3, 5, 7]。
 *
 * 例 2：
 * 输入: [2,2,2,2,2]
 * 输出: 5
 * 解释: 最长递增子序列的长度是1，并且存在5个子序列的长度为1，因此输出5。
 *
 * 说明：
 * - 给定的数组长度不超过 2000 并且结果一定是32位有符号整数。
 */
public class E673_Medium_NumbeOfLongestIncreasingSubsequence {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(2, method.applyAsInt(new int[]{1,3,5,4,7}));
        assertEquals(5, method.applyAsInt(new int[]{2,2,2,2,2}));
    }

    /**
     * LeetCode 耗时：18 ms - 90.41%
     *          内存消耗：37.9 MB - 76.16%
     */
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        // dp[i] 表示以 i 结尾的最长递增子序列长度和个数
        int[][] dp = new int[n][2];
        int max = 1;
        for (int i = 0; i < n; i++) {
            dp[i][0] = dp[i][1] = 1;
            for (int j = i - 1; j >= 0; j--) {
                if (nums[j] < nums[i]) {
                    if (dp[j][0] + 1 > dp[i][0]) {
                        dp[i][0] = dp[j][0] + 1;
                        dp[i][1] = dp[j][1];
                    } else if (dp[j][0] + 1 == dp[i][0]) {
                        dp[i][1] += dp[j][1];
                    }
                }
            }
            max = Math.max(max, dp[i][0]);
        }

        int result = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i][0] == max) {
                result += dp[i][1];
            }
        }

        return result;
    }

    @Test
    public void testFindNumberOfLIS() {
        test(this::findNumberOfLIS);
    }


    /**
     * 贪心+二分查找方法+前缀和，其中求最长递增子序列的思想参见：
     * {@link E300_Medium_LongestIncreasingSubsequence#binarySearchMethod(int[])}。
     *
     * 整体思想参见：
     * https://leetcode-cn.com/problems/number-of-longest-increasing-subsequence/solution/zui-chang-di-zeng-zi-xu-lie-de-ge-shu-by-w12f/
     *
     * 我们将数组 tail 扩展成一个二维数组，其中 tail[i] 数组表示所有能成为长度为 i 的最长上升子序列的末尾元素的值。
     * 具体地，我们将更新 tail[i]=nums[j] 这一操作替换成将 nums[j] 置于 tail[i] 数组末尾。
     * 这样 tail[i] 中就保留了历史信息，且 tail[i] 中的元素是有序的（单调非增）。
     *
     * 类似地，我们也可以定义一个二维数组 cnt，其中 cnt[i][j] 记录了以 tail[i][j] 为结尾的最长上升子序列的个数。
     * 在代码实现时，由于 tail[i] 中的元素是有序的，我们可以二分得到最小的满足 tail[i−1][k]<tail[i][j] 的下标 k。
     * 另一处优化是将 cnt 改为其前缀和，并在开头填上 0，此时 tail[i][j] 对应的最长上升子序列的个数就是
     * cnt[i−1][−1]−cnt[i−1][k]，这里 [-1] 表示数组的最后一个元素。
     *
     * LeetCode 耗时：5 ms - 99.84%
     *          内存消耗：38.8 MB - 5.06%
     */
    public int greedyMethod(int[] nums) {
        List<Integer>[] tail = new List[nums.length], cnts = new List[nums.length];
        // len 表示 tail 数组实际长度
        int len = 0;
        for (int num : nums) {
            // 找到最左边大于等于 num 的下标
            int lo = 0, hi = len;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (getLast(tail[mid]) < num) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            int c = 1;
            if (lo > 0) {
                int k = binarySearch(tail[lo - 1], num);
                c = getLast(cnts[lo - 1]) - cnts[lo - 1].get(k);
            }
            if (lo == len) {
                List<Integer> tailList = new ArrayList<>();
                tailList.add(num);
                tail[len] = tailList;
                List<Integer> cntList = new ArrayList<>();
                cntList.add(0);
                cntList.add(c);
                cnts[len] = cntList;
                len++;
            } else {
                tail[lo].add(num);
                cnts[lo].add(getLast(cnts[lo]) + c);
            }
        }

        return getLast(cnts[len - 1]);
    }

    /**
     * list 单调非增的，找到第一个小于 target 的元素下标
     */
    private int binarySearch(List<Integer> list, int target) {
        int lo = 0, hi = list.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (target <= list.get(mid)) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    private int getLast(List<Integer> list) {
        return list.get(list.size() - 1);
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
