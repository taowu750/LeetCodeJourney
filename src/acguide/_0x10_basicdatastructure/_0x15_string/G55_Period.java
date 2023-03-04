package acguide._0x10_basicdatastructure._0x15_string;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * 周期：https://www.acwing.com/problem/content/143/
 *
 * 如果一个字符串 S 是由一个字符串 T 重复 K 次形成的，则称 K 是 S 的循环元。使 K 最大的字符串 T 称为 S 的最小循环元，
 * 此时的 K 称为最大循环次数。
 *
 * 我们希望知道一个 N 位字符串 S，对于每一个从头开始的长度为 i(i>1) 的前缀，前缀是否具有最小循环元。
 *
 * 输入格式:
 * - 输入包括多组测试数据，每组测试数据包括两行。
 * - 第一行输入字符串 S 的长度 N。
 * - 第二行输入字符串 S。
 * - 输入数据以只包括一个 0 的行作为结尾。
 *
 * 输出格式:
 * - 对于每组测试数据，第一行输出 Test case # 和测试数据的编号。
 * - 接下来的每一行，输出具有循环节的前缀的长度 i 和其对应 K，中间用一个空格隔开。
 * - 前缀长度需要升序排列。
 * - 在每组测试数据的最后输出一个空行。
 *
 * 数据范围:
 * - 2≤N≤1000000
 *
 *
 * 例 1：
 * 输入：
 * 3
 * aaa
 * 4
 * abcd
 * 12
 * aabaabaabaab
 * 0
 * 输出：
 * Test case #1
 * 2 2
 * 3 3
 *
 * Test case #2
 *
 * Test case #3
 * 2 2
 * 6 2
 * 9 3
 * 12 4
 *
 */
public class G55_Period {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x15_string/data/G055_input.txt",
                "acguide/_0x10_basicdatastructure/_0x15_string/data/G055_output.txt");
    }

    public void findCycleMetas() {
        Scanner in = new Scanner(System.in);
        for (int len = in.nextInt(), caseNum = 1; len != 0; len = in.nextInt()) {
            System.out.println("Test case #" + caseNum++);
            in.nextLine();
            char[] s = in.nextLine().toCharArray();
            int[] next = findNext(s);
            for (int i = 1; i < len; i++) {
                int l = i + 1, meta = l - next[i];
                if (next[i] > 0 && next[i] % meta == 0) {
                    System.out.println(l + " " + l / meta);
                }
            }
            System.out.println();
        }
    }

    public static int[] findNext(char[] s) {
        final int n = s.length;
        int[] next = new int[n];
        for (int t = 1, nxt = 0; t < n;) {
            if (s[t] == s[nxt]) {
                next[t++] = ++nxt;
            } else if (nxt != 0) {
                nxt = next[nxt - 1];
            } else {
                t++;
            }
        }

        return next;
    }

    @Test
    public void testFindCycleMetas() {
        test(this::findCycleMetas);
    }
}
