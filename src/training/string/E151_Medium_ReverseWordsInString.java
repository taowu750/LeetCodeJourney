package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 151. 翻转字符串里的单词: https://leetcode-cn.com/problems/reverse-words-in-a-string/
 *
 * 给定一个字符串，逐个翻转字符串中的每个单词。
 *
 * 说明：
 * - 无空格字符构成一个「单词」。
 * - 输入字符串可以在前面或者后面包含多余的空格，但是反转后的字符不能包括。
 * - 如果两个单词间有多余的空格，将反转后单词间的空格减少到只含一个。
 *
 * 例 1：
 * 输入："the sky is blue"
 * 输出："blue is sky the"
 *
 * 例 2：
 * 输入："  hello world!  "
 * 输出："world! hello"
 * 解释：输入字符串可以在前面或者后面包含多余的空格，但是反转后的字符不能包括。
 *
 * 例 3：
 * 输入："a good   example"
 * 输出："example good a"
 * 解释：如果两个单词间有多余的空格，将反转后单词间的空格减少到只含一个。
 *
 * 例 4：
 * 输入：s = "  Bob    Loves  Alice   "
 * 输出："Alice Loves Bob"
 *
 * 例 5：
 * 输入：s = "Alice does not even like bob"
 * 输出："bob like even not does Alice"
 *
 * 约束：
 * 1 <= s.length <= 10**4
 * s 包含英文大小写字母、数字和空格 ' '
 * s 中「至少存在一个单词」
 */
public class E151_Medium_ReverseWordsInString {

    public static void test(Function<String, String> method) {
        assertEquals("blue is sky the", method.apply("the sky is blue"));
        assertEquals("world! hello", method.apply("  hello world!  "));
        assertEquals("example good a", method.apply("a good   example"));
        assertEquals("Alice Loves Bob", method.apply("  Bob    Loves  Alice   "));
        assertEquals("bob like even not does Alice",
                method.apply("Alice does not even like bob"));
        assertEquals("b", method.apply("b"));
        assertEquals("a b c d", method.apply("  d  c   b a "));
    }

    /**
     * LeetCode 耗时：3 ms - 96.08%
     *          内存消耗：38.4 MB - 82.47%
     */
    public String reverseWords(String s) {
        int lo = 0, hi = s.length() - 1;
        // 跳过前后空格
        while (s.charAt(lo) == ' ')
            lo++;
        while (s.charAt(hi) == ' ')
            hi--;

        char[] result = new char[hi - lo + 1];
        int idx = result.length - 1;
        while (lo <= hi) {
            int end = lo;
            // 找到下一个单词的范围
            while (++end <= hi && s.charAt(end) != ' ');

            // 将其复制到 result
            for (int i = end - 1; i >= lo; i--, idx--)
                result[idx] = s.charAt(i);
            if ((lo = end) > hi)
                break;
            // 添加一个空格
            result[idx--] = ' ';

            // 跳过空格，为下一个单词准备
            while (++lo <= hi && s.charAt(lo) == ' ');
        }

        return new String(result, idx + 1, result.length - idx - 1);
    }

    @Test
    void testReverseWords() {
        test(this::reverseWords);
    }
}
