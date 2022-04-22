package training.partition;

import org.junit.jupiter.api.Test;
import util.datastructure.function.ToIntTriFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1274. 矩形内船只的数目: https://leetcode-cn.com/problems/number-of-ships-in-a-rectangle/
 *
 * 在用笛卡尔坐标系表示的二维海平面上，有一些船。每一艘船都在一个整数点上，且每一个整数点最多只有 1 艘船。
 *
 * 有一个函数 Sea.hasShips(topRight, bottomLeft)，输入参数为右上角和左下角两个点的坐标，
 * 当且仅当这两个点所表示的矩形区域（包含边界）内至少有一艘船时，这个函数才返回 true，否则返回 false。
 *
 * 给你矩形的右上角 topRight 和左下角 bottomLeft 的坐标，请你返回此矩形内船只的数目。
 * 题目保证矩形内「至多只有 10 艘船」。
 *
 * 调用函数 hasShips 「超过400次」的提交将被判为错误答案（Wrong Answer）。
 *
 * 例 1：
 * 输入：
 * ships = [[1,1],[2,2],[3,3],[5,5]], topRight = [4,4], bottomLeft = [0,0]
 * 输出：3
 * 解释：在 [0,0] 到 [4,4] 的范围内总共有 3 艘船。
 *
 * 例 2：
 * 输入：ans = [[1,1],[2,2],[3,3]], topRight = [1000,1000], bottomLeft = [0,0]
 * 输出：3
 *
 * 说明：
 * - ships 数组只用于评测系统内部初始化。你必须“蒙着眼睛”解决这个问题。你无法得知 ships 的信息，
 *   所以只能通过调用 hasShips 接口来求解。
 * - 0 <= bottomLeft[0] <= topRight[0] <= 1000
 * - 0 <= bottomLeft[1] <= topRight[1] <= 1000
 * - topRight != bottomLeft
 */
public class E1274_Hard_NumberOfShipsInRectangle {

    public static class Sea {
        private int cnt;
        private final int[][] ships;

        public Sea(int[][] ships) {
            this.ships = ships;
        }

        public boolean hasShips(int[] topRight, int[] bottomLeft) {
            if (++cnt > 400) {
                throw new AssertionError("调用次数超过400");
            }
            for (int[] ship : ships) {
                if (ship[0] >= bottomLeft[0] && ship[1] >= bottomLeft[1]
                        && ship[0] <= topRight[0] && ship[1] <= topRight[1]) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void test(ToIntTriFunction<Sea, int[], int[]> method) {
        assertEquals(3, method.applyAsInt(new Sea(new int[][]{{1,1},{2,2},{3,3},{5,5}}), new int[]{4, 4}, new int[]{0, 0}));
        assertEquals(3, method.applyAsInt(new Sea(new int[][]{{1,1},{2,2},{3,3}}), new int[]{1000, 1000}, new int[]{0, 0}));
        assertEquals(5, method.applyAsInt(new Sea(new int[][]{{6,6},{100,50},{999,81},{50,50},{700,600}}), new int[]{1000, 1000}, new int[]{0, 0}));
    }

    /**
     * LeetCode 耗时：0 ms - 100%
     *          内存消耗：39 MB - 58.33%
     */
    public int countShips(Sea sea, int[] topRight, int[] bottomLeft) {
        ans = 0;
        partition(sea, topRight, bottomLeft);
        return ans;
    }

    private int ans;

    private void partition(Sea sea, int[] topRight, int[] bottomLeft) {
        // 如果已经收缩到一个点
        if (topRight[0] == bottomLeft[0] && topRight[1] == bottomLeft[1]) {
            if (sea.hasShips(topRight, bottomLeft)) {
                ans++;
            }
            return;
        }

        if (!sea.hasShips(topRight, bottomLeft)) {
            return;
        }
        // 两点 x 坐标相同，则分成上下两块搜索
        if (topRight[0] == bottomLeft[0]) {
            // 注意+1，将 (0,3)(0,4) 这种挨在一起的下标正确分隔开，这样 midY 就等于 4，让它和上贴近
            int midY = (topRight[1] + bottomLeft[1] + 1) / 2;
            // 上
            partition(sea, topRight, new int[]{topRight[0], midY});
            // 下。注意-1，否则会无限递归，例如 (1,2) 坐标
            partition(sea, new int[]{bottomLeft[0], midY - 1}, bottomLeft);
        } else if (topRight[1] == bottomLeft[1]) {  // 两点 y 坐标相同，则分成左右两块搜索
            // 让 midX 和右贴近
            int midX = (topRight[0] + bottomLeft[0] + 1) / 2;
            // 左
            partition(sea, new int[]{midX - 1, bottomLeft[1]}, bottomLeft);
            // 右
            partition(sea, topRight, new int[]{midX, topRight[1]});
        } else {  // 否则分成四块搜索
            int midX = (topRight[0] + bottomLeft[0] + 1) / 2;
            int midY = (topRight[1] + bottomLeft[1] + 1) / 2;
            // 右上
            partition(sea, topRight, new int[]{midX, midY});
            // 右下
            partition(sea, new int[]{topRight[0], midY - 1}, new int[]{midX, bottomLeft[1]});
            // 左下
            partition(sea, new int[]{midX - 1, midY - 1}, bottomLeft);
            // 左上
            partition(sea, new int[]{midX - 1, topRight[1]}, new int[]{bottomLeft[0], midY});
        }
    }

    @Test
    public void testCountShips() {
        test(this::countShips);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/number-of-ships-in-a-rectangle/solution/ju-xing-nei-chuan-zhi-de-shu-mu-by-leetcode-soluti/
     *
     * 在方法一中，我们使用将一个区域分成四个小区域的方法，通过递归查找，得到船只的数目。
     * 可以预见的是，我们会进行很多次失败的查找。
     *
     * 那么有什么办法可以减少失败的查找次数呢？设想一下，如果我们将区域仅划分为两个小区域 A 和 B，
     * 那么当对 A 区域调用 API 返回 False 时，我们可以直接断定，对 B 区域调用 API 一定会返回 True，
     * 这样就省去了一次 API 的调用。
     *
     * LeetCode 耗时：0 ms - 100%
     *          内存消耗：39 MB - 58.33%
     */
    public int betterMethod(Sea sea, int[] topRight, int[] bottomLeft) {
        return find(sea, topRight, bottomLeft, false);
    }

    private int find(Sea sea, int[] topRight, int[] bottomLeft, boolean claim) {
        int x1 = topRight[0], y1 = topRight[1];
        int x2 = bottomLeft[0], y2 = bottomLeft[1];
        if (x1 < x2 || y1 < y2) {
            return 0;
        }

        boolean judge = claim || sea.hasShips(topRight, bottomLeft);
        if (!judge) {
            return 0;
        }
        if (x1 == x2 && y1 == y2) {
            return 1;
        }

        if (x1 == x2) {
            int yMid = (y1 + y2) / 2;
            int A = find(sea, new int[]{x1, yMid}, new int[]{x1, y2}, false);
            return A + find(sea, new int[]{x1, y1}, new int[]{x1, yMid + 1}, A == 0);
        } else {
            int xMid = (x1 + x2) / 2;
            int A = find(sea, new int[]{xMid, y1}, new int[]{x2, y2}, false);
            return A + find(sea, new int[]{x1, y1}, new int[]{xMid + 1, y2}, A == 0);
        }
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
