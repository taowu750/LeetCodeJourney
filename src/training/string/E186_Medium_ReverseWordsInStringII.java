package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 186. 翻转字符串里的单词 II: https://leetcode-cn.com/problems/reverse-words-in-a-string-ii/
 *
 * 给定一个字符串，逐个翻转字符串中的每个单词。使用 O(1) 额外空间复杂度的原地解法。
 *
 * 例 1：
 * 输入: ["t","h","e"," ","s","k","y"," ","i","s"," ","b","l","u","e"]
 * 输出: ["b","l","u","e"," ","i","s"," ","s","k","y"," ","t","h","e"]
 *
 * 说明：
 * - 单词的定义是不包含空格的一系列字符
 * - 输入字符串中不会包含前置或尾随的空格
 * - 单词与单词之间永远是以单个空格隔开的
 */
public class E186_Medium_ReverseWordsInStringII {

    public static void test(Consumer<char[]> method) {
        char[] s = {'t','h','e',' ','s','k','y',' ','i','s',' ','b','l','u','e'};
        method.accept(s);
        assertArrayEquals(new char[]{'b','l','u','e',' ','i','s',' ','s','k','y',' ','t','h','e'}, s);
    }

    /**
     * LeetCode 耗时：1 ms - 99.68%
     *          内存消耗：41.7 MB - 30.48%
     */
    public void reverseWords(char[] s) {
        // 首先翻转整个字符串，再依次翻转每个单词
        for (int i = 0, limit = s.length / 2; i < limit; i++) {
            char tmp = s[i];
            s[i] = s[s.length - i - 1];
            s[s.length - i - 1] = tmp;
        }

        int start = 0;
        for (int i = 0; i < s.length;) {
            if (i + 1 == s.length || s[i + 1] == ' ') {
                for (int j = start, k = i; j < k; j++, k--) {
                    char tmp = s[j];
                    s[j] = s[k];
                    s[k] = tmp;
                }
                i += 2;
                start = i;
            } else {
                i++;
            }
        }
    }

    @Test
    public void testReverseWords() {
        test(this::reverseWords);
    }
}
