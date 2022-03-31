package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 473. 火柴拼正方形: https://leetcode-cn.com/problems/matchsticks-to-square/
 *
 * 你将得到一个整数数组 matchsticks ，其中 matchsticks[i] 是第 i 个火柴棒的长度。你要用「所有的火柴棍」拼成一个正方形。
 * 你「不能折断」任何一根火柴棒，但你可以把它们连在一起，而且每根火柴棒必须「使用一次」。
 *
 * 如果你能使这个正方形，则返回 true ，否则返回 false。
 *
 * 例 1：
 * 输入: matchsticks = [1,1,2,2,2]
 * 输出: true
 * 解释: 能拼成一个边长为2的正方形，每边两根火柴。
 *
 * 例 2：
 * 输入: matchsticks = [3,3,3,3,4]
 * 输出: false
 * 解释: 不能用所有火柴拼成一个正方形。
 *
 * 说明：
 * - 1 <= matchsticks.length <= 15
 * - 1 <= matchsticks[i] <= 10^8
 */
public class E473_Medium_MatchsticksToSquare {

    public static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{1,1,2,2,2}));
        assertFalse(method.test(new int[]{3,3,3,3,4}));
    }

    /**
     * 参见 {@link E698_Medium_PartitionKEqualSumSubsets}
     *
     * LeetCode 耗时：3 ms - 75.50%
     *          内存消耗：39 MB - 49.70%
     */
    public boolean makesquare(int[] matchsticks) {
        int sum = 0;
        for (int matchstick : matchsticks) {
            sum += matchstick;
        }
        if (sum % 4 != 0) {
            return false;
        }

        Arrays.sort(matchsticks);
        int[] edges = new int[4];

        return dfs(matchsticks, edges, sum / 4, matchsticks.length - 1);
    }

    private boolean dfs(int[] matchsticks, int[] edges, int edgeLen, int i) {
        if (i < 0) {
            return true;
        }

        int m = matchsticks[i];
        for (int j = 0; j < edges.length; j++) {
            if (edges[j] + m <= edgeLen) {
                edges[j] += m;
                if (dfs(matchsticks, edges, edgeLen, i - 1)) {
                    return true;
                }
                edges[j] -= m;
            }
            if (edges[j] == 0 || edges[j] + m == edgeLen) {
                break;
            }
        }

        return false;
    }

    @Test
    public void testMakesquare() {
        test(this::makesquare);
    }
}
