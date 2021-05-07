package learn.queue_stack;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定整数 n，返回求和为 n 的最少”完美平方“数量。
 * <p>
 * ”完美平方“是一个整数的平方，同时”完美平方“还是这个整数的倍数。例如，1、4、9 和 16 是”完美平方“，
 * 而 3 和 11 不是。
 * <p>
 * 例 1：
 * Input: n = 12
 * Output: 3
 * Explanation: 12 = 4 + 4 + 4.
 * <p>
 * 例 2：
 * Input: n = 13
 * Output: 2
 * Explanation: 13 = 4 + 9.
 * <p>
 * 约束：
 * - 1 <= n <= 10**4
 */
public class PerfectSquares {

    static void test(IntUnaryOperator method) {
        assertEquals(method.applyAsInt(12), 3);

        assertEquals(method.applyAsInt(13), 2);

        assertEquals(method.applyAsInt(1), 1);

        assertEquals(method.applyAsInt(9), 1);

        assertEquals(method.applyAsInt(17), 2);
    }

    /**
     * BFS，每一层减去所有小于当前数的“完美平方”数，然后作为下一层。
     * 注意添加下一层时可以做一下判断，看看是否已经满足要求。这个优化技巧提升巨大。
     *
     * LeetCode 耗时：6ms - 96.65%
     */
    public int numSquares(int n) {
        int sqr;
        if ((sqr = (int) Math.sqrt(n)) * sqr == n && n % sqr == 0)
            return 1;

        // 计算所有小于 n 的完美平方数
        List<Integer> perfectSquareList = new ArrayList<>();
        for (int i = 1; i * i < n; i++) {
            perfectSquareList.add(i * i);
        }

        int level = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(n);

        // BFS
        LABEL_OUTER:
        while (!queue.isEmpty()) {
            level++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                n = queue.remove();
                int insertion = Collections.binarySearch(perfectSquareList, n);
                if (insertion >= 0)
                    break LABEL_OUTER;
                else {
                    insertion = -insertion - 1;
                    for (int j = insertion - 1; j >= 0; j--) {
                        int m = n - perfectSquareList.get(j);
                        // 不要无脑添加，先做个判断。不做这个判断之前，耗时为 32ms，
                        // 做了之后耗时为 6ms，大大减少了时间消耗。
                        if (Collections.binarySearch(perfectSquareList, m) >= 0)
                            return level + 1;
                        queue.add(m);
                    }
                }
            }
        }

        return level;
    }

    @Test
    public void testNumSquares() {
        test(this::numSquares);
    }


    /**
     * 动态规划方法。类似于 {@link training.dynamicprogramming.E322_Medium_CoinChange}。
     *
     * LeetCode 耗时：161 ms - 14.65%
     *          内存消耗：38 MB - 23.27%
     */
    public int dpMethod(int n) {
        int sqr;
        if ((sqr = (int) Math.sqrt(n)) * sqr == n && n % sqr == 0)
            return 1;

        // 计算所有小于 n 的完美平方数
        List<Integer> perfectSquares = new ArrayList<>(sqr + 1);
        for (int i = 2; i * i < n; i++) {
            perfectSquares.add(i * i);
        }

        int[] dp = new int[n + 1];
        dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + 1;
            for (int perfectSquare : perfectSquares) {
                if (perfectSquare > i)
                    break;
                dp[i] = Math.min(dp[i], dp[i - perfectSquare] + 1);
            }
        }

        return dp[n];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
