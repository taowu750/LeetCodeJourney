package training.string;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1156. 单字符重复子串的最大长度: https://leetcode-cn.com/problems/swap-for-longest-repeated-character-substring/
 *
 * 如果字符串中的所有字符都相同，那么这个字符串是单字符重复的字符串。
 *
 * 给你一个字符串 text，你只能交换其中两个字符一次或者什么都不做，然后得到一些单字符重复的子串。返回其中最长的子串的长度。
 *
 * 例 1：
 * 输入：text = "ababa"
 * 输出：3
 *
 * 例 2：
 * 输入：text = "aaabaaa"
 * 输出：6
 *
 * 例 3：
 * 输入：text = "aaabbaaa"
 * 输出：4
 *
 * 例 4：
 * 输入：text = "aaaaa"
 * 输出：5
 *
 * 例 5：
 * 输入：text = "abcdef"
 * 输出：1
 *
 * 说明：
 * - 1 <= text.length <= 20000
 * - text 仅由小写英文字母组成。
 */
public class E1156_Medium_SwapForLongestRepeatedCharacterSubstring {

    public static void test(ToIntFunction<String> method) {
        assertEquals(3, method.applyAsInt("ababa"));
        assertEquals(6, method.applyAsInt("aaabaaa"));
        assertEquals(4, method.applyAsInt("aaabbaaa"));
        assertEquals(5, method.applyAsInt("aaaaa"));
        assertEquals(1, method.applyAsInt("abcdef"));
    }

    /**
     * LeetCode 耗时：8 ms - 44.39%
     *          内存消耗：41.1 MB - 17.35%
     */
    public int maxRepOpt1(String text) {
        // 记录每种字符重复子串的范围
        List<Segment>[] charSegments = new List[26];
        for (int i = 0, j; i < text.length(); i = j) {
            char c = text.charAt(i);
            j = i + 1;
            while (j < text.length() && text.charAt(j) == c) {
                j++;
            }
            if (charSegments[c - 'a'] == null) {
                charSegments[c - 'a'] = new ArrayList<>();
            }
            charSegments[c - 'a'].add(new Segment(i, j));
        }

        // 遍历每种字符，查找最大长度
        int result = 1;
        for (List<Segment> charSegment : charSegments) {
            if (charSegment == null) {
                continue;
            }
            // 该字符只有一段
            if (charSegment.size() == 1) {
                result = Math.max(result, charSegment.get(0).len());
            } else if (charSegment.size() == 2) {  // 该字符有两段
                Segment left = charSegment.get(0), right = charSegment.get(1);
                // 两段中间只隔着一个别的字符，则可以连接起来
                if (right.i - left.j == 1) {
                    result = Math.max(result, left.len() + right.len());
                } else {  // 否则每段都只能增加一个字符
                    result = Math.max(result, Math.max(left.len() + 1, right.len() + 1));
                }
            } else {  // 该字符多于三段，每两段之间都可能用第三段的字符连接起来
                for (int i = 0; i < charSegment.size() - 1; i++) {
                    Segment left = charSegment.get(i), right = charSegment.get(i + 1);
                    if (right.i - left.j == 1) {
                        result = Math.max(result, left.len() + right.len() + 1);
                    } else {
                        result = Math.max(result, Math.max(left.len() + 1, right.len() + 1));
                    }
                }
            }
        }

        return result;
    }

    /**
     * [i,j) 范围内的子串字符相同
     */
    public static class Segment {
        public int i, j;

        public Segment(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int len() {
            return j - i;
        }
    }

    @Test
    public void testMaxRepOpt1() {
        test(this::maxRepOpt1);
    }
}
