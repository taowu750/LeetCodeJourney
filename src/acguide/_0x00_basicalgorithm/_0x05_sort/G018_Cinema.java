package acguide._0x00_basicalgorithm._0x05_sort;

import org.junit.jupiter.api.Test;
import util.datastructure.function.ToIntTriFunction;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Cinema: https://www.acwing.com/problem/content/105/
 *
 * 有 m 部正在上映的电影，每部电影的语音和字幕都采用不同的语言，用一个 int 范围内的整数来表示语言。
 * 有 n 个人相约一起去看其中一部电影，每个人只会一种语言，如果一个人能听懂电影的语音，他会很高兴；如果能看懂字幕，
 * 他会比较高兴；如果语音和字幕都不懂，他会不开心。
 *
 * 请你选择一部电影让这 n 个人一起看，使很高兴的人最多。若答案不唯一，则在此前提下再让比较高兴的人最多。
 *
 * 例 1：
 * 输入：
 * a=[2, 3, 2], b=[3, 2], c=[2,3]
 * 输出：
 * 2
 * 解释：
 * 输入a是每个人会的语言列表，输入b是每部电影的语音语言，输入c是每部电影的字幕语言
 * 输出表示选择了电影2
 *
 * 说明：
 * - 1 ≤ n,m ≤ 2 * 10^5
 * - 1 ≤ ai, bi, ci ≤ 10^9
 */
public class G018_Cinema {

    public static void test(ToIntTriFunction<int[], int[], int[]> method) {
        assertEquals(2, method.applyAsInt(new int[]{2, 3, 2}, new int[]{3, 2}, new int[]{2, 3}));
        assertEquals(5, method.applyAsInt(new int[]{7, 6, 1, 2, 7, 3, 9, 7, 7, 9},
                new int[]{2, 9, 6, 5, 9, 3, 10, 3, 1, 6}, new int[]{4, 6, 7, 9, 7, 4, 1, 9, 2, 5}));
    }

    public int chooseCinema(int[] a, int[] b, int[] c) {
        /*
        计算每种语言的人数，从最多人数语言开始，看看有没有这种语音的电影
        每种语言人的数量可能相同，一种语音的所有电影中可能包含多种字幕

        选择一门语言后，首先看看这门语言对应的语言电影，然后看看这门电影对应的字幕有多少人懂
        如果最大人数语言（要有对应的电影）不唯一，那每门语言都需要看看

        如果没有对应的语言电影，就只比较字幕了

        如果语言、字幕都没有对应的电影，就选最后一部


        要能够选择最多数量人的语言，还要支持按语言查找人数
        查找这个语言对应语音的电影，以及它们的字幕；查找语言对应字幕的电影
         */

        // 将人按语言分类并排序
        Map<Integer, Integer> lan2cnt = new HashMap<>(a.length / 2 + 1);
        for (int l : a) {
            lan2cnt.merge(l, 1, Integer::sum);
        }
        Lan[] lans = new Lan[lan2cnt.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : lan2cnt.entrySet()) {
            lans[i++] = new Lan(entry.getKey(), entry.getValue());
        }
        Arrays.sort(lans);

        // 记录每种语言对应语音的电影，以及字幕语言对应的电影
        Map<Integer, List<Integer>> yy2movies = new HashMap<>(b.length / 2 + 1);
        Map<Integer, List<Integer>> zm2movies = new HashMap<>(b.length / 2 + 1);
        for (i = 0; i < b.length; i++) {
            yy2movies.computeIfAbsent(b[i], k -> new ArrayList<>()).add(i + 1);
            zm2movies.computeIfAbsent(c[i], k -> new ArrayList<>()).add(i + 1);
        }

        int ans = 0;
        for (int l = lans.length - 1, r = lans.length; r > 0; r = l, l--) {
            while (l > 0 && lans[l - 1].cnt == lans[l].cnt) {
                l--;
            }
            // [l,r) 之间是人数相同的语言
            int maxZm = -1;
            for (int j = l; j < r; j++) {
                // 选择对应语言语音的电影
                for (int movie : yy2movies.getOrDefault(lans[j].lan, Collections.emptyList())) {
                    // 查找电影字幕对应的人数
                    int zmrs = lan2cnt.getOrDefault(c[movie - 1], 0);
                    // 选择较高兴最多人数的电影
                    if (zmrs > maxZm) {
                        maxZm = zmrs;
                        ans = movie;
                    }
                }
            }
            if (maxZm != -1) {
                break;
            }
        }
        // 没有对应语言的电影
        if (ans == 0) {
            for (int l = lans.length - 1, r = lans.length; r > 0; r = l, l--) {
                while (l > 0 && lans[l - 1].cnt == lans[l].cnt) {
                    l--;
                }
                // [l,r) 之间是人数相同的语言
                for (int j = l; j < r; j++) {
                    // 选择对应语言字幕的电影
                    for (int movie : zm2movies.getOrDefault(lans[j].lan, Collections.emptyList())) {
                        ans = movie;
                    }
                }
                if (ans != 0) {
                    break;
                }
            }
        }

        // 语音、字幕都不满足，就选最后的电影
        return ans != 0 ? ans : b.length;
    }

    static class Lan implements Comparable<Lan> {
        int lan;
        int cnt;

        public Lan(int lan, int cnt) {
            this.lan = lan;
            this.cnt = cnt;
        }


        @Override
        public int compareTo(Lan o) {
            return Integer.compare(cnt, o.cnt);
        }

        @Override
        public String toString() {
            return "(" + lan + "," + cnt + ")";
        }
    }

    @Test
    public void testChooseCinema() {
        test(this::chooseCinema);
    }
}
