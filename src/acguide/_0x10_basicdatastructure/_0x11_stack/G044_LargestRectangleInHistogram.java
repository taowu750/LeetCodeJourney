package acguide._0x10_basicdatastructure._0x11_stack;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * 直方图中最大的矩形: https://www.acwing.com/problem/content/133/
 *
 * 直方图是由在公共基线处对齐的一系列矩形组成的多边形。矩形具有相等的宽度，但可以具有不同的高度。
 * 例如，图例左侧显示了由高度为 2,1,4,5,1,3,3 的矩形组成的直方图，矩形的宽度都为 1
 *
 * 现在，请你计算在公共基线处对齐的直方图中最大矩形的面积。图例右图显示了所描绘直方图的最大对齐矩形。
 *
 * 输入格式：
 * - 输入包含几个测试用例。每个测试用例占据一行，用以描述一个直方图，并以整数 n 开始，表示组成直方图的矩形数目。
 * - 然后跟随 n 个整数 h1，…，hn。这些数字以从左到右的顺序表示直方图的各个矩形的高度。
 * - 每个矩形的宽度为 1。
 * - 同行数字用空格隔开。
 * - 当输入用例为 n=0 时，结束输入，且该用例不用考虑。
 *
 * 输出格式：
 * - 对于每一个测试用例，输出一个整数，代表指定直方图中最大矩形的区域面积。
 * - 每个数据占一行。
 * - 请注意，此矩形必须在公共基线处对齐。
 *
 * 数据范围：
 * - 1 ≤ n ≤ 100000,
 * - 0 ≤ hi ≤ 1000000000
 *
 *
 * 例 1：
 * 输入：
 * 7 2 1 4 5 1 3 3
 * 4 1000 1000 1000 1000
 * 0
 * 输出：
 * 8
 * 4000
 */
public class G044_LargestRectangleInHistogram {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x11_stack/data/G044_input.txt",
                "acguide/_0x10_basicdatastructure/_0x11_stack/data/G044_output.txt");
    }

    public void rectangleArea() {
        Scanner in = new Scanner(System.in);
        for (;;) {
            int n = in.nextInt();
            if (n == 0) {
                break;
            }

            int[] heights = new int[n];
            for (int i = 0; i < n; i++) {
                heights[i] = in.nextInt();
            }

            // 单增栈，由于找上一个小于的元素
            int[] stack = new int[n];
            int top = -1;
            // leftLess[i] = 左边大于 i 的连续序列长度
            int[] leftLess = new int[n];
            for (int i = 0; i < n; i++) {
                while (top != -1 && heights[stack[top]] >= heights[i]) {
                    top--;
                }
                if (top == -1) {
                    leftLess[i] = i;
                } else {
                    leftLess[i] = i - stack[top] - 1;
                }
                stack[++top] = i;
            }

            // 找右边大于 i 的连续序列长度，并和 leftLess 合并得到最终长度
            top = -1;
            long ans = 0;
            for (int i = n - 1; i >= 0; i--) {
                while (top != -1 && heights[stack[top]] >= heights[i]) {
                    top--;
                }
                if (top == -1) {
                    ans = Math.max(ans, (leftLess[i] + n - i) * (long) heights[i]);
                } else {
                    ans = Math.max(ans, (leftLess[i] + stack[top] - i) * (long) heights[i]);
                }
                stack[++top] = i;
            }

            System.out.println(ans);
        }
    }

    @Test
    public void testRectangleArea() {
        test(this::rectangleArea);
    }


    /**
     * 书上的方法
     */
    public void bookMethod() {
        Scanner in = new Scanner(System.in);
        for (;;) {
            int n = in.nextInt();
            if (n == 0) {
                break;
            }

            int[] heights = new int[n + 1];
            for (int i = 0; i < n; i++) {
                heights[i] = in.nextInt();
            }

            // 单增栈，记录高度和宽度
            int[][] stack = new int[n][2];
            int top = -1;
            long ans = 0;
            for (int i = 0; i <= n; i++) {
                int width = 0;
                while (top != -1 && stack[top][0] >= heights[i]) {
                    width += stack[top][1];
                    ans = Math.max(ans, (long) stack[top][0] * width);
                    top--;
                }
                stack[++top][0] = heights[i];
                stack[top][1] = width + 1;
            }

            System.out.println(ans);
        }
    }

    @Test
    public void testBookMethod() {
        test(this::bookMethod);
    }


    public void betterMethod() {
        Scanner in = new Scanner(System.in);
        while (true) {
            int n = in.nextInt();
            if (n == 0) {
                break;
            }

            int[] heights = new int[n + 1];
            for (int i = 0; i < n; i++) {
                heights[i] = in.nextInt();
            }

            // 单调栈存储下标，从而不用存储宽度
            int[] stack = new int[n];
            int top = -1;
            long ans = 0;
            for (int i = 0; i <= n; i++) {
                while (top != -1 && heights[stack[top]] >= heights[i]) {
                    int idx = stack[top--];
                    if (top == -1) {
                        ans = Math.max(ans, (long) i * heights[idx]);
                    } else {
                        ans = Math.max(ans, (long) (i - stack[top] - 1) * heights[idx]);
                    }
                }
                stack[++top] = i;
            }

            System.out.println(ans);
        }
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
