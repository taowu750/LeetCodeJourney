package company.baidu;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * C市现在要转移一批罪犯到D市，C市有n名罪犯，按照入狱时间有顺序，另外每个罪犯有一个罪行值，值越大罪越重。
 *
 * 现在为了方便管理，市长决定转移入狱时间连续的c名犯人，同时要求转移犯人的罪行值之和不超过t，问有多少种选择的方式
 * （一组测试用例可能包含多组数据，请注意处理）？
 *
 * 输入描述：
 * - 第一行数据三个整数: n，t，c (1≤n≤2e5, 0≤t≤1e9, 1≤c≤n)
 * - 第二行按入狱时间给出每个犯人的罪行值 ai (0≤ai≤1e9)
 *
 * 输出描述：
 * - 一行输出答案。
 *
 * 例 1：
 * 输入:
 * 3 100 2
 * 1 2 3
 * 输出:
 * 2
 */
public class BD1_OffenderTransfer {

    static void test(Runnable method) {
        StdIOTestUtil.test(method, "company/baidu/data/bd1_input.txt",
                "company/baidu/data/bd1_expect.txt");
    }

    /**
     * 牛客网耗时：443ms - 38.88%
     *      内存消耗：36960KB - 29.14%
     */
    public void offenderTransfer() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            // 至少有一个犯人，上限至少为 0，至少转移一个犯人，罪行值至少为 0
            int n = in.nextInt(), t = in.nextInt(), c = in.nextInt();
            int[] offender = new int[n];
            for (int i = 0; i < n; i++)
                offender[i] = in.nextInt();

            int cnt = 0, subSum = 0;
            for (int i = 0; i < c - 1; i++)
                subSum += offender[i];
            for (int i = c - 1; i < n; i++) {
                subSum += offender[i];
                if (subSum <= t)
                    cnt++;
                subSum -= offender[i - c + 1];
            }
            System.out.println(cnt);
        }
    }

    @Test
    public void testOffenderTransfer() {
        test(this::offenderTransfer);
    }
}
