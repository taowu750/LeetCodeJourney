package training.string;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 273. 整数转换英文表示: https://leetcode-cn.com/problems/integer-to-english-words/
 *
 * 将非负整数 num 转换为其对应的英文表示。
 *
 * 英语的数字单位有：
 * - 1-9: One-Nine
 * - 10-19: Ten-Nineteen
 * - 20-90: twenty-Ninety
 * - 百: Hundred
 * - 千: Thousand
 * - 万: Ten Thousand
 * - 十万: One Hundred Thousand
 * - 百万: Million
 * - 千万: Ten Million
 * - 亿: One Hundred Million
 * - 十亿: Billion
 *
 * 可以看到，千、百万、十亿这些单位可以和 1-100 的单位组合来表示数，以3个位数作为单位的进位
 *
 * 例 1：
 * 输入：num = 123
 * 输出："One Hundred Twenty Three"
 *
 * 例 2：
 * 输入：num = 12345
 * 输出："Twelve Thousand Three Hundred Forty Five"
 *
 * 例 3：
 * 输入：num = 1234567
 * 输出："One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"
 *
 * 说明：
 * - 0 <= num <= 2^31 - 1
 */
public class E273_Hard_IntegerToEnglishWords {

    public static void test(IntFunction<String> method) {
        assertEquals("One Hundred Twenty Three", method.apply(123));
        assertEquals("Twelve Thousand Three Hundred Forty Five", method.apply(12345));
        assertEquals("One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven", method.apply(1234567));
        assertEquals("Zero", method.apply(0));
        assertEquals("Two Billion One Hundred Forty Seven Million Four Hundred Eighty Three Thousand Six Hundred Forty Seven",
                method.apply(Integer.MAX_VALUE));
        assertEquals("One Thousand", method.apply(1000));
        assertEquals("One Million", method.apply(1000000));
    }

    // 1-19
    public static final String[] UNITS = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    // 20,30,...,90
    public static final String[] TEN_UNITS = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    // 每三个数字一组，每组数字的单位：空字符串、千、百万、十亿。单位前加一个空格方便代码编写
    public static final String[] GROUP_UNITS = {"", " Thousand", " Million", " Billion"};

    /**
     * LeetCode 耗时：2 ms - 85.98%
     *          内存消耗：39.3 MB - 33.38%
     */
    public String numberToWords(int num) {
        // 0 是特殊情况
        if (num == 0) {
            return "Zero";
        }

        // 因为最后输出的顺序和我们切分数字的顺序相反，因此用一个栈
        Deque<StringBuilder> groups = new ArrayDeque<>(4);
        int size = 0, groupIdx = 0;
        while (num != 0) {
            StringBuilder group = new StringBuilder();
            // 每三个数字一组
            int groupNum = num % 1000;
            // 处理有百位的情况
            if (groupNum / 100 != 0) {
                group.append(UNITS[groupNum / 100]).append(' ').append("Hundred");
                // 如果百位后还有数字，则需要添加一个空格
                if (groupNum % 100 != 0) {
                    group.append(' ');
                }
            }
            // 十位数字小于 20，则可以用英语的专有数字
            if (groupNum % 100 < 20) {
                group.append(UNITS[groupNum % 100]);
            } else {
                // 否则需要使用 20-90
                group.append(TEN_UNITS[groupNum % 100 / 10]);
                // 个位还有数字也需要添加
                if (groupNum % 10 != 0) {
                    group.append(' ').append(UNITS[groupNum % 10]);
                }
            }
            // 当该组有数字时
            if (group.length() > 0) {
                // 最后添加每组的单位
                group.append(GROUP_UNITS[groupIdx]);
                // 添加到结果中
                groups.push(group);
                // 计算结果的大小。因为每个单元之间要用空格分隔，所以+1
                size += group.length() + 1;
            }
            num /= 1000;
            groupIdx++;
        }

        // 拼接结果
        StringBuilder result = new StringBuilder(size);
        for(;;) {
            result.append(groups.pop());
            if (!groups.isEmpty()) {
                result.append(' ');
            } else {
                break;
            }
        }

        return result.toString();
    }

    @Test
    public void testNumberToWords() {
        test(this::numberToWords);
    }
}
