package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 679. 24 点游戏: https://leetcode-cn.com/problems/24-game/
 *
 * 你有 4 张写有 1 到 9 数字的牌。你需要判断是否能通过 *，/，+，-，(，) 的运算得到 24。
 *
 * 例 1：
 * 输入: [4, 1, 8, 7]
 * 输出: True
 * 解释: (8-4) * (7-1) = 24
 *
 * 例 2：
 * 输入: [1, 2, 1, 2]
 * 输出: False
 *
 * 说明：
 * - 除法运算符 / 表示实数除法，而不是整数除法。例如 4 / (1 - 2/3) = 12。
 * - 每个运算符对两个数进行运算。特别是我们不能用 - 作为一元运算符。例如，[1, 1, 1, 1] 作为输入时，
 *   表达式 -1 - 1 - 1 - 1 是不允许的。
 * - 你不能将数字连接在一起。例如，输入为 [1, 2, 1, 2] 时，不能写成 12 + 12 。
 */
public class E679_Hard_24Game {

    public static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{4, 1, 8, 7}));
        assertFalse(method.test(new int[]{1, 2, 1, 2}));
        assertTrue(method.test(new int[]{1, 2, 3, 4}));
        assertTrue(method.test(new int[]{1, 3, 4, 6}));
        assertFalse(method.test(new int[]{1, 1, 7, 7}));
    }

    /**
     * 其实可以直接用 double，不用搞一个 Fraction 出来。
     *
     * LeetCode 耗时：5 ms - 26.71%
     *          内存消耗 ：38 MB - 66.46%
     */
    public boolean judgePoint24(int[] cards) {
        List<Fraction> fractions = new LinkedList<>();
        for (int card : cards) {
            fractions.add(new Fraction(card, 1));
        }

        return dfs(fractions);
    }

    /**
     * 分数
     */
    public static class Fraction {
        // 分子、分母
        public int numerator, denominator;

        public Fraction(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public double value() {
            return numerator * 1. / denominator;
        }

        @Override
        public String toString() {
            return numerator + "/" + denominator;
        }

        public static Fraction add(Fraction a, Fraction b) {
            // 取 a、b 分母的最小公倍数作为结果的分母
            int deno = lcm(a.denominator, b.denominator);
            // 取 a、b 统一分母后的分子相加的数作为结果的分子
            int nume = a.numerator * deno / a.denominator + b.numerator * deno / b.denominator;
            // 让分子、分母除以它们的最大公约数
            int g = gcd(nume, deno);
            return new Fraction(nume / g, deno / g);
        }

        public static Fraction minus(Fraction a, Fraction b) {
            int deno = lcm(a.denominator, b.denominator);
            int nume = a.numerator * deno / a.denominator - b.numerator * deno / b.denominator;
            int g = gcd(nume, deno);
            return new Fraction(nume / g, deno / g);
        }

        public static Fraction multi(Fraction a, Fraction b) {
            int deno = a.denominator * b.denominator;
            int nume = a.numerator * b.numerator;
            int g = gcd(nume, deno);
            return new Fraction(nume / g, deno / g);
        }

        public static Fraction divide(Fraction a, Fraction b) {
            int deno = a.denominator * b.numerator;
            int nume = a.numerator * b.denominator;
            int g = gcd(nume, deno);
            return new Fraction(nume / g, deno / g);
        }

        private static int gcd(int a, int b) {
            while (b != 0) {
                int tmp = b;
                b = a % b;
                a = tmp;
            }

            return a;
        }

        /**
         * 最小公倍数，等于两数之积除以两数最大公约数
         */
        private static int lcm(int a, int b) {
            return a * b / gcd(a, b);
        }
    }

    private boolean dfs(List<Fraction> fractions) {
        if (fractions.size() == 1) {
            return fractions.get(0).value() == 24;
        }

        // 取一个数
        for (int i = 0; i < fractions.size() - 1; i++) {
            Fraction a = fractions.remove(i);
            // 循环取后面每一个数，让这两个数做运算
            for (int j = i; j < fractions.size(); j++) {
                Fraction b = fractions.remove(j);

                // a + b
                fractions.add(i, Fraction.add(a, b));
                if (dfs(fractions)) {
                    return true;
                }
                fractions.remove(i);

                // a - b
                fractions.add(i, Fraction.minus(a, b));
                if (dfs(fractions)) {
                    return true;
                }
                fractions.remove(i);

                // b - a
                fractions.add(i, Fraction.minus(b, a));
                if (dfs(fractions)) {
                    return true;
                }
                fractions.remove(i);

                // a * b
                fractions.add(i, Fraction.multi(a, b));
                if (dfs(fractions)) {
                    return true;
                }
                fractions.remove(i);

                // a / b，注意 b 不能为 0
                if (b.numerator != 0) {
                    fractions.add(i, Fraction.divide(a, b));
                    if (dfs(fractions)) {
                        return true;
                    }
                    fractions.remove(i);
                }

                // b / a，注意 a 不能为 0
                if (a.numerator != 0) {
                    fractions.add(i, Fraction.divide(b, a));
                    if (dfs(fractions)) {
                        return true;
                    }
                    fractions.remove(i);
                }

                // 将 b 放回原来的位置
                fractions.add(j, b);
            }
            // 将 a 放回原来的位置
            fractions.add(i, a);
        }

        return false;
    }

    @Test
    public void testJudgePoint24() {
        test(this::judgePoint24);
    }
}
