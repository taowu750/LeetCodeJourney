package company.baidu;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * 现在有两个好友 A 和 B，住在一片长有蘑菇的由 n＊m 个方格组成的草地，A 在 (1,1),B 在 (n,m)。
 *
 * 现在 A 想要拜访 B，由于她只想去 B 的家，所以每次她只会走 (i,j+1) 或 (i+1,j) 这样的路线，
 * 在草地上有 k 个蘑菇种在格子里(多个蘑菇可能在同一方格),问：A 如果每一步随机选择的话(若她在边界上，则只有一种选择)，
 * 那么她不碰到蘑菇走到 B 的家的概率是多少？
 *
 * 输入描述:
 * - 第一行 N，M，K(1 ≤ N,M ≤ 20, k ≤ 100), N,M 为草地大小
 * - 接下来 K 行，每行两个整数 x，y，代表 (x,y) 处有一个蘑菇。
 *
 * 输出描述:
 * 输出一行，代表所求概率(保留到2位小数)
 *
 * 例 1：
 * 输入：
 * 2 2 1
 * 2 1
 * 输出：
 * 0.50
 */
public class BD4_Hard_MushroomArray {

    static void test(Runnable method) {
        StdIOTestUtil.test(method, "company/baidu/data/bd4_input.txt",
                "company/baidu/data/bd4_expect.txt");
        StdIOTestUtil.test(method, "company/baidu/data/bd4_input2.txt",
                "company/baidu/data/bd4_expect2.txt");
    }

    public void mushroomArray() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int N = in.nextInt(), M = in.nextInt(), K = in.nextInt();
            int[][] board = new int[N][M];
            for (int i = 0; i < K; i++) {
                int x = in.nextInt(), y = in.nextInt();
                board[x - 1][y - 1]++;
            }
//            System.out.printf("%.2f\n", probDpMethod(board));
            System.out.printf("%.2f\n", compressMethod(board));
        }
    }

    /**
     * 使用回溯算法计算两种路径的数目。但这种算法复杂度是指数级别的。
     * 因为要穷举所有的可能，没有剪枝，别说答案是否正确，就连时间都不允许
     */
    private void dfs(int[][] board, int[] pathSum, int i, int j, int mushroomCnt) {
        if (i < board.length && j < board[0].length) {
            mushroomCnt += board[i][j];
            if (i == board.length - 1 && j == board[0].length - 1) {
                if (mushroomCnt > 0)
                    pathSum[0]++;
                else
                    pathSum[1]++;
            } else {
                dfs(board, pathSum, i + 1, j, mushroomCnt);
                dfs(board, pathSum, i, j + 1, mushroomCnt);
            }
        }
    }

    /**
     * 使用动态规划可以把复杂度降到 O(NM)。此方法虽然可以正确地计算路径数量，
     * 但是走到每个格子的概率是不一样的，因此不能简单地将路径和相除。
     */
    private double dpMethod(int[][] board) {
        int N = board.length, M = board[0].length;
        // 令 P[i][j] 为达到 (i,j) 处的路径数量。因为只能向右走或向下走，
        // 因此 P[i][j] 只依赖于上边和左边的状态。
        int[][] P = new int[N][M];
        // 令 H[i][j] 为达到 (i,j) 处的无蘑菇路径数量
        int[][] H = new int[N][M];

        // base case
        P[0][0] = 1;
        H[0][0] = board[0][0] == 0 ? 1 : 0;
        for (int j = 1; j < M; j++) {
            P[0][j] = 1;
            H[0][j] = board[0][j] == 0 && H[0][j - 1] > 0 ? 1 : 0;
        }
        for (int i = 1; i < N; i++) {
            P[i][0] = 1;
            H[i][0] = board[i][0] == 0 && H[i][i - 1] > 0 ? 1 : 0;
        }

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                P[i][j] += P[i][j - 1] + P[i - 1][j];
                if (board[i][j] == 0)
                    H[i][j] += H[i][j - 1] + H[i - 1][j];
            }
        }

        return (double) H[N - 1][M - 1] / P[N - 1][M - 1];
    }

    /**
     * 到达每个格子 (i,j) 的概率是 0.5*p[i-1][j] + 0.5*p[i][j-1]，
     * 其中 p[0][0]=1，而最右下方的格子到达概率为 p[i-1][j]+p[i][j-1]（它的左边和上边的格子只能到达它）
     *
     * 注意第一行的格子只能从左边到达(0.5)，而第一列的格子只能从上边到达(0.5)。
     * 最后一行的格子只能走右边(1)，最后一列的格子只能走下边(1)。
     *
     * 参见：https://blog.csdn.net/u010909667/article/details/75207703
     *
     * 牛客网耗时：32ms - 62.46%
     *      内存消耗：10876KB - 6.04%
     */
    private double probDpMethod(int[][] board) {
        if (board[0][0] > 0)
            return 0;

        int N = board.length, M = board[0].length;
        // dp[i][j] 表示走到 (i,j) 不碰到蘑菇的概率
        double[][] dp = new double[N][M];

        // base case
        dp[0][0] = 1;
        for (int j = 1; j < M; j++) {
            // 如果只有一行
            dp[0][j] = board[0][j] == 0 ? dp[0][j - 1] : 0;
            if (N > 1)
                dp[0][j] *= 0.5;
        }
        for (int i = 1; i < N; i++) {
            // 如果只有一列
            dp[i][0] = board[i][0] == 0 ? dp[i - 1][0] : 0;
            if (M > 1)
                dp[i][0] *= 0.5;
        }

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (board[i][j] == 0) {
                    // 注意最后一行和最后一列的边界情况
                    dp[i][j] = i < N - 1 ? 0.5 * dp[i][j - 1] : dp[i][j - 1];
                    dp[i][j] += j < M - 1 ? 0.5 * dp[i - 1][j] : dp[i - 1][j];
                }
            }
        }

        return dp[N - 1][M - 1];
    }

    private double compressMethod(int[][] board) {
        if (board[0][0] > 0)
            return 0;

        final int N = board.length, M = board[0].length;
        final double[] dp = new double[M];

        dp[0] = 1;
        for (int j = 1; j < M; j++) {
            dp[j] = board[0][j] == 0 ? dp[j - 1] : 0;
            if (N > 1)
                dp[j] *= 0.5;
        }

        for (int i = 1; i < N; i++) {
            dp[0] = board[i][0] == 0 ? dp[0] : 0;
            if (M > 1)
                dp[0] *= 0.5;
            for (int j = 1; j < M; j++) {
                if (board[i][j] == 0) {
                    double tmp = dp[j];
                    dp[j] = i < N - 1 ? 0.5 * dp[j - 1] : dp[j - 1];
                    dp[j] += j < M - 1 ? 0.5 * tmp : tmp;
                } else {
                    // 这里不要忘了置 0
                    dp[j] = 0;
                }
            }
        }

        return dp[M - 1];
    }

    @Test
    public void testMushroomArray() {
        test(this::mushroomArray);
    }
}
