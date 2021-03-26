package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
 * <p>
 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
 * 请计算能组成「一组」“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）的「最多」信封个数。
 * <p>
 * 注意：不允许旋转信封，即将高宽对换。
 * <p>
 * 例 1：
 * Input：envelopes = [[5,4],[6,4],[6,7],[2,3]]
 * Output：3
 * Explanation：最多组合信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
 * <p>
 * 例 2：
 * Input：envelopes = [[1,1],[1,1],[1,1]]
 * Output：1
 * <p>
 * 约束：
 * - 1 <= envelopes.length <= 5000
 * - envelopes[i].length == 2
 * - 1 <= wi, hi <= 10**4
 */
public class E354_Hard_EnvelopeNesting {

    static void test(ToIntFunction<int[][]> method) {
        assertEquals(method.applyAsInt(new int[][]{{5, 4}, {6, 4}, {6, 7}, {2, 3}}), 3);
        assertEquals(method.applyAsInt(new int[][]{{1, 1}, {1, 1}, {1, 1}}), 1);
    }

    /**
     * LeetCode 耗时：8 ms - 98.68%
     *          内存消耗：39.9 MB - 57.19%
     */
    public int maxEnvelopes(int[][] envelopes) {
        // 将信封先按照宽度升序排序，再按照高度降序排序。
        // 之所以按照高度降序排序，是因为相同宽度的信封是不能包含的。
        Arrays.sort(envelopes, (ei, ej) -> {
            int cmp;
            return (cmp = Integer.compare(ei[0], ej[0])) != 0
                    ? cmp
                    : -Integer.compare(ei[1], ej[1]);
        });

        // 最后求高度的最长递增子序列
        return lengthOfLIS(envelopes);
    }

    /**
     * 参见 {@link E300_Medium_LongestIncreasingSubsequence}。
     */
    private int lengthOfLIS(int[][] nums) {
        int[] top = new int[nums.length];
        int piles = 0;

        for (int i = 0; i < nums.length; i++) {
            int lo = 0, hi = piles;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (nums[i][1] > top[mid])
                    lo = mid + 1;
                else
                    hi = mid;
            }
            if (lo == piles)
                piles++;
            top[lo] = nums[i][1];
        }

        return piles;
    }

    @Test
    public void testMaxEnvelopes() {
        test(this::maxEnvelopes);
    }
}
