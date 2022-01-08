package training.string;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 392. 判断子序列: https://leetcode-cn.com/problems/is-subsequence/
 *
 * 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
 *
 * 字符串的一个子序列是原始字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。
 * （例如，"ace"是"abcde"的一个子序列，而"aec"不是）。
 *
 * 如果有大量输入的 S，称作 S1, S2, ... , Sk 其中 k >= 10亿，你需要依次检查它们是否为 T 的子序列。
 * 在这种情况下，你会怎样改变代码？
 *
 * 例 1：
 * 输入：s = "abc", t = "ahbgdc"
 * 输出：true
 *
 * 例 2：
 * 输入：s = "axc", t = "ahbgdc"
 * 输出：false
 *
 * 约束：
 * - 0 <= s.length <= 100
 * - 0 <= t.length <= 10^4
 * - 两个字符串都只由小写字符组成。
 */
public class E392_Medium_IsSubsequence {

    public static void test(BiPredicate<String, String> method) {
        assertTrue(method.test("abc", "ahbgdc"));
        assertFalse(method.test("axc", "ahbgdc"));
        assertTrue(method.test("", "ahbgdc"));
        assertFalse(method.test("acb", "ahbgdc"));
        assertFalse(method.test("aaaaaa","bbaaaa"));
    }

    /**
     * LeetCode 耗时：1 ms - 82.72%
     *          内存消耗：36.4 MB - 42.50%
     */
    public boolean isSubsequence(String s, String t) {
        int m = s.length(), n = t.length();
        if (m == 0)
            return true;
        if (m > n)
            return false;

        int i = 0, j = 0;
        for (; i < n; i++) {
            if (s.charAt(j) == t.charAt(i)) {
                if (++j == m)
                    break;
            }
        }

        return j == m;
    }

    @Test
    public void testIsSubsequence() {
        test(this::isSubsequence);
    }


    private List<Integer>[] indices;

    /**
     * 如果有大量输入的 S，称作 S1, S2, ... , Sk 其中 k >= 10亿，我们不能使用上面 O(n) 的方法。
     * 我们需要能够解析 T 的结构，从而更快速地判断子序列。
     *
     * 算法思想参见：https://labuladong.gitee.io/algo/5/43/
     *
     * LeetCode 耗时：2 ms - 47.29%
     *          内存消耗：38.4 MB - 10.04%
     */
    public boolean binarySearchMethod(String s, String t) {
        int m = s.length(), n = t.length();
        if (m == 0)
            return true;
        if (m > n)
            return false;

        // 记录 t 中每个字符的下标
        //noinspection unchecked
        indices = new ArrayList[128];
        for (int i = 0; i < n; i++) {
            char c = t.charAt(i);
            if (indices[c] == null) {
                indices[c] = new ArrayList<>();
            }
            indices[c].add(i);
        }

        // lastIdx 表示上次在 t 中查找到的下标
        int lastIdx = -1;
        for (int i = 0; i < m; i++) {
            char c = s.charAt(i);
            List<Integer> idxes = indices[c];
            // 字符不存在则返回 false
            if (idxes == null)
                return false;
            // 使用二分查找找到 t 中大于 lastIdx 的最左边的 c
            int lo = 0, hi = idxes.size();
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (lastIdx >= idxes.get(mid)) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            // 没有找到则返回 false
            if (lo >= idxes.size())
                return false;
            // 更新 lastIdx
            lastIdx = idxes.get(lo);
        }

        return true;
    }

    @Test
    public void testBinarySearchMethod() {
        test(this::binarySearchMethod);
    }
}
