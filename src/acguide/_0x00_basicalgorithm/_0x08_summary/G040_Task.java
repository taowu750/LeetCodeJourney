package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.*;

/**
 * 任务：https://www.acwing.com/problem/content/129/
 *
 * 今天某公司有 M 个任务需要完成。每个任务都有相应的难度级别和完成任务所需时间。
 * 第 i 个任务的难度级别为 yi，完成任务所需时间为 xi 分钟。
 * 如果公司完成此任务，他们将获得（500×xi+2×yi）美元收入。
 *
 * 该公司有 N 台机器，每台机器都有最长工作时间和级别。
 * 如果任务所需时间超过机器的最长工作时间，则机器无法完成此任务。
 * 如果任务难度级别超过机器的级别，则机器无法完成此任务。
 *
 * 每台机器一天内只能完成一项任务。每个任务只能由一台机器完成。
 *
 * 请为他们设计一个任务分配方案，使得该公司能够最大化他们今天可以完成的任务数量。
 * 如果有多种解决方案，他们希望选取赚取利润最高的那种。
 *
 * 输入格式：
 * - 输入包含几个测试用例。
 * - 对于每个测试用例，第一行包含两个整数 N 和 M，分别代表机器数量和任务数量。
 * - 接下来 N 行，每行包含两个整数 xi,yi，分别代表机器最长工作时间和机器级别。
 * - 再接下来 M 行，每行包含两个整数 xi,yi，分别代表完成任务所需时间和任务难度级别。
 *
 * 输出格式：
 * - 对于每个测试用例，输出两个整数，代表公司今天可以完成的最大任务数以及他们将获得的收入。
 *
 * 数据范围：
 * - 1 ≤ N,M ≤ 100000,
 * - 0 < xi < 1440,
 * - 0 ≤ yi ≤ 100
 *
 *
 * 例 1：
 * 输入：
 * 1 2
 * 100 3
 * 100 2
 * 100 1
 * 输出：
 * 1 50004
 */
public class G040_Task {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G040_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G040_output.txt");
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G040_input1.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G040_output1.txt");
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G040_input2.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G040_output2.txt");
    }

    /**
     * 参见 https://www.acwing.com/solution/content/2546/
     */
    public void completeTask() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt(), m = in.nextInt();
            int[][] robots = new int[n][2];
            for (int i = 0; i < n; i++) {
                robots[i][0] = in.nextInt();
                robots[i][1] = in.nextInt();
            }
            Arrays.sort(robots, (a, b) -> a[0] - b[0]);

            int[][] tasks = new int[m][2];
            for (int i = 0; i < m; i++) {
                tasks[i][0] = in.nextInt();
                tasks[i][1] = in.nextInt();
            }
            Arrays.sort(tasks, (a, b) -> a[0] - b[0] != 0 ? a[0] - b[0] : a[1] - b[1]);

            long sum = 0, cnt = 0;
            TreeMap<Integer, Integer> tree = new TreeMap<>();
            for (int i = m - 1, j = n - 1; i >= 0; i--) {
                // 将时间能够满足 tasks[i] 的机器的等级放到集合中，并且这些机器的时间也可以满足后面的任务
                while (j >= 0 && tasks[i][0] <= robots[j][0]) {
                    tree.merge(robots[j--][1], 1, Integer::sum);
                }

                // 从集合中查找大于等于 tasks[i] 等级的最小值并移除
                // 如果有的话，我们就找到一个刚好满足 tasks[i] 的机器
                Integer level = tree.ceilingKey(tasks[i][1]);
                if (level != null) {
                    sum += 500L * tasks[i][0] + 2L * tasks[i][1];
                    cnt++;
                    tree.merge(level, -1, (old, neg) -> old + neg > 0 ? old + neg : null);
                }
            }
            System.out.println(cnt + " " + sum);
        }
    }

    @Test
    public void testCompleteTask() {
        test(this::completeTask);
    }
}
