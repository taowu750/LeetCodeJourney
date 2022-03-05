package training.stack;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 735. 行星碰撞: https://leetcode-cn.com/problems/asteroid-collision/
 *
 * 给定一个整数数组 asteroids，表示在同一行的行星。
 *
 * 对于数组中的每一个元素，其绝对值表示行星的大小，正负表示行星的移动方向（正表示向右移动，负表示向左移动）。
 * 每一颗行星以相同的速度移动。
 *
 * 找出碰撞后剩下的所有行星。碰撞规则：两个行星相互碰撞，较小的行星会爆炸。如果两颗行星大小相同，则两颗行星都会爆炸。
 * 两颗移动方向相同的行星，永远不会发生碰撞。
 *
 * 例 1：
 * 输入：asteroids = [5,10,-5]
 * 输出：[5,10]
 * 解释：10 和 -5 碰撞后只剩下 10 。 5 和 10 永远不会发生碰撞。
 *
 * 例 2：
 * 输入：asteroids = [8,-8]
 * 输出：[]
 * 解释：8 和 -8 碰撞后，两者都发生爆炸。
 *
 * 例 3：
 * 输入：asteroids = [10,2,-5]
 * 输出：[10]
 * 解释：2 和 -5 发生碰撞后剩下 -5 。10 和 -5 发生碰撞后剩下 10 。
 *
 * 例 4：
 * 输入：asteroids = [-2,-1,1,2]
 * 输出：[-2,-1,1,2]
 * 解释：-2 和 -1 向左移动，而 1 和 2 向右移动。 由于移动方向相同的行星不会发生碰撞，所以最终没有行星发生碰撞。
 *
 * 说明：
 * - 2 <= asteroids.length <= 10^4
 * - -1000 <= asteroids[i] <= 1000
 * - asteroids[i] != 0
 */
public class E735_Medium_AsteroidCollision {

    public static void test(UnaryOperator<int[]> method) {
        assertArrayEquals(new int[]{5,10}, method.apply(new int[]{5,10,-5}));
        assertArrayEquals(new int[]{}, method.apply(new int[]{8,-8}));
        assertArrayEquals(new int[]{10}, method.apply(new int[]{10,2,-5}));
        assertArrayEquals(new int[]{-2,-1,1,2}, method.apply(new int[]{-2,-1,1,2}));
    }

    /**
     * LeetCode 耗时：3 ms - 84.27%
     *          内存消耗：42.3 MB - 16.89%
     */
    public int[] asteroidCollision(int[] asteroids) {
        Deque<Integer> stack = new ArrayDeque<>(asteroids.length);
        for (int i = 0; i < asteroids.length;) {
            if (stack.isEmpty() || !canCollision(stack.getLast(), asteroids[i])) {
                stack.addLast(asteroids[i]);
                i++;
            } else {
                int cmp = Math.abs(stack.getLast()) - Math.abs(asteroids[i]);
                if (cmp == 0) {
                    stack.removeLast();
                    i++;
                } else if (cmp < 0) {
                    stack.removeLast();
                } else {
                    i++;
                }
            }
        }

        int[] result = new int[stack.size()];
        int i = 0;
        for (int a : stack) {
            result[i++] = a;
        }

        return result;
    }

    private boolean canCollision(int a, int b) {
        int da = a / Math.abs(a), db = b / Math.abs(b);
        return da == 1 && db == -1;
    }

    @Test
    public void testAsteroidCollision() {
        test(this::asteroidCollision);
    }
}
