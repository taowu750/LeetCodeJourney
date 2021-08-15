package training.sort;

import org.junit.jupiter.api.Test;
import util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 969. 煎饼排序: https://leetcode-cn.com/problems/pancake-sorting/
 *
 * 给你一个整数数组 arr ，请使用「煎饼翻转」完成对数组的排序。一次煎饼翻转的执行过程如下：
 * - 选择一个整数 k ，1 <= k <= arr.length
 * - 反转子数组 arr[0...k-1]（下标从 0 开始）
 *
 * 例如，arr = [3,2,1,4] ，选择 k = 3 进行一次煎饼翻转，反转子数组 [3,2,1] ，得到 arr = [1,2,3,4] 。
 *
 * 以数组形式返回能使 arr 有序的煎饼翻转操作所对应的 k 值序列。任何将数组排序且翻转次数在 10 * arr.length
 * 范围内的有效答案都将被判断为正确。
 *
 * 例 1：
 * 输入：[3,2,4,1]
 * 输出：[4,2,4,3]
 * 解释：
 * 我们执行 4 次煎饼翻转，k 值分别为 4，2，4，和 3。
 * 初始状态 arr = [3, 2, 4, 1]
 * 第一次翻转后（k = 4）：arr = [1, 4, 2, 3]
 * 第二次翻转后（k = 2）：arr = [4, 1, 2, 3]
 * 第三次翻转后（k = 4）：arr = [3, 2, 1, 4]
 * 第四次翻转后（k = 3）：arr = [1, 2, 3, 4]，此时已完成排序。
 *
 * 例 2：
 * 输入：[1,2,3]
 * 输出：[]
 * 解释：
 * 输入已经排序，因此不需要翻转任何内容。
 * 请注意，其他可能的答案，如 [3，3] ，也将被判断为正确。
 *
 * 约束：
 * - 1 <= arr.length <= 100
 * - 1 <= arr[i] <= arr.length
 * - arr 中的所有整数互不相同（即，arr 是从 1 到 arr.length 整数的一个排列）
 */
public class E969_Medium_PancakeSorting {

    static void applyPancakeSort(int[] arr, Function<int[], List<Integer>> method) {
        int[] copy = arr.clone();
        List<Integer> reverse = method.apply(copy);
        assertTrue(reverse.size() < 10 * arr.length, reverse.size() + " - " + arr.length);
        for (int k : reverse) {
            for (int i = 0, len = k >>> 1; i < len; i++) {
                int tmp = arr[i];
                arr[i] = arr[k - i - 1];
                arr[k - i - 1] = tmp;
            }
        }
        ArrayUtil.isAscending(arr);
    }

    static void test(Function<int[], List<Integer>> method) {
        applyPancakeSort(new int[]{3,2,4,1}, method);
        applyPancakeSort(new int[]{1,2,3}, method);
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.2 MB - 98.28%
     */
    public List<Integer> pancakeSort(int[] arr) {
        List<Integer> result = new ArrayList<>(arr.length * 2);
        int[] indices = new int[arr.length];
        // 使用 indices 记录每个值下的标，避免每次查找最大值
        for (int i = 0; i < arr.length; i++) {
            indices[arr[i] - 1] = i;
        }
        for (int n = arr.length; n > 0; n--) {
            int maxIdx = indices[n - 1];
            if (maxIdx != n - 1) {
                reverse(arr, maxIdx + 1, indices);
                result.add(maxIdx + 1);
                reverse(arr, n, indices);
                result.add(n);
            }
        }

        return result;
    }

    private void reverse(int[] arr, int n, int[] indices) {
        for (int i = 0, len = n >>> 1; i < len; i++) {
            indices[arr[i] - 1] = n - i - 1;
            indices[arr[n - i - 1] - 1] = i;
            int tmp = arr[i];
            arr[i] = arr[n - i - 1];
            arr[n - i - 1] = tmp;
        }
    }

    @Test
    public void testPancakeSort() {
        test(this::pancakeSort);
    }
}
