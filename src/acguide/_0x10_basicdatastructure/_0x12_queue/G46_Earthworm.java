package acguide._0x10_basicdatastructure._0x12_queue;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

/**
 * 蚯蚓: https://www.acwing.com/problem/content/135/
 *
 * 蛐蛐国最近蚯蚓成灾了！隔壁跳蚤国的跳蚤也拿蚯蚓们没办法，蛐蛐国王只好去请神刀手来帮他们消灭蚯蚓。
 *
 * 蛐蛐国里现在共有 n 只蚯蚓，第 i 只蚯蚓的长度为 ai ，所有蚯蚓的长度都是非负整数，即可能存在长度为 0 的蚯蚓。
 *
 * 每一秒，神刀手会在所有的蚯蚓中，准确地找到最长的那一只，将其切成两段。若有多只最长的，则任选一只。
 *
 * 神刀手切开蚯蚓的位置由有理数 p 决定。一只长度为 x 的蚯蚓会被切成两只长度分别为 ⌊px⌋ 和 x−⌊px⌋ 的蚯蚓。
 * 特殊地，如果这两个数的其中一个等于 0，则这个长度为 0 的蚯蚓也会被保留。
 *
 * 此外，除了刚刚产生的两只新蚯蚓，其余蚯蚓的长度都会增加一个非负整数 q。
 *
 * 蛐蛐国王知道这样不是长久之计，因为蚯蚓不仅会越来越多，还会越来越长。
 * 蛐蛐国王决定求助于一位有着洪荒之力的神秘人物，但是救兵还需要 m 秒才能到来。
 *
 * 蛐蛐国王希望知道这 m 秒内的战况。具体来说，他希望知道：
 * 1. m 秒内，每一秒被切断的蚯蚓被切断前的长度，共有 m 个数。
 * 2. m 秒后，所有蚯蚓的长度，共有 n+m 个数。
 *
 * 输入格式:
 * - 第一行包含六个整数 n,m,q,u,v,t，其中：n,m,q 的意义参考题目描述；u,v,t 均为正整数；
 *   你需要自己计算 p=u/v（保证 0<u<v）；t 是输出参数，其含义将会在输出格式中解释。
 * - 第二行包含 n 个非负整数，为 a1,a2,…,an，即初始时 n 只蚯蚓的长度。
 * - 同一行中相邻的两个数之间，恰好用一个空格隔开。
 *
 * 输出格式:
 * - 第一行输出 ⌊m/t⌋ 个整数，按时间顺序，依次输出第 t 秒，第 2t 秒，第 3t 秒，……被切断蚯蚓（在被切断前）的长度。
 * - 第二行输出 ⌊(n+m)/t⌋ 个整数，输出 m 秒后蚯蚓的长度；需要按从大到小的顺序，依次输出排名第 t，第 2t，第 3t，……的长度。
 * - 同一行中相邻的两个数之间，恰好用一个空格隔开。
 * - 即使某一行没有任何数需要输出，你也应输出一个空行。
 * - 请阅读样例来更好地理解这个格式。
 *
 * 数据范围:
 * - 1≤n≤10^5,
 * - 0≤ai≤10^8,
 * - 0<p<1,
 * - 0≤q≤200,
 * - 0≤m≤7∗10^6,
 * - 0<u<v≤10^9,
 * - 1≤t≤71
 *
 *
 * 例 1：
 * 输入：
 * 3 7 1 1 3 1
 * 3 3 2
 * 输出：
 * 3 4 4 4 5 5 6
 * 6 6 6 5 5 4 4 3 2 2
 * 解释:
 * 样例中，在神刀手到来前：3 只蚯蚓的长度为 3,3,2。
 * 1 秒后：一只长度为 3 的蚯蚓被切成了两只长度分别为 1 和 2 的蚯蚓，其余蚯蚓的长度增加了 1。最终 4 只蚯蚓的长度分别为 (1,2),4,3。 括号表示这个位置刚刚有一只蚯蚓被切断。
 * 2 秒后：一只长度为 4 的蚯蚓被切成了 1 和 3。5 只蚯蚓的长度分别为：2,3,(1,3),4。
 * 3 秒后：一只长度为 4 的蚯蚓被切断。6 只蚯蚓的长度分别为：3,4,2,4,(1,3)。
 * 4 秒后：一只长度为 4 的蚯蚓被切断。7 只蚯蚓的长度分别为：4,(1,3),3,5,2,4。
 * 5 秒后：一只长度为 5 的蚯蚓被切断。8 只蚯蚓的长度分别为：5,2,4,4,(1,4),3,5。
 * 6 秒后：一只长度为 5 的蚯蚓被切断。9 只蚯蚓的长度分别为：(1,4),3,5,5,2,5,4,6。
 * 7 秒后：一只长度为 6 的蚯蚓被切断。10 只蚯蚓的长度分别为：2,5,4,6,6,3,6,5,(2,4)。
 * 所以，7 秒内被切断的蚯蚓的长度依次为 3,4,4,4,5,5,6。
 * 7 秒后，所有蚯蚓长度从大到小排序为 6,6,6,5,5,4,4,3,2,2。
 */
public class G46_Earthworm {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x12_queue/data/G046_input.txt",
                "acguide/_0x10_basicdatastructure/_0x12_queue/data/G046_output.txt");
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x12_queue/data/G046_input1.txt",
                "acguide/_0x10_basicdatastructure/_0x12_queue/data/G046_output1.txt");
    }

    public void cut() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(), m = in.nextInt(), q = in.nextInt(), u = in.nextInt(), v = in.nextInt(),
                t = in.nextInt();
        int[] earthworm = new int[n];
        for (int i = 0; i < n; i++) {
            earthworm[i] = in.nextInt();
        }
        Arrays.sort(earthworm);

        int[] ei = {n-1};
        int delta = 0;
        Deque<Integer> splitPartOne = new ArrayDeque<>(m);
        Deque<Integer> splitPartTwo = new ArrayDeque<>(m);
        for (int i = 1; i <= m; i++) {
            int max = getMax(earthworm, ei, splitPartOne, splitPartTwo) + delta;
            int px = (int) ((long) max * u / v), cpx = max - px;
            splitPartOne.add(px - delta - q);
            splitPartTwo.add(cpx - delta - q);
            if (i % t == 0) {
                System.out.print(max);
                if (i + t <= m) {
                    System.out.print(" ");
                }
            }

            delta += q;
        }
        System.out.println();

        for (int i = 1; i <= m + n; i++) {
            int max = getMax(earthworm, ei, splitPartOne, splitPartTwo) + delta;
            if (i % t == 0) {
                System.out.print(max);
                if (i + t <= m + n) {
                    System.out.print(" ");
                }
            }
        }
        System.out.println();
    }

    int getMax(int[] earthworm, int[] ei, Deque<Integer> splitPartOne, Deque<Integer> splitPartTwo) {
        int e = ei[0] >= 0 ? earthworm[ei[0]] : Integer.MIN_VALUE;
        int one = !splitPartOne.isEmpty() ? splitPartOne.element() : Integer.MIN_VALUE;
        int two = !splitPartTwo.isEmpty() ? splitPartTwo.element() : Integer.MIN_VALUE;
        int max;
        if (e >= one && e >= two) {
            max = e;
            ei[0]--;
        } else if (one >= e && one >= two) {
            max = one;
            splitPartOne.remove();
        } else {
            max = two;
            splitPartTwo.remove();
        }

        return max;
    }

    @Test
    public void testCut() {
        test(this::cut);
    }
}
