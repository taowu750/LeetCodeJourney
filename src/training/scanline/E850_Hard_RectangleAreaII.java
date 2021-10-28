package training.scanline;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 850. 矩形面积 II: https://leetcode-cn.com/problems/rectangle-area-ii/
 *
 * 我们给出了一个（轴对齐的）矩形列表 rectangles。对于 rectangle[i] = [x1, y1, x2, y2]，其中（x1，y1）
 * 是矩形 i 左下角的坐标，（x2，y2）是该矩形右上角的坐标。
 *
 * 找出平面中所有矩形叠加覆盖后的总面积。 由于答案可能太大，请返回它对 10 ^ 9 + 7 取模的结果。
 *
 * 例 1：
 * 输入：[[0,0,2,2],[1,0,2,3],[1,0,3,1]]
 * 输出：6
 * 解释：如图所示。
 *
 * 例 2：
 * 输入：[[0,0,1000000000,1000000000]]
 * 输出：49
 * 解释：答案是 10^18 对 (10^9 + 7) 取模的结果， 即 (10^9)^2 → (-7)^2 = 49 。
 *
 * 说明：
 * - 1 <= rectangles.length <= 200
 * - rectanges[i].length = 4
 * - 0 <= rectangles[i][j] <= 10^9
 * - 矩形叠加覆盖后的总面积不会超越 2^63 - 1，这意味着可以用一个 64 位有符号整数来保存面积结果。
 */
public class E850_Hard_RectangleAreaII {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(6, method.applyAsInt(new int[][]{
                {0,0,2,2},
                {1,0,2,3},
                {1,0,3,1}}));
        assertEquals(49, method.applyAsInt(new int[][]{{0,0,1000000000,1000000000}}));
        assertEquals(2, method.applyAsInt(new int[][]{{0,0,1,1}, {2,2,3,3}}));
    }

    public static final int MOD = 1000000007;

    /**
     * 扫描线算法，需要用到线段树、线段树的染色问题、离散化。参见：
     * https://zhuanlan.zhihu.com/p/103616664
     *
     * LeetCode 耗时：10 ms - 61.11%
     *          内存消耗：38.4 MB - 42.59%
     */
    public int rectangleArea(int[][] rectangles) {
        SegmentTree segmentTree = new SegmentTree(rectangles);
        Scanline[] scanLines = segmentTree.getScanLines();
        long result = 0;
        for (int i = 1; i < scanLines.length; i++) {
            // 两个扫描线之间是扫到的矩形的宽度
            result += (long) segmentTree.query() * (scanLines[i].x - scanLines[i - 1].x);
            segmentTree.update(scanLines[i]);
        }

        return (int) (result % MOD);
    }

    public static class Scanline {
        // 边的 x 坐标
        public int x;
        // 边的上 y 坐标，下 y 坐标
        public int upY, downY;
        // 入边为 1，出边为 -1
        public int inOut;

        public Scanline() {
        }

        public Scanline(int x, int upY, int downY, int inOut) {
            this.x = x;
            this.upY = upY;
            this.downY = downY;
            this.inOut = inOut;
        }
    }

    /**
     * 用来查询整个高度区间上最大有效高度的线段树
     */
    public static class SegmentTree {

        // 存放第 i 个高度区间对应覆盖情况的值
        private int[] cover;
        // 存放第 i 个高度区间下的有效高度
        private int[] length;
        // 存放离散后的 y 值和下标的映射，使用二分查找查找下标
        private int[] idx2y;
        // 扫描线
        private Scanline[] scanLines;


        public SegmentTree(int[][] rectangles) {
            scanLines = new Scanline[rectangles.length * 2 + 1];
            TreeSet<Integer> ySet = new TreeSet<>();
            scanLines[0] = new Scanline();
            for (int i = 0, j = 1; i < rectangles.length; i++) {
                scanLines[j++] = new Scanline(rectangles[i][0], rectangles[i][3], rectangles[i][1], 1);
                scanLines[j++] = new Scanline(rectangles[i][2], rectangles[i][3], rectangles[i][1], -1);
                ySet.add(rectangles[i][1]);
                ySet.add(rectangles[i][3]);
            }
            // 将扫描线按照 x 轴方向从左到右排序
            Arrays.sort(scanLines, (a, b) -> Integer.compare(a.x, b.x));

            /*
            注意，length 和 cover 都表示区间，而 idx2y 表示点。
            区间表示的树不是一颗完全树，会有一些空隙，所以需要更大的空间。不过 root 的计算方法仍然可以按照完全树来。

            例如：[1,2,3,4]，区间如下：
                    [1,4]
                   /     \
                [1,2]    [2,4]
                        /     \
                     [2,3]   [3,4]
             */
            length = new int[4 * ySet.size()];
            cover = new int[4 * ySet.size()];
            // 离散化 y 下标
            idx2y = new int[ySet.size() + 1];
            int i = 1;
            for (int y : ySet) {
                idx2y[i++] = y;
            }
        }

        public Scanline[] getScanLines() {
            return scanLines;
        }

        public void update(Scanline scanline) {
            update(Arrays.binarySearch(idx2y, 1, idx2y.length, scanline.downY),
                    Arrays.binarySearch(idx2y, 1, idx2y.length, scanline.upY),
                    scanline.inOut, 1, 1, idx2y.length - 1);
        }

        /**
         * 查询整个区间的有效长度
         */
        public int query() {
            return length[1];
        }

        private void update(int targetLeft, int targetRight, int inOut, int root, int curLeft, int curRight) {
            // 当前范围为空，或不相交
            if (curLeft >= curRight || curRight < targetLeft || curLeft > targetRight) {
                return;
            }

            // 目标区间包含当前区间
            if (curLeft >= targetLeft && curRight <= targetRight) {
                cover[root] += inOut;
                pushUp(root, curLeft, curRight);
            } else {  // 当前区间包含目标区间或与它有交集
                // 到了叶子节点
                if (curLeft + 1 == curRight) {
                    return;
                }

                int mid = (curLeft + curRight) >>> 1;
                update(targetLeft, targetRight, inOut, root * 2, curLeft, mid);
                // 这里不再是 mid+1,因为要进入类似 [1,2] [2,3] 的叶子节点
                update(targetLeft, targetRight, inOut, root * 2 + 1, mid, curRight);
                // 更新或上推长度
                pushUp(root, curLeft, curRight);
            }
        }

        /**
         * 上推长度。和一般的线段树不一样的是，这里只需要上推长度，不需要下推或上推 cover。
         * 因为所有的查询都是在根节点查的，就是查整个范围内的合法区间长度
         */
        private void pushUp(int root, int left, int right) {
            // 如果某个区间的 cover 为正，更新它的有效长度
            if (cover[root] > 0) {
                length[root] = idx2y[right] - idx2y[left];
            } else if (left + 1 == right) {
                /*
                这棵树和之前的线段树不一样，它的叶子节点的[l,r]不相等，而是差别为1。
                这是因为点对于求面积的题目毫无意义，我们最需要的是它每一个基础的线段。

                此时 cover 为 0，到了叶子节点，更新长度为 0
                 */
                length[root] = 0;
            } else {
                // 上推长度
                length[root] = length[root * 2] + length[root * 2 + 1];
            }
        }
    }

    @Test
    public void testRectangleArea() {
        test(this::rectangleArea);
    }
}
