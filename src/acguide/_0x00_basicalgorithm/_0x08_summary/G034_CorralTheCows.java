package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.ArrayUtil;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 围住奶牛：https://ac.nowcoder.com/acm/contest/1004
 *
 * 农夫约翰希望为他的奶牛建一个畜栏。要求畜栏是方形的，并且畜栏至少包含C（1≤C≤500）块三叶草地，供下午享用。
 * 畜栏的边缘必须与X、Y轴平行，边长得是整数，不过它的边缘不需要处于整数位置。
 *
 * 约翰的土地总共包含N（C≤N≤500）个三叶草地，每个块大小为1 x 1，其坐标 X 和 Y 在 1…10000 范围内。
 * 有时，一个以上的三叶草地在同一位置；这样的草地的位置会在输入中出现两次（或更多）。
 * 三叶草田不能在畜栏边界上，必须在畜栏内部。
 *
 * 帮助约翰，告诉他包含 C 个三叶草地的最小正方形的边长。
 *
 *
 * 输入格式：
 * - 第1行：两个空格分隔的整数：C和N
 * - 第2..N+1行：每行包含两个空格分隔的整数，它们是三叶草地的X、Y坐标。
 *
 * 输出格式：
 * - 正方形畜栏的边长
 *
 *
 * 例 1：
 * 输入：
 * 3 4
 * 1 2
 * 2 1
 * 4 1
 * 5 2
 * 输出：
 * 4
 */
public class G034_CorralTheCows {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G034_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G034_output.txt");
    }

    public void corralLength() {
        Scanner in = new Scanner(System.in);
        int C = in.nextInt(), N = in.nextInt();
        int[][] pos = new int[N][2];
        int minX = 100000, maxX = 0, minY = 100000, maxY = 0;
        for (int i = 0; i < N; i++) {
            pos[i][0] = in.nextInt();
            pos[i][1] = in.nextInt();
            minX = Math.min(minX, pos[i][0]);
            maxX = Math.max(maxX, pos[i][0]);
            minY = Math.min(minY, pos[i][1]);
            maxY = Math.max(maxY, pos[i][1]);
        }
        int maxLen = Math.max(maxX - minX, maxY - minY) + 1;
        if (C == 1) {
            System.out.println(1);
            return;
        } else if (C == N) {
            System.out.println(maxLen);
            return;
        }

        // 计算离散化的 y
        Arrays.sort(pos, (a, b) -> a[1] - b[1]);
        int[] ys = new int[N];
        ys[0] = pos[0][1];
        int ysLen = 1;
        for (int i = 1; i < N; i++) {
            if (pos[i][1] != pos[i - 1][1]) {
                ys[ysLen++] = pos[i][1];
            }
        }

        // 计算离散化的 x
        Arrays.sort(pos, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
        int[] xs = new int[N];
        xs[0] = pos[0][0];
        int xsLen = 1;
        for (int i = 1; i < N; i++) {
            if (pos[i][0] != pos[i - 1][0]) {
                xs[xsLen++] = pos[i][0];
            }
        }

        int[][] prefix = new int[xsLen + 1][ysLen + 1];
        for (int i = 1; i <= xsLen; i++) {
            for (int j = 1; j <= ysLen; j++) {
                int x = xs[i - 1], y = ys[j - 1];
                prefix[i][j] = prefix[i - 1][j] + prefix[i][j - 1] - prefix[i - 1][j - 1] +
                        bsCnt(pos, x, y);
            }
        }

        int lo = 1, hi = maxLen;
        while (lo < hi) {
            int mi = (lo + hi) / 2;

            boolean can = false;
            for (int i = 1; i <= xsLen; i++) {
                for (int j = 1; j <= ysLen; j++) {
                    int x = xs[i - 1], y = ys[j - 1];
                    int xEnd = ArrayUtil.bsr(xs, i - 1, xsLen, x + mi - 1);
                    int yEnd = ArrayUtil.bsr(ys, j - 1, ysLen, y + mi - 1);
                    int sum = prefix[xEnd][yEnd] - prefix[xEnd][j - 1] - prefix[i - 1][yEnd] + prefix[i - 1][j - 1];
                    if (sum >= C) {
                        can = true;
                        break;
                    }
                }
            }
            if (can) {
                hi = mi;
            } else {
                lo = mi + 1;
            }
        }
        System.out.println(lo);
    }

    int bsCnt(int[][] pos, int x, int y) {
        // 找出 x 的左右边界
        int lo = 0, hi = pos.length;
        while (lo < hi) {
            int mi = (lo + hi) / 2;
            if (x <= pos[mi][0]) {
                hi = mi;
            } else {
                lo = mi + 1;
            }
        }
        int xL = lo;

        lo = 0;
        hi = pos.length;
        while (lo < hi) {
            int mi = (lo + hi) / 2;
            if (x >= pos[mi][0]) {
                lo = mi + 1;
            } else {
                hi = mi;
            }
        }
        int xR = lo;

        // 找到 y 的位置
        lo = xL;
        hi = xR;
        while (lo < hi) {
            int mi = (lo + hi) / 2;
            if (y <= pos[mi][1]) {
                hi = mi;
            } else {
                lo = mi + 1;
            }
        }

        int sum = 0;
        // 可能一块田都没有，也可能有多块田
        while (lo < xR && pos[lo][0] == x && pos[lo][1] == y) {
            sum++;
            lo++;
        }

        return sum;
    }

    @Test
    public void testCorralLength() {
        test(this::corralLength);
    }
}
