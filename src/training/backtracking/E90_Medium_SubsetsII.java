package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static util.CollectionUtil.deepEqualsIgnoreOrder;

/**
 * 90. 子集 II: https://leetcode-cn.com/problems/subsets-ii/
 *
 * 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
 *
 * 解集「不能」包含重复的子集。返回的解集中，子集可以按「任意顺序」排列。
 *
 * 例 1：
 * 输入：nums = [1,2,2]
 * 输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
 *
 * 例 2：
 * 输入：nums = [0]
 * 输出：[[],[0]]
 *
 * 说明：
 * - 1 <= nums.length <= 10
 * - -10 <= nums[i] <= 10
 */
public class E90_Medium_SubsetsII {

    public static void test(Function<int[], List<List<Integer>>> method) {
        deepEqualsIgnoreOrder(asList(asList(), asList(1), asList(1,2), asList(1,2,2), asList(2), asList(2,2)),
                method.apply(new int[]{1,2,2}));
        deepEqualsIgnoreOrder(asList(asList(), asList(0)), method.apply(new int[]{0}));
    }

    /**
     * LeetCode 耗时：1 ms - 98.06%
     *          内存消耗：41.6 MB - 25.39%
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(Collections.emptyList());
        ArrayList<Integer> path = new ArrayList<>(nums.length);
        // 排序，让相同的数字排到一块
        Arrays.sort(nums);
        // 选择包含不同数量元素的子集
        for (int cnt = 1; cnt <= nums.length; cnt++) {
            dfs(nums, result, path, cnt, 0);
        }

        return result;
    }

    private void dfs(int[] nums, List<List<Integer>> result, ArrayList<Integer> path,
                     int cnt, int i) {
        if (cnt == 0) {
            result.add((List<Integer>) path.clone());
            return;
        }

        // [i, j) 是相同的元素
        int j = i + 1;
        while (j < nums.length && nums[j] == nums[i]) {
            j++;
        }
        /*
        对这些相同元素，有这样一些情况：可以分别选 [0, min(cnt, j-i)] 个；
        但需要注意，后面的元素数量为 n-j（n 是总元素数），如果 n-j<cnt，那么最少要 cnt-n+j 个 nums[i]
         */
        final int n = nums.length;
        int lo = n - j >= cnt ? 0 : cnt - n + j, hi = Math.min(cnt, j - i);
        for (int c = lo; c <= hi; c++) {
            for (int k = 0; k < c; k++) {
                path.add(nums[i]);
            }
            dfs(nums, result, path, cnt - c, j);
            for (int k = 0; k < c; k++) {
                path.remove(path.size() - 1);
            }
        }
    }

    @Test
    public void testSubsetsWithDup() {
        test(this::subsetsWithDup);
    }
}
