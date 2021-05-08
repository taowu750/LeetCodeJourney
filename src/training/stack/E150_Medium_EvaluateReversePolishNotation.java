package training.stack;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 用逆波兰表示法(RPN)计算算术表达式的值。
 * 有效的运算符为 +，-，*，/。每个操作数可以是整数或另一个表达式。
 * <p>
 * 注意：
 * - 两个整数之间的除法应截断为零。
 * - 给定的RPN表达式始终有效。这意味着表达式将始终求结果，并且不会被零运算除以。
 * <p>
 * 例 1：
 * Input: ["2", "1", "+", "3", "*"]
 * Output: 9
 * Explanation: ((2 + 1) * 3) = 9
 * <p>
 * 例 2：
 * Input: ["4", "13", "5", "/", "+"]
 * Output: 6
 * Explanation: (4 + (13 / 5)) = 6
 * <p>
 * 例 3：
 * Input: ["10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"]
 * Output: 22
 * Explanation:
 * ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
 * = ((10 * (6 / (12 * -11))) + 17) + 5
 * = ((10 * (6 / -132)) + 17) + 5
 * = ((10 * 0) + 17) + 5
 * = (0 + 17) + 5
 * = 17 + 5
 * = 22
 */
public class E150_Medium_EvaluateReversePolishNotation {

    static void test(ToIntFunction<String[]> method) {
        String[] tokens = {"2", "1", "+", "3", "*"};
        assertEquals(method.applyAsInt(tokens), 9);

        tokens = new String[]{"4", "13", "5", "/", "+"};
        assertEquals(method.applyAsInt(tokens), 6);

        tokens = new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17",
                "+", "5", "+"};
        assertEquals(method.applyAsInt(tokens), 22);
    }

    /**
     * LeetCode 耗时：2ms - 99.66%
     */
    public int evalRPN(String[] tokens) {
        int top = -1;
        int[] stack = new int[tokens.length / 2 + 1];

        for (String token : tokens) {
            switch (token) {
                case "+":
                    int rightOperand = stack[top--], leftOperand = stack[top--];
                    stack[++top] = leftOperand + rightOperand;
                    break;

                case "-":
                    rightOperand = stack[top--];
                    leftOperand = stack[top--];
                    stack[++top] = leftOperand - rightOperand;
                    break;

                case "*":
                    rightOperand = stack[top--];
                    leftOperand = stack[top--];
                    stack[++top] = leftOperand * rightOperand;
                    break;

                case "/":
                    rightOperand = stack[top--];
                    leftOperand = stack[top--];
                    stack[++top] = leftOperand / rightOperand;
                    break;

                default:
                    stack[++top] = Integer.parseInt(token);
                    break;
            }
        }

        return stack[0];
    }

    @Test
    public void testEvalRPN() {
        test(this::evalRPN);
    }
}
