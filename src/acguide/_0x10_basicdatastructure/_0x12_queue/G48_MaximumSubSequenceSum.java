package acguide._0x10_basicdatastructure._0x12_queue;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * 最大子序和: https://www.acwing.com/problem/content/137/
 *
 * 输入一个长度为 n 的整数序列，从中找出一段长度不超过 m 的连续子序列，使得子序列中所有数的和最大。
 * 注意： 子序列的长度至少是 1。
 *
 * 输入格式:
 * - 第一行输入两个整数 n,m。
 * - 第二行输入 n 个数，代表长度为 n 的整数序列。
 * - 同一行数之间用空格隔开。
 *
 * 输出格式:
 * - 输出一个整数，代表该序列的最大子序和。
 *
 * 数据范围:
 * - 1≤n,m≤300000
 *
 *
 * 例 1：
 * 输入：
 * 6 4
 * 1 -3 5 1 -2 3
 * 输出：
 * 7
 */
public class G48_MaximumSubSequenceSum {

    public static void test(Runnable method) {
        // 1 -2 3 4 2 5
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x12_queue/data/G048_input.txt",
                "acguide/_0x10_basicdatastructure/_0x12_queue/data/G048_output.txt");
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x12_queue/data/G048_input1.txt",
                "acguide/_0x10_basicdatastructure/_0x12_queue/data/G048_output1.txt");
    }

    public void maxSum() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(), m = in.nextInt();
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i+1] = prefix[i] + in.nextInt();
        }
        long ans = Long.MIN_VALUE;
        Deque<Integer> deque = new ArrayDeque<>(m+1);
        // 哨兵
        deque.addLast(0);
        for (int i = 1; i <= n; i++) {
            if (!deque.isEmpty() && i - deque.getFirst() > m) {
                deque.removeFirst();
            }
            // 即使 prefix[i] 小于 prefix[deque.getFirst()] 也没有关系，结果不会错
            // 注意，不能 ans = Math.max(ans, prefix[i])，这种相当于 [0,i] 范围内的序列和
            ans = Math.max(ans, prefix[i] - prefix[deque.getFirst()]);
            while (!deque.isEmpty() && prefix[deque.getLast()] >= prefix[i]) {
                deque.removeLast();
            }
            deque.addLast(i);
        }
        System.out.println(ans);
    }

    @Test
    public void testMaxSum() {
        test(this::maxSum);
    }
}
