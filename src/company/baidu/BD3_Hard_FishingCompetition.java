package company.baidu;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * ss请cc来家里钓鱼，鱼塘可划分为n＊m的格子，每个格子有不同的概率钓上鱼，cc一直在坐标(x,y)的格子钓鱼，
 * 而ss每分钟随机钓一个格子。问t分钟后他们谁至少钓到一条鱼的概率大？为多少？
 *
 * 输入描述：
 * - 第一行五个整数 n,m,x,y,t(1≤n,m,t≤1000,1≤x≤n,1≤y≤m);
 * - 接下来为一个 n＊m 的矩阵，每行 m 个一位小数，共 n 行
 * - 第 i 行第 j 个数代表坐标为 (i,j) 的格子钓到鱼的概率为 p(0≤p≤1)
 *
 * 输出描述：
 * - 输出两行。第一行为概率大的人的名字(cc/ss/equal),第二行为这个概率(保留 2 位小数)
 *
 * 例 1：
 * 输入：
 * 2 2 1 1 1
 * 0.2 0.1
 * 0.1 0.4
 * 输出：
 * equal
 * 0.20
 */
public class BD3_Hard_FishingCompetition {

    static void test(Runnable method) {
        StdIOTestUtil.test(method, "company/baidu/data/bd3_input.txt",
                "company/baidu/data/bd3_expect.txt");
    }

    /**
     * 牛客上用 BufferedReader 读取一行然后自己切分解析数字比 Scanner 快很多。
     *
     * 牛客运行时间：1869ms - 0.24%
     *    内存消耗：42096KB - 31.01%
     */
    public void fishingCompetition() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            // 1≤ n,m,t ≤1000
            int n = in.nextInt(), m = in.nextInt();
            // 1≤x≤n, 1≤y≤m
            int x = in.nextInt(), y = in.nextInt(), t = in.nextInt();
            // 最终概率保留 2 位小数

            double ccP = 0, ssP = 0;
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    float p = in.nextFloat();
                    if (i == x && y == j)
                        ccP = p;
                    ssP += p;
                }
            }
            ssP /= n * m;

            // 如果算至少钓到一条鱼的概率，情况很多，可能需要用伯努力方程，还会导致结果不精确。
            // 采用逆向思维，减去钓不到鱼的概率，从而解出题目
            double cc = 1 - Math.pow((1 - ccP), t);
            double ss = 1 - Math.pow((1 - ssP), t);
            if (Math.abs(cc - ss) < 1e-5) {
                System.out.println("equal");
                System.out.printf("%.2f\n", cc);
            } else if (cc > ss) {
                System.out.println("cc");
                System.out.printf("%.2f\n", cc);
            } else {
                System.out.println("ss");
                System.out.printf("%.2f\n", ss);
            }
        }
    }

    @Test
    public void testFishingCompetition() {
        test(this::fishingCompetition);
    }
}
