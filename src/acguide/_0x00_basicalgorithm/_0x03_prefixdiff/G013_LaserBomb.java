package acguide._0x00_basicalgorithm._0x03_prefixdiff;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * 激光炸弹：https://ac.nowcoder.com/acm/contest/999/A
 *
 * 一种新型的激光炸弹，可以摧毁一个边长为R的正方形内的所有的目标。
 * 现在地图上有N(N ≤ 10000)个目标，用整数Xi,Yi(其值在[0,5000])表示目标在地图上的位置，每个目标都有一个价值。
 *
 * 激光炸弹的投放是通过卫星定位的，但其有一个缺点，就是其爆破范围，即那个边长为R的正方形的边必须和x，y轴平行。
 * 若目标位于爆破正方形的边上，该目标将不会被摧毁。
 *
 * 求一颗炸弹最多能炸掉地图上总价值为多少的目标。
 *
 * 例 1：
 * 输入：
 * R = 1, targets = [[0, 0, 1], [1, 1, 1]]
 * 输出：
 * 1
 * 解释：
 * targets[i] 表示 (Xi, Yi, value)
 */
public class G013_LaserBomb {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x03_prefixdiff/data/G013_input.txt",
                "acguide/_0x00_basicalgorithm/_0x03_prefixdiff/data/G013_expect.txt");
    }

    public void bombardment() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt(), r = in.nextInt();
            int[][] grid = new int[5002][5002];
            // 让最大边界值至少是 r，方便后面的操作
            int maxI = r, maxJ = r;
            // 读取数据，放入网格中
            for (int k = 0; k < n; k++) {
                int i = in.nextInt() + 1, j = in.nextInt() + 1;
                grid[i][j] = in.nextInt();
                maxI = Math.max(maxI, i);
                maxJ = Math.max(maxJ, j);
            }

            // 计算二维前缀和
            for (int i = 1; i <= maxI; i++) {
                for (int j = 1; j <= maxJ; j++) {
                    grid[i][j] = grid[i][j] + grid[i - 1][j] + grid[i][j - 1] - grid[i - 1][j - 1];
                }
            }

            int ans = 0;
            // 计算区域 r 内的价值
            for (int i = r; i <= maxI; i++) {
                for (int j = r; j <= maxJ; j++) {
                    ans = Math.max(ans, grid[i][j] - grid[i][j - r] - grid[i - r][j] + grid[i - r][j - r]);
                }
            }

            System.out.println(ans);
        }
    }

    @Test
    public void testBombardment() {
        test(this::bombardment);
    }
}
