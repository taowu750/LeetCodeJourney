package training.math;

import org.junit.jupiter.api.Test;
import util.datastructure.function.IntEightOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 223. 矩形面积: https://leetcode-cn.com/problems/rectangle-area/
 *
 * 给你二维平面上两个由直线构成且边与坐标轴平行/垂直的矩形，请你计算并返回两个矩形覆盖的总面积。
 *
 * 每个矩形由其「左下」顶点和「右上」顶点坐标表示：
 * - 第一个矩形由其左下顶点 (ax1, ay1) 和右上顶点 (ax2, ay2) 定义。
 * - 第二个矩形由其左下顶点 (bx1, by1) 和右上顶点 (bx2, by2) 定义。
 *
 * 例 1：
 * 输入：ax1 = -3, ay1 = 0, ax2 = 3, ay2 = 4, bx1 = 0, by1 = -1, bx2 = 9, by2 = 2
 * 输出：45
 *
 * 例 2：
 * 输入：ax1 = -2, ay1 = -2, ax2 = 2, ay2 = 2, bx1 = -2, by1 = -2, bx2 = 2, by2 = 2
 * 输出：16
 *
 * 说明：
 * - -10^4 <= ax1, ay1, ax2, ay2, bx1, by1, bx2, by2 <= 10^4
 */
public class E223_Medium_RectangleArea {

    public static void test(IntEightOperator method) {
        assertEquals(45, method.applyAsInt(-3, 0, 3, 4, 0, -1, 9, 2));
        assertEquals(16, method.applyAsInt(-2, -2, 2, 2, -2, -2, 2, 2));
        assertEquals(4, method.applyAsInt(0, 0, 0, 0, -1, -1, 1, 1));
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：40.8 MB - 14.38%
     */
    public int computeArea(int ax1, int ay1, int ax2, int ay2, int bx1, int by1, int bx2, int by2) {
        // 计算两个矩形在 y、x 轴上投影的重叠长度
        int yInter = Math.max(0, Math.min(ay2, by2) - Math.max(ay1, by1));
        int xInter = Math.max(0, Math.min(ax2, bx2) - Math.max(ax1, bx1));
        return (ax2 - ax1) * (ay2 - ay1) + (bx2 - bx1) * (by2 - by1) - yInter * xInter;
    }

    @Test
    public void testComputeArea() {
        test(this::computeArea);
    }
}
