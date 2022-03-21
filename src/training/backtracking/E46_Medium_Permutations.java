package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static util.CollectionUtil.deepEqualsIgnoreOutOrder;

/**
 * 46. 全排列：https://leetcode-cn.com/problems/permutations/
 *
 * 给定一个「没有重复」数字的序列，返回其所有可能的全排列。
 *
 * 例 1：
 * Input: [1,2,3]
 * Output:
 * [
 *   [1,2,3],
 *   [1,3,2],
 *   [2,1,3],
 *   [2,3,1],
 *   [3,1,2],
 *   [3,2,1]
 * ]
 */
public class E46_Medium_Permutations {

    public static void test(Function<int[], List<List<Integer>>> method) {
        deepEqualsIgnoreOutOrder(method.apply(new int[]{1,2,3}),
                Arrays.asList(Arrays.asList(1,2,3), Arrays.asList(1,3,2), Arrays.asList(2,1,3), Arrays.asList(2,3,1),
                        Arrays.asList(3,1,2), Arrays.asList(3,2,1)));
    }

    /**
     * LeetCode 耗时：3 ms - 23%
     *          内存消耗：38.7 MB - 52.42%
     */
    public List<List<Integer>> permute(int[] nums) {
        int n = nums.length;
        if (n == 0)
            return Collections.emptyList();
        if (n == 1)
            return Collections.singletonList(Collections.singletonList(nums[0]));

        int fac = n;
        for (int i = n - 1; i > 1; i--)
            fac *= i;

        List<List<Integer>> l1 = new ArrayList<>(fac), l2 = new ArrayList<>(fac);
        l1.add(Arrays.asList(nums[n - 2], nums[n - 1]));
        l1.add(Arrays.asList(nums[n - 1], nums[n - 2]));

        for (int i = n - 3; i >= 0; i--) {
            for (List<Integer> arrange : l1) {
                int len = arrange.size();
                // 将 1 个数字插入到上一个排列之中，形成下一个排列
                for (int j = 0; j <= len; j++) {
                    List<Integer> result = new ArrayList<>(len + 1);
                    for (int k = 0; k < j; k++)
                        result.add(arrange.get(k));
                    result.add(nums[i]);
                    for (int k = j; k < arrange.size(); k++)
                        result.add(arrange.get(k));
                    l2.add(result);
                }
            }
            l1.clear();
            List<List<Integer>> tmp = l1;
            l1 = l2;
            l2 = tmp;
        }

        return l1.isEmpty() ? l2 : l1;
    }

    @Test
    public void testPermute() {
        test(this::permute);
    }


    /**
     * dfs 方法。
     *
     * LeetCode 耗时：1 ms - 96.66%
     *          内存消耗：38.7 MB - 52.42%
     */
    public List<List<Integer>> dfsMethod(int[] nums) {
        int fac = nums.length;
        for (int i = nums.length - 1; i > 1; i--)
            fac *= i;
        List<List<Integer>> result = new ArrayList<>(fac);
        dfs(nums, result, 0);
        return result;
    }

    /**
     * 计算 [start, len) 的全排列
     */
    private void dfs(int[] nums, List<List<Integer>> result, int start) {
        if (start == nums.length) {
            List<Integer> arrange = new ArrayList<>(start);
            for (int num : nums)
                arrange.add(num);
            result.add(arrange);
            return;
        }

        // [start, len) 的全排列，就是将 nums[start] 插入到 [start + 1, len) 的全排列的间隔中
        for (int i = start; i < nums.length; i++) {
            swap(nums, i, start);
            dfs(nums, result, start + 1);
            // 恢复
            swap(nums, i, start);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    @Test
    public void testDfsMethod() {
        test(this::dfsMethod);
    }
}
