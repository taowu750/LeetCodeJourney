package acguide._0x00_basicalgorithm._0x05_sort;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Running Median: https://www.acwing.com/problem/content/108/
 *
 * 动态维护中位数问题：依次读入一个整数序列，每当已经读入的整数个数为奇数时，输出已读入的整数构成的序列的中位数。
 *
 * 输入格式：
 * 第一行输入一个整数 P，代表后面数据集的个数，接下来若干行输入各个数据集。
 * 每个数据集的第一行首先输入一个代表数据集的编号的整数。
 * 然后输入一个整数 M，代表数据集中包含数据的个数，M 一定为奇数，数据之间用空格隔开。
 * 数据集的剩余行由数据集的数据构成，每行包含 10 个数据，最后一行数据量可能少于 10 个，数据之间用空格隔开。
 *
 * 输出格式
 * 对于每个数据集，第一行输出两个整数，分别代表数据集的编号以及输出中位数的个数（应为数据个数加一的二分之一），数据之间用空格隔开。
 * 数据集的剩余行由输出的中位数构成，每行包含 10 个数据，最后一行数据量可能少于 10 个，数据之间用空格隔开。
 * 输出中不应该存在空行。
 *
 * 例 1：
 * 输入：
 * 3
 * 1 9
 * 1 2 3 4 5 6 7 8 9
 * 2 9
 * 9 8 7 6 5 4 3 2 1
 * 3 23
 * 23 41 13 22 -3 24 -31 -11 -8 -7
 * 3 5 103 211 -311 -45 -67 -73 -81 -99
 * -33 24 56
 * 输出：
 * 1 5
 * 1 2 3 4 5
 * 2 5
 * 9 8 7 6 5
 * 3 12
 * 23 23 22 22 13 3 5 5 3 -3
 * -7 -3
 * 解释：
 * 对每个数据集，每输入一个数字就判断长度是不是奇数，是的话就输出一个中位数
 *
 * 说明：
 * - 1 ≤ P ≤ 1000,
 * - 1 ≤ M ≤ 99999,
 * - 所有 M 相加之和不超过 5×10^5。
 */
public class G021_RunningMedian {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x05_sort/data/G021_input.txt",
                "acguide/_0x00_basicalgorithm/_0x05_sort/data/G021_expect.txt");
    }

    public void calcMedian() {
        Scanner in = new Scanner(System.in);
        int numSets = in.nextInt();
        for (int o = 0; o < numSets; o++) {
            int set = in.nextInt(), cnt = in.nextInt();
            System.out.println(set + " " + (cnt + 1) / 2);
            // 对顶堆求中位数
            // 左半边大顶堆
            PriorityQueue<Integer> left = new PriorityQueue<>(cnt / 2 + 1, (a, b) -> -Integer.compare(a, b));
            // 右半边小顶堆
            PriorityQueue<Integer> right = new PriorityQueue<>(cnt / 2 + 1);
            StringBuilder res = new StringBuilder(50);
            for (int i = 1; i <= cnt; i++) {
                int num = in.nextInt();
                if (left.size() == right.size()) {
                    if (right.size() > 0 && right.element() < num) {
                        left.add(right.remove());
                        right.add(num);
                    } else {
                        left.add(num);
                    }
                } else {
                    if (num >= left.element()) {
                        right.add(num);
                    } else {
                        right.add(left.remove());
                        left.add(num);
                    }
                }
                if ((i & 1) == 1) {
                    res.append(left.element().intValue());
                    // 每十个中位数就要换一行
                    if ((i + 1) % 20 == 0) {
                        System.out.println(res);
                        res.setLength(0);
                    } else if (i != cnt) {
                        res.append(' ');
                    }
                }
            }
            if (res.length() > 0) {
                System.out.println(res);
            }
        }
    }

    @Test
    public void testCalcMedian() {
        test(this::calcMedian);
    }
}
