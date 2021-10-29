package training.string;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 247. 中心对称数 II: https://leetcode-cn.com/problems/strobogrammatic-number-ii/
 *
 * 中心对称数是指一个数字在旋转了 180 度之后看起来依旧相同的数字（或者上下颠倒地看）。
 * 找到所有长度为 n 的中心对称数。
 *
 * 例 1：
 * 输入:  n = 2
 * 输出: ["11","69","88","96"]
 */
public class E247_Medium_StrobogrammaticNumberII {

    public static void test(IntFunction<List<String>> method) {
        equalsIgnoreOrder(Arrays.asList("11","69","88","96"), method.apply(2));
        equalsIgnoreOrder(Arrays.asList("0","1","8"), method.apply(1));
        equalsIgnoreOrder(Arrays.asList("101","111","181","808","818","888","609","619","689","906","916","986"),
                method.apply(3));
    }

    /**
     * LeetCode 耗时：4 ms - 97.81%
     *          内存消耗：48.3 MB - 92.54%
     */
    public List<String> findStrobogrammatic(int n) {
        /*
        只有 0、1、6、8、9 倒过来还是数字。其中 0、1、8 倒过来还是原数，6 和 9 互反。
        注意，数字倒过来后顺序也反了。所以只有相同或对称的数字序列才能反过来。
        注意，0 不能在开头
         */
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        find(result, sb, n / 2 - 1);

        return result;
    }

    private static final char[][] CHARS = {{'0', '0'}, {'1', '1'}, {'8', '8'}, {'6', '9'}, {'9', '6'}};
    private static final char[][] CHARS_NO_ZERO = {{'1', '1'}, {'8', '8'}, {'6', '9'}, {'9', '6'}};

    private void find(List<String> result, StringBuilder sb, int idx) {
        int n = sb.length();
        if (idx < 0) {
            if ((n & 1) != 0) {
                sb.setCharAt(n / 2, '0');
                result.add(sb.toString());
                sb.setCharAt(n / 2, '1');
                result.add(sb.toString());
                sb.setCharAt(n / 2, '8');
                result.add(sb.toString());
            } else {
                result.add(sb.toString());
            }
            return;
        }

        char[][] chars = CHARS;
        if (idx == 0) {
            chars = CHARS_NO_ZERO;
        }
        for (char[] ch : chars) {
            sb.setCharAt(idx, ch[0]);
            sb.setCharAt(n - idx - 1, ch[1]);
            find(result, sb, idx - 1);
        }
    }

    @Test
    public void testFindStrobogrammatic() {
        test(this::findStrobogrammatic);
    }
}
