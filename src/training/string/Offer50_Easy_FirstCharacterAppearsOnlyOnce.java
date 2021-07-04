package training.string;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 剑指 Offer 50. 第一个只出现一次的字符: https://leetcode-cn.com/problems/di-yi-ge-zhi-chu-xian-yi-ci-de-zi-fu-lcof/
 *
 * 在字符串 s 中找出第一个只出现一次的字符。如果没有，返回一个单空格。 s 只包含小写字母。
 *
 * 例 1：
 * s = "abaccdeff"
 * 返回 "b"
 *
 * 例 2：
 * s = ""
 * 返回 " "
 *
 * 约束：
 * - 0 <= s 的长度 <= 50000
 */
public class Offer50_Easy_FirstCharacterAppearsOnlyOnce {

    static void test(Function<String, Character> method) {
        assertEquals('b', method.apply("abaccdeff"));
        assertEquals(' ', method.apply(""));
    }

    /**
     * LeetCode 耗时：29 ms - 52.60%
     *          内存消耗：39.3 MB - 5.22%
     */
    public char firstUniqChar(String s) {
        Map<Character, Boolean> map = new LinkedHashMap<>();
        for (int i = 0; i < s.length(); i++) {
            map.put(s.charAt(i), !map.containsKey(s.charAt(i)));
        }

        for (Map.Entry<Character, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                return entry.getKey();
            }
        }

        return ' ';
    }

    @Test
    public void testFirstUniqChar() {
        test(this::firstUniqChar);
    }
}
