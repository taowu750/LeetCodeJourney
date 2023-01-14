package acguide._0x10_basicdatastructure._0x13_linkedlist_and_adjacencylist;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 动态中位数：https://www.acwing.com/problem/content/108/
 *
 * 依次读入一个整数序列，每当已经读入的整数个数为奇数时，输出已读入的整数构成的序列的中位数。
 *
 * 输入格式:
 * - 第一行输入一个整数 P，代表后面数据集的个数，接下来若干行输入各个数据集。
 * - 每个数据集的第一行首先输入一个代表数据集的编号的整数。
 * - 然后输入一个整数 M，代表数据集中包含数据的个数，M 一定为奇数，数据之间用空格隔开。
 * - 数据集的剩余行由数据集的数据构成，每行包含 10 个数据，最后一行数据量可能少于 10 个，数据之间用空格隔开。
 *
 * 输出格式:
 * - 对于每个数据集，第一行输出两个整数，分别代表数据集的编号以及输出中位数的个数（应为数据个数加一的二分之一），
 *   数据之间用空格隔开。
 * - 数据集的剩余行由输出的中位数构成，每行包含 10 个数据，最后一行数据量可能少于 10 个，数据之间用空格隔开。
 * - 输出中不应该存在空行。
 *
 * 数据范围:
 * - 1≤P≤1000,
 * - 1≤M≤99999,
 * - 所有 M 相加之和不超过 5×10^5。
 *
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
 */
public class G50_RunningMedian {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x13_linkedlist_and_adjacencylist/data/G050_input.txt",
                "acguide/_0x10_basicdatastructure/_0x13_linkedlist_and_adjacencylist/data/G050_output.txt");
    }

    public static class Node {
        int val, oriIdx, nowIdx;
        Node prev, next;

        public Node(int val, int oriIdx) {
            this.val = val;
            this.oriIdx = oriIdx;
        }

        public void delete() {
            if (prev != null) {
                prev.next = next;
            }
            if (next != null) {
                next.prev = prev;
            }
        }

        public String toString() {
            return Integer.toString(val);
        }
    }

    public void calcMedian() {
        Scanner in = new Scanner(System.in);
        final int P = in.nextInt();
        for (int p = 0; p < P; p++) {
            // 和 G49 类似的方法，排序链表节点，记录原下标
            final int label = in.nextInt(), M = in.nextInt();
            Node[] nodes = new Node[M];
            for (int i = 0; i < M; i++) {
                nodes[i] = new Node(in.nextInt(), i);
            }
            Arrays.sort(nodes, (a, b) -> Integer.compare(a.val, b.val));

            // 连接链表节点，并记录每个节点的现下标
            for (int i = 1; i < M; i++) {
                nodes[i].prev = nodes[i-1];
                nodes[i-1].next = nodes[i];
                nodes[i].nowIdx = i;
            }

            // pos 记录原序列元素在链表中的指针
            Node[] pos = new Node[M];
            for (int i = 0; i < M; i++) {
                pos[nodes[i].oriIdx] = nodes[i];
            }

            // median 动态维护链表的中位数
            Node median = nodes[M/2];
            int[] medians = new int[M/2+1];
            for (int i = M-1, j = M/2; i >= 0; i--) {
                // 到了奇数位置，记录中位数
                if ((i & 1) == 0) {
                    medians[j--] = median.val;
                }
                pos[i].delete();
                // 如果删除时，链表长度是奇数
                if ((i & 1) == 0) {
                    // 如果删除的节点在中位数右边，或者就是中位数，那么中位数左移
                    if (pos[i].nowIdx >= median.nowIdx) {
                        median = median.prev;
                    }
                } else { // 否则如果链表长度是偶数
                    if (pos[i].nowIdx <= median.nowIdx) {
                        median = median.next;
                    }
                }
            }

            // 输出中位数
            System.out.println(label + " " + medians.length);
            for (int i = 1; i <= medians.length; i++) {
                System.out.print(medians[i-1]);
                // 注意，只有最后一个数字不需要输出空格
                if (i != medians.length) {
                    System.out.print(' ');
                }
                if (i % 10 == 0) {
                    System.out.println();
                }
            }
            // 避免输出多余的空行
            if (medians.length % 10 != 0 && p != P - 1) {
                System.out.println();
            }
        }
    }

    @Test
    public void testCalcMedian() {
        test(this::calcMedian);
    }
}
