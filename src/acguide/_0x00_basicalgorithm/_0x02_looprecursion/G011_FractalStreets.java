package acguide._0x00_basicalgorithm._0x02_looprecursion;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriLongOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Fractal Streets：https://ac.nowcoder.com/acm/contest/998/G
 *
 * 城市扩建的规划是个令人头疼的大问题。规划师设计了一个极其复杂的方案:当城市规模扩大之后,
 * 把与原来城市结构一样的区域复制或旋转90度之后建设原来在的城市周围（详细地，说将原来的城市复制一放遍在原城市的上方，
 * 将顺时针旋转90度后的城市放在原城市的左上方，将逆时针旋转90度后的城市放在原城市的左方)，再用道路将四部分首尾连接起来，
 * 如下图所示。
 *
 * 容易看出，扩建后的城市各的个房屋仍然由一条道路连接。定义 N 级城市为拥有 2^2N 座房屋的城市。
 * 对于任意等级的城市，从左上角开始沿着唯一的道路走，依次为房屋标号,就能够到得每间房屋的编号了。
 *
 * 住在其中两间房屋里的两人想知道，如果城市发展到了一定等级，他俩各自所处的房屋之间的直线距离是多少。
 * 你可以认为图中的每个格子都是边长为10米的正方形，房屋均位于每个格子的中心点上。
 *
 * T 次询问，每次输入等级 N，两个编号 S、D，求 S 与 D 之间的直线距离（S,D < 2^31, T ≤ 10^4, N ≤ 31）。
 * 输出结果四舍五入为整数。
 *
 * 例 1：
 * 输入：
 * 1 1 2
 * 输出：
 * 10
 *
 * 例 2：
 * 输入：
 * 2 16 1
 * 输出：
 * 30
 *
 * 例 3：
 * 输入：
 * 3 4 33
 * 输出：
 * 50
 */
public class G011_FractalStreets {

    public static void test(TriLongOperator method) {
        assertEquals(10, method.applyAsLong(1, 1, 2));
        assertEquals(30, method.applyAsLong(2, 16, 1));
        assertEquals(50, method.applyAsLong(3, 4, 33));
    }

    public long distance(long n, long s, long d) {
        long[] sPos = calc(n, s - 1), dPos = calc(n, d - 1);
        // +0.5为了四舍五入
        return (long) (Math.sqrt((sPos[0] - dPos[0]) * (sPos[0] - dPos[0])
                + (sPos[1] - dPos[1]) * (sPos[1] - dPos[1])) * 10 + 0.5);
    }

    /**
     * 计算给定等级 n、标号 m（从 0 开始）的房屋的下标。
     * 注意坐标原点在左上角，往右是 x 轴，往下是 y 轴。
     */
    private long[] calc(long n, long m) {
        if (n == 0) {
            return new long[]{0, 0};
        }
        long len = 1L << n - 1, cnt = 1L << 2 * n - 2;
        long[] subPos = calc(n - 1, m % cnt);
        long x = subPos[0], y = subPos[1];
        long area = m / cnt;
        // 左上
        if (area == 0) {
            subPos[0] = y;
            subPos[1] = x;
        } else if (area == 1) {  // 右上
            subPos[1] = y + len;
        } else if (area == 2) {  // 右下
            subPos[0] = x + len;
            subPos[1] = y + len;
        } else {  // 左下
            subPos[0] = 2 * len - y - 1;
            subPos[1] = len - x - 1;
        }

        return subPos;
    }

    @Test
    public void testDistance() {
        test(this::distance);
    }
}
