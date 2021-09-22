package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 149. 直线上最多的点数: https://leetcode-cn.com/problems/max-points-on-a-line/
 * <p>
 * 给你一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。求最多有多少个点在同一条直线上。
 * <p>
 * 例 1：
 * 输入：points = [[1,1],[2,2],[3,3]]
 * 输出：3
 * <p>
 * 例 2：
 * 输入：points = [[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]]
 * 输出：4
 * <p>
 * 约束：
 * - 1 <= points.length <= 300
 * - points[i].length == 2
 * - -10^4 <= xi, yi <= 10^4
 * - points 中的所有点互不相同
 */
public class E149_Hard_MaxPointsOnLine {

    static void test(ToIntFunction<int[][]> method) {
        assertEquals(3, method.applyAsInt(new int[][]{{1, 1}, {2, 2}, {3, 3}}));
        assertEquals(4, method.applyAsInt(new int[][]{{1, 1}, {3, 2}, {5, 3}, {4, 1}, {2, 3}, {1, 4}}));
        assertEquals(3, method.applyAsInt(new int[][]{{2, 3}, {3, 3}, {-5, 3}}));
    }

    /**
     * 暴力法。
     * <p>
     * LeetCode 耗时：19 ms - 33.82%
     * 内存消耗：37.4 MB - 75.26%
     */
    public int maxPoints(int[][] points) {
        if (points.length <= 2) {
            return points.length;
        }

        int result = 2;
        LinkedList<int[]> list = new LinkedList<>(Arrays.asList(points));
        LinkedList<int[]> backup = new LinkedList<>();
        // 以每个点作为起始点，和其他点连成直线
        for (int[] point1 : points) {
            while (!list.isEmpty()) {
                // 找到一个和 point1 不同的点连成直线
                int[] point2 = list.removeFirst();
                if (point1[0] == point2[0] && point1[1] == point2[1]) {
                    continue;
                }
                int len = 2;
                Iterator<int[]> iter = list.iterator();
                // 遍历其他点，看看哪些点在直线上
                while (iter.hasNext()) {
                    int[] other = iter.next();
                    if (point1[0] != other[0] || point1[1] != other[1]) {
                        // 两点式
                        if ((other[0] - point1[0]) * (point2[1] - point1[1])
                                == (other[1] - point1[1]) * (point2[0] - point1[0])) {
                            len++;
                            backup.add(other);
                            iter.remove();
                        }
                    } else {
                        iter.remove();
                    }
                }
                if (len > result) {
                    result = len;
                }
                backup.add(point2);
            }
            backup.add(point1);

            LinkedList<int[]> tmp = list;
            list = backup;
            backup = tmp;
        }

        return result;
    }

    @Test
    public void testMaxPoints() {
        test(this::maxPoints);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/max-points-on-a-line/solution/zhi-xian-shang-zui-duo-de-dian-shu-by-le-tq8f/
     *
     * LeetCode 耗时：8 ms - 78.00%
     *          内存消耗：37.9 MB - 46.11%
     */
    public int hashMethod(int[][] points) {
        if (points.length <= 2) {
            return points.length;
        }

        int result = 0;
        for (int i = 0; i < points.length - 1; i++) {
            if (result >= points.length - i || result > points.length / 2) {
                break;
            }
            Map<Integer, Integer> slopes = new HashMap<>((int) ((points.length - i - 1) / 0.75) + 1);
            for (int j = i + 1; j < points.length; j++) {
                int mx = points[i][0] - points[j][0], my = points[i][1] - points[j][1];
                if (mx == 0) {
                    my = 1;
                } else if (my == 0) {
                    mx = 1;
                } else {
                    if (my < 0) {
                        mx = -mx;
                        my = -my;
                    }
                    int gcd = gcd(Math.abs(mx), Math.abs(my));
                    mx /= gcd;
                    my /= gcd;
                }
                slopes.merge(my + mx * 20001, 1, Integer::sum);
            }

            int max = 1;
            for (Integer nums : slopes.values()) {
                if (nums > max) {
                    max = nums;
                }
            }
            if (result < max + 1) {
                result = max + 1;
            }
        }

        return result;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int tmp = b;
            b = a % b;
            a = tmp;
        }

        return a;
    }

    @Test
    public void testHashMethod() {
        test(this::hashMethod);
    }
}
