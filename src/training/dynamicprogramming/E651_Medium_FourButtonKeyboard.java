package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 651. 4键键盘: https://leetcode-cn.com/problems/4-keys-keyboard/
 *
 * 假设你有一个特殊的键盘，键盘上有如下键:
 * - 键 1: (A): 在屏幕上打印一个'A'。
 * - 键 2: (Ctrl-A): 选择整个屏幕。
 * - 键 3: (Ctrl-C): 复制选择到缓冲区。
 * - 键 4: (Ctrl-V): 在屏幕上已有的内容后面追加打印缓冲区的内容。
 *
 * 现在，你只能按键盘 N 次(使用以上四个键)，找出你可以在屏幕上打印的“A”的最大数量。
 *
 * 例 1：
 * 输入: 3
 * 输出: 3
 * 解释: A, A, A
 *
 * 例 2：
 * 输入: 7
 * 输出: 9
 * 解释: A, A, A, Ctrl A, Ctrl C, Ctrl V, Ctrl V
 *
 * 约束：
 * - 1 <= N <= 50
 * - 答案将在 32 位有符号整数的范围内。
 */
public class E651_Medium_FourButtonKeyboard {

    static void test(IntUnaryOperator method) {
        assertEquals(method.applyAsInt(3), 3);

        assertEquals(method.applyAsInt(7), 9);

        assertEquals(method.applyAsInt(5), 5);

        assertEquals(method.applyAsInt(8), 12);

        assertEquals(method.applyAsInt(25), 1296);
    }

    /**
     * 参见 README.md 中“4键键盘”题解。
     *
     * LeetCode 耗时：1ms - 98.67%
     *          内存消耗：35.1 MB - 66.00%
     */
    public int maxA(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i - 1] + 1;
            for (int j = 2; j < i; j++)
                dp[i] = Math.max(dp[i], dp[j - 2] * (i - j + 1));
        }
        return dp[n];
    }

    @Test
    public void testMaxA() {
        test(this::maxA);
    }


    class Status implements Comparable<Status> {

        int n, aNum, copy;

        Status(int n, int aNum, int copy) {
            this.n = n;
            this.aNum = aNum;
            this.copy = copy;
        }

        public int compareTo(Status o) {
            int cmp = Integer.compare(n, o.n);
            return cmp != 0
                    ? cmp
                    : (cmp = Integer.compare(aNum, o.aNum)) != 0 ? cmp : Integer.compare(copy, o.copy);
        }
    }

    /**
     * 超时
     */
    public int firstMethod(int N) {
        return dp(N, 0, 0, new HashMap<>());
    }

    private int dp(int N, int aNum, int copy, Map<Status, Integer> memory) {
        if (N <= 0)
            return aNum;
        Status s = new Status(N, aNum, copy);
        int max = memory.getOrDefault(s, -1);
        if (max != -1)
            return max;
        max = Math.max(dp(N - 1, aNum + 1, copy, memory),
                Math.max(dp(N - 1, aNum + copy, copy, memory),
                        dp(N - 2, aNum, aNum, memory)));
        memory.put(s, max);

        return max;
    }

    @Test
    public void testOtherMethod() {
        test(this::firstMethod);
    }
}
