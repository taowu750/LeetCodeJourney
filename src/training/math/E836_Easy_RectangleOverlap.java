package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 836. 矩形重叠: https://leetcode-cn.com/problems/rectangle-overlap/
 *
 * 矩形以列表 [x1, y1, x2, y2] 的形式表示，其中 (x1, y1) 为左下角的坐标，(x2, y2) 是右上角的坐标。
 * 矩形的上下边平行于 x 轴，左右边平行于 y 轴。
 *
 * 如果相交的面积为「正」，则称两矩形重叠。需要明确的是，只在角或边接触的两个矩形不构成重叠。
 *
 * 给出两个矩形 rec1 和 rec2 。如果它们重叠，返回 true；否则，返回 false 。
 *
 * 例 1：
 * 输入：rec1 = [0,0,2,2], rec2 = [1,1,3,3]
 * 输出：true
 *
 * 例 2：
 * 输入：rec1 = [0,0,1,1], rec2 = [1,0,2,1]
 * 输出：false
 *
 * 例 3：
 * 输入：rec1 = [0,0,1,1], rec2 = [2,2,3,3]
 * 输出：false
 *
 * 说明：
 * - rect1.length == 4
 * - rect2.length == 4
 * - -10^9 <= rec1[i], rec2[i] <= 10^9
 * - rec1 和 rec2 表示一个面积不为零的有效矩形
 */
public class E836_Easy_RectangleOverlap {

    public static void test(BiPredicate<int[], int[]> method) {
        assertTrue(method.test(new int[]{0,0,2,2}, new int[]{1,1,3,3}));
        assertFalse(method.test(new int[]{0,0,1,1}, new int[]{1,0,2,1}));
        assertFalse(method.test(new int[]{0,0,1,1}, new int[]{2,2,3,3}));
    }

    /**
     * 其实就是判断矩形是否相交。参见：https://blog.csdn.net/qianchenglenger/article/details/50484053
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.7 MB - 54.95%
     */
    public boolean isRectangleOverlap(int[] rec1, int[] rec2) {
        return !(rec1[1] >= rec2[3]  // rec1 在 rec2 上边
                || rec1[3] <= rec2[1]  // rec1 在 rec2 下边
                || rec1[2] <= rec2[0]  // rec1 在 rec2 左边
                || rec1[0] >= rec2[2]);  // rec1 在 rec2 右边
    }

    @Test
    public void testIsRectangleOverlap() {
        test(this::isRectangleOverlap);
    }
}
