package company.wangyi;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * https://www.nowcoder.com/questionTerminal/14c0359fb77a48319f0122ec175c9ada
 *
 * 有三种葡萄，每种分别有 a,b,c 颗。有三个人，第一个人只吃第 1,2 种葡萄，第二个人只吃第 2,3 种葡萄，
 * 第三个人只吃第 1,3 种葡萄。
 * 适当安排三个人使得吃完所有的葡萄,并且且三个人中吃的最多的那个人吃得尽量少。
 *
 * 例 1：
 * 输入：1 2 3
 * 输出：2
 *
 * 例 2：
 * 输入：1 2 6
 * 输出：3
 *
 * 例 3：
 * 输入：12 13 11
 * 输出：12
 */
public class WY1_Medium_EatingGrapes {

    static void test(TriFunction<Integer, Integer, Integer, Integer> method) {
        assertEquals(2, method.apply(1,2,3));
        assertEquals(3, method.apply(1,2,6));
        assertEquals(12, method.apply(12,13,11));
    }

    /**
     * 把 a、b、c 看成是三角形的三条边，三个人分别要在三个顶点处往相邻边切分。
     *
     * 参见：https://labuladong.gitee.io/algo/5/34/
     */
    public int eatingGrapes(int a, int b, int c) {
        int[] grapes = {a, b, c};
        Arrays.sort(grapes);

        if (2 * (grapes[0] + grapes[1]) >= grapes[2]) {
            // 向上取整小技巧：(M + (N - 1)) / N
            return (a + b + c + 2) / 3;
        } else {
            return (grapes[2] + 1) / 2;
        }
    }

    @Test
    public void testEatingGrapes() {
        test(this::eatingGrapes);
    }
}
