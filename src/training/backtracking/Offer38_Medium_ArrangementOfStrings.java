package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static util.ArrayUtil.equalsIgnoreOrder;

/**
 * 剑指 Offer 38. 字符串的排列：https://leetcode-cn.com/problems/zi-fu-chuan-de-pai-lie-lcof/
 *
 * 输入一个字符串，打印出该字符串中字符的所有排列。
 * 你可以以任意顺序返回这个字符串数组，但里面不能有重复元素。
 *
 * 例 1：
 * 输入：s = "abc"
 * 输出：["abc","acb","bac","bca","cab","cba"]
 *
 * 说明：
 * - 1 <= s 的长度 <= 8
 */
public class Offer38_Medium_ArrangementOfStrings {

    public static void test(Function<String, String[]> method) {
        equalsIgnoreOrder(new String[]{"abc","acb","bac","bca","cab","cba"}, method.apply("abc"));
        equalsIgnoreOrder(new String[]{"abac","abca","acab","acba","aabc","aacb","baca","baac","bcaa","caba","caab","cbaa"},
                method.apply("abac"));
    }

    /**
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：42.8 MB - 63.95%
     */
    public String[] permutation(String s) {
        char[] chars = s.toCharArray();
        List<String> result = new ArrayList<>();

        dfs(result, chars, 0);

        return result.toArray(new String[0]);
    }

    private void dfs(List<String> result, char[] chars, int i) {
        if (i == chars.length - 1) {
            result.add(String.valueOf(chars));
            return;
        }

        dfs(result, chars, i + 1);
        for (int j = i + 1; j < chars.length; j++) {
            // 判断已经交换过的字符中有没有和 chars[j] 相同的
            int k = i;
            for (; k < j; k++) {
                if (chars[k] == chars[j])
                    break;
            }
            if (k != j) {
                continue;
            }

            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
            dfs(result, chars, i + 1);

            tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
    }

    @Test
    public void testPermutation() {
        test(this::permutation);
    }
}
