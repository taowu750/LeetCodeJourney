package training.doubly;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * P2880. [USACO07JAN] Balanced Lineup G: https://www.luogu.com.cn/problem/P2880
 *
 * 每天,农夫 John 的 n 头牛总是按同一序列排队。
 *
 * 有一天, John 决定让一些牛们玩一场飞盘比赛。他准备找一群在队列中位置连续的牛来进行比赛。但是为了避免水平悬殊，
 * 牛的身高不应该相差太大。John 准备了 q 个可能的牛的选择和所有牛的身高 h_i(1≤i≤n)。
 *
 * 每个选择是两个数字 a 和 b，表示询问第 a 头牛到第 b 头牛里的最高和最低的牛的身高差。
 * 他想知道每一组里面最高和最低的牛的身高差。
 *
 * 例 1：
 * 输入：
 * height=[1,7,3,4,2,5], query=[[1,5],[4,6],[2,2]]
 * 输出：
 * [6,3,0]
 *
 * 说明：
 * - 1 ≤ n ≤ 5×10^4
 * - 1 ≤ q ≤ 1.8×10^5
 * - 1 ≤ h_i ≤ 10^6
 * - 1 ≤ a,b ≤ n
 */
public class P2880_BalancedLineupG {

    public interface IHeightQuery {

        int query(int a, int b);
    }

    public static void test(Function<int[], IHeightQuery> factory) {
        IHeightQuery q = factory.apply(new int[]{1,7,3,4,2,5});
        int[][] query = new int[][]{{1,5}, {4,6}, {2,2}};
        int[] result = new int[]{6,3,0};
        for (int i = 0; i < query.length; i++) {
            assertEquals(result[i], q.query(query[i][0], query[i][1]));
        }
    }

    /**
     * 洛谷的一些测试样例超出了最大内存限制，可能需要使用静态内存，或者别的算法
     */
    public static class STHeightQuery implements IHeightQuery {

        private final int[] log2;
        private final int[][] max, min;

        public STHeightQuery(int[] heights) {
            final int n = heights.length;
            // 对每个 len，求 k 使得 2^k <= i < 2^{k+1}
            log2 = new int[n + 1];
            for (int len = 1, k = 0; len <= n; len++) {
                if (1 << k + 1 <= len) {
                    k++;
                }
                log2[len] = k;
            }

            max = new int[n][log2[n] + 1];
            min = new int[n][log2[n] + 1];
            for (int i = 0; i < n; i++) {
                max[i][0] = heights[i];
                min[i][0] = heights[i];
            }

            for (int j = 1; j <= log2[n]; j++) {
                // i + (1 << j) - 1 < n
                for (int i = 0; i < n - (1 << j) + 1; i++) {
                    max[i][j] = Math.max(max[i][j - 1], max[i + (1 << (j - 1))][j - 1]);
                    min[i][j] = Math.min(min[i][j - 1], min[i + (1 << (j - 1))][j - 1]);
                }
            }
        }

        @Override
        public int query(int a, int b) {
            // 注意 a、b 从 1 开始
            a--;
            b--;
            int l = log2[b - a + 1];
            return Math.max(max[a][l], max[b - (1 << l) + 1][l]) -
                    Math.min(min[a][l], min[b - (1 << l) + 1][l]);
        }
    }

    @Test
    public void testSTHeightQuery() {
        test(STHeightQuery::new);
    }
}
