package training.scanline;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 554. 砖墙: https://leetcode-cn.com/problems/brick-wall/
 *
 * 你的面前有一堵矩形的、由 n 行砖块组成的砖墙。这些砖块高度相同（也就是一个单位高）但是宽度不同。
 * 每一行砖块的宽度之和相等。
 *
 * 你现在要画一条「自顶向下」的、穿过「最少」砖块的垂线。如果你画的线只是从砖块的边缘经过，就不算穿过这块砖。
 * 你不能沿着墙的两个垂直边缘之一画线，这样显然是没有穿过一块砖的。
 *
 * 给你一个二维数组 wall ，该数组包含这堵墙的相关信息。其中，wall[i] 是一个代表从左至右每块砖的宽度的数组。
 * 你需要找出怎样画才能使这条线「穿过的砖块数量最少」，并且返回「穿过的砖块数量」。
 *
 * 例 1：
 * 输入：wall = [[1,2,2,1],[3,1,2],[1,3,2],[2,4],[3,1,2],[1,3,1,1]]
 * 输出：2
 *
 * 例 2：
 * 输入：wall = [[1],[1],[1]]
 * 输出：3
 *
 * 约束：
 * - n == wall.length
 * - 1 <= n <= 10^4
 * - 1 <= wall[i].length <= 10^4
 * - 1 <= sum(wall[i].length) <= 2 * 10^4
 * - 对于每一行 i ，sum(wall[i]) 是相同的
 * - 1 <= wall[i][j] <= 2^31 - 1
 */
public class E554_Medium_BrickWall {

    public static void test(ToIntFunction<List<List<Integer>>> method) {
        assertEquals(2, method.applyAsInt(asList(asList(1,2,2,1), asList(3,1,2), asList(1,3,2), asList(2,4),
                asList(3,1,2), asList(1,3,1,1))));
        assertEquals(3, method.applyAsInt(asList(singletonList(1), singletonList(1), singletonList(1))));
    }

    /**
     * 扫描线算法，类似于 {@link training.scanline.E391_Hard_PerfectRectangle#scanlineMethod(int[][])}。
     *
     * LeetCode 耗时：17 ms - 13.04%
     *          内存消耗：44.3 MB - 5.06%
     */
    public int leastBricks(List<List<Integer>> wall) {
        int n = wall.size();
        // 扫描线，记录每个砖块的右边界 (x, downY)；因为高度都为 1，而且不重叠，没必要记录 topY
        PriorityQueue<int[]> scanLines = new PriorityQueue<>((a, b) -> {
            int cmp = Integer.compare(a[0], b[0]);
            return cmp != 0 ? cmp : Integer.compare(a[1], b[1]);
        });

        // 假设坐标原点在左上角，y 轴向下延伸
        for (int i = 0; i < n; i++) {
            // 最后一个砖块不添加，因为它的扫描线处在墙的右垂直边缘
            for (int k = 0, j = 0, size = wall.get(i).size(); k < size - 1; k++) {
                int len = wall.get(i).get(k);
                scanLines.add(new int[]{j + len, i});
                j += len;
            }
        }

        int result = n;
        while (!scanLines.isEmpty()) {
            int sameX = 1;
            // 记录所有扫描线相同的砖块
            int[] top = scanLines.remove();
            while (!scanLines.isEmpty() && scanLines.peek()[0] == top[0]) {
                sameX++;
                scanLines.remove();
            }
            // 从扫描线穿过去就不会经过砖块
            result = Math.min(result, n - sameX);
        }

        return result;
    }

    @Test
    public void testLeastBricks() {
        test(this::leastBricks);
    }


    /**
     * 观察上面的扫描线算法，可以发现我们只需要知道相同右边界 x 的砖块数量即可。
     *
     * LeetCode 耗时：10 ms - 99.68%
     *          内存消耗：42.9 MB - 5.06%
     */
    public int improveMethod(List<List<Integer>> wall) {
        Map<Integer, Integer> scanlineCnts = new HashMap<>();
        for (List<Integer> line : wall) {
            // 最后一个砖块不添加，因为它的扫描线处在墙的右垂直边缘
            for (int k = 0, j = 0, size = line.size(); k < size - 1; k++) {
                int len = line.get(k);
                scanlineCnts.merge(j + len, 1, Integer::sum);
                j += len;
            }
        }

        int n = wall.size(), result = n;
        for (int sameX : scanlineCnts.values()) {
            result = Math.min(result, n - sameX);
        }

        return result;
    }

    @Test
    public void testImproveMethod() {
        test(this::improveMethod);
    }
}
