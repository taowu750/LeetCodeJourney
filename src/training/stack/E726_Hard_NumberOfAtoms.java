package training.stack;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 726. 原子的数量: https://leetcode-cn.com/problems/number-of-atoms/
 *
 * 给你一个字符串化学式 formula ，返回「每种原子的数量」。
 *
 * 原子总是以一个大写字母开始，接着跟随 0 个或任意个小写字母，表示原子的名字。
 * 如果数量大于 1，原子后会跟着数字表示原子的数量。如果数量等于 1 则不会跟数字。
 * - 例如，"H2O" 和 "H2O2" 是可行的，但 "H1O2" 这个表达是不可行的。
 *
 * 两个化学式连在一起可以构成新的化学式。
 * - 例如 "H2O2He3Mg4" 也是化学式。
 *
 * 由括号括起的化学式并佐以数字（可选择性添加）也是化学式。
 * - 例如 "(H2O2)" 和 "(H2O2)3" 是化学式。
 *
 * 返回所有原子的数量，格式为：第一个（按字典序）原子的名字，跟着它的数量（如果数量大于 1），
 * 然后是第二个原子的名字（按字典序），跟着它的数量（如果数量大于 1），以此类推。
 *
 * 例 1：
 * 输入：formula = "H2O"
 * 输出："H2O"
 * 解释：原子的数量是 {'H': 2, 'O': 1}。
 *
 * 例 2：
 * 输入：formula = "Mg(OH)2"
 * 输出："H2MgO2"
 * 解释：原子的数量是 {'H': 2, 'Mg': 1, 'O': 2}。
 *
 * 例 3：
 * 输入：formula = "K4(ON(SO3)2)2"
 * 输出："K4N2O14S4"
 * 解释：原子的数量是 {'K': 4, 'N': 2, 'O': 14, 'S': 4}。
 *
 * 说明：
 * - 1 <= formula.length <= 1000
 * - formula 由英文字母、数字、'(' 和 ')' 组成
 * - formula 总是有效的化学式
 */
public class E726_Hard_NumberOfAtoms {

    public static void test(UnaryOperator<String> method) {
        assertEquals("H2O", method.apply("H2O"));
        assertEquals("H2MgO2", method.apply("Mg(OH)2"));
        assertEquals("K4N2O14S4", method.apply("K4(ON(SO3)2)2"));
        assertEquals("H2MgNO", method.apply("Mg(H2O)N"));
    }

    /**
     * LeetCode 耗时：4 ms - 80.99%
     *          内存消耗：40 MB - 24.30%
     */
    public String countOfAtoms(String formula) {
        TreeMap<String, Integer> atom2cnt = new TreeMap<>();
        Deque<Map<String, Integer>> stack = new LinkedList<>();
        stack.push(atom2cnt);
        for (int i = 0; i < formula.length();) {
            char c = formula.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                StringBuilder atom = new StringBuilder(2);
                atom.append(c);
                // 解析原子后的小写部分（可能没有）
                while (++i < formula.length() && formula.charAt(i) >= 'a' && formula.charAt(i) <= 'z') {
                    atom.append(formula.charAt(i));
                }
                int cnt = 1;
                // 解析原子后的数字部分（可能没有）
                if (i < formula.length() && Character.isDigit(formula.charAt(i))) {
                    cnt = 0;
                    do {
                        cnt = cnt * 10 + formula.charAt(i) - '0';
                        i++;
                    } while (i < formula.length() && Character.isDigit(formula.charAt(i)));
                }
                stack.element().merge(atom.toString(), cnt, Integer::sum);
            } else if (c == '(') {
                stack.push(new HashMap<>());
                i++;
            } else if (c == ')') {
                // 解析括号后的数字部分（可能没有）
                int cnt = 1;
                if (++i < formula.length() && Character.isDigit(formula.charAt(i))) {
                    cnt = 0;
                    do {
                        cnt = cnt * 10 + formula.charAt(i) - '0';
                        i++;
                    } while (i < formula.length() && Character.isDigit(formula.charAt(i)));
                }
                // 合并到上一个 map 中
                for (Map.Entry<String, Integer> entry : stack.pop().entrySet()) {
                    stack.element().merge(entry.getKey(), entry.getValue() * cnt, Integer::sum);
                }
            }
        }

        StringBuilder result = new StringBuilder(atom2cnt.size() * 3);
        for (Map.Entry<String, Integer> entry : atom2cnt.entrySet()) {
            result.append(entry.getKey());
            if (entry.getValue() > 1) {
                result.append(entry.getValue());
            }
        }

        return result.toString();
    }

    @Test
    public void testCountOfAtoms() {
        test(this::countOfAtoms);
    }
}
