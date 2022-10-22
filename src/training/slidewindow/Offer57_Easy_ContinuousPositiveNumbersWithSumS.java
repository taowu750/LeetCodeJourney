package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 剑指 Offer 57 - II. 和为s的连续正数序列: https://leetcode-cn.com/problems/he-wei-sde-lian-xu-zheng-shu-xu-lie-lcof/
 *
 * 输入一个正整数 target ，输出所有和为 target 的连续正整数序列（至少含有两个数）。
 * 序列内的数字由小到大排列，不同序列按照首个数字从小到大排列。
 *
 * 例 1：
 * 输入：target = 9
 * 输出：[[2,3,4],[4,5]]
 *
 * 例 2：
 * 输入：target = 15
 * 输出：[[1,2,3,4,5],[4,5,6],[7,8]]
 *
 * 约束：
 * - 1 <= target <= 10**5
 */
public class Offer57_Easy_ContinuousPositiveNumbersWithSumS {

    public static void test(IntFunction<int[][]> method) {
        assertArrayEquals(new int[][]{{2, 3, 4}, {4, 5}}, method.apply(9));
        assertArrayEquals(new int[][]{{1, 2, 3, 4, 5}, {4, 5, 6}, {7, 8}}, method.apply(15));
        assertArrayEquals(new int[][]{{9, 10, 11, 12, 13, 14, 15, 16}, {18, 19, 20, 21, 22}}, method.apply(100));
    }

    /**
     * LeetCode 耗时：33 ms - 5.17%
     *          内存消耗：38 MB - 7.93%
     */
    public int[][] findContinuousSequence(int target) {
        if (target < 3) {
            return new int[][]{};
        }

        // 生成前缀数组
        int[] prefixArray = new int[target];
        int prefix = 0;
        for (int i = 1; i < target; i++) {
            prefix += i;
            prefixArray[i] = prefix;
        }

        List<int[]> result = new ArrayList<>();
        // 找到开始位置（大于等于 target 的最小值）
        int i = Arrays.binarySearch(prefixArray, target);
        if (i < 0) {
            i = -i - 1;
        }
        for (; i < target; i++) {
            // 如果一个前缀减去 target 等于另一个前缀，那么这两个前缀之差等于 target
            int start = Arrays.binarySearch(prefixArray, prefixArray[i] - target);
            if (start >= 0) {
                int[] seq = new int[i - start];
                for (int j = 0; j < seq.length; j++) {
                    seq[j] = start + j + 1;
                }
                result.add(seq);
            }
        }

        return result.toArray(new int[][]{});
    }

    @Test
    public void testFindContinuousSequence() {
        test(this::findContinuousSequence);
    }


    /**
     * 滑动窗口方法。
     *
     * LeetCode 耗时：2 ms - 95.59%
     *          内存消耗：36.1 MB - 97.94%
     */
    public int[][] flipWindowMethod(int target) {
        List<int[]> ans = new ArrayList<>();
        int l = 1, r = 1, sum = 0;
        while (r < target) {
            sum += r++;
            while (sum > target) {
                sum -= l++;
            }
            if (sum == target) {
                int[] arr = new int[r - l];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = l + i;
                }
                ans.add(arr);
            }
        }

        return ans.toArray(new int[0][]);
    }

    @Test
    public void testFlipWindowMethod() {
        test(this::flipWindowMethod);
    }
}
