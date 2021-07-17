package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 17. 电话号码的字母组合: https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/
 *
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按「任意顺序」返回。
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 * - 2: abc
 * - 3: def
 * - 4: ghi
 * - 5: jkl
 * - 6: mno
 * - 7: pqrs
 * - 8: tuv
 * - 9: wxyz
 *
 * 例 1：
 * 输入：digits = "23"
 * 输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
 *
 * 例 2：
 * 输入：digits = ""
 * 输出：[]
 *
 * 例 3：
 * 输入：digits = "2"
 * 输出：["a","b","c"]
 *
 * 约束：
 * - 0 <= digits.length <= 4
 * - digits[i] 是范围 ['2', '9'] 的一个数字。
 */
public class E17_Medium_LetterCombinationsOfPhoneNumber {

    static void test(Function<String, List<String>> method) {
        equalsIgnoreOrder(Arrays.asList("ad","ae","af","bd","be","bf","cd","ce","cf"),
                method.apply("23"));
        equalsIgnoreOrder(Collections.emptyList(), method.apply(""));
        equalsIgnoreOrder(Arrays.asList("a","b","c"), method.apply("2"));
    }

    static Map<Character, char[]> digit2char = new HashMap<>();
    static {
        digit2char.put('2', new char[]{'a', 'b', 'c'});
        digit2char.put('3', new char[]{'d', 'e', 'f'});
        digit2char.put('4', new char[]{'g', 'h', 'i'});
        digit2char.put('5', new char[]{'j', 'k', 'l'});
        digit2char.put('6', new char[]{'m', 'n', 'o'});
        digit2char.put('7', new char[]{'p', 'q', 'r', 's'});
        digit2char.put('8', new char[]{'t', 'u', 'v'});
        digit2char.put('9', new char[]{'w', 'x', 'y', 'z'});
    }

    /**
     * LeetCode 耗时：0ms - 100%
     *          内存消耗：38.3MB - 35%
     */
    public List<String> letterCombinations(String digits) {
        if (digits.length() == 0) {
            return Collections.emptyList();
        }

        StringBuilder sb = new StringBuilder(digits.length());
        for (int i = 0; i < digits.length(); i++) {
            sb.append(' ');
        }
        List<String> result = new ArrayList<>();
        dfs(digits, 0, sb, result);

        return result;
    }

    private void dfs(String digits, int i, StringBuilder sb, List<String> result) {
        if (i == digits.length()) {
            result.add(sb.toString());
            return;
        }

        char[] chars = digit2char.get(digits.charAt(i));
        for (char ch : chars) {
            sb.setCharAt(i, ch);
            dfs(digits, i + 1, sb, result);
        }
    }

    @Test
    public void testLetterCombinations() {
        test(this::letterCombinations);
    }
}
