package acguide._0x00_basicalgorithm._0x07_greedy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.*;

/**
 * Stall Reservations：https://www.acwing.com/problem/content/113/
 *
 * 有 N 头牛在畜栏中吃草。每个畜栏在同一时间段只能提供给一头牛吃草，所以可能会需要多个畜栏。
 *
 * 给定 N 头牛和每头牛开始吃草的时间 A 以及结束吃草的时间 B，每头牛在 [A,B] 这一时间段内都会一直吃草。
 * 当两头牛的吃草区间存在交集时「包括端点」，这两头牛不能被安排在同一个畜栏吃草。
 *
 * 求需要的最小畜栏数目和每头牛对应的畜栏方案。
 *
 * 输入格式
 * 第 1 行：输入一个整数 N。
 * 第 2..N+1 行：第 i+1 行输入第 i 头牛的开始吃草时间 A 以及结束吃草时间 B，数之间用空格隔开。
 *
 * 输出格式
 * 第 1 行：输入一个整数，代表所需最小畜栏数。
 * 第 2..N+1 行：第 i+1 行输入第 i 头牛被安排到的畜栏编号，编号是从 1 开始的「连续」整数，只要方案合法即可。
 *
 * 例 1：
 * 输入：
 * 5
 * 1 10
 * 2 4
 * 3 6
 * 5 8
 * 4 7
 * 输出：
 * 4
 * 1
 * 2
 * 3
 * 2
 * 4
 *
 * 说明：
 * - 1 ≤ N ≤ 50000,
 * - 1 ≤ A,B ≤ 1000000
 */
public class G026_StallReservations {

    public static void test(Runnable method) {
        int[] expectStalls = {4};

        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G026_input.txt", (inputData, actualResult) -> {
            Scanner in = new Scanner(inputData);
            int n = in.nextInt();
            int[][] time = new int[n][2];
            for (int i = 0; i < n; i++) {
                time[i][0] = in.nextInt();
                time[i][1] = in.nextInt();
            }

            Scanner actualIn = new Scanner(actualResult);
            int minStalls = actualIn.nextInt();
            Assertions.assertEquals(expectStalls[0], minStalls);

            Map<Integer, List<int[]>> stall2range = new HashMap<>();
            for (int i = 0; i < n; i++) {
                /*
                根据输出的 stall，查询当前牲畜 i 的时间段和其他使用 stall 的牲畜时间段是否重叠
                 */
                int stall = actualIn.nextInt();
                if (stall2range.containsKey(stall)) {
                    for (int[] range : stall2range.get(stall)) {
                        if (!(time[i][1] < range[0] || time[i][0] > range[1])) {
                            throw new AssertionError("conflict stall arrange: " + stall2range +
                                    ", (" + stall + ":" + Arrays.toString(time[i]) + ")");
                        }
                    }
                } else {
                    stall2range.computeIfAbsent(stall, k -> new ArrayList<>()).add(time[i]);
                }
            }
        });
    }

    public void arrange() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        // cow 还需要记录记录下标
        int[][] cow = new int[n][3];
        for (int i = 0; i < n; i++) {
            cow[i][0] = in.nextInt();
            cow[i][1] = in.nextInt();
            cow[i][2] = i;
        }

        // 按照开始时间进行排序
        Arrays.sort(cow, Comparator.comparingInt((int[] a) -> a[0]));
        // a[] = {结束时间，所处的畜栏}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt((int[] a) -> a[0]));
        // 最大围栏数量
        int stallCnt = 0;
        // stalls[i] = 第 i 头牛所处的围栏
        int[] stalls = new int[n];
        Queue<Integer> unusedStalls = new LinkedList<>();;
        for (int i = 0; i < n; i++) {
            // 将已经结束的牛弹出来，这样就有围栏空了出来
            while (!pq.isEmpty() && pq.peek()[0] < cow[i][0]) {
                unusedStalls.offer(pq.poll()[1]);
            }
            // 如果没有可用的围栏，就需要新建围栏
            int idx = cow[i][2];
            if (unusedStalls.isEmpty()) {
                stalls[idx] = ++stallCnt;
            } else {  // 否则复用围栏
                stalls[idx] = unusedStalls.poll();
            }
            pq.offer(new int[]{cow[i][1], stalls[idx]});
        }

        // 输出围栏数量
        System.out.println(stallCnt);
        // 输出每头牛所处的围栏
        for (int i = 0; i < n; i++) {
            System.out.println(stalls[i]);
        }
    }

    @Test
    public void testArrange() {
        test(this::arrange);
    }
}
