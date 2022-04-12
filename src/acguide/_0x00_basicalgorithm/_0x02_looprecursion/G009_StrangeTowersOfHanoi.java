package acguide._0x00_basicalgorithm._0x02_looprecursion;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 奇怪的汉诺塔：https://ac.nowcoder.com/acm/contest/998/E
 *
 * 解出 n 个盘子 A、B、C 和 D 四座塔的Hanoi(汉诺塔)问题最少需要多少步?可以使用以下算法：
 * 1. 首先，塔 A 下面的 k >= 1 个盘子是固定的，其余 n-k 个盘子使用四塔的算法从塔 A 移动到塔 B。
 * 2. 然后使用三个塔的算法将塔 A 中剩下的 k 个盘子移动到塔 D。
 * 3. 最后，使用四个塔的算法将塔 B 中的 n-k 个盘子再次移动到塔 D。
 * 4. 对所有 k ∈ {1, .... , n} 执行此操作，并找到移动次数最少的 k。
 *
 * 例 1：
 * 输入：
 * 无
 * 输出：
 * 对于每个 n (1 <= n <= 12) 打印一行，其中包含解决四个塔和 n 个磁盘问题的最小移动次数。
 */
public class G009_StrangeTowersOfHanoi {

    public static void test(IntFunction<int[]> method) {
        assertArrayEquals(new int[]{0, 1, 3, 5, 9, 13, 17, 25, 33, 41, 49, 65, 81}, method.apply(12));
    }

    public int[] hanoi(int n) {
        int[] ans = new int[n + 1];
        Arrays.fill(ans, 0x3f3f3f3f);
        ans[0] = 0;
        ans[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int k = 1; k <= i; k++) {
                ans[i] = Math.min(ans[i], 2 * ans[i - k] + THREE[k]);
            }
        }

        return ans;
    }

    /**
     * 计算在三个柱子中，从一个柱子移动 n 个盘子到另一个柱子的移动次数
     */
    private static final int[] THREE = new int[13];
    static {
        THREE[1] = 1;
        for (int i = 2; i <= 12; i++) {
            THREE[i] = THREE[i - 1] * 2 + 1;
        }
    }

    @Test
    public void testHanoi() {
        test(this::hanoi);
    }
}
