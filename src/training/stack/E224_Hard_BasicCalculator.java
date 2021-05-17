package training.stack;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 224. 基本计算器: https://leetcode-cn.com/problems/basic-calculator/
 *
 * 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
 *
 * 例 1：
 * 输入：s = "1 + 1"
 * 输出：2
 *
 * 例 2：
 * 输入：s = " 2-1 + 2 "
 * 输出：3
 *
 * 例 3：
 * 输入：s = "(1+(4+5+2)-3)+(6+8)"
 * 输出：23
 *
 * 约束：
 * - 1 <= s.length <= 3 * 10**5
 * - s 由数字、'+'、'-'、'('、')'、和 ' ' 组成
 * - s 表示一个有效的表达式
 */
public class E224_Hard_BasicCalculator {

    static void test(ToIntFunction<String> method) {
        assertEquals(2, method.applyAsInt("1 + 1"));
        assertEquals(3, method.applyAsInt(" 2-1 + 2 "));
        assertEquals(23, method.applyAsInt("(1+(4+5+2)-3)+(6+8)"));
        assertEquals(-1, method.applyAsInt("-2+ 1"));
        assertEquals(-12, method.applyAsInt("- (3 + (4 + 5))"));
    }

    /**
     * LeetCode 耗时：26 ms - 18.88%
     *          内存消耗：39.6 MB - 23.48%
     */
    public int calculate(String s) {
        return evaluateSuffix(infixToSuffix(s));
    }

    private static final Map<Character, Integer> OPERATOR_PRIORITY = new HashMap<>(7);
    static {
        OPERATOR_PRIORITY.put('(', 0);
        OPERATOR_PRIORITY.put('+', 1);
        OPERATOR_PRIORITY.put('-', 1);
        OPERATOR_PRIORITY.put('*', 2);
        OPERATOR_PRIORITY.put('/', 2);
    }

    public static List<Object> infixToSuffix(String infix) {
        int n = infix.length();
        char[] operatorStack = new char[n];
        int top = -1;
        List<Object> suffix = new ArrayList<>(n);

        /*
        lastTokenType 表示上一个 token 的类型：
        -1：上一次是空
        0：上一次是运算符或者左括号
        1：上一次是数字或者右括号
         */
        int lastSignType = -1;
        for (int i = 0; i < n; i++) {
            char c = infix.charAt(i);
            if (c == ' ')
                continue;
            // 将数字添加到后缀表达式中
            if (c >= '0' && c <= '9') {
                int num = 0;
                do {
                    // 将 c - '0' 括起来，防止潜在的数字溢出
                    num = num * 10 + (c - '0');
                    if (++i == n)
                        break;
                    c = infix.charAt(i);
                } while (c >= '0' && c <= '9');
                suffix.add(num);
                i--;

                lastSignType = 1;
            }
            // 如果 token 是 )，将操作符栈中直到 ( 的符号都弹出
            else if (c == ')') {
                while (operatorStack[top] != '(') {
                    suffix.add(operatorStack[top--]);
                }
                // 不要忘记把 ( 也给弹出来
                top--;
                lastSignType = 1;
            }
            // 否则 token 是操作符或 (
            else {
                // 当上个 token 表示符号，则加一个 0
                if ((c == '+' || c == '-') && lastSignType < 1) {
                    suffix.add(0);
                }
                // 否则如果 token 不是 (，则尝试从操作符栈中弹出优先级大于等于 c 的
                else if (c != '(') {
                    while (top > -1 && OPERATOR_PRIORITY.get(operatorStack[top]) >=
                            OPERATOR_PRIORITY.get(c)) {
                        suffix.add(operatorStack[top--]);
                    }
                }
                operatorStack[++top] = c;
                lastSignType = 0;
            }
        }
        while (top > -1) {
            suffix.add(operatorStack[top--]);
        }
//        System.out.println(suffix);

        return suffix;
    }

    public static int evaluateSuffix(List<Object> suffix) {
        int top = -1;
        int[] stack = new int[suffix.size() / 2 + 1];

        for (Object token : suffix) {
            if (token instanceof Character) {
                char c = (char) token;
                switch (c) {
                    case '+':
                        int rightOperand = stack[top--], leftOperand = stack[top--];
                        stack[++top] = leftOperand + rightOperand;
                        break;

                    case '-':
                        rightOperand = stack[top--];
                        leftOperand = stack[top--];
                        stack[++top] = leftOperand - rightOperand;
                        break;

                    case '*':
                        rightOperand = stack[top--];
                        leftOperand = stack[top--];
                        stack[++top] = leftOperand * rightOperand;
                        break;

                    case '/':
                        rightOperand = stack[top--];
                        leftOperand = stack[top--];
                        stack[++top] = leftOperand / rightOperand;
                        break;
                }
            } else {
                stack[++top] = (int) token;
            }
        }

        return stack[0];
    }

    @Test
    public void testCalculate() {
        test(this::calculate);
    }


    private int top, i;

    /**
     * LeetCode 耗时：3 ms - 98.79%
     *          内存消耗：38.4 MB - 93.37%
     */
    public int betterMethod(String s) {
        top = -1;
        i = 0;
        int[] stack = new int[s.length() + 1];

        return dfs(s, stack);
    }

    private int dfs(String s, int[] stack) {
        int n = s.length(), num = 0;
        // 将符号分给每个数字，然后将它们压入栈中，最后计算结果
        char sign = '+';
        int topStart = top + 1;
        for (; i < n; i++) {
            char c = s.charAt(i);
            if (c == ' ')
                continue;
            if (c >= '0' && c <= '9') {
                num = num * 10 + (c - '0');
            } else if (c == '+' || c == '-') {
                if (sign == '+') {
                    stack[++top] = num;
                } else {
                    stack[++top] = -num;
                }
                num = 0;
                sign = c;
            }
            // 遇到 ( 则递归计算结果
            else if (c == '(') {
                i++;
                int sub = dfs(s, stack);
                // 将递归结果压入栈中
                if (sign == '+') {
                    stack[++top] = sub;
                } else {
                    stack[++top] = -sub;
                }
            }
            // 遇到 )，则跳出循环，计算括号内的结果
            else {
                break;
            }
        }
        // num 不为 0，则需要记录到栈中
        if (num != 0) {
            if (sign == '+') {
                stack[++top] = num;
            } else {
                stack[++top] = -num;
            }
        }

        int result = 0;
        for (int j = topStart; j <= top; j++) {
            result += stack[j];
        }
        // 结果计算完后收缩栈
        top = topStart - 1;

        return result;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
