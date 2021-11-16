package training.queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.function.BiFunction;

/**
 * 239. 滑动窗口最大值: https://leetcode-cn.com/problems/sliding-window-maximum/
 *
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
 *
 * 例 1：
 * 输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
 * 输出：[3,3,5,5,6,7]
 * 解释：
 * 滑动窗口的位置                  最大值
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       3
 *  1 [3  -1  -3] 5  3  6  7       3
 *  1  3 [-1  -3  5] 3  6  7       5
 *  1  3  -1 [-3  5  3] 6  7       5
 *  1  3  -1  -3 [5  3  6] 7       6
 *  1  3  -1  -3  5 [3  6  7]      7
 *
 * 例 2：
 * 输入：nums = [1], k = 1
 * 输出：[1]
 *
 * 例 3：
 * 输入：nums = [1,-1], k = 1
 * 输出：[1,-1]
 *
 * 例 4：
 * 输入：nums = [9,11], k = 2
 * 输出：[11]
 *
 * 例 5：
 * 输入：nums = [4,-2], k = 2
 * 输出：[4]
 *
 * 约束：
 * - 1 <= nums.length <= 10**5
 * - -10**4 <= nums[i] <= 10**4
 * - 1 <= k <= nums.length
 */
public class E239_Hard_SlidingWindowMaximum {

    public static void test(BiFunction<int[], Integer, int[]> method) {
        Assertions.assertArrayEquals(new int[]{3,3,5,5,6,7},
                method.apply(new int[]{1,3,-1,-3,5,3,6,7}, 3));

        Assertions.assertArrayEquals(new int[]{1},
                method.apply(new int[]{1}, 1));

        Assertions.assertArrayEquals(new int[]{1,-1},
                method.apply(new int[]{1,-1}, 1));

        Assertions.assertArrayEquals(new int[]{11},
                method.apply(new int[]{9,11}, 2));

        Assertions.assertArrayEquals(new int[]{4},
                method.apply(new int[]{4,-2}, 2));

        Assertions.assertArrayEquals(new int[]{5,5,6,6,6,2,2},
                method.apply(new int[]{5,5,3,-1,6,2,2,2,1}, 3));
    }

    /**
     * LeetCode 耗时：456 ms - 5.03%
     *          内存消耗：58.9 MB - 18.04%
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (k == 1)
            return nums;

        int n = nums.length;
        int[] result = new int[n - k + 1];
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();

        BiFunction<Integer, Integer, Integer> sum = new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer o1, Integer o2) {
                int res = o1 + o2;
                return res <= 0 ? null : res;
            }
        };
        for (int i = 0; i < k; i++)
            treeMap.merge(nums[i], 1, sum);
        result[0] = treeMap.lastKey();

        for (int i = k; i < n; i++) {
            treeMap.merge(nums[i - k], -1, sum);
            treeMap.merge(nums[i], 1, sum);
            result[i - k + 1] = treeMap.lastKey();
        }

        return result;
    }

    @Test
    public void testMaxSlidingWindow() {
        test(this::maxSlidingWindow);
    }


    /**
     * 单调队列。
     *
     * 在一堆数字中，已知最值为A，如果给这堆数添加一个数B，那么比较一下A和B就可以立即算出新的最值；
     * 但如果减少一个数，就不能直接得到最值了，因为如果减少的这个数恰好是A，就需要遍历所有数重新找新的最值。
     *
     * 单调队列保证添加数和减少数的均摊时间复杂度为 O(1)。
     * 单独看push操作的复杂度确实不是O(1)，但是算法整体的复杂度依然是O(N)线性时间。
     * 要这样想，nums中的每个元素最多被push_back和pop_back一次，没有任何多余操作，所以整体的复杂度还是O(N)。
     *
     * 参见：
     * https://labuladong.gitee.io/algo/数据结构系列/单调队列.html
     */
    static class MonotonicQueue {
        private LinkedList<Integer> queue;

        MonotonicQueue() {
            queue = new LinkedList<>();
        }

        /**
         * 在队尾添加元素 x，并保证队列是单调递减的序列
         */
        void push(int x) {
            while (!queue.isEmpty() && x > queue.getLast())
                queue.removeLast();
            queue.addLast(x);
        }

        /**
         * 获取最大值
         */
        int max() {
            return queue.getFirst();
        }

        /**
         * 如果队头等于 x，则删除它
         */
        void pop(int x) {
            if (x == queue.getFirst())
                queue.removeFirst();
        }
    }

    /**
     * LeetCode 耗时：37 ms - 64.12%
     *          内存消耗：53.7 MB - 31.68%
     */
    public int[] monotonicQueueMethod(int[] nums, int k) {
        if (k == 1)
            return nums;

        int n = nums.length;
        int[] result = new int[n - k + 1];
        MonotonicQueue queue = new MonotonicQueue();

        for (int i = 0; i < k; i++)
            queue.push(nums[i]);
        result[0] = queue.max();

        for (int i = k; i < n; i++) {
            queue.pop(nums[i - k]);
            queue.push(nums[i]);
            result[i - k + 1] = queue.max();
        }

        return result;
    }

    @Test
    public void testMonotonicQueueMethod() {
        test(this::monotonicQueueMethod);
    }
}
