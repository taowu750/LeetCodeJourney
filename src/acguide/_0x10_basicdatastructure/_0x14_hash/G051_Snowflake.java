package acguide._0x10_basicdatastructure._0x14_hash;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;
import util.algorithm.MathUtil;

import java.util.Scanner;

/**
 * 雪花：https://www.acwing.com/problem/content/139/
 *
 * 有 N 片雪花，每片雪花由六个角组成，每个角都有长度。
 * 第 i 片雪花六个角的长度从某个角开始顺时针依次记为 ai,1,ai,2,…,ai,6。
 *
 * 因为雪花的形状是封闭的环形，所以从任何一个角开始顺时针或逆时针往后记录长度，得到的六元组都代表形状相同的雪花。
 *
 * 例如 ai,1,ai,2,…,ai,6 和 ai,2,ai,3,…,ai,6，ai,1 就是形状相同的雪花。
 * ai,1,ai,2,…,ai,6 和 ai,6,ai,5,…,ai,1 也是形状相同的雪花。
 *
 * 我们称两片雪花形状相同，当且仅当它们各自从某一角开始顺时针或逆时针记录长度，能得到两个相同的六元组。
 * 求这 N 片雪花中是否存在两片形状相同的雪花。
 *
 * 输入格式:
 * - 第一行输入一个整数 N，代表雪花的数量。
 * - 接下来 N 行，每行描述一片雪花。
 * - 每行包含 6 个整数，分别代表雪花的六个角的长度（这六个数即为从雪花的随机一个角顺时针或逆时针记录长度得到）。
 * - 同行数值之间，用空格隔开。
 *
 * 输出格式:
 * - 如果不存在两片形状相同的雪花，则输出：
 *   No two snowflakes are alike.
 * - 如果存在两片形状相同的雪花，则输出：
 *   Twin snowflakes found.
 *
 * 数据范围:
 * - 1≤N≤100000,
 * - 0≤ai,j<10000000
 *
 *
 * 例 1：
 * 输入：
 * 2
 * 1 2 3 4 5 6
 * 4 3 2 1 6 5
 * 输出：
 * Twin snowflakes found.
 */
public class G051_Snowflake {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x14_hash/data/G051_input.txt",
                "acguide/_0x10_basicdatastructure/_0x14_hash/data/G051_output.txt");
    }

    public static class SnowflakeHash {
        private final int[][] snowflakes;
        private final int[] head, next;
        private final int prime;
        private int size;

        public SnowflakeHash(int n) {
            snowflakes = new int[n+1][];
            head = new int[n+1];
            next = new int[n+1];
            prime = (int) MathUtil.floorPrime(n+1);
        }

        public boolean insert(int[] snowflake) {
            int hv = hash(snowflake);
            for (int i = head[hv]; i != 0; i = next[i]) {
                if (check(snowflakes[i], snowflake)) {
                    return true;
                }
            }

            // size 是不断增长的，所以往 next 里面存的都是不一样的值，不会出现冲突
            // head 下标是 hash 值，里面存的还是 size
            snowflakes[++size] = snowflake;
            // 头插法
            next[size] = head[hv];
            head[hv] = size;

            return false;
        }

        private boolean check(int[] a, int [] b) {
            final int len = a.length;
            // 轮询 a 的每个位置做开头
            for (int i = 0; i < len; i++) {
                boolean equal = true;
                // 让 a 和顺时针的 b 比较
                for (int j = 0; j < len; j++) {
                    if (a[(i + j) % len] != b[j]) {
                        equal = false;
                        break;
                    }
                }
                if (equal) {
                    return true;
                }

                equal = true;
                // 让 a 和逆时针的 b 比较
                for (int j = 0; j < len; j++) {
                    if (a[(i + j) % len] != b[len - 1 - j]) {
                        equal = false;
                        break;
                    }
                }
                if (equal) {
                    return true;
                }
            }

            return false;
        }

        private int hash(int[] snowflake) {
            int sum = 0;
            long product = 1;
            for (int i : snowflake) {
                sum = (sum + i) % prime;
                product = (product * i) % prime;
            }
            return (int) ((sum + product) % prime);
        }
    }

    public void sameSnowflake() {
        Scanner in = new Scanner(System.in);
        final int N = in.nextInt();
        SnowflakeHash hash = new SnowflakeHash(N);
        for (int i = 0; i < N; i++) {
            int[] snowflake = new int[6];
            for (int j = 0; j < 6; j++) {
                snowflake[j] = in.nextInt();
            }
            if (hash.insert(snowflake)) {
                System.out.println("Twin snowflakes found.");
                return;
            }
        }
        System.out.println("No two snowflakes are alike.");
    }

    @Test
    public void testSameSnowflake() {
        test(this::sameSnowflake);
    }
}
