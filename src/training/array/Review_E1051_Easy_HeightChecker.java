package training.array;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个数组，数组中是学生的高度，要求将学生以身高非递减的顺序排列。
 * 返回为了排成非递减顺序而需要移动的学生数量的最小值。
 * <p>
 * 例 1：
 * Input: heights = [1,1,4,2,1,3]
 * Output: 3
 * Explanation:
 * 当前数组：[1,1,4,2,1,3]；目标数组：[1,1,1,2,3,4]
 * 在索引 2，因为 4 不属于这里，所以我们必须移动这个学生。
 * 在索引 4，因为 1 不属于这里，所以我们必须移动这个学生。
 * 在索引 5，因为 3 不属于这里，所以我们必须移动这个学生。
 * <p>
 * 例 2：
 * Input: heights = [5,1,2,3,4]
 * Output: 5
 * <p>
 * 例 3：
 * Input: heights = [1,2,3,4,5]
 * Output: 0
 * <p>
 * 约束：
 * - 1 <= heights.length <= 100
 * - 1 <= heights[i] <= 100
 */
public class Review_E1051_Easy_HeightChecker {

    static void test(ToIntFunction<int[]> method) {
        int[] heights = {1, 1, 4, 2, 1, 3};
        assertEquals(method.applyAsInt(heights), 3);

        heights = new int[]{5, 1, 2, 3, 4};
        assertEquals(method.applyAsInt(heights), 5);

        heights = new int[]{1, 2, 3, 4, 5};
        assertEquals(method.applyAsInt(heights), 0);

        heights = new int[]{2, 1, 2, 1, 1, 2, 2, 1};
        assertEquals(method.applyAsInt(heights), 4);

        heights = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
        assertEquals(method.applyAsInt(heights), 0);
    }

    /**
     * hint：
     * - 判断数组 i 位置的数字属不属于这个位置，也就是该数字是不是数组第 i 大的数字
     * <p>
     * 需要考虑的情况：
     * - 数组长度为 1
     * - 数组中有重复数字
     * - 数组中所有数字一样
     */
    @SuppressWarnings("ConstantConditions")
    public int heightChecker(int[] heights) {
        int len = heights.length;
        // 使用优先队列解决问题
        PriorityQueue<Integer> pq = new PriorityQueue<>(len + 1);
        for (int height : heights) {
            pq.add(height);
        }

        int moveCnt = 0;
        for (int height : heights) {
            if (height != pq.poll())
                moveCnt++;
        }

        return moveCnt;
    }

    @Test
    public void testHeightChecker() {
        test(this::heightChecker);
    }



    /**
     * 使用计数排序解决问题
     */
    public int fasterMethod(int[] heights) {
        int len = heights.length;
        int[] buckets = new int[101];
        for (int height : heights) {
            buckets[height]++;
        }

        int moveCnt = 0, curHeight = 0;
        for (int i = 0; i < len; i++) {
            while (buckets[curHeight] == 0)
                curHeight++;
            if (heights[i] != curHeight)
                moveCnt++;
            buckets[curHeight]--;
        }

        return moveCnt;
    }

    @Test
    public void testFasterMethod() {
        test(this::fasterMethod);
    }
}
