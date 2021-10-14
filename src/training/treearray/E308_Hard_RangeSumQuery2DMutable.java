package training.treearray;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 308. 二维区域和检索 - 可变: https://leetcode-cn.com/problems/range-sum-query-2d-mutable/
 *
 * 给你一个二维矩阵 matrix ，你需要处理下面两种类型的若干次查询：
 * - 更新：更新 matrix 中某个单元的值。
 * - 求和：计算矩阵 matrix 中某一矩形区域元素的「和」，该区域由 左上角 (row1, col1) 和 右下角 (row2, col2) 界定。
 *
 * 实现 NumMatrix 类：
 * - NumMatrix(int[][] matrix) 用整数矩阵 matrix 初始化对象。
 * - void update(int row, int col, int val) 更新 matrix[row][col] 的值到 val 。
 * - int sumRegion(int row1, int col1, int row2, int col2) 返回矩阵 matrix 中指定矩形区域元素的「和」，
 * 该区域由「左上角 (row1, col1)」和「右下角 (row2, col2)」界定。
 *
 * 例 1：
 * 输入
 * ["NumMatrix", "sumRegion", "update", "sumRegion"]
 * [[[[3, 0, 1, 4, 2],
 *    [5, 6, 3, 2, 1],
 *    [1, 2, 0, 1, 5],
 *    [4, 1, 0, 1, 7],
 *    [1, 0, 3, 0, 5]]],
 *  [2, 1, 4, 3], [3, 2, 2], [2, 1, 4, 3]]
 * 输出
 * [null, 8, null, 10]
 * 解释
 * NumMatrix numMatrix = new NumMatrix([[3, 0, 1, 4, 2], [5, 6, 3, 2, 1], [1, 2, 0, 1, 5], [4, 1, 0, 1, 7], [1, 0, 3, 0, 5]]);
 * numMatrix.sumRegion(2, 1, 4, 3); // 返回 8 (即, 左侧红色矩形的和)
 * numMatrix.update(3, 2, 2);       // 矩阵从左图变为右图
 * numMatrix.sumRegion(2, 1, 4, 3); // 返回 10 (即，右侧红色矩形的和)
 *
 * 约束：
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= m, n <= 200
 * - -10^5 <= matrix[i][j] <= 10^5
 * - 0 <= row < m
 * - 0 <= col < n
 * - -10^5 <= val <= 10^5
 * - 0 <= row1 <= row2 < m
 * - 0 <= col1 <= col2 < n
 * - 最多调用 10^4 次 sumRegion 和 update 方法
 */
public class E308_Hard_RangeSumQuery2DMutable {

    public static void test(Function<int[][], INumMatrix> factory) {
        INumMatrix numMatrix = factory.apply(new int[][]{
                {3, 0, 1, 4, 2},
                {5, 6, 3, 2, 1},
                {1, 2, 0, 1, 5},
                {4, 1, 0, 1, 7},
                {1, 0, 3, 0, 5}});
        assertEquals(8, numMatrix.sumRegion(2, 1, 4, 3));
        numMatrix.update(3, 2, 2);
        assertEquals(10, numMatrix.sumRegion(2, 1, 4, 3));

        numMatrix = factory.apply(new int[][]{{1}});
        assertEquals(1, numMatrix.sumRegion(0, 0, 0, 0));
        numMatrix.update(0, 0, -1);
        assertEquals(-1, numMatrix.sumRegion(0, 0, 0, 0));

        numMatrix = factory.apply(new int[][]{{1}, {2}});
        assertEquals(1, numMatrix.sumRegion(0, 0, 0, 0));
        assertEquals(2, numMatrix.sumRegion(1, 0, 1, 0));
        assertEquals(3, numMatrix.sumRegion(0, 0, 1, 0));
        numMatrix.update(0, 0, 3);
        numMatrix.update(1, 0, 5);
        assertEquals(8, numMatrix.sumRegion(0, 0, 1, 0));
    }

    @Test
    public void testNumMatrix() {
        test(NumMatrix::new);
    }

    @Test
    public void testBetterTreeArrayMethod() {
        test(BetterTreeArrayMethod::new);
    }
}

interface INumMatrix {

    void update(int row, int col, int val);

    int sumRegion(int row1, int col1, int row2, int col2);
}

/**
 * 树状数组解法，参见 https://www.cnblogs.com/xenny/p/9739600.html
 *
 * LeetCode 耗时：16 ms - 29.63%
 *          内存消耗：44.5 MB - 17.28%
 */
class NumMatrix implements INumMatrix {

    int m, n;
    private int[][] matrix;
    private int[] tree;

    public NumMatrix(int[][] matrix) {
        // 构建树状数组，并将二维变成一维
        m = matrix.length;
        n = matrix[0].length;
        this.matrix = matrix;
        tree = new int[m * n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                add(idx(i, j), matrix[i][j]);
            }
        }
    }

    private void add(int idx, int val) {
        for (int i = idx; i <= m * n; i += lowbit(i)) {
            tree[i] += val;
        }
    }

    private int prefixSum(int end) {
        int sum = 0;
        for (int i = end; i > 0; i -= lowbit(i)) {
            sum += tree[i];
        }

        return sum;
    }

    private int lowbit(int x) {
        return x & -x;
    }

    private int idx(int i, int j) {
        return i * n + j + 1;
    }

    public void update(int row, int col, int val) {
        add(idx(row, col), val - matrix[row][col]);
        matrix[row][col] = val;
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int sum = 0;
        for (int i = row1; i <= row2; i++) {
            sum += prefixSum(idx(i, col2)) - prefixSum(idx(i, col1 - 1));
        }

        return sum;
    }
}

/**
 * 二维树状数组，每一行和 matrix 一行对应。
 *
 * LeetCode 耗时：13 ms - 49.38%
 *          内存消耗：44.4 MB - 39.51%
 */
class BetterTreeArrayMethod implements INumMatrix {

    int m, n;
    private int[][] matrix;
    private int[][] tree;

    public BetterTreeArrayMethod(int[][] matrix) {
        m = matrix.length;
        n = matrix[0].length;
        this.matrix = matrix;
        tree = new int[m][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                add(i, j + 1, matrix[i][j]);
            }
        }
    }

    private void add(int r, int c, int val) {
        for (int i = c; i <= n; i += lowbit(i)) {
            tree[r][i] += val;
        }
    }

    private int prefixSum(int r, int endC) {
        int sum = 0;
        for (int i = endC; i > 0; i -= lowbit(i)) {
            sum += tree[r][i];
        }

        return sum;
    }

    private int lowbit(int x) {
        return x & -x;
    }

    public void update(int row, int col, int val) {
        add(row, col + 1, val - matrix[row][col]);
        matrix[row][col] = val;
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int sum = 0;
        for (int i = row1; i <= row2; i++) {
            sum += prefixSum(i, col2 + 1) - prefixSum(i, col1);
        }

        return sum;
    }
}
