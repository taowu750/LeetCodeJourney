package training.stack;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定每日温度的列表 T，请返回一个列表，以便对于输入中的每一天，告诉需要等多少天才能变暖。
 * 如果将来没有变暖的可能，则为 0。
 *
 * 例 1：
 * Input: T = [73, 74, 75, 71, 69, 72, 76, 73]
 * Output: [1, 1, 4, 2, 1, 1, 0, 0]
 *
 * 约束：
 * - T 长度范围为 [1, 30000]
 * - 温度的范围为 [30, 100]
 */
public class E739_Medium_DailyTemperatures {

    static void test(Function<int[], int[]> method) {
        int[] T = {73, 74, 75, 71, 69, 72, 76, 73};
        assertArrayEquals(method.apply(T), new int[]{1, 1, 4, 2, 1, 1, 0, 0});

        T = new int[]{30};
        assertArrayEquals(method.apply(T), new int[]{0});

        T = new int[]{73, 74, 79, 71, 69, 72, 76, 73};
        assertArrayEquals(method.apply(T), new int[]{1, 1, 0, 2, 1, 1, 0, 0});
    }

    /**
     * 利用已经记录的信息帮助查找。
     *
     * LeetCode 耗时：3ms - 99.47%
     */
    public int[] dailyTemperatures(int[] T) {
        int len = T.length;
        int[] predict = new int[len];

        // 从右往左
        for (int i = len - 2; i >= 0; i--) {
            if (T[i] < T[i + 1])
                predict[i] = 1;
            else {
                // 利用已经记录的信息，进行查找
                for (int j = i + 1; predict[j] != 0; j = j + predict[j]) {
                    if (T[i] < T[j + predict[j]]) {
                        predict[i] = j + predict[j] - i;
                        break;
                    }
                }
            }
        }

        return predict;
    }

    @Test
    public void testDailyTemperatures() {
        test(this::dailyTemperatures);
    }


    /**
     * 使用栈的方法。
     *
     * LeetCode 耗时：3ms - 99.47%
     */
    public int[] stackMethod(int[] T) {
        int top = -1;
        int[] stack = new int[T.length];
        int[] res = new int[T.length];

        // 从左往右
        for (int i = 0, j; i < T.length; i++) {
            // 如果当前元素大于栈中下标指向的元素，则弹出该元素，并记录变暖天数
            while (top > -1 && T[i] > T[stack[top]]) {
                j = stack[top--];
                res[j] = i - j;
            }
            // 在栈中记录所有元素的下标
            stack[++top] = i;
        }

        return res;
    }

    @Test
    public void testStackMethod() {
        test(this::stackMethod);
    }
}
