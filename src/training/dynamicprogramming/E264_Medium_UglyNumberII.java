package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 264. 丑数 II: https://leetcode-cn.com/problems/ugly-number-ii/
 *
 * 给你一个整数 n ，请你找出并返回第 n 个「丑数」。
 *
 * 「丑数」就是只包含质因数 2、3 和/或 5 的正整数。
 *
 * 例 1：
 * 输入：n = 10
 * 输出：12
 * 解释：[1, 2, 3, 4, 5, 6, 8, 9, 10, 12] 是由前 10 个丑数组成的序列。
 *
 * 例 2：
 * 输入：n = 1
 * 输出：1
 * 解释：1 通常被视为丑数。
 *
 * 约束：
 * - 1 <= n <= 1690
 */
public class E264_Medium_UglyNumberII {

    static void test(IntUnaryOperator method) {
        assertEquals(12, method.applyAsInt(10));
        assertEquals(1, method.applyAsInt(1));
        assertEquals(1536, method.applyAsInt(100));
        assertEquals(2123366400, method.applyAsInt(1690));
    }

    /**
     * 堆解法，逆向思维。参见：
     * https://leetcode-cn.com/problems/ugly-number-ii/solution/chou-shu-ii-by-leetcode-solution-uoqd/
     *
     * 初始时堆为空。首先将最小的丑数 11 加入堆。每次取出堆顶元素 x，则 x 是堆中最小的丑数，由于 2x, 3x, 5x 也是丑数，
     * 因此将 2x, 3x, 5x 加入堆。这样也就保证了每个数都是丑数。
     *
     * 上述做法会导致堆中出现重复元素的情况。为了避免重复元素，可以使用哈希集合去重，避免相同元素多次加入堆。
     *
     * LeetCode 耗时：43 ms - 31.10%
     *          内存消耗：37.8 MB - 28.90%
     */
    public int nthUglyNumber(int n) {
        PriorityQueue<Long> pq = new PriorityQueue<>(n);
        Set<Long> set = new HashSet<>((int) (n / 0.75) + 1);
        pq.add(1L);
        set.add(1L);

        long num = 1;
        for (int i = 0; i < n; i++) {
            num = pq.remove();
            if (!set.contains(2 * num)) {
                pq.add(2 * num);
                set.add(2 * num);
            }
            if (!set.contains(3 * num)) {
                pq.add(3 * num);
                set.add(3 * num);
            }
            if (!set.contains(5 * num)) {
                pq.add(5 * num);
                set.add(5 * num);
            }
        }

        return (int) num;
    }

    @Test
    public void testNthUglyNumber() {
        test(this::nthUglyNumber);
    }


    /**
     * 动态规划+三指针法：
     * https://leetcode-cn.com/problems/ugly-number-ii/solution/san-zhi-zhen-fang-fa-de-li-jie-fang-shi-by-zzxn/
     *
     * LeetCode 耗时：3 ms - 77.44%
     *          内存消耗：37.4 MB - 77.02%
     */
    public int dpMethod(int n) {
        long[] dp = new long[n + 1];
        dp[1] = 1;

        int p2 = 1, p3 = 1, p5 = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = Math.min(dp[p2] * 2, Math.min(dp[p3] * 3, dp[p5] * 5));
            if (dp[i] == dp[p2] * 2) {
                p2++;
            }
            if (dp[i] == dp[p3] * 3) {
                p3++;
            }
            if (dp[i] == dp[p5] * 5) {
                p5++;
            }
        }

        return (int) dp[n];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
