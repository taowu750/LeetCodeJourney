package training.backtracking;

import org.junit.jupiter.api.Test;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给你一个整数数组 nums ，数组中的元素「互不相同」。返回该数组所有可能的子集（幂集）。
 *
 * 解集「不能」包含重复的子集。你可以按「任意顺序」返回解集。
 *
 * 例 1：
 * 输入：nums = [1,2,3]
 * 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 *
 * 例 2：
 * 输入：nums = [0]
 * 输出：[[],[0]]
 *
 * 约束：
 * - 1 <= nums.length <= 10
 * - -10 <= nums[i] <= 10
 * - nums 中的所有元素互不相同
 */
public class E78_Medium_Subsets {

    static void test(Function<int[], List<List<Integer>>> method) {
        List<List<Integer>> result = method.apply(new int[]{1,2,3});
        assertTrue(CollectionUtil.deepEqualsIgnoreOrder(result,
                Arrays.asList(emptyList(), singletonList(1), singletonList(2), singletonList(3),
                        Arrays.asList(1,2), Arrays.asList(1,3), Arrays.asList(2, 3),
                        Arrays.asList(1,2,3))));

        assertTrue(CollectionUtil.deepEqualsIgnoreOrder(method.apply(new int[]{0}),
                Arrays.asList(emptyList(), singletonList(0))));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.9 MB - 13.30%
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>((int) Math.pow(2, nums.length));
        // 外面一个 for 循环，指定子集的长度。
        for (int len = 0; len <= nums.length; len++) {
            dfs(result, nums, 0, len, new ArrayList<>(len + 1));
        }

        return result;
    }

    private void dfs(List<List<Integer>> result, int[] nums, int i, int len, ArrayList<Integer> subset) {
        if (len == 0) {
            result.add((List<Integer>) subset.clone());
            return;
        }

        // 先固定一个元素，然后查找剩余的元素，最后删除最开始固定的元素
        int limit = nums.length - len;
        for (int j = i; j <= limit; j++) {
            subset.add(nums[j]);
            dfs(result, nums, j + 1, len - 1, subset);
            subset.remove(subset.size() - 1);
        }
    }

    @Test
    public void testSubsets() {
        test(this::subsets);
    }
}
