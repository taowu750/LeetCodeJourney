package acguide._0x10_basicdatastructure._0x13_linkedlist_and_adjacencylist;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * 邻值查找：https://www.acwing.com/problem/content/138/
 *
 * 给定一个长度为 n 的序列 A，A 中的数各不相同。
 * 对于 A 中的每一个数 Ai，求：min_{1≤j<i}|Ai−Aj|，以及令上式取到最小值的 j（记为 Pi）。
 * 若最小值点不唯一，则选择使 Aj 较小，Pi 较大的那个。
 *
 * 输入格式:
 * - 第一行输入整数 n，代表序列长度。
 * - 第二行输入 n 个整数A1…An,代表序列的具体数值，数值之间用空格隔开。
 *
 * 输出格式:
 * - 输出共 n−1 行，每行输出两个整数，数值之间用空格隔开。分别表示当 i 取 2∼n 时，对应的 min_{1≤j<i}|Ai−Aj| 和 Pi 的值。
 *
 * 数据范围:
 * - n≤10^5
 * - |Ai|≤10^9
 *
 *
 * 例 1：
 * 输入：
 * 6
 * 1 5 1 3 1 2
 * 输出：
 * 4 1
 * 0 1
 * 2 3
 * 0 3
 * 1 5
 */
public class G49_NeighborValueLookup {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x13_linkedlist_and_adjacencylist/data/G049_input.txt",
                "acguide/_0x10_basicdatastructure/_0x13_linkedlist_and_adjacencylist/data/G049_output.txt");
    }

    public void lookup() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        TreeMap<Integer, Integer> tree = new TreeMap<>();
        tree.put(in.nextInt(), 1);
        for (int i = 2; i <= n; i++) {
            int a = in.nextInt();
            Integer prev = tree.floorKey(a), next = tree.ceilingKey(a);
            int prevDiff = prev != null ? a - prev : Integer.MAX_VALUE;
            int nextDiff = next != null ? next - a: Integer.MAX_VALUE;
            if (prevDiff <= nextDiff) {
                System.out.println(prevDiff + " " + tree.get(prev));
            } else {
                System.out.println(nextDiff + " " + tree.get(next));
            }
            tree.put(a, i);
        }
    }

    @Test
    public void testLookup() {
        test(this::lookup);
    }


    public static class Node {
        int val, idx;
        Node prev, next;

        public Node(int val, int idx) {
            this.val = val;
            this.idx = idx;
        }

        public int[] getAndDelete() {
            int lAbs = prev != null ? val - prev.val : Integer.MAX_VALUE;
            int rAbs = next != null ? next.val - val : Integer.MAX_VALUE;
            int[] result;
            if (lAbs <= rAbs) {
                result = new int[]{lAbs, prev.idx};
            } else {
                result = new int[]{rAbs, next.idx};
            }
            if (prev != null) {
                prev.next = next;
            }
            if (next != null) {
                next.prev = prev;
            }

            return result;
        }
    }

    public void linkMethod() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Node[] arr = new Node[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Node(in.nextInt(), i);
        }
        // 排序链表并串起来
        Arrays.sort(arr, (a, b) -> Integer.compare(a.val, b.val));
        for (int i = 1; i < n; i++) {
            arr[i].prev = arr[i-1];
            arr[i-1].next = arr[i];
        }

        // 记录排序前序列元素在链表中的指针
        Node[] ori = new Node[n];
        for (Node node : arr) {
            ori[node.idx] = node;
        }

        int[][] ans = new int[n][];
        for (int i = n-1; i >= 1; i--) {
            ans[i] = ori[i].getAndDelete();
        }
        for (int i = 1; i < n; i++) {
            System.out.println(ans[i][0] + " " + (ans[i][1] + 1));
        }
    }

    @Test
    public void testLinkMethod() {
        test(this::linkMethod);
    }
}
