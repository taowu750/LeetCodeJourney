package training.trie;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 386. 字典序排数: https://leetcode-cn.com/problems/lexicographical-numbers/
 *
 * 给你一个整数 n ，按字典序返回范围 [1, n] 内所有整数。
 *
 * 你必须设计一个时间复杂度为 O(n) 且使用 O(1) 额外空间的算法。
 *
 * 例 1：
 * 输入：n = 13
 * 输出：[1,10,11,12,13,2,3,4,5,6,7,8,9]
 *
 * 例 2：
 * 输入：n = 2
 * 输出：[1,2]
 *
 * 说明：
 * - 1 <= n <= 5 * 10^4
 */
public class E386_Medium_LexicographicalNumbers {

    public static void test(IntFunction<List<Integer>> method) {
        assertEquals(asList(1,10,11,12,13,2,3,4,5,6,7,8,9), method.apply(13));
        assertEquals(asList(1,2), method.apply(2));
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：42.3 MB - 93.44%
     */
    public List<Integer> lexicalOrder(int n) {
        List<Integer> result = new ArrayList<>(n);
        dfs(result, 1, n);

        return result;
    }

    private void dfs(List<Integer> result, long prefix, long n) {
        if (prefix > n) {
            return;
        }
        // 首先添加当前前缀
        result.add((int) prefix);
        // 进入到子树中
        dfs(result, prefix * 10, n);
        // 如果还有右兄弟，再遍历右兄弟
        if (prefix % 10 < 9) {
            dfs(result, prefix + 1, n);
        }
    }

    @Test
    public void testLexicalOrder() {
        test(this::lexicalOrder);
    }
}
