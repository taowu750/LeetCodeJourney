package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 袭击: https://www.acwing.com/problem/content/121/
 *
 * 在与联盟的战斗中屡战屡败后，帝国撤退到了最后一个据点。依靠其强大的防御系统，帝国击退了联盟的六波猛烈进攻。
 *
 * 经过几天的苦思冥想，联盟将军亚瑟终于注意到帝国防御系统唯一的弱点就是能源供应。
 * 该系统由 N 个核电站供应能源，其中任何一个被摧毁都会使防御系统失效。
 *
 * 将军派出了 N 个特工进入据点之中，打算对能源站展开一次突袭。不幸的是，由于受到了帝国空军的袭击，他们未能降落在预期位置。
 * 作为一名经验丰富的将军，亚瑟很快意识到他需要重新安排突袭计划。他现在最想知道的事情就是哪个特工距离其中任意一个发电站的距离最短。
 *
 * 你能帮他算出来这最短的距离是多少吗？
 *
 * 输入格式：
 * 输入中包含多组测试用例。
 * 第一行输入整数 T，代表测试用例的数量。
 * 对于每个测试用例，第一行输入整数 N。
 * 接下来 N 行，每行输入两个整数 X 和 Y，代表每个核电站的位置的 X，Y 坐标。
 * 在接下来 N 行，每行输入两个整数 X 和 Y，代表每名特工的位置的 X，Y 坐标。
 *
 * 输出格式：
 * 每个测试用例，输出一个最短距离值，结果保留三位小数。
 * 每个输出结果占一行。
 *
 * 数据范围：
 * 1 ≤ N ≤ 100000,
 * 0 ≤ X,Y ≤ 1000000000
 *
 * 例 1：
 * 输入：
 * 2
 * 4
 * 0 0
 * 0 1
 * 1 0
 * 1 1
 * 2 2
 * 2 3
 * 3 2
 * 3 3
 * 4
 * 0 0
 * 0 0
 * 0 0
 * 0 0
 * 0 0
 * 0 0
 * 0 0
 * 0 0
 * 输出：
 * 1.414
 * 0.000
 */
public class G032_Raid {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G032_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G032_output.txt");
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G032_input1.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G032_output1.txt");
    }

    public static class Point implements Comparable<Point> {
        public double x, y;
        public boolean type;
        public int rand;

        public Point(int x, int y, boolean type) {
            this.x = x;
            this.y = y;
            this.type = type;
            rand = ThreadLocalRandom.current().nextInt(1000000);
        }

        @Override
        public int compareTo(Point o) {
            /*
            有hack数据，是位于y轴上的一列数：下半边是A组，上半边是B组。
            这导致在分治时，除了最后一次将两组数混合在了一起，其余情况均是A组在一起，B组在一起。每次计算答案都会将所有点考虑在内。
            给每个点加一个随机数，排序时若横坐标相同，则按随机数排序。
             */
            if (Math.abs(x - o.x) < 1e-6) {
                return rand - o.rand;
            } else {
                return x < o.x ? -1 : 1;
            }
        }
    }

    double min;

    /**
     * 参见 https://www.acwing.com/solution/content/15774/
     * 和 https://www.acwing.com/problem/content/discussion/content/2743/
     * 和 https://www.acwing.com/solution/content/119700/
     */
    public void closestDistance() {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int o = 0; o < T; o++) {
            int N = in.nextInt();
            Point[] points = new Point[2 * N];
            for (int i = 0; i < N; i++) {
                points[i] = new Point(in.nextInt(), in.nextInt(), false);
            }
            for (int i = N; i < 2 * N; i++) {
                points[i] = new Point(in.nextInt(), in.nextInt(), true);
            }
            Arrays.sort(points);
            min = dist(points[0], points[2*N-1]);

            System.out.printf("%.3f\n", find(points, new Point[2 * N], 0, 2 * N - 1));
        }
    }

    private double find(Point[] points, Point[] tmp, int l, int r) {
        if (l == r) {
            return Double.MAX_VALUE;
        }
        int mid = (l + r) / 2;
        double midX = points[mid].x;
        double ans = Math.min(find(points, tmp, l, mid), find(points, tmp, mid + 1, r));

        // 先将 points 中的 [l, mid] 和 [mid + 1, r] 两段进行按 y 轴坐标进行按序归并
        // 注意这里一定要归并，后面对于每个点我们才能快速找出对应的（至多）6 个点，以保证总时间复杂度是 O(n log n)
        for (int i = l, j = mid + 1, k = l; i <= mid || j <= r; k++) {
            if (i > mid) {
                tmp[k] = points[j++];
            } else if (j > r) {
                tmp[k] = points[i++];
            } else if (points[i].y <= points[j].y) {
                tmp[k] = points[i++];
            } else {
                tmp[k] = points[j++];
            }
        }
        System.arraycopy(tmp, l, points, l, r + 1 - l);

        // 找到所有在 [mid_x - ans, mid_x + ans] 中的点，存入 tmp
        int cnt = 0;
        for (int i = l; i <= r; i++) {
            if (points[i].x > midX - ans && points[i].x < midX + ans) {
                tmp[cnt++] = points[i];
            }
        }

        // 下面第二层循环中，由于已经按 y 排序，并且有 tmp[i].y - tmp[j].y < ans 这个判断，才能保证我们对于每个点最多只考虑六个点
        // 这样在每层递归中，就可以保证时间复杂度是线性的，否则时间复杂度是平方级别的
        for (int i = 1; i < cnt; i++) {
            for (int j = i - 1; j >= 0 && tmp[i].y - tmp[j].y + 1e-6 <= ans; j--) {
                ans = Math.min(ans, dist(tmp[i], tmp[j]));
            }
        }
        min = Math.min(min, ans);

        return ans;
    }

    private double dist(Point a, Point b) {
        // 同类型的点计算距离为无穷大的话，如果 [l, r) 中全是同类型的点，在计算来自分别左右两个区间 [l, mid)，[mid, r) 的点的最小距离时，
        // 依然会枚举所有可能的点对。这是因为对于返回距离无穷大的时候，我们并不能通过 x 坐标差大于计算的当前最小值来跳出循环，
        // 因为当前最小值一定为无穷大
        if (a.type == b.type) {
            return min;
        }
        double x = a.x - b.x, y = a.y - b.y;
        return Math.sqrt(x * x + y * y);
    }

    @Test
    public void testClosestDistance() {
        test(this::closestDistance);
    }

    @Test
    public void testPath() {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(new File("dir").getAbsolutePath());
        System.out.println(new File("/dir").getAbsolutePath());
        System.out.println();

        System.out.println(getClass().getResource("").getPath());
        System.out.println(getClass().getResource("/").getPath());
        // getResource 的路径必须存在
        System.out.println(getClass().getResource("data").getPath());
        // 注意 getResource 使用的是编译后的路径（也就是 out 里面的目录），src 在编译后的目录中不存在
//        System.out.println(this.getClass().getResource("/src").toString());
        System.out.println(getClass().getResource("/acguide").getPath());
        System.out.println();

        System.out.println(getClass().getClassLoader().getResource("").getPath());
//        System.out.println(this.getClass().getClassLoader().getResource("/").toString());
        System.out.println(getClass().getClassLoader().getResource("acguide").getPath());
    }

    /**
     * 创建 hack 数据
     */
    @Test
    public void testCreateHackData() throws IOException {
        String dataPath = this.getClass().getResource("data").getPath();
        File file = Paths.get(dataPath, "G032_input1.txt").toFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        int n = 10_0000;
        PrintStream out = new PrintStream(new FileOutputStream(file));
        out.println("1");
        out.printf("%d\n", n);
        for (int i = 0; i < 2 * n; i++) {
            out.printf("%d %d\n", 0, i);
        }
        out.close();

        file = Paths.get(dataPath, "G032_output1.txt").toFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        out = new PrintStream(new FileOutputStream(file));
        out.println("1.000");
        out.close();
    }
}
