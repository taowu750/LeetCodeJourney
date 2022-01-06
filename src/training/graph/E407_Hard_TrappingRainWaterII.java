package training.graph;

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
     * 根据木桶原理，接到的雨水的高度由这个容器周围最短的木板来确定的。我们可以知道「容器内水的高度取决于最外层高度最低的方块」。
     * 我们假设已经知道最外层的方块接水后的高度的最小值，则此时我们根据木桶原理，肯定可以确定最小高度方块的相邻方块的接水高度。
     * 我们同时更新最外层的方块标记，我们在新的最外层的方块再次找到接水后的高度的最小值，同时确定与其相邻的方块的接水高度。
     * 然后再次更新最外层，依次迭代直到求出所有的方块的接水高度，即可知道矩阵中的接水容量。
     *
     * 这样，就从最外层向内推进。核心思想就是先确定木桶的外围，找到外围的最短板子后对其周围能填水的地方填水，然后更新木桶外围。
     *
     * 此方法是一个 Dijkstra 算法的变形，参见：
     * https://leetcode-cn.com/problems/trapping-rain-water-ii/solution/gong-shui-san-xie-jing-dian-dijkstra-yun-13ik/
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


    /**
     * 参见 https://leetcode-cn.com/problems/trapping-rain-water-ii/solution/jie-yu-shui-ii-by-leetcode-solution-vlj3/
     *
     * 我们假设初始时矩阵的每个格子都接满了水，且高度均为 maxHeight，其中 maxHeight 为矩阵中高度最高的格子。
     *
     * 我们首先假设每个方块 (i,j) 的接水后的高度均为 water[i][j]=maxHeight，首先我们知道最外层的方块的肯定不能接水，
     * 所有的多余的水都会从最外层的方块溢出，我们每次发现当前方块 (i,j) 的接水高度 water[i][j] 小于与它相邻的 4 个模块的接水高度时，
     * 则我们将进行调整接水高度，我们将其相邻的四个方块的接水高度调整与 (i,j) 的高度保持一致，我们不断重复的进行调整，
     * 直到所有的方块的接水高度不再有调整时即为满足要求。
     *
     * 这也是从最外层向内推进的算法。只不过上面是“填水”的算法，这个是“漏水”的算法。
     *
     * LeetCode 耗时：16 ms - 90.73%
     *          内存消耗：42.9 MB - 11.05%
     */
    public int bfsMethod(int[][] heightMap) {
        if (heightMap.length <= 2 || heightMap[0].length <= 2) {
            return 0;
        }

        final int m = heightMap.length, n = heightMap[0].length;
        // 求 maxHeight
        int maxHeight = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                maxHeight = Math.max(maxHeight, heightMap[i][j]);
            }
        }
        // 初始化方块接水高度为 maxHeight
        int[][] water = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                water[i][j] = maxHeight;
            }
        }
        // queue 装着 (i, j)
        Queue<int[]> queue = new LinkedList<>();
        // 最外层的方块 water[i][j] 等于 heightMap[i][j]
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    if (water[i][j] > heightMap[i][j]) {
                        water[i][j] = heightMap[i][j];
                        queue.add(new int[]{i, j});
                    }
                }
            }
        }

        final int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] pos = queue.remove();
            int i = pos[0], j = pos[1];
            for (int[] dir : dirs) {
                int ni = i + dir[0], nj = j + dir[1];
                // 如果 water[i][j] 旁边的方块 g 接水高度大于自己，并且也大于 g 自身的高度，那么 g 多余的水就会流走
                if (ni >= 0 && ni < m && nj >= 0 && nj < n &&
                        water[ni][nj] > water[i][j] && water[ni][nj] > heightMap[ni][nj]) {
                    water[ni][nj] = Math.max(water[i][j], heightMap[ni][nj]);
                    queue.add(new int[]{ni, nj});
                }
            }
        }

        int result = 0;
        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                result += water[i][j] - heightMap[i][j];
            }
        }

        return result;
    }

    @Test
    public void testBfsMethod() {
        test(this::bfsMethod);
    }
}
