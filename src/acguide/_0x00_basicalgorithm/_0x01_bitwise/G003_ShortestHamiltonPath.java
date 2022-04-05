package acguide._0x00_basicalgorithm._0x01_bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 最短 Hamilton 路径：https://ac.nowcoder.com/acm/contest/996/D
 *
 * 给定一张 n(n≤20) 个点的带权无向图 a，点从0∼n−1标号，求起点 0 到终点 n-1 的最短Hamilton路径。
 * Hamilton路径的定义是从 0 到 n-1 不重不漏地经过每个点恰好一次。
 *
 * 例 1：
 * 输入：
 * 0 2 1 3
 * 2 0 2 1
 * 1 2 0 1
 * 3 1 1 0
 * 输出：
 * 4
 * 解释：
 * 从0到3的Hamilton路径有两条，0-1-2-3和0-2-1-3。前者的长度为2+2+1=5，后者的长度为1+2+1=4
 *
 * 说明：
 * - 0 < a[i,j] <= 10^7 (i != j)
 * - 对于任意的x,y,z，数据保证 a[x,x]=0，a[x,y]=a[y,x] 并且a[x,y]+a[y,z]≥a[x,z]。
 */
public class G003_ShortestHamiltonPath {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(4, method.applyAsInt(new int[][]{
                {0, 2, 1, 3},
                {2, 0, 2, 1},
                {1, 2, 0, 1},
                {3, 1, 1, 0}}));
    }

    private static final int[][] dp = new int[1 << 20][20];

    public int shortestPath(int[][] a) {
        final int n = a.length;
        int statusLen = 1 << n;
        for (int i = 0; i < statusLen; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = 0x3f3f3f3f;
            }
        }
        dp[1][0] = 0;

        for (int i = 2; i < statusLen; i++) {
            for (int j = 0; j < n; j++) {
                if ((i & 1 << j) == 0) {
                    continue;
                }
                for (int k = 0; k < n; k++) {
                    if (((i ^ 1 << j) & 1 << k) == 0) {
                        continue;
                    }
                    dp[i][j] = Math.min(dp[i][j], dp[i ^ (1 << j)][k] + a[k][j]);
                }
            }
        }

        return dp[statusLen - 1][n - 1];
    }

    @Test
    public void testShortestPath() {
        test(this::shortestPath);
    }
}
