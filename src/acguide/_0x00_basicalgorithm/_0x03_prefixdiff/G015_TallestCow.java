package acguide._0x00_basicalgorithm._0x03_prefixdiff;

import org.junit.jupiter.api.Test;
import util.datastructure.function.FourFunction;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Tallest Cow: https://ac.nowcoder.com/acm/contest/999/C
 *
 * 有 N 头牛站成一行。两头牛能够相互看见，当且仅当它们中间的牛身高都比它们矮。
 * 现在，我们只知道其中最高的牛是第 P 头，它的身高是 H, 不知道剩余 N - 1 头牛的身高。
 * 但是，我们还知道 M 对关系，每对关系都指明了某两头牛 A_i 和 B_i 可以相互看见。注意「可能有重复关系」。
 * 求每头牛的身高最大可能是多少。
 *
 * 例 1：
 * 输入：
 * N = 9, P = 3, H = 5, relations = [[1,3],[5,3],[4,3],[3,7],[9,8]]
 * 输出：
 * [5,4,5,3,4,4,5,5,5]
 *
 * 说明：
 * - 1 ≤ N,M ≤ 10^4
 * - 1 ≤ P,A_i,B_i ≤ N
 * - 1 ≤ H ≤ 10^6
 */
public class G015_TallestCow {

    public static void test(FourFunction<Integer, Integer, Integer, int[][], int[]> method) {
        assertArrayEquals(new int[]{5,4,5,3,4,4,5,5,5}, method.apply(9, 3, 5, new int[][]{{1,3},{5,3},{4,3},{3,7},{9,8}}));
    }

    public int[] maxHeights(int n, int p, int h, int[][] relations) {
        /*
        首先我们注意到，每对关系的范围之间不会出现交叉（可以相邻或包含），否则会出现矛盾的结果。
        由于是求最高，我们可以先假设所有牛身高都是 H。

        对每一对关系 A_i,B_i，它们之间牛的身高都会-1。
         */

        // 差分数组表示身高的减少
        int[] diff = new int[n];
        diff[0] = h;
        // 关系去重
        Set<Relation> visited = new HashSet<>();
        for (int[] relation : relations) {
            int ai = relation[0] - 1, bi = relation[1] - 1;
            if (ai > bi) {
                int tmp = ai;
                ai = bi;
                bi = tmp;
            }
            Relation rel = new Relation(ai, bi);
            if (visited.contains(rel)) {
                continue;
            }
            visited.add(rel);
            if (ai < bi - 1) {
                diff[ai + 1]--;
                diff[bi]++;
            }
        }

        for (int i = 1; i < n; i++) {
            diff[i] += diff[i - 1];
        }

        return diff;
    }

    public static class Relation {
        int ai, bi;

        public Relation(int ai, int bi) {
            this.ai = ai;
            this.bi = bi;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Relation)) return false;
            Relation relation = (Relation) o;
            return ai == relation.ai && bi == relation.bi;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ai, bi);
        }
    }

    @Test
    public void testMaxHeights() {
        test(this::maxHeights);
    }
}
