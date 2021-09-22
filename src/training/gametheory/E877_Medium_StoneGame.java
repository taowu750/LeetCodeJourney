package training.gametheory;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 877. 石子游戏: https://leetcode-cn.com/problems/stone-game/
 *
 * 亚历克斯和李用几堆石子在做游戏。偶数堆石子排成一行，每堆都有正整数颗石子 piles[i]。
 * 游戏以谁手中的石子最多来决出胜负。石子的总数是奇数，所以没有平局。
 *
 * 亚历克斯和李轮流进行，亚历克斯先开始。 每回合，玩家从行的开始或结束处取走整堆石头。
 * 这种情况一直持续到没有更多的石子堆为止，此时手中石子最多的玩家获胜。
 *
 * 假设亚历克斯和李都发挥出最佳水平，当亚历克斯赢得比赛时返回 true，当李赢得比赛时返回 false。
 *
 * 例 1：
 * 输入：[5,3,4,5]
 * 输出：true
 * 解释：
 * 亚历克斯先开始，只能拿前 5 颗或后 5 颗石子 。
 * 假设他取了前 5 颗，这一行就变成了 [3,4,5] 。
 * 如果李拿走前 3 颗，那么剩下的是 [4,5]，亚历克斯拿走后 5 颗赢得 10 分。
 * 如果李拿走后 5 颗，那么剩下的是 [3,4]，亚历克斯拿走后 4 颗赢得 9 分。
 * 这表明，取前 5 颗石子对亚历克斯来说是一个胜利的举动，所以我们返回 true。
 *
 * 例 2：
 * 输入：[2, 1, 9, 5]
 * 输出：true
 * 解释：
 * 亚历克斯先开始，拿了 2 颗，然后李拿了 5 颗。然后亚历克斯拿了 9 颗，李拿了最后 1 颗。
 * 可以看到，并不是简单的挑数字大的选。
 *
 * 约束：
 * - 2 <= piles.length <= 500
 * - piles.length 是偶数。
 * - 1 <= piles[i] <= 500
 * - sum(piles) 是奇数。
 */
public class E877_Medium_StoneGame {

    static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{5,3,4,5}));
        assertTrue(method.test(new int[]{2,1,9,5}));
    }

    /**
     * 亚历克斯是必胜无疑的，因为亚历克斯是先手。
     *
     * 我们以 piles=[2, 1, 9, 5] 讲解，假设这四堆石头从左到右的索引分别是 1，2，3，4。
     * 如果我们把这四堆石头按索引的奇偶分为两组，即第 1、3 堆和第 2、4 堆，那么这两组石头的数量一定不同，
     * 也就是说一堆多一堆少。因为石头的总数是奇数，不能被平分。
     *
     * 而作为第一个拿石头的人，你可以控制自己拿到所有偶数堆，或者所有的奇数堆。
     * - 你最开始可以选择第 1 堆或第 4 堆。如果你想要偶数堆，你就拿第 4 堆，这样留给对手的选择只有第 1、3 堆，
     *   他不管怎么拿，第 2 堆又会暴露出来，你就可以拿。
     * - 同理，如果你想拿奇数堆，你就拿第 1 堆，留给对手的只有第 2、4 堆，他不管怎么拿，第 3 堆又给你暴露出来了。
     *
     * 也就是说，你可以在第一步就观察好，奇数堆的石头总数多，还是偶数堆的石头总数多，然后步步为营，就一切尽在掌控之中了。
     *
     * 参见 https://mp.weixin.qq.com/s/Edealug3XsW7pfmQQKIDlA
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.3 MB - 55.55%
     */
    public boolean stoneGame(int[] piles) {
        return true;
    }

    @Test
    public void testStoneGame() {
        test(this::stoneGame);
    }


    /**
     * 参见 https://leetcode-cn.com/problems/stone-game/solution/ji-yi-hua-di-gui-dong-tai-gui-hua-shu-xue-jie-java/
     * 动态规划方法。理解此问题的最好方法是画个选择状态图。
     *
     * LeetCode 耗时：6 ms - 48.47%
     *          内存消耗：39.1 MB - 29.97%
     */
    public boolean dpMethod(int[] piles) {
        final int n = piles.length;
        /*
        dp[i][j] 表示在 piles[i..j] 范围内，先手和后手的分数之差。
        当范围内元素数量为偶数时，表示是亚历克斯先选，否则是李先选（因为亚历克斯先手，且牌堆数初始为偶数）
        也就是说当前自己做出选择的时候，得分为正，留给对方做选择的时候，相对于自己而言，得分为负。
        状态转移：dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1])
        通过这种方式，就能够表达两者分别所做的最优选择。
         */
        final int[][] dp = new int[n][n];
        // base case
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }
        // i > j 的都是无效的

        // 当前状态依赖左边和下方的状态，因此从下到上、从左到右进行扫描
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }

        return dp[0][n-1] > 0;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
