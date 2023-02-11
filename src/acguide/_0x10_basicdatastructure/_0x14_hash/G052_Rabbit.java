package acguide._0x10_basicdatastructure._0x14_hash;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;
import util.algorithm.MathUtil;

import java.util.Scanner;

/**
 * 兔子与兔子：https://www.acwing.com/problem/content/140/
 *
 * 很久很久以前，森林里住着一群兔子。有一天，兔子们想要研究自己的 DNA 序列。
 *
 * 我们首先选取一个好长好长的 DNA 序列（小兔子是外星生物，DNA 序列可能包含 26 个小写英文字母）。
 * 然后我们每次选择两个区间，询问如果用两个区间里的 DNA 序列分别生产出来两只兔子，这两个兔子是否一模一样。
 *
 * 注意两个兔子一模一样只可能是他们的 DNA 序列一模一样。
 *
 * 输入格式:
 * - 第一行输入一个 DNA 字符串 S。
 * - 第二行一个数字 m，表示 m 次询问。
 * - 接下来 m 行，每行四个数字 l1,r1,l2,r2，分别表示此次询问的两个区间，注意字符串的位置从 1 开始编号。
 *
 * 输出格式:
 * - 对于每次询问，输出一行表示结果。
 * - 如果两只兔子完全相同输出 Yes，否则输出 No（注意大小写）。
 *
 * 数据范围:
 * - 1≤length(S),m≤1000000
 *
 *
 * 例 1：
 * 输入：
 * aabbaabb
 * 3
 * 1 3 5 7
 * 1 3 6 8
 * 1 2 1 2
 * 输出：
 * Yes
 * No
 * Yes
 */
public class G052_Rabbit {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x14_hash/data/G052_input.txt",
                "acguide/_0x10_basicdatastructure/_0x14_hash/data/G052_output.txt");
    }

    @Test
    public void test() {
        // 找一个大于字符串最大长度平方的质数：1000000000039
        System.out.println(MathUtil.ceilPrime(1000000L * 1000000L));
        // 找到平方不大于 Long 最大值的质数： 3037000493
        System.out.println(MathUtil.floorPrime((long) Math.sqrt(Long.MAX_VALUE)));
    }

    public static class HashStr {

        private final long[][] hashes;
        private final long[][] basePow;

        public HashStr(char[] s, int[] primes) {
            hashes = new long[primes.length][s.length+1];
            basePow = new long[primes.length][s.length+1];
            for (int i = 0; i < primes.length; i++) {
                int p = primes[i];
                /*
                0  c0  c0*p+c1  c0*p^2+c1*p+c2  ...
                1  p^1 p^2      p^3             ...

                hashes[i] 表示 s[0..i) 的哈希值
                basePow[i] 表示 p^i
                */
                basePow[i][0] = 1;
                for (int j = 0; j < s.length; j++) {
                    hashes[i][j+1] = hashes[i][j] * p + s[j] - 'a';
                    basePow[i][j+1] = basePow[i][j] * p;
                }
            }
        }

        public boolean eq(int l1, int r1, int l2, int r2) {
            if (r1 - l1 != r2 - l2) {
                return false;
            } else if (l1 == l2) {
                return true;
            }
            for (int i = 0; i < hashes.length; i++) {
                /*
                注意，如果要取模的话：
                (hashes[i][r1] - hashes[i][l1-1] * basePow[i][r1 - l1 + 1]) % m

                减法那里取模需要改成下面这样，负数取模会出错：
                (hashes[i][r1] - hashes[i][l1-1] * basePow[i][r1 - l1 + 1] + m) % m
                 */
                long hash1 = hashes[i][r1] - hashes[i][l1-1] * basePow[i][r1 - l1 + 1];
                long hash2 = hashes[i][r2] - hashes[i][l2-1] * basePow[i][r2 - l2 + 1];
                if (hash1 != hash2) {
                    return false;
                }
            }
            return true;
        }
    }

    public void findSameRabbit() {
        Scanner in = new Scanner(System.in);
        char[] s = in.nextLine().toCharArray();
        HashStr hash = new HashStr(s, new int[]{31});
        final int n = in.nextInt();
        for (int o = 0; o < n; o++) {
            int l1 = in.nextInt(), r1 = in.nextInt();
            int l2 = in.nextInt(), r2 = in.nextInt();
            if (hash.eq(l1, r1, l2, r2)) {
                System.out.println("Yes");
            } else {
                System.out.println("No");
            }
        }
    }

    @Test
    public void testFindSameRabbit() {
        test(this::findSameRabbit);
    }
}
