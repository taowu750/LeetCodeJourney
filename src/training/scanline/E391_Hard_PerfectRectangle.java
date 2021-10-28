package training.scanline;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 391. 完美矩形: https://leetcode-cn.com/problems/perfect-rectangle/
 *
 * 我们有 N 个与坐标轴对齐的矩形, 其中 N > 0, 判断它们是否能精确地覆盖一个矩形区域。
 *
 * 每个矩形用左下角的点和右上角的点的坐标来表示。例如，一个单位正方形可以表示为 [1,1,2,2]。
 * ( 左下角的点的坐标为 (1, 1) 以及右上角的点的坐标为 (2, 2) )。
 *
 * 例 1：
 * rectangles = [
 *   [1,1,3,3],
 *   [3,1,4,2],
 *   [3,2,4,4],
 *   [1,3,2,4],
 *   [2,3,3,4]
 * ]
 * 图片见链接。
 * 返回 true。5个矩形一起可以精确地覆盖一个矩形区域。
 *
 * 例 2：
 * rectangles = [
 *   [1,1,2,3],
 *   [1,3,2,4],
 *   [3,1,4,2],
 *   [3,2,4,4]
 * ]
 * 返回 false。两个矩形之间有间隔，无法覆盖成一个矩形。
 *
 * 例 3：
 * rectangles = [
 *   [1,1,3,3],
 *   [3,1,4,2],
 *   [1,3,2,4],
 *   [3,2,4,4]
 * ]
 * 返回 false。图形顶端留有间隔，无法覆盖成一个矩形。
 *
 * 例 4：
 * rectangles = [
 *   [1,1,3,3],
 *   [3,1,4,2],
 *   [1,3,2,4],
 *   [2,2,4,4]
 * ]
 * 返回 false。因为中间有相交区域，虽然形成了矩形，但不是精确覆盖。
 */
public class E391_Hard_PerfectRectangle {

    static void test(Predicate<int[][]> method) {
        assertTrue(method.test(new int[][] {
                {1, 1, 3, 3},
                {3, 1, 4, 2},
                {3, 2, 4, 4},
                {1, 3, 2, 4},
                {2, 3, 3, 4}
        }));

        assertFalse(method.test(new int[][]{
                {1, 1, 2, 3},
                {1, 3, 2, 4},
                {3, 1, 4, 2},
                {3, 2, 4, 4}
        }));

        assertFalse(method.test(new int[][]{
                {1,1,3,3},
                {3,1,4,2},
                {1,3,2,4},
                {3,2,4,4}
        }));

        assertFalse(method.test(new int[][]{
                {1, 1, 3, 3},
                {3, 1, 4, 2},
                {1, 3, 2, 4},
                {2, 2, 4, 4}
        }));

        assertFalse(method.test(new int[][]{
                {0, 0, 1, 1},
                {0, 1, 3, 2},
                {1, 0, 2, 2}
        }));
    }

    /**
     * LeetCode 耗时：944 ms - 5.56%
     *          内存消耗：47.3 MB - 46.18%
     */
    public boolean isRectangleCover(int[][] rectangles) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = 0, maxY = 0;
        int actualArea = 0;
        // 首先判断能包围它们的矩形面积是否相等
        for (int[] rectangle : rectangles) {
            int min = Math.min(rectangle[0], rectangle[2]),
                    max = Math.max(rectangle[0], rectangle[2]);
            if (minX > min) {
                minX = min;
            }
            if (maxX < max) {
                maxX = max;
            }

            min = Math.min(rectangle[1], rectangle[3]);
            max = Math.max(rectangle[1], rectangle[3]);
            if (minY > min) {
                minY = min;
            }
            if (maxY < max) {
                maxY = max;
            }

            actualArea += (rectangle[2] - rectangle[0]) * (rectangle[3] - rectangle[1]);
        }

        // 不相等则不是精确覆盖
        if (actualArea != (maxX - minX) * (maxY - minY)) {
            return false;
        }

        // 再判断矩形之间是否相交
        int n = rectangles.length;
        for (int i = 0; i < n - 1; i++) {
            int[] ri = rectangles[i];
            for (int j = i + 1; j < n; j++) {
                int[] rj = rectangles[j];
                /*
                思路来自：https://blog.csdn.net/qianchenglenger/article/details/50484053

                设 ri(p1, p2)，rj(p3, p4)，其中 p=(x,y)。
                我们逆向思考，不妨先解决出“不重叠”的情况。也就是：
                - ri 在 rj 上边：p1.y >= p4.y
                - ri 在 rj 下边：p2.y <= p3.y
                - ri 在 rj 左边：p2.x <= p3.x
                - ri 在 rj 右边：p1.x >= p4.x

                那么重叠的情况就是上述的相反情况：
                !((p1.y >= p4.y) || (p2.y <= p3.y) || (p2.x <= p3.x) || (p1.x >= p4.x))
                根据德摩根定律，可简化为：
                (p1.y < p4.y) && (p2.y > p3.y) && (p2.x > p3.x) && (p1.x < p4.x)
                 */
                if ((ri[1] < rj[3]) && (ri[3] > rj[1])
                        && (ri[2] > rj[0]) && (ri[0] < rj[2])) {
                    return false;
                }
            }
        }

        return true;
    }

    @Test
    public void testIsRectangleCover() {
        test(this::isRectangleCover);
    }


    /**
     * 参见 https://labuladong.gitee.io/algo/5/41/
     *
     * LeetCode 耗时：26 ms - 97.57%
     *          内存消耗：47.5 MB - 37.85%
     */
    public boolean betterMethod(int[][] rectangles) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = 0, maxY = 0;
        int actualArea = 0;
        Set<Pair<Integer, Integer>> vetexes = new HashSet<>();
        // 首先判断能包围它们的矩形面积是否相等
        for (int[] rectangle : rectangles) {
            int min = Math.min(rectangle[0], rectangle[2]),
                    max = Math.max(rectangle[0], rectangle[2]);
            if (minX > min) {
                minX = min;
            }
            if (maxX < max) {
                maxX = max;
            }

            min = Math.min(rectangle[1], rectangle[3]);
            max = Math.max(rectangle[1], rectangle[3]);
            if (minY > min) {
                minY = min;
            }
            if (maxY < max) {
                maxY = max;
            }

            actualArea += (rectangle[2] - rectangle[0]) * (rectangle[3] - rectangle[1]);

            //noinspection unchecked
            Pair<Integer, Integer>[] points = new Pair[4];
            // 矩形四个顶点
            points[0] = new Pair<>(rectangle[0], rectangle[1]);
            points[1] = new Pair<>(rectangle[2], rectangle[3]);
            points[2] = new Pair<>(rectangle[0], rectangle[3]);
            points[3] = new Pair<>(rectangle[2], rectangle[1]);
            // 当某一个点同时是偶数个小矩形的顶点时，该点最终不是顶点；
            // 当某一个点同时是奇数个小矩形的顶点时，该点最终是一个顶点。
            for (Pair<Integer, Integer> p : points) {
                // 奇数个顶点最终会留下，偶数个顶点则会被删除
                if (!vetexes.remove(p)) {
                    vetexes.add(p);
                }
            }
        }

        // 不相等则不是精确覆盖
        if (actualArea != (maxX - minX) * (maxY - minY)) {
            return false;
        }

        // 完美矩形一定只有四个顶点
        if (vetexes.size() != 4) {
            return false;
        }

        // 最后还要判断留下的顶点是不是就是完美矩形的顶点
        return vetexes.contains(new Pair<>(minX, minY))
                && vetexes.contains(new Pair<>(minX, maxY))
                && vetexes.contains(new Pair<>(maxX, minY))
                && vetexes.contains(new Pair<>(maxX, maxY));
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }


    /**
     * 扫描线算法，参见：
     * https://leetcode-cn.com/problems/perfect-rectangle/solution/wan-mei-ju-xing-sao-miao-xian-by-he-qi-e-o9m2/
     *
     * LeetCode 耗时：25 ms - 91.57%
     *          内存消耗：47.4 MB - 48.19%
     */
    public boolean scanlineMethod(int[][] rectangles) {
        // 从左到右、从下到上排序矩形
        Arrays.sort(rectangles, (a, b) -> {
            int cmp = Integer.compare(a[0], b[0]);
            return cmp != 0 ? cmp : Integer.compare(a[1], b[1]);
        });

        // 记录扫描线的堆。扫描线包含矩形的右边界的 x、下边 y 和上边 y
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            int cmp = Integer.compare(a[0], b[0]);
            if (cmp != 0) {
                return cmp;
            }
            cmp = Integer.compare(a[1], b[1]);
            return cmp != 0 ? cmp : Integer.compare(a[2], b[2]);
        });

        // i 记录已经扫描的矩形
        int i = 0, n = rectangles.length;
        int nextX = rectangles[0][0], nextY = Integer.MIN_VALUE;
        // 现将最左边一列的矩形加到堆中，作为匹配的开始
        while (i < n && rectangles[i][0] == nextX) {
            // 只有当上一个矩形的上边和下一个矩形的下边相邻时，才符合完美矩形
            if (nextY == Integer.MIN_VALUE || nextY == rectangles[i][1]) {
                nextY = rectangles[i][3];
            } else {
                return false;
            }
            // 添加矩形的扫描线，也就是右边界
            pq.add(new int[]{rectangles[i][2], rectangles[i][1], rectangles[i][3]});
            i++;
        }

        // 从左到右，从下到上，从已经匹配的矩形开始往右推进，不断进行匹配
        // 因为都是正方形，所以当匹配成功时也一定是正方形
        while (!pq.isEmpty()) {
            int[] top = pq.remove();
            // 从堆顶找一条 x 相同、连续的最长的线段，也就是上下边相接
            int xStart = top[0], yStart = top[1], yEnd = top[2];
            while (!pq.isEmpty() && pq.peek()[0] == xStart && pq.peek()[1] == yEnd) {
                yEnd = pq.remove()[2];
            }
            // 从 rectangles[i:] 开始找到与上面线段完美匹配的连续矩形，
            // 也就是和上面线段 x 相接并且高度相等的矩形
            while (i < n && rectangles[i][0] == xStart && rectangles[i][1] == yStart) {
                yStart = rectangles[i][3];
                pq.add(new int[]{rectangles[i][2], rectangles[i][1], rectangles[i][3]});
                i++;
            }
            // 如果没有完美匹配，要么匹配错了，要么已经匹配完了最右的一列
            if (yStart != yEnd && (i < n || !pq.isEmpty())) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testScanlineMethod() {
        test(this::scanlineMethod);
    }
}
