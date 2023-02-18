package acguide._0x10_basicdatastructure._0x14_hash;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 后缀数组: https://www.acwing.com/problem/content/142/
 *
 * 后缀数组 (SA) 是一种重要的数据结构，通常使用倍增或者 DC3 算法实现，这超出了我们的讨论范围。
 * 在本题中，我们希望使用快排、Hash 与二分实现一个简单的 O(nlog2n) 的后缀数组求法。
 *
 * 详细地说，给定一个长度为 n 的字符串 S（下标 0∼n−1），我们可以用整数 k(0≤k<n) 表示字符串 S 的后缀 S(k∼n−1)。
 * 把字符串 S 的所有后缀按照字典序排列，排名为 i 的后缀记为 SA[i]。
 * 额外地，我们考虑排名为 i 的后缀与排名为 i−1 的后缀，把二者的最长公共前缀的长度记为 Height[i]。
 *
 * 我们的任务就是求出 SA 与 Height 这两个数组。
 *
 * 输入格式：
 * - 输入一个字符串，其长度不超过 30 万。
 * - 字符串由小写字母构成。
 *
 * 输出格式：
 * - 第一行为数组 SA，相邻两个整数用 1 个空格隔开。
 * - 第二行为数组 Height，相邻两个整数用 1 个空格隔开，我们规定 Height[1]=0。
 *
 *
 * 例 1：
 * 输入：
 * ponoiiipoi
 * 输出：
 * 9 4 5 6 2 8 3 1 7 0
 * 0 1 2 1 0 0 2 1 0 2
 */
public class G054_SuffixArray {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x14_hash/data/G054_input.txt",
                "acguide/_0x10_basicdatastructure/_0x14_hash/data/G054_output.txt");
    }

    public static class Suffix implements Comparable<Suffix> {
        public final int k;
        private final char[] s;
        private final Hash hash;

        public Suffix(int k, char[] s, Hash hash) {
            this.k = k;
            this.s = s;
            this.hash = hash;
        }

        @Override
        public int compareTo(Suffix o) {
            int len = maxPrefixLen(o);
            if (len == len()) {
                return o.len() > len ? -1 : 0;
            } else if (len == o.len()) {
                return len() > len ? 1 : 0;
            } else {
                return s[k + len] - s[o.k + len];
            }
        }

        public int len() {
            return s.length - k;
        }

        public int maxPrefixLen(Suffix o) {
            int lo = 0, hi = Math.min(len(), o.len());
            while (lo < hi) {
                int mi = (lo + hi + 1) >>> 1;
                if (hash.hash(k + 1, k + mi) ==
                        hash.hash(o.k + 1, o.k + mi)) {
                    lo = mi;
                } else {
                    hi = mi - 1;
                }
            }
            return lo;
        }
    }

    public static class Hash {
        private final long[] hashes;
        private final long[] pow;
        private final int p;

        public Hash(char[] s, int p) {
            hashes = new long[s.length + 1];
            pow = new long[s.length + 1];
            pow[0] = 1;
            this.p = p;
            for (int i = 0; i < s.length; i++) {
                hashes[i + 1] = hashes[i] * p + s[i] - 'a';
                pow[i + 1] = pow[i] * p;
            }
        }

        // l、r 从 1 开始
        public long hash(int l, int r) {
            return hashes[r] - hashes[l - 1] * pow[r - l + 1];
        }
    }

    public void suffixArray() {
        Scanner in = new Scanner(System.in);
        char[] s = in.nextLine().toCharArray();
        if (s.length == 0) {
            return;
        } else if (s.length == 1) {
            System.out.println("0");
            return;
        }

        Hash hash = new Hash(s, 31);
        Suffix[] suffixes = new Suffix[s.length];
        for (int i = 0; i < s.length; i++) {
            suffixes[i] = new Suffix(i, s, hash);
        }
        Arrays.parallelSort(suffixes);
        StringBuilder sb = new StringBuilder(suffixes.length * 6);
        sb.append(suffixes[0].k);
        for (int i = 1; i < suffixes.length; i++) {
            sb.append(' ').append(suffixes[i].k);
        }
        System.out.println(sb);

        sb.setLength(0);
        sb.append('0');
        for (int i = 1; i < suffixes.length; i++) {
            sb.append(' ').append(suffixes[i-1].maxPrefixLen(suffixes[i]));
        }
        System.out.println(sb);
    }

    @Test
    public void testSuffixArray() {
        test(this::suffixArray);
    }
}
