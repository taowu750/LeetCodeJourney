package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import static util.ArrayUtil.equalsIgnoreOutOrder;

/**
 * 973. 最接近原点的 K 个点: https://leetcode-cn.com/problems/k-closest-points-to-origin/
 *
 * 给定一个数组 points，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点；以及一个整数 k。
 * 返回离原点 (0,0) 最近的 k 个点。
 *
 * 这里，平面上两点之间的距离是欧几里德距离（√(x1 - x2)^2+ (y1 - y2)^2）。
 *
 * 你可以按「任何顺序」返回答案。除了点坐标的顺序之外，答案确保是「唯一」的。
 *
 * 例 1：
 * 输入：points = [[1,3],[-2,2]], k = 1
 * 输出：[[-2,2]]
 * 解释：
 * (1, 3) 和原点之间的距离为 sqrt(10)，
 * (-2, 2) 和原点之间的距离为 sqrt(8)，
 * 由于 sqrt(8) < sqrt(10)，(-2, 2) 离原点更近。
 * 我们只需要距离原点最近的 K = 1 个点，所以答案就是 [[-2,2]]。
 *
 * 例 2：
 * 输入：points = [[3,3],[5,-1],[-2,4]], k = 2
 * 输出：[[3,3],[-2,4]]
 * （答案 [[-2,4],[3,3]] 也会被接受。）
 *
 * 说明：
 * - 1 <= k <= points.length <= 10^4
 * - -10^4 < xi, yi < 10^4
 */
public class E973_Medium_KClosestPointsToOrigin {

    public static void test(BiFunction<int[][], Integer, int[][]> method) {
        equalsIgnoreOutOrder(new int[][]{{-2,2}}, method.apply(new int[][]{{1,3},{-2,2}}, 1));
        equalsIgnoreOutOrder(new int[][]{{3,3},{-2,4}}, method.apply(new int[][]{{3,3},{5,-1},{-2,4}}, 2));
    }

    /**
     * 此题也可用快速排序，参见 {@link training.sort.E215_Medium_KthLargestElementInAnArray}。
     *
     * LeetCode 耗时：17 ms - 84.99%
     *          内存消耗：49.6 MB - 10.43%
     */
    public int[][] kClosest(int[][] points, int k) {
        Comparator<int[]> comparator = (a, b) -> -Integer.compare(a[0] * a[0] + a[1] * a[1], b[0] * b[0] + b[1] * b[1]);
        PriorityQueue<int[]> pq = new PriorityQueue<>(k, comparator);
        for (int[] point : points) {
            if (pq.size() < k) {
                pq.add(point);
            } else if (comparator.compare(point, pq.peek()) > 0) {
                pq.remove();
                pq.add(point);
            }
        }

        int[][] result = new int[pq.size()][];
        int i = 0;
        for (int[] point : pq) {
            result[i++] = point;
        }

        return result;
    }

    @Test
    public void testKClosest() {
        test(this::kClosest);
    }
}
