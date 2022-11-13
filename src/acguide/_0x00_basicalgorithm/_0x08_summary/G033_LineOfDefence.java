package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 防线：https://www.acwing.com/problem/content/122/
 *
 * 强大的怪兽达达在城堡周围布置了一条“不可越过”的坚固防线。防线由很多防具组成，这些防具分成了 N 组。
 *
 * 我们可以认为防线是一维的，那么每一组防具都分布在防线的某一段上，并且同一组防具是等距离排列的。
 * 也就是说，我们可以用三个整数 S， E 和 D 来描述一组防具，即这一组防具布置在防线的
 * S，S+D，S+2D，…，S+KD(K∈Z，S+KD≤E，S+(K+1)D>E)位置上。
 *
 * 如果防线的某个位置有偶数个防具，那么这个位置就是毫无破绽的(包括这个位置一个防具也没有的情况，因为 0 也是偶数)。
 * 只有有奇数个防具的位置有破绽，但是整条防线上也「最多只有一个」位置有奇数个防具。
 *
 * 请找出找到防线的破绽。
 *
 *
 * 输入格式：
 * - 输入文件的第一行是一个整数 T，表示有 T 组互相独立的测试数据。
 * - 每组数据的第一行是一个整数 N。
 * - 之后 N 行，每行三个整数 Si，Ei，Di，代表第 i 组防具的三个参数，数据用空格隔开。
 *
 * 输出格式：
 * - 对于每组测试数据，如果防线没有破绽，即所有的位置都有偶数个防具，输出一行 "There's no weakness."(不包含引号) 。
 * - 否则在一行内输出两个空格分隔的整数 P 和 C，表示在位置 P 有 C 个防具。当然 C 应该是一个奇数。
 *
 * 数据范围：
 * - 防具总数不多于10^8,
 * - Si ≤ Ei,
 * - 1 ≤ T ≤ 5,
 * - N ≤ 200000,
 * - 0 ≤ Si，Ei，Di ≤ 2^31−1
 *
 *
 * 例 1：
 * 输入：
 * 3
 * 2
 * 1 10 1
 * 2 10 1
 * 2
 * 1 10 1
 * 1 10 1
 * 4
 * 1 10 1
 * 4 4 1
 * 1 5 1
 * 6 10 1
 * 输出：
 * 1 1
 * There's no weakness.
 * 4 3
 */
public class G033_LineOfDefence {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G033_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G033_output.txt");
    }

    /**
     * 参见：https://www.acwing.com/solution/content/2545/
     */
    public void findWeak() {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        while (T-- > 0) {
            int n = in.nextInt();
            int[][] armors = new int[n][3];
            int end = 0;
            for (int i = 0; i < n; i++) {
                int s = in.nextInt(), e = in.nextInt(), d = in.nextInt();
                armors[i][0] = s;
                armors[i][1] = s + (e - s) / d * d;
                armors[i][2] = d;
                end = Math.max(end, armors[i][1]);
            }
            Arrays.sort(armors, (a, b) -> a[0] - b[0]);

            if (getSum(armors, end) % 2 == 0) {
                System.out.println("There's no weakness.");
            } else {
                int lo = armors[0][0], hi = end;
                while (lo < hi) {
                    int mi = (int) (((long) lo + hi) / 2);
                    // 计算 s<=mi 区间的防具数量
                    int sum = getSum(armors, mi);
                    if (sum % 2 == 0) {
                        lo = mi + 1;
                    } else {
                        hi = mi;
                    }
                }
                System.out.println(lo + " " + (getSum(armors, lo) - getSum(armors, lo - 1)));
            }
        }
    }

    int bsr(int[][] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mi = (lo + hi) / 2;
            if (x >= a[mi][0]) {
                lo = mi + 1;
            } else {
                hi = mi;
            }
        }

        return lo;
    }

    int getSum(int[][] armors, int mi) {
        int sum = 0, limit = bsr(armors, mi);
        for (int i = 0; i < limit; i++) {
            sum += (Math.min(armors[i][1], mi) - armors[i][0]) / armors[i][2] + 1;
        }

        return sum;
    }

    @Test
    public void testFindWeak() {
        test(this::findWeak);
    }
}
