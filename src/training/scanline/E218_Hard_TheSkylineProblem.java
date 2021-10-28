package training.scanline;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 218. 天际线问题: https://leetcode-cn.com/problems/the-skyline-problem/
 *
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。给你所有建筑物的位置和高度，
 * 请返回由这些建筑物形成的「天际线」。
 *
 * 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
 * - lefti 是第 i 座建筑物左边缘的 x 坐标。
 * - righti 是第 i 座建筑物右边缘的 x 坐标。
 * - heighti 是第 i 座建筑物的高度。
 *
 * 「天际线」应该表示为由 “关键点” 组成的列表，格式 [[x1,y1],[x2,y2],...] ，并按 x 坐标进行「排序」。
 * 关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的终点，y 坐标始终为 0 ，仅用于标记天际线的终点。
 * 此外，任何两个相邻建筑物之间的地面都应被视为天际线轮廓的一部分。
 *
 * 注意：输出天际线中不得有连续的相同高度的水平线。例如 [...[2 3], [4 5], [7 5], [11 5], [12 7]...]
 * 是不正确的答案；三条高度为 5 的线应该在最终输出中合并为一个：[...[2 3], [4 5], [12 7], ...]
 *
 * 例 1：
 * 输入：buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
 * 输出：[[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
 * 解释：
 * 图 A 显示输入的所有建筑物的位置和高度，
 * 图 B 显示由这些建筑物形成的天际线。图 B 中的红点表示输出列表中的关键点。
 *
 * 例 2：
 * 输入：buildings = [[0,2,3],[2,5,3]]
 * 输出：[[0,3],[5,0]]
 *
 * 约束：
 * - 1 <= buildings.length <= 10^4
 * - 0 <= lefti < righti <= 2^31 - 1
 * - 1 <= heighti <= 2^31 - 1
 * - buildings 按 lefti 「非递减排序」
 */
public class E218_Hard_TheSkylineProblem {

    public static void test(Function<int[][], List<List<Integer>>> method) {
        assertEquals(asList(asList(2,10), asList(3,15), asList(7,12), asList(12,0), asList(15,10), asList(20,8), asList(24,0)),
                method.apply(new int[][]{{2,9,10}, {3,7,15}, {5,12,12}, {15,20,10}, {19,24,8}}));

        assertEquals(asList(asList(0, 3), asList(5, 0)), method.apply(new int[][]{{0,2,3}, {2,5,3}}));
    }

    /**
     * 扫描线算法，用到了优先队列+延迟删除技巧，参见：
     * https://leetcode-cn.com/problems/the-skyline-problem/solution/you-xian-dui-lie-java-by-liweiwei1419-jdb5/
     *
     * 扫描线的思想应用参见：
     * https://leetcode-cn.com/problems/the-skyline-problem/solution/gong-shui-san-xie-sao-miao-xian-suan-fa-0z6xc/
     *
     * 因为在 Java 的优先队列中移除一个元素（remove() 方法），是个耗时的操作（先线性找到这个元素 O(N)，再移除 O(logN)）。
     * 这里引入「延迟删除」技巧，设计一个哈希表 delayed，记录删除元素，以及被删除的次数。规则是：一旦堆顶元素在延迟删除集合中：
     * - 删除堆顶元素；
     * - 延迟删除的哈希表里，对应的次数 -1。
     * 反复这样做下去，直到堆顶元素不在延迟删除的集合中。
     *
     * 如果把天际线「从左到右」一笔画出来，「关键点」是转折点。
     * 「关键点」出现在 从「竖直方向」转向「水平方向」的地方，从「水平方向」到「竖直方向」不产生关键点，
     * 因此有竖直方向移动产生高度差的地方，就会出现「关键点」。
     *
     * 这里「关键点」按照「竖直方向」是「从下到上」还是「从上到下」，可以分为两种情况，我们分别命名成「规则 1」和「规则 2」：
     * - 规则 1：如果是「从下到上」转向「水平方向」，纵坐标最大的点是关键点；
     * - 规则 2：如果是「从上到下」转向「水平方向」，纵坐标第二大的点是关键点。
     *
     * 优先队列中保存的是已经扫描过的点的「纵坐标」。
     * - 先把输入数组处理成点的样子，也就是处理成「(横坐标, 纵坐标)」的格式；
     * - 由于要区分点是位于「从下到上」还是「从上到下」的地方，可以把左端点的纵坐标处理成负数：
     *   - 如果当前是「从下到上」，这个负数的纵坐标先要变成正数，然后参与最大的点的选拔；
     *   - 如果当前是「从上到下」，它就是正数，它不可以参与最大点的选拔，需要将它从优先队列中删除。
     * - 把输入数组按照左端点升序排序，表示我们是「从左到右」依次扫描这些矩形。如果遇到左端点重合的情况，
     *   让纵坐标大的排在前面，因此左端点一定都是「从下到上」，由「规则 1」让纵坐标大的先出现，
     *   纵坐标小的就没有出头之日，就不会被选中。
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        // 第 1 步：预处理，将矩形转化为点
        int[][] buildingPoints = new int[buildings.length * 2][2];
        int i = 0;
        for (int[] building : buildings) {
            // 负号表示左边高度的转折点
            buildingPoints[i][0] = building[0];
            buildingPoints[i][1] = -building[2];
            i++;
            buildingPoints[i][0] = building[1];
            buildingPoints[i][1] = building[2];
            i++;
        }

        // 第 2 步：按照横坐标排序，横坐标相同的时候，高度高的在前面
        Arrays.sort(buildingPoints, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                // 注意：这里因为左端点传进去的时候，数值是负的，在比较的时候还按照升序排序
                return a[1] - b[1];
            }
        });

        // 第 3 步：扫描一遍动态计算出结果
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        // 哈希表，记录「延迟删除」的元素，key 为元素，value 为需要删除的次数
        Map<Integer, Integer> delayedMap = new HashMap<>();

        // 最开始的时候，需要产生高度差，所以需要加上一个高度为 0，宽度为 0 的矩形
        maxHeap.offer(0);
        // 为了计算高度差，需要保存之前最高的高度
        int lastHeight = 0;
        List<List<Integer>> res = new ArrayList<>();
        // 遍历数组，可以看做是在水平方向上移动
        for (int[] buildingPoint : buildingPoints) {
            if (buildingPoint[1] < 0) {
                // 说明此时是「从下到上」，纵坐标参与选拔最大值，请见「规则 1」
                maxHeap.add(-buildingPoint[1]);
            } else {
                /*
                这里不需要把高度添加到堆中，因为左端点和右端点高度是一样的，之前就已经存在了。
                把 buildingPoint[1] 放进 delayed，等到堆顶元素是它的时候，才真的删除
                 */
                delayedMap.merge(buildingPoint[1], 1, Integer::sum);
            }

            /*
            如果堆顶元素在延迟删除集合中，才真正删除，这一步可能执行多次，所以放在 while 中。
            while (true) 都是可以的，maxHeap 一定不会为空，因为有最开始的一个 0
             */
            while (true) {
                int maxHeight = maxHeap.element();
                if (delayedMap.containsKey(maxHeight)) {
                    delayedMap.merge(maxHeight, -1, Integer::sum);
                    if (delayedMap.get(maxHeight) == 0) {
                        delayedMap.remove(maxHeight);
                    }
                    maxHeap.poll();
                } else {
                    break;
                }
            }

            int curHeight = maxHeap.element();
            // 有高度差，才有关键点出现
            if (curHeight != lastHeight) {
                res.add(Arrays.asList(buildingPoint[0], curHeight));
                lastHeight = curHeight;
            }
        }

        return res;
    }

    @Test
    public void testGetSkyline() {
        test(this::getSkyline);
    }


    /**
     * 整体思路和上面的方法相同，只是使用 TreeMap 替换了优先队列+Map。
     *
     * LeetCode 耗时：19 ms - 51.78%
     *          内存消耗：42.6 MB - 5.05%
     */
    public List<List<Integer>> treeMapMethod(int[][] buildings) {
        int[][] buildingPoints = new int[buildings.length * 2][2];
        int i = 0;
        for (int[] building : buildings) {
            buildingPoints[i][0] = building[0];
            buildingPoints[i][1] = -building[2];
            i++;
            buildingPoints[i][0] = building[1];
            buildingPoints[i][1] = building[2];
            i++;
        }

        Arrays.sort(buildingPoints, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                return a[1] - b[1];
            }
        });

        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(0, 1);
        int lastHeight = 0;
        List<List<Integer>> res = new ArrayList<>();
        for (int[] buildingPoint : buildingPoints) {
            if (buildingPoint[1] < 0) {
                treeMap.merge(-buildingPoint[1], 1, Integer::sum);
            } else {
                treeMap.merge(buildingPoint[1], -1, Integer::sum);
            }

            while (true) {
                Map.Entry<Integer, Integer> max = treeMap.lastEntry();
                if (max.getValue() == 0) {
                    treeMap.pollLastEntry();
                } else {
                    break;
                }
            }

            int curHeight = treeMap.lastKey();
            if (curHeight != lastHeight) {
                res.add(Arrays.asList(buildingPoint[0], curHeight));
                lastHeight = curHeight;
            }
        }

        return res;
    }

    @Test
    public void testTreeMapMethod() {
        test(this::treeMapMethod);
    }
}
