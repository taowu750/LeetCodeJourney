package training.thinkstraight;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 781. 森林中的兔子: https://leetcode-cn.com/problems/rabbits-in-forest/
 *
 * 森林中有未知数量的兔子。提问其中若干只兔子 "还有多少只兔子与你（指被提问的兔子）颜色相同?" ，
 * 将答案收集到一个整数数组 answers 中，其中 answers[i] 是第 i 只兔子的回答。
 *
 * 给你数组 answers ，返回森林中兔子的最少数量。
 *
 * 例 1：
 * 输入：answers = [1,1,2]
 * 输出：5
 * 解释：
 * 两只回答了 "1" 的兔子可能有相同的颜色，设为红色。
 * 之后回答了 "2" 的兔子不会是红色，否则他们的回答会相互矛盾。
 * 设回答了 "2" 的兔子为蓝色。
 * 此外，森林中还应有另外 2 只蓝色兔子的回答没有包含在数组中。
 * 因此森林中兔子的最少数量是 5 只：3 只回答的和 2 只没有回答的。
 *
 * 例 2：
 * 输入：answers = [10,10,10]
 * 输出：11
 *
 * 说明：
 * - 1 <= answers.length <= 1000
 * - 0 <= answers[i] < 1000
 */
public class E781_Medium_RabbitsInForest {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(5, method.applyAsInt(new int[]{1,1,2}));
        assertEquals(11, method.applyAsInt(new int[]{10,10,10}));
        assertEquals(1, method.applyAsInt(new int[]{0}));
        assertEquals(3, method.applyAsInt(new int[]{0,0,0}));
    }

    /**
     * LeetCode 耗时：2 ms - 73.90%
     *          内存消耗：40.8 MB - 19.58%
     */
    public int numRabbits(int[] answers) {
        /*
        回答相同数量的兔子可能是同一种颜色，但前提是它们回答的数量必须能够包含它们自己。
        所以可以先排序，让同一数量的兔子排在一起
         */
        Arrays.sort(answers);

        int result = 0;
        // 令 number 为当前组内兔子的数量，cap 为当前组内兔子的最大容量
        for (int i = 0, number = 1, cap = answers[0] + 1; i < answers.length; i++) {
            // 当数量没有超过容量，并且下一个兔子的回答和当前兔子相同，则它们可以是同色的
            if (number < cap && i < answers.length - 1 && answers[i] == answers[i + 1]) {
                number++;
            } else {  // 否则不可能同色，则更新结果和变量
                result += cap;
                if (i < answers.length - 1) {
                    number = 1;
                    cap = answers[i + 1] + 1;
                }
            }
        }

        return result;
    }

    @Test
    public void testNumRabbits() {
        test(this::numRabbits);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/rabbits-in-forest/solution/sen-lin-zhong-de-tu-zi-by-leetcode-solut-kvla/
     *
     * 如果有 x 只兔子都回答 y，则至少有 ⌈x/y+1⌉ 种不同的颜色，且每种颜色有 y+1 只兔子，因此兔子数至少为
     *          ⌈x/y+1⌉·(y+1)
     *
     * LeetCode 耗时：3 ms - 63.69%
     *          内存消耗：40.6 MB - 32.06%
     */
    public int hashMethod(int[] answers) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int y : answers) {
            count.merge(y, 1, Integer::sum);
        }

        int ans = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            int y = entry.getKey(), x = entry.getValue();
            ans += (x + y) / (y + 1) * (y + 1);
        }

        return ans;
    }

    @Test
    public void testHashMethod() {
        test(this::hashMethod);
    }
}
