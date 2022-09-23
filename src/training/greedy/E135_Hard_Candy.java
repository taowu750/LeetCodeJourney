package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 135. 分发糖果：https://leetcode-cn.com/problems/candy/
 *
 * 老师想给孩子们分发糖果，有 N 个孩子站成了一条直线，老师会根据每个孩子的表现，预先给他们评分。
 * 你需要按照以下要求，帮助老师给这些孩子分发糖果：
 * - 每个孩子至少分配到 1 个糖果。
 * - 评分更高的孩子必须比他两侧的邻位孩子获得更多的糖果。
 *
 * 那么这样下来，老师至少需要准备多少颗糖果呢？
 *
 * 例 1：
 * 输入：[1,0,2]
 * 输出：5
 * 解释：你可以分别给这三个孩子分发 2、1、2 颗糖果。
 *
 * 例 2：
 * 输入：[1,2,2]
 * 输出：4
 * 解释：你可以分别给这三个孩子分发 1、2、1 颗糖果。
 *      第三个孩子只得到 1 颗糖果，这已满足上述两个条件。
 */
public class E135_Hard_Candy {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(5, method.applyAsInt(new int[]{1,0,2}));
        assertEquals(4, method.applyAsInt(new int[]{1,2,2}));
        assertEquals(1, method.applyAsInt(new int[]{10}));
        assertEquals(3, method.applyAsInt(new int[]{1,3}));
        assertEquals(3, method.applyAsInt(new int[]{3,1}));
        assertEquals(2, method.applyAsInt(new int[]{3,3}));
    }

    /**
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：39.4 MB - 66.71%
     */
    public int candy(int[] ratings) {
        /*
        单增或单减的序列分配的糖果数会变化，所以从左边遍历，找出所有单增的序列，并分配糖果；再从右边遍历，
        找出单增的序列（相对于左边单减），并分配糖果。
        最后对每个位置，计算两次遍历的最大值。
         */

        final int n = ratings.length;
        int[] candies = new int[n];
        Arrays.fill(candies, 1);

        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }

        int sum = 0;
        for (int candy : candies) {
            sum += candy;
        }

        return sum;
    }

    @Test
    public void testCandy() {
        test(this::candy);
    }


    /**
     * 我们从左到右枚举每一个同学，记前一个同学分得的糖果数量为 pre：
     * - 如果当前同学比上一个同学评分高，说明我们就在最近的递增序列中，直接分配给该同学 pre+1 个糖果即可。
     * - 否则我们就在一个递减序列中，我们直接分配给当前同学一个糖果，并把该同学所在的递减序列中所有的同学都再多分配一个糖果，
     *   以保证糖果数量还是满足条件。
     *   - 我们无需显式地额外分配糖果，只需要记录当前的递减序列长度，即可知道需要额外分配的糖果数量。
     *   - 同时注意当当前的递减序列长度和上一个递增序列等长时，需要把最近的递增序列的最后一个同学也并进递减序列中。
     *     之所以要并入，是因为可能后面递减序列还有延续的可能。
     *
     * 这样，我们只要记录当前递减序列的长度 dec，最近的递增序列的长度 inc 和前一个同学分得的糖果数量 pre 即可。
     *
     * 参见：
     * https://leetcode-cn.com/problems/candy/solution/fen-fa-tang-guo-by-leetcode-solution-f01p/
     *
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：39.1 MB - 93.21%
     */
    public int o1Method(int[] ratings) {
        int n = ratings.length, result = 1;
        int inc = 1, dec = 0, pre = 1;
        for (int i = 1; i < n; i++) {
            if (ratings[i] >= ratings[i - 1]) {
                dec = 0;
                pre = ratings[i] == ratings[i - 1] ? 1 : pre + 1;
                result += pre;
                inc = pre;
            } else {
                dec++;
                if (dec == inc) {
                    dec++;
                }
                result += dec;
                pre = 1;
            }
        }

        return result;
    }

    @Test
    public void testO1Method() {
        test(this::o1Method);
    }
}
