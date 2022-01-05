package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 407. 接雨水 II: https://leetcode-cn.com/problems/trapping-rain-water-ii/
 *
 * 给你一个 m x n 的矩阵，其中的值均为非负整数，代表二维高度图每个单元的高度，请计算图中形状最多能接多少体积的雨水。
 *
 * 例 1：
 * 输入: heightMap = [[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]
 * 输出: 4
 * 解释: 下雨后，雨水将会被上图蓝色的方块中。总的接雨水量为1+2+1=4。
 *
 * 例 2：
 * 输入: heightMap = [[3,3,3,3,3],[3,2,2,2,3],[3,2,1,2,3],[3,2,2,2,3],[3,3,3,3,3]]
 * 输出: 10
 *
 * 说明：
 * - m == heightMap.length
 * - n == heightMap[i].length
 * - 1 <= m, n <= 200
 * - 0 <= heightMap[i][j] <= 2 * 10^4
 */
public class E407_Hard_TrappingRainWaterII {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(4, method.applyAsInt(new int[][]{
                {1,4,3,1,3,2},
                {3,2,1,3,2,4},
                {2,3,3,2,3,1}}));
        assertEquals(10, method.applyAsInt(new int[][]{
                {3,3,3,3,3},
                {3,2,2,2,3},
                {3,2,1,2,3},
                {3,2,2,2,3},
                {3,3,3,3,3}}));
    }

    /**
     * 最小堆解法，参见：
     * https://leetcode-cn.com/problems/trapping-rain-water-ii/solution/jie-yu-shui-ii-by-leetcode-solution-vlj3/
     *
     * 首先我们思考一下什么样的方块一定可以接住水：
     * - 该方块不为最外层的方块；
     * - 该方块自身的高度比其上下左右四个相邻的方块「接水后的高度」都要低；
     *
     * 我们假设方块的索引为 (i,j)，方块的高度为 heightMap[i][j]，方块接水后的高度为 water[i][j]。
     * 则我们知道方块 (i,j) 的接水后的高度为：
     *      water[i][j]=max(heightMap[i][j], min(water[i−1][j],water[i+1][j],water[i][j−1],water[i][j+1]))
     *
     * 我们知道方块 (i,j) 实际接水的容量计算公式为 water[i][j]−heightMap[i][j]。
     * 首先我们可以确定的是，矩阵的最外层的方块接水后的高度就是方块的自身高度，因为最外层的方块无法接水，
     * 因此最外层的方块 water[i][j]=heightMap[i][j]。
     *
     * 根据木桶原理，接到的雨水的高度由这个容器周围最短的木板来确定的。我们可以知道容器内水的高度取决于最外层高度最低的方块。
     * 我们假设已经知道最外层的方块接水后的高度的最小值，则此时我们根据木桶原理，肯定可以确定最小高度方块的相邻方块的接水高度。
     * 我们同时更新最外层的方块标记，我们在新的最外层的方块再次找到接水后的高度的最小值，同时确定与其相邻的方块的接水高度。
     * 然后再次更新最外层，依次迭代直到求出所有的方块的接水高度，即可知道矩阵中的接水容量。
     *
     * 这样，就从最外层向内推进。
     *
     * LeetCode 耗时：18 ms - 82.83%
     *          内存消耗：41.2 MB - 88.44%
     */
    public int trapRainWater(int[][] heightMap) {
        if (heightMap.length <= 2 || heightMap[0].length <= 2) {
            return 0;
        }

        final int m = heightMap.length, n = heightMap[0].length;
        // [i,j] 是不是最外层
        boolean[][] isOuterLayer = new boolean[m][n];
        // (i, j, water[i][j])
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]);
        // 初始化二维平面最外围的一圈
        for (int i = 0; i < m; i++) {
            isOuterLayer[i][0] = isOuterLayer[i][n - 1] = true;
            pq.add(new int[]{i, 0, heightMap[i][0]});
            pq.add(new int[]{i, n - 1, heightMap[i][n - 1]});
        }
        for (int j = 1; j < n - 1; j++) {
            isOuterLayer[0][j] = isOuterLayer[m - 1][j] = true;
            pq.add(new int[]{0, j, heightMap[0][j]});
            pq.add(new int[]{m - 1, j, heightMap[m - 1][j]});
        }

        final int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int result = 0;
        while (!pq.isEmpty()){
            int[] pos = pq.remove();
            for (int[] dir : dirs) {
                int r = pos[0] + dir[0], c = pos[1] + dir[1];
                if (r >= 0 && r < m && c >= 0 && c < n && !isOuterLayer[r][c]) {
                    int water = Math.max(pos[2], heightMap[r][c]);
                    result += water - heightMap[r][c];
                    isOuterLayer[r][c] = true;
                    pq.add(new int[]{r, c, water});
                }
            }
        }

        return result;
    }

    @Test
    public void testTrapRainWater() {
        test(this::trapRainWater);
    }
}
