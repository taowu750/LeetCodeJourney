package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 6. Z 字形变换: https://leetcode-cn.com/problems/zigzag-conversion/
 *
 * 将一个给定字符串 s 根据给定的行数 numRows ，以从上往下、从左到右进行 Z 字形排列。
 *
 * 比如输入字符串为 "PAYPALISHIRING" 行数为 3 时，排列如下：
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 *
 * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："PAHNAPLSIIGYIR"。
 *
 * 例 1：
 * 输入：s = "PAYPALISHIRING", numRows = 3
 * 输出："PAHNAPLSIIGYIR"
 *
 * 例 2：
 * 输入：s = "PAYPALISHIRING", numRows = 4
 * 输出："PINALSIGYAHRPI"
 * 解释：
 * P     I    N
 * A   L S  I G
 * Y A   H R
 * P     I
 *
 * 例 3：
 * 输入：s = "A", numRows = 1
 * 输出："A"
 *
 * 说明：
 * - 1 <= s.length <= 1000
 * - s 由英文字母（小写和大写）、',' 和 '.' 组成
 * - 1 <= numRows <= 1000
 */
public class E6_Medium_ZigZagConversion {

    public static void test(BiFunction<String, Integer, String> method) {
        assertEquals("PAHNAPLSIIGYIR", method.apply("PAYPALISHIRING", 3));
        assertEquals("PINALSIGYAHRPI", method.apply("PAYPALISHIRING", 4));
        assertEquals("A", method.apply("A", 1));
        assertEquals("ACEGBDF", method.apply("ABCDEFG", 2));
    }

    /**
     * LeetCode 耗时：4 ms - 81.96%
     *          内存消耗：39.2 MB - 21.36%
     */
    public String convert(String s, int numRows) {
        if (numRows == 1 || numRows >= s.length()) {
            return s;
        }

        int size = 2 * s.length() / numRows + 1;
        StringBuilder[] lines = new StringBuilder[numRows];
        lines[0] = new StringBuilder(s.length());
        for (int i = 1; i < lines.length; i++) {
            lines[i] = new StringBuilder(size);
        }

        for (int i = 0, row = 0, add = 1; i < s.length(); i++) {
            lines[row].append(s.charAt(i));
            row += add;
            if (row == numRows - 1) {
                add = -1;
            } else if (row == 0) {
                add = 1;
            }
        }

        for (int i = 1; i < lines.length; i++) {
            lines[0].append(lines[i]);
        }

        return lines[0].toString();
    }

    @Test
    public void testConvert() {
        test(this::convert);
    }


    /**
     * 参见 ：https://leetcode-cn.com/problems/zigzag-conversion/solution/z-zi-xing-bian-huan-by-leetcode/
     *
     * 逐行添加字符，计算每一行中字符的位置。对于所有的 k（k 表示一行的第几个字符，从 0 开始）：
     * - 行 0 中的字符位于索引 k⋅(2⋅numRows−2) 处;
     * - 行 numRows−1 中的字符位于索引 k⋅(2⋅numRows−2)+numRows−1 处;
     * - 内部的行 i 中的字符位于索引 k⋅(2⋅numRows−2)+i 以及 (k+1)⋅(2⋅numRows−2)−i 处;
     *
     * 注意到内部的行中间的间隙只会有一个字符，并且每个这个字符和间隙开头字符距离都是一样的。
     *
     * LeetCode 耗时：2 ms - 99.99%
     *          内存消耗：38.6 MB - 77.23%
     */
    public String betterMethod(String s, int numRows) {
        if (numRows == 1 || numRows >= s.length()) {
            return s;
        }

        StringBuilder result = new StringBuilder();
        int n = s.length();
        int cycleLen = 2 * numRows - 2;

        for (int row = 0; row < numRows; row++) {
            for (int j = 0; j + row < n; j += cycleLen) {
                result.append(s.charAt(j + row));
                if (row != 0 && row != numRows - 1 && j + cycleLen - row < n)
                    result.append(s.charAt(j + cycleLen - row));
            }
        }

        return result.toString();
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
