package training.dynamicprogramming;

import org.junit.jupiter.api.Test;
import util.datastructure.function.FourFunction;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1687. 从仓库到码头运输箱子: https://leetcode-cn.com/problems/delivering-boxes-from-storage-to-ports/
 *
 * 你有一辆货运卡车，你需要用这一辆车把一些箱子从仓库运送到码头。
 * 这辆卡车每次运输有「箱子数目的限制」和「总重量的限制」。
 *
 * 给你一个箱子数组 boxes 和三个整数 portsCount, maxBoxes 和 maxWeight，
 * 其中 boxes[i] = [portsi, weighti]。
 * - portsi 表示第 i 个箱子需要送达的码头，weightsi 是第 i 个箱子的重量。
 * - portsCount 是码头的数目。
 * - maxBoxes 和 maxWeight 分别是卡车每趟运输箱子数目和重量的限制。
 *
 * 箱子需要按照「数组顺序」运输，同时每次运输需要遵循以下步骤：
 * 1. 卡车从 boxes 队列中按顺序取出若干个箱子，但不能违反 maxBoxes 和 maxWeight 限制。
 * 2. 对于在卡车上的箱子，我们需要「按顺序」处理它们，卡车会通过「一趟行程」将最前面的箱子送到目的地码头并卸货。
 *    如果卡车已经在对应的码头，那么不需要「额外行程」，箱子也会立马被卸货。
 * 3. 卡车上所有箱子都被卸货后，卡车需要「一趟行程」回到仓库，从箱子队列里再取出一些箱子。
 *
 * 卡车在将所有箱子运输并卸货后，最后必须回到仓库。
 * 请你返回将所有箱子送到相应码头的「最少行程」次数。
 *
 * 例 1：
 * 输入：boxes = [[1,1],[2,1],[1,1]], portsCount = 2, maxBoxes = 3, maxWeight = 3
 * 输出：4
 * 解释：最优策略如下：
 * - 卡车将所有箱子装上车，到达码头 1 ，然后去码头 2 ，然后再回到码头 1 ，最后回到仓库，总共需要 4 趟行程。
 * 所以总行程数为 4 。
 *
 * 例 2：
 * 输入：boxes = [[1,2],[3,3],[3,1],[3,1],[2,4]], portsCount = 3, maxBoxes = 3, maxWeight = 6
 * 输出：6
 * 解释：最优策略如下：
 * - 卡车首先运输第一个箱子，到达码头 1 ，然后回到仓库，总共 2 趟行程。
 * - 卡车运输第二、第三、第四个箱子，到达码头 3 ，然后回到仓库，总共 2 趟行程。
 * - 卡车运输第五个箱子，到达码头 3 ，回到仓库，总共 2 趟行程。
 * 总行程数为 2 + 2 + 2 = 6 。
 *
 * 例 3：
 * 输入：boxes = [[1,4],[1,2],[2,1],[2,1],[3,2],[3,4]], portsCount = 3, maxBoxes = 6, maxWeight = 7
 * 输出：6
 * 解释：最优策略如下：
 * - 卡车运输第一和第二个箱子，到达码头 1 ，然后回到仓库，总共 2 趟行程。
 * - 卡车运输第三和第四个箱子，到达码头 2 ，然后回到仓库，总共 2 趟行程。
 * - 卡车运输第五和第六个箱子，到达码头 3 ，然后回到仓库，总共 2 趟行程。
 * 总行程数为 2 + 2 + 2 = 6 。
 *
 * 例 4：
 * 输入：boxes = [[2,4],[2,5],[3,1],[3,2],[3,7],[3,1],[4,4],[1,3],[5,2]], portsCount = 5, maxBoxes = 5, maxWeight = 7
 * 输出：14
 * 解释：最优策略如下：
 * - 卡车运输第一个箱子，到达码头 2 ，然后回到仓库，总共 2 趟行程。
 * - 卡车运输第二个箱子，到达码头 2 ，然后回到仓库，总共 2 趟行程。
 * - 卡车运输第三和第四个箱子，到达码头 3 ，然后回到仓库，总共 2 趟行程。
 * - 卡车运输第五个箱子，到达码头 3 ，然后回到仓库，总共 2 趟行程。
 * - 卡车运输第六和第七个箱子，到达码头 3 ，然后去码头 4 ，然后回到仓库，总共 3 趟行程。
 * - 卡车运输第八和第九个箱子，到达码头 1 ，然后去码头 5 ，然后回到仓库，总共 3 趟行程。
 * 总行程数为 2 + 2 + 2 + 2 + 3 + 3 = 14 。
 *
 * 约束：
 * - 1 <= boxes.length <= 10**5
 * - 1 <= portsCount, maxBoxes, maxWeight <= 10**5
 * - 1 <= portsi <= portsCount
 * - 1 <= weightsi <= maxWeight
 */
public class E1687_Hard_DeliveringBoxesFromStorageToPorts {

    static void test(FourFunction<int[][], Integer, Integer, Integer, Integer> method) {
        assertEquals(4, method.apply(new int[][]{{1, 1}, {2, 1}, {1, 1}}, 2, 3, 3));
        assertEquals(6, method.apply(new int[][]{{1, 2}, {3, 3}, {3, 1}, {3, 1}, {2, 4}},
                3, 3, 6));
        assertEquals(6, method.apply(new int[][]{{1, 4}, {1, 2}, {2, 1}, {2, 1}, {3, 2}, {3, 4}},
                3, 6, 7));
        assertEquals(14, method.apply(
                new int[][]{{2, 4}, {2, 5}, {3, 1}, {3, 2}, {3, 7}, {3, 1}, {4, 4}, {1, 3}, {5, 2}},
                5, 5, 7));
    }

    /**
     * 动态规划。
     *
     * 参见：https://leetcode-cn.com/problems/delivering-boxes-from-storage-to-ports/solution/cong-cang-ku-dao-ma-tou-yun-shu-xiang-zi-dqnq/
     */
    public int boxDelivering(int[][] boxes, int portsCount, int maxBoxes, int maxWeight) {
        int n = boxes.length;
        // weightPrefix 表示前 i 个重量的前缀和，portDiffPrefix 表示前 i 个相邻两项不等的次数的前缀和
        int[] weightPrefix = new int[n + 1], portDiffPrefix = new int[n + 1];
        weightPrefix[1] = boxes[0][1];
        for (int i = 2; i <= n; i++) {
            weightPrefix[i] = weightPrefix[i - 1] + boxes[i - 1][1];
            portDiffPrefix[i] = portDiffPrefix[i - 1] + (boxes[i - 1][0] != boxes[i - 2][0] ? 1 : 0);
        }

        /*
        dp[i] 表示运送前 i 个箱子所需的最小行程数。状态转移方程如下：
        dp[i] = min{dp[j] + portDiffPrefix[j + 1, i] + 2}
        subject: i - j <= maxBoxes, weightPrefix[i] - weightPrefix[j] <= maxWeight

        最终时间复杂度为 O(N^2)
         */
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i - 1] + 2;
            for (int j = i - 2; j >= 0 && i - j <= maxBoxes
                    && weightPrefix[i] - weightPrefix[j] <= maxWeight; j--) {
                dp[i] = Math.min(dp[j] + portDiffPrefix[i] - portDiffPrefix[j + 1] + 2, dp[i]);
            }
        }

        return dp[n];
    }

    @Test
    public void testBoxDelivering() {
        test(this::boxDelivering);
    }


    /**
     * 动态规划+单调队列优化。
     *
     * LeetCode 耗时：31 ms - 40.00%
     *          内存消耗：85 MB - 71.11%
     */
    public int monotonicQueueOptimize(int[][] boxes, int portsCount, int maxBoxes, int maxWeight) {
        int n = boxes.length;
        int[] weightPrefix = new int[n + 1], portDiffPrefix = new int[n + 1];
        weightPrefix[1] = boxes[0][1];
        for (int i = 2; i <= n; i++) {
            weightPrefix[i] = weightPrefix[i - 1] + boxes[i - 1][1];
            portDiffPrefix[i] = portDiffPrefix[i - 1] + (boxes[i - 1][0] != boxes[i - 2][0] ? 1 : 0);
        }

        int[] dp = new int[n + 1];
        /*
        注意到 portDiffPrefix[j + 1, i] = portDiffPrefix[i] - portDiffPrefix[j + 1]
        因此：dp[i] = min{dp[j] - portDiffPrefix[j + 1]} + portDiffPrefix[i] + 2;

        而求滑动窗口的最值正是单调队列的长处。这里我们用 minDp 存上面的最小值，
        在单调队列存 minDp 中的下标。存下标方便判断 maxBoxes 和 maxWeight 条件。
         */
        int[] minDp = new int[n + 1];
        LinkedList<Integer> monoQueue = new LinkedList<>();
        monoQueue.addLast(0);
        for (int i = 1; i <= n; i++) {
            // 去除不符合限制条件的下标
            while (!monoQueue.isEmpty() && (i - monoQueue.getFirst() > maxBoxes
                    || weightPrefix[i] - weightPrefix[monoQueue.getFirst()] > maxWeight)) {
                monoQueue.removeFirst();
            }

            dp[i] = minDp[monoQueue.getFirst()] + portDiffPrefix[i] + 2;

            if (i != n) {
                minDp[i] = dp[i] - portDiffPrefix[i + 1];
                // 向单调队列中加入新元素
                while (!monoQueue.isEmpty() && minDp[i] < minDp[monoQueue.getLast()]) {
                    monoQueue.removeLast();
                }
                monoQueue.addLast(i);
            }
        }

        return dp[n];
    }

    @Test
    public void test() {
        test(this::monotonicQueueOptimize);
    }
}
