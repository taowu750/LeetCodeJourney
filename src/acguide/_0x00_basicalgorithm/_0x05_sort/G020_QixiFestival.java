package acguide._0x00_basicalgorithm._0x05_sort;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 七夕祭：https://www.acwing.com/problem/content/107/
 *
 * 七夕节因牛郎织女的传说而被扣上了“情人节”的帽子。于是 TYVI 今年举办了一次线下七夕祭。Vani 同学今年成功邀请到了 cl 同学陪他来共度七夕，
 * 于是他们决定去 TYVJ 七夕祭游玩。
 *
 * TYVI 七夕祭和11 区的夏祭的形式很像。矩形的祭典会场由 N 行 M 列共计 N * M 个摊点组成。虽然摊点种类繁多，
 * 不过 cl 只对其中的 T 个摊点感兴趣，比如章鱼烧、苹果糖、棉花糖、射的屋什么的。Vani 预先联系了七夕祭的负责人 zhq,
 * 希望能够通过恰当地布置会场，使得各行中 cl 感兴趣的摊点数一样多，并且各列中 cl 感兴趣的摊点数也一样多。
 *
 * 不过 zhq 告诉 Vani, 摊点已经随意布置完毕了，如果想满足 cl 的要求，唯一的调整方式就是交换两个相邻的摊点。
 * 两个摊点相邻，当且仅当它们处在同一行或者同一列的相邻位置上。因为 zhq 率领的 TYVI 开发小组成功地扭曲了空间，
 * 每一行或每一列的第一个位置和最后一个位置也算作相邻。现在 Vani 想知道他的两个要求最多能满足多少个。在此前提下，
 * 至少需要交换多少次摊点。
 *
 * 输出格式：
 * - 如果能满足 Vani 的全部两个要求，输出 both；
 * - 如果通过调整只能使得各行中 cl 感兴趣的摊点数一样多，输出 row；
 * - 如果只能使各列中 cl 感兴趣的摊点数一样多，输出 column；
 * - 如果均不能满足，输出 impossible。
 * - 如果输出的字符串不是 impossible， 接下来输出最小交换次数，与字符串之间用一个空格隔开。
 *
 * 例 1：
 * 输入：
 * N=2, M=3, interest=[[1,3],[2,1],[2,2],[2,3]]
 * 输出：
 * row 1
 * 说明：
 * interest的坐标从 1 开始
 *
 * 说明：
 * - 1 ≤ N,M ≤ 10^5
 * - 0 ≤ T ≤ min(N*M, 10^5)
 */
public class G020_QixiFestival {

    public static class Answer {
        public final String satisfy;
        public final long exch;

        public Answer(String satisfy, long exch) {
            this.satisfy = satisfy;
            this.exch = exch;
        }

        public Answer() {
            satisfy = "impossible";
            exch = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Answer)) return false;
            Answer answer = (Answer) o;
            return exch == answer.exch && Objects.equals(satisfy, answer.satisfy);
        }

        @Override
        public int hashCode() {
            return Objects.hash(satisfy, exch);
        }

        @Override
        public String toString() {
            if (!satisfy.equals("impossible")) {
                return satisfy + " " + exch;
            } else {
                return satisfy;
            }
        }
    }

    public static void test(TriFunction<Integer, Integer, int[][], Answer> method) {
        assertEquals(new Answer("row", 1), method.apply(2, 3, new int[][]{{1,3}, {2,1}, {2,2}, {2,3}}));
    }

    public Answer arrange(int n, int m, int[][] interests) {
        int num = interests.length;
        if (num % n != 0 && num % m != 0) {
            return new Answer();
        }

        String satisfy = "both";
        if (num % n != 0) {
            satisfy = "column";
        } else if (num % m != 0) {
            satisfy = "row";
        }

        long exch = 0;
        List<int[]> indices = new ArrayList<>(2);
        if (satisfy.equals("row") || satisfy.equals("both")) {
            indices.add(new int[]{0, n});
        }
        if (satisfy.equals("column") || satisfy.equals("both")) {
            indices.add(new int[]{1, m});
        }
        for (int[] idxAndLen : indices) {
            int idx = idxAndLen[0], len = idxAndLen[1];
            // 计算当前每行/列的摊点数
            int[] seqs = new int[len];
            for (int[] interest : interests) {
                seqs[interest[idx] - 1]++;
            }
            // 计算减去平均值的前缀和
            for (int i = 0; i < len; i++) {
                seqs[i] -= num / len;
            }
            for (int i = 1; i < len; i++) {
                seqs[i] += seqs[i - 1];
            }
            // 然后应用“货仓选址”算法
            Arrays.sort(seqs);
            int mid = seqs[(len + 1) / 2 - 1];
            for (int i = 0; i < len; i++) {
                exch += Math.abs(seqs[i] - mid);
            }
        }

        return new Answer(satisfy, exch);
    }

    @Test
    public void testArrange() {
        test(this::arrange);
    }
}
