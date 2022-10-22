package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 632. 最小区间: https://leetcode-cn.com/problems/smallest-range-covering-elements-from-k-lists/
 *
 * 你有 k 个「非递减排列」的整数列表。找到一个「最小」区间，使得 k 个列表中的每个列表至少有一个数包含在其中。
 *
 * 我们定义如果 b-a < d-c 或者在 b-a == d-c 时 a < c，则区间 [a,b] 比 [c,d] 小。
 *
 * 例 1：
 * 输入：nums = [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]
 * 输出：[20,24]
 * 解释：
 * 列表 1：[4, 10, 15, 24, 26]，24 在区间 [20,24] 中。
 * 列表 2：[0, 9, 12, 20]，20 在区间 [20,24] 中。
 * 列表 3：[5, 18, 22, 30]，22 在区间 [20,24] 中。
 *
 * 例 2：
 * 输入：nums = [[1,2,3],[1,2,3],[1,2,3]]
 * 输出：[1,1]
 *
 * 例 3：
 * 输入：nums = [[10,10],[11,11]]
 * 输出：[10,11]
 *
 * 例 4：
 * 输入：nums = [[10],[11]]
 * 输出：[10,11]
 *
 * 例 5：
 * 输入：nums = [[1],[2],[3],[4],[5],[6],[7]]
 * 输出：[1,7]
 *
 * 约束：
 * - nums.length == k
 * - 1 <= k <= 3500
 * - 1 <= nums[i].length <= 50
 * - -10**5 <= nums[i][j] <= 10**5
 * - nums[i] 按非递减顺序排列
 */
public class E632_Hard_SmallestRangeCoveringElementsFromKLists {

    public static void test(Function<List<List<Integer>>, int[]> method) {
        assertArrayEquals(new int[]{20,24},
                method.apply(asList(asList(4,10,15,24,26), asList(0,9,12,20), asList(5,18,22,30))));

        assertArrayEquals(new int[]{1,1},
                method.apply(asList(asList(1,2,3), asList(1,2,3), asList(1,2,3))));

        assertArrayEquals(new int[]{10,11},
                method.apply(asList(asList(10,10), asList(11,11))));

        assertArrayEquals(new int[]{10,11},
                method.apply(asList(singletonList(10), singletonList(11))));

        assertArrayEquals(new int[]{1,7},
                method.apply(asList(singletonList(1), singletonList(2), singletonList(3),
                        singletonList(4), singletonList(5), singletonList(6), singletonList(7))));
    }

    /**
     * LeetCode 耗时：32 ms - 52.99%
     *          内存消耗：45 MB - 47.76%
     */
    public int[] smallestRange(List<List<Integer>> nums) {
        final int k = nums.size();
        int[] ans = {0, Integer.MAX_VALUE};
        TreeSet<int[]> tree = new TreeSet<>((a, b) -> {
            int cmp = a[0] - b[0];
            return cmp != 0 ? cmp : a[1] - b[1];
        });
        for (int i = 0; i < k; i++) {
            tree.add(new int[]{nums.get(i).get(0), i, 0});
        }
        while (!tree.isEmpty()) {
            int min = tree.first()[0], max = tree.last()[0];
            if (max - min < ans[1] - ans[0]) {
                ans[0] = min;
                ans[1] = max;
            }
            int[] a = tree.pollFirst();
            if (a[2] == nums.get(a[1]).size() - 1) {
                break;
            } else {
                a[2]++;
                a[0] = nums.get(a[1]).get(a[2]);
                tree.add(a);
            }
        }

        return ans;
    }

    @Test
    public void testSmallestRange() {
        test(this::smallestRange);
    }


    /**
     * 滑动窗口方法。参见：
     * https://leetcode-cn.com/problems/smallest-range-covering-elements-from-k-lists/solution/pai-xu-hua-chuang-by-netcan/
     *
     * LeetCode 耗时：20 ms - 95.40%
     *          内存消耗：44.6 MB - 47.90%
     */
    public int[] slidingWindowMethod(List<List<Integer>> nums) {
        int size = 0;
        for (List<Integer> numList : nums) {
            size += numList.size();
        }
        // 将所有数字合并，并记录它们所属的组
        int[][] allNums = new int[size][2];
        for (int i = 0, j = 0; i < nums.size(); i++) {
            for (int num : nums.get(i)) {
                allNums[j][0] = num;
                allNums[j++][1] = i;
            }
        }
        // 排序这些数字
        Arrays.sort(allNums, (a, b) -> Integer.compare(a[0], b[0]));

        int[] window = new int[nums.size()];
        int left = 0, right = 0, lo = allNums[0][0], hi = allNums[size - 1][0];
        int distinct = 0;
        while (right < allNums.length) {
            int[] data = allNums[right++];
            if (window[data[1]]++ == 0) {
                distinct++;
            }

            while (distinct == nums.size()) {
                if (hi - lo > allNums[right - 1][0] - allNums[left][0]) {
                    lo = allNums[left][0];
                    hi = allNums[right - 1][0];
                }
                if (--window[allNums[left++][1]] == 0) {
                    distinct--;
                }
            }
        }

        return new int[]{lo, hi};
    }

    @Test
    public void testSlidingWindowMethod() {
        test(this::slidingWindowMethod);
    }
}
