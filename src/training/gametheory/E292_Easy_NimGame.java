package training.gametheory;

import org.junit.jupiter.api.Test;

import java.util.function.IntPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 你和你的朋友，两个人一起玩 Nim 游戏：
 * - 桌子上有一堆石头。
 * - 你们轮流进行自己的回合，你作为先手。
 * - 每一回合，轮到的人拿掉 1 - 3 块石头。
 * - 拿掉最后一块石头的人就是获胜者。
 *
 * 假设你们每一步都是最优解。请编写一个函数，来判断你是否可以在给定石头数量为 n 的情况下赢得游戏。
 * 如果可以赢，返回 true；否则，返回 false 。
 *
 * 例 1：
 * 输入：n = 4
 * 输出：false
 * 解释：如果堆中有 4 块石头，那么你永远不会赢得比赛；
 *      因为无论你拿走 1 块、2 块 还是 3 块石头，最后一块石头总是会被你的朋友拿走。
 *
 * 例 2：
 * 输入：n = 1
 * 输出：true
 *
 * 例 3：
 * 输入：n = 2
 * 输出：true
 *
 * 约束：
 * - 1 <= n <= 2**31 - 1
 */
public class E292_Easy_NimGame {

    static void test(IntPredicate method) {
        assertFalse(method.test(4));
        assertTrue(method.test(1));
        assertTrue(method.test(2));
    }

    /**
     * 解决这种问题的思路一般都是「反着思考」：
     * - 如果我能赢，那么最后轮到我取石子的时候必须要剩下 1~3 颗石子，这样我才能一把拿完。
     * - 如何营造这样的一个局面呢？显然，如果对手拿的时候只剩 4 颗石子，那么无论他怎么拿，
     *   总会剩下 1~3 颗石子，我就能赢。
     * - 如何逼迫对手面对 4 颗石子呢？要想办法，让我选择的时候还有 5~7 颗石子，
     *   这样的话我就有把握让对方不得不面对 4 颗石子。
     * - 如何营造 5~7 颗石子的局面呢？让对手面对 8 颗石子，无论他怎么拿，都会给我剩下 5~7 颗，我就能赢。
     *
     * 这样一直循环下去，我们发现只要踩到 4 的倍数，就落入了圈套，永远逃不出 4 的倍数，而且一定会输。
     *
     * 思路参见：https://mp.weixin.qq.com/s/Edealug3XsW7pfmQQKIDlA
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.2 MB - 46.15%
     */
    public boolean canWinNim(int n) {
        return n % 4 != 0;
    }

    @Test
    public void testCanWinNim() {
        test(this::canWinNim);
    }
}
