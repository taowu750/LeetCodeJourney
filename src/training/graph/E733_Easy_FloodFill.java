package training.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 一个图像由一个二维整数数组表示，每个整数代表图像的像素值（从0到65535）。
 * 给定一个起始像素坐标 (sr，sc)，以及一个像素值 newColor，对图像进行 "Flood Fill"。
 *
 * 要执行"Flood Fill"，从起始像素开始，上下左右 4 个方向上与该起始像素颜色相同的所有像素，
 * 再加上与这些像素 4 方向连接的所有像素，依此类推。用 newColor 替换所有上述像素的颜色。
 * 最后，返回修改后的图像。
 *
 * 例 1：
 * Input:
 * image = [[1,1,1],
 *          [1,1,0],
 *          [1,0,1]]
 * sr = 1, sc = 1, newColor = 2
 * Output: [[2,2,2],
 *          [2,2,0],
 *          [2,0,1]]
 *
 * 约束：
 * - image 和 image[0] 的长度将在 [1，50] 范围内。
 * - 给定的起始像素将满足 0 <= sr < image.length 和 0 <= sc < image[0].length。
 * - image[i][j] 和 newColor 中每种颜色的值将是 [0，65535] 中的整数。
 */
public class E733_Easy_FloodFill {

    interface FloodFillMethod {
        int[][] floodFill(int[][] image, int sr, int sc, int newColor);
    }

    static void test(FloodFillMethod method) {
        assertArrayEquals(
                method.floodFill(new int[][]{{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}, 1, 1, 2),
                new int[][]{{2, 2, 2}, {2, 2, 0}, {2, 0, 1}});

        assertArrayEquals(
                method.floodFill(new int[][]{{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}, 1, 1, 1),
                new int[][]{{1, 1, 1}, {1, 1, 0}, {1, 0, 1}});

        assertArrayEquals(
                method.floodFill(new int[][]{{0, 0, 0}, {0, 1, 1}}, 1, 1, 1),
                new int[][]{{0, 0, 0}, {0, 1, 1}});
    }


    private int[][] image;
    private int oldColor, newColor;

    /**
     * 使用 DFS。
     *
     * LeetCode 耗时：0ms - 100%
     */
    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        this.image = image;
        this.newColor = newColor;
        oldColor = image[sr][sc];
        // 防止 oldColor 和 newColor 一样
        if (oldColor != newColor)
            floodFill(sr, sc);

        return image;
    }

    private void floodFill(int sr, int sc) {
        if (sr < 0 || sr >= image.length || sc < 0 || sc >= image[0].length
                || image[sr][sc] != oldColor)
            return;
        image[sr][sc] = newColor;
        floodFill(sr - 1, sc);
        floodFill(sr + 1, sc);
        floodFill(sr, sc - 1);
        floodFill(sr, sc + 1);
    }

    @Test
    public void testFloodFill() {
        test(this::floodFill);
    }
}
