package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 耍杂技的牛: https://www.acwing.com/problem/content/127/
 *
 * 农民约翰的 N 头奶牛（编号为 1..N）计划逃跑并加入马戏团，为此它们决定练习表演杂技。
 *
 * 奶牛们不是非常有创意，只提出了一个杂技表演：叠罗汉，表演时，奶牛们站在彼此的身上，形成一个高高的垂直堆叠。
 * 奶牛们正在试图找到自己在这个堆叠中应该所处的位置顺序。
 *
 * 这 N 头奶牛中的每一头都有着自己的重量 Wi 以及自己的强壮程度 Si。
 * 一头牛支撑不住的可能性取决于它头上所有牛的总重量（不包括它自己）减去它的身体强壮程度的值，现在称该数值为风险值，
 * 风险值越大，这只牛撑不住的可能性越高。
 *
 * 您的任务是确定奶牛的排序，使得所有奶牛的风险值中的最大值尽可能的小。
 *
 * 输入格式：
 * - 第一行输入整数 N，表示奶牛数量。
 * - 接下来 N 行，每行输入两个整数，表示牛的重量和强壮程度，第 i 行表示第 i 头牛的重量 Wi 以及它的强壮程度 Si。
 *
 * 输出格式：
 * - 输出一个整数，表示最大风险值的最小可能值。
 *
 * 数据范围
 * - 1 ≤ N ≤ 50000,
 * - 1 ≤ Wi ≤ 10,000,
 * - 1 ≤ Si ≤ 1,000,000,000
 *
 *
 * 例 1：
 * 输入：
 * 3
 * 10 3
 * 2 5
 * 3 3
 * 输出：
 * 2
 */
public class G038_CowAcrobats {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G038_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G038_output.txt");
    }

    /**
     * 严谨的证明参见：https://www.acwing.com/solution/content/845/
     */
    public void maxRisk() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        /*
        最下面的牛，要承担除了它之外其他所有牛的重量，我们要确保它的风险值在所有最下面的牛的选择中最小
        risk = sum - wi - si

        最下面的牛安排好后，其他的牛也是一样的道理
         */
        int weightSum = 0;
        int[][] ws = new int[n][2];
        for (int i = 0; i < n; i++) {
            int w = in.nextInt(), s = in.nextInt();
            ws[i][0] = w;
            ws[i][1] = s;
            weightSum += w;
        }
        Arrays.sort(ws, (a, b) -> b[0] + b[1] - a[0] - a[1]);

        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, weightSum - ws[i][0] - ws[i][1]);
            weightSum -= ws[i][0];
        }

        System.out.println(ans);
    }

    @Test
    public void testMaxRisk() {
        test(this::maxRisk);
    }
}
