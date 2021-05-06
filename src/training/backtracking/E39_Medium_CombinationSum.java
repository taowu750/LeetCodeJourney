package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static util.CollectionUtil.deepEqualsIgnoreOrder;

/**
 * 39. 组合总和: https://leetcode-cn.com/problems/combination-sum/
 *
 * 给定一个无重复元素的数组 candidates 和一个目标数 target，找出 candidates 中所有可以使数字和为 target 的组合。
 * candidates 中的数字可以无限制重复被选取。
 *
 * 说明：
 * - 所有数字（包括 target）都是正整数。
 * - 解集不能包含重复的组合。
 *
 * 例 1：
 * 输入：candidates = [2,3,6,7], target = 7,
 * 所求解集为：
 * [
 *   [7],
 *   [2,2,3]
 * ]
 *
 * 例 2：
 * 输入：candidates = [2,3,5], target = 8,
 * 所求解集为：
 * [
 *   [2,2,2,2],
 *   [2,3,3],
 *   [3,5]
 * ]
 *
 * 约束：
 * - 1 <= candidates.length <= 30
 * - 1 <= candidates[i] <= 200
 * - candidate 中的每个元素都是独一无二的。
 * - 1 <= target <= 500
 */
public class E39_Medium_CombinationSum {

    static void test(BiFunction<int[], Integer, List<List<Integer>>> method) {
        deepEqualsIgnoreOrder(asList(singletonList(7), asList(2,2,3)),
                method.apply(new int[]{2,3,6,7}, 7));

        deepEqualsIgnoreOrder(asList(asList(2,2,2,2), asList(2,3,3), asList(3,5)),
                method.apply(new int[]{2,3,5}, 8));
    }

    /**
     * 动态规划解法，类似于 {@link training.dynamicprogramming.E518_Medium_CoinChangeII}。
     *
     * LeetCode 耗时：6 ms - 20.26%
     *          内存消耗：38.8 MB - 34.59%
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        @SuppressWarnings("unchecked")
        List<List<Integer>>[] dp = (List<List<Integer>>[]) new List[target + 1];
        for (int i = 0; i <= target; i++)
            dp[i] = new ArrayList<>();
        dp[0].add(Collections.emptyList());

        for (int i = 1; i <= candidates.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (candidates[i - 1] <= j) {
                    for (List<Integer> last : dp[j - candidates[i - 1]]) {
                        List<Integer> newSeq = new ArrayList<>(last.size() + 1);
                        newSeq.addAll(last);
                        newSeq.add(candidates[i - 1]);
                        dp[j].add(newSeq);
                    }
                }
            }
        }

        return dp[target];
    }

    @Test
    public void testCombinationSum() {
        test(this::combinationSum);
    }
}
