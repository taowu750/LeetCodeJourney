package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 写一个反转字符串的函数。输入字符串以字符数组 char[] 的形式给出。
 * 不要为另一个数组分配额外的空间，必须通过使用 O(1) 额外空间修改输入数组来实现。
 * 您可以假设所有字符都由可打印的 ascii 字符组成。
 *
 * 例 1：
 * Input: ["h","e","l","l","o"]
 * Output: ["o","l","l","e","h"]
 *
 * 例 2：
 * Input: ["H","a","n","n","a","h"]
 * Output: ["h","a","n","n","a","H"]
 */
public class E344_Easy_ReverseString {

    static void test(Consumer<char[]> method) {
        char[] s = new char[]{'h','e','l','l','o'};
        method.accept(s);
        assertArrayEquals(s, new char[]{'o','l','l','e','h'});

        s = new char[]{'H','a','n','n','a','h'};
        method.accept(s);
        assertArrayEquals(s, new char[]{'h','a','n','n','a','H'});
    }

    /**
     * LeetCode 耗时：1ms - 96.34%
     */
    public void reverseString(char[] s) {
        int len = s.length;
        int halfLen = len / 2;
        for (int i = 0; i < halfLen; i++) {
            char tmp = s[i];
            s[i] = s[len - i - 1];
            s[len - i - 1] = tmp;
        }
    }

    @Test
    public void testReverseString() {
        test(this::reverseString);
    }
}
