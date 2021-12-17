package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 986. 区间列表的交集: https://leetcode-cn.com/problems/interval-list-intersections/
 *
 * 给定两个由一些「闭区间」组成的列表，firstList 和 secondList ，其中 firstList[i] = [starti, endi] 而
 * secondList[j] = [startj, endj] 。每个区间列表都是成对「不相交」的，并且「已经排序」。
 *
 * 返回这「两个区间列表的交集」。
 *
 * 形式上，闭区间 [a, b]（其中 a <= b）表示实数 x 的集合，而 a <= x <= b 。
 *
 * 两个闭区间的「交集」是一组实数，要么为空集，要么为闭区间。例如，[1, 3] 和 [2, 4] 的交集为 [2, 3] 。
 *
 * 例 1：
 * 输入：firstList = [[0,2],[5,10],[13,23],[24,25]], secondList = [[1,5],[8,12],[15,24],[25,26]]
 * 输出：[[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
 *
 * 例 2：
 * 输入：firstList = [[1,3],[5,9]], secondList = []
 * 输出：[]
 *
 * 例 3：
 * 输入：firstList = [], secondList = [[4,8],[10,12]]
 * 输出：[]
 *
 * 例 4：
 * 输入：firstList = [[1,7]], secondList = [[3,10]]
 * 输出：[[3,7]]
 *
 * 说明：
 * - 0 <= firstList.length, secondList.length <= 1000
 * - firstList.length + secondList.length >= 1
 * - 0 <= starti < endi <= 109
 * - endi < start_i+1
 * - 0 <= startj < endj <= 109
 * - endj < start_j+1
 */
public class E986_Medium_IntervalListIntersections {

    public static void test(BinaryOperator<int[][]> method) {
        assertArrayEquals(new int[][]{{1,2}, {5,5}, {8,10}, {15,23}, {24,24}, {25,25}},
                method.apply(new int[][]{{0,2}, {5,10}, {13,23}, {24,25}},
                        new int[][]{{1,5}, {8,12}, {15,24}, {25,26}}));
        assertArrayEquals(new int[][]{}, method.apply(new int[][]{{1,3}, {5,9}}, new int[][]{}));
        assertArrayEquals(new int[][]{}, method.apply(new int[][]{}, new int[][]{{4,8}, {10,12}}));
        assertArrayEquals(new int[][]{{3,7}}, method.apply(new int[][]{{1,7}}, new int[][]{{3,10}}));
    }

    /**
     * LeetCode 耗时：3 ms - 97.54%
     *          内存消耗：39.2 MB - 69.71%
     */
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        List<int[]> result = new ArrayList<>();
        for (int i = 0, j = 0; i < firstList.length && j < secondList.length;) {
            int intervalHead = Math.max(firstList[i][0], secondList[j][0]);
            int intervalEnd = Math.min(firstList[i][1], secondList[j][1]);
            if (intervalHead <= intervalEnd) {
                result.add(new int[]{intervalHead, intervalEnd});
            }
            if (firstList[i][1] == intervalEnd) {
                i++;
            }
            if (secondList[j][1] == intervalEnd) {
                j++;
            }
        }

        return result.toArray(new int[0][0]);
    }

    @Test
    public void testIntervalIntersection() {
        test(this::intervalIntersection);
    }
}
