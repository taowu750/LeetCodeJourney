package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 659. 分割数组为连续子序列: https://leetcode-cn.com/problems/split-array-into-consecutive-subsequences/
 *
 * 给你一个按升序排序的整数数组 num（可能包含重复数字），请你将它们分割成一个或多个长度至少为 3 的子序列，
 * 其中每个子序列都由「连续整数」组成。
 * 如果可以完成上述分割，则返回 true ；否则，返回 false 。
 *
 * 例 1：
 * 输入: [1,2,3,3,4,5]
 * 输出: True
 * 解释:
 * 你可以分割出这样两个连续子序列 :
 * 1, 2, 3
 * 3, 4, 5
 *
 * 例 2：
 * 输入: [1,2,3,3,4,4,5,5]
 * 输出: True
 * 解释:
 * 你可以分割出这样两个连续子序列 :
 * 1, 2, 3, 4, 5
 * 3, 4, 5
 *
 * 例 3：
 * 输入: [1,2,3,4,4,5]
 * 输出: False
 *
 * 约束：
 * - 1 <= nums.length <= 10000
 */
public class E659_Medium_SplitArrayIntoConsecutiveSubsequences {

    public static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{1,2,3,3,4,5}));
        assertTrue(method.test(new int[]{1,2,3,3,4,4,5,5}));
        assertFalse(method.test(new int[]{1,2,3,4,4,5}));
    }

    /**
     * 使用 HashMap 和最小堆的算法。来自：
     * https://leetcode-cn.com/problems/split-array-into-consecutive-subsequences/solution/fen-ge-shu-zu-wei-lian-xu-zi-xu-lie-by-l-lbs5/
     *
     * 当 v 在数组中时，如果存在一个子序列以 v-1 结尾，长度为 k，则可以将 v 加入该子序列中，
     * 得到长度为 k+1 的子序列。
     * 如果不存在以 v-1 结尾的子序列，则必须新建一个只包含 v 的子序列，长度为 1。
     *
     * 当 v 在数组中时，如果存在多个子序列以 v-1 结尾，应该将 v 加入其中的哪一个子序列？
     * 由于题目要求每个子序列的长度至少为 3，显然应该让最短的子序列尽可能长，因此应该将 v 加入其中最短的子序列。
     *
     * 基于上述分析，可以使用哈希表和最小堆进行实现。
     * 哈希表的键为子序列的最后一个数字，值为最小堆，用于存储所有的子序列长度，最小堆满足堆顶的元素是最小的，
     * 因此堆顶的元素即为最小的子序列长度。
     *
     * LeetCode 耗时：77 ms - 34.15%
     *          内存消耗：39.7 MB - 61.64%
     */
    public boolean hashHeapMethod(int[] nums) {
        Map<Integer, PriorityQueue<Integer>> hashHeap = new HashMap<>((int) (nums.length / 2.25) + 1);

        for (int v : nums) {
            // 添加 v
            if (!hashHeap.containsKey(v)) {
                hashHeap.put(v, new PriorityQueue<>());
            }
            // 如果存在以 v - 1 结尾的序列
            if (hashHeap.containsKey(v - 1)) {
                PriorityQueue<Integer> heap = hashHeap.get(v - 1);
                // 从中移除长度最小的序列
                int minSize = heap.remove();
                // 如果 heap 为空，则移除它
                if (heap.isEmpty()) {
                    hashHeap.remove(v - 1);
                }
                // 将它添加到以 v - 1 结尾的序列中
                hashHeap.get(v).add(minSize + 1);
            }
            // 不存在则单独以 v 开头
            else {
                hashHeap.get(v).add(1);
            }
        }
        // 最后检查所有序列长度都要大于等于 3
        for (PriorityQueue<Integer> heap : hashHeap.values()) {
            //noinspection ConstantConditions
            if (heap.peek() < 3)
                return false;
        }

        return true;
    }

    @Test
    public void testHashHeapMethod() {
        test(this::hashHeapMethod);
    }


    /**
     * 算法思路参见：https://labuladong.gitee.io/algo/5/33/
     *
     * 这个算法其实是对上面算法的改进，是一种贪心策略。
     *
     * 我们想把 nums 的元素划分到若干个子序列中，其实就是下面这个代码逻辑：
     * for (int v : nums) {
     *     if (...) {
     *         // 将 v 分配到某个子序列中
     *     } else {
     *         // 实在无法分配 v
     *         return false;
     *     }
     *     return true;
     * }
     *
     * 关键在于，我们怎么知道当前元素 v 如何进行分配呢？肯定得分情况讨论，把情况讨论清楚了，题目也就做出来了。
     * 总共有两种情况：
     * 1. 当前元素 v 自成一派，「以自己开头」构成一个长度至少为 3 的序列。
     *    比如输入 nums = [1,2,3,6,7,8]，遍历到元素 6 时，它只能自己开头形成一个符合条件的子序列 [6,7,8]。
     * 2. 当前元素 v 接到已经存在的子序列后面。
     *    比如输入 nums = [1,2,3,4,5]，遍历到元素 4 时，它只能接到已经存在的子序列 [1,2,3] 后面。
     *    它没办法自成开头形成新的子序列，因为少了个 6。
     * 3. 如果这两种情况都可以， 应该优先判断自己是否能够接到其他序列后面，如果不行，再判断是否可以作为新的子序列开头
     *
     * 这就是整体的思路，想让算法代码实现这两个选择，需要两个哈希表来做辅助：
     * 1. freq 记录每个元素出现的次数，帮助一个元素判断自己是否能够作为开头。
     *    如果我发现 freq[3], freq[4], freq[5] 都是大于 0 的，那就说明元素 3 可以作为开头组成一个长度为 3 的子序列。
     * 2. need 记录哪些元素可以被接到其他子序列后面。
     *    比如说现在已经组成了两个子序列 [1,2,3,4] 和 [2,3,4]，那么 need[5] 的值就应该是 2，说明对元素 5 的需求为 2。
     *
     * LeetCode 耗时：30 ms - 90.24%
     *          内存消耗：39.8 MB - 51.66%
     */
    public boolean isPossible(int[] nums) {
        if (nums.length < 3)
            return false;

        int n = nums.length;
        Map<Integer, Integer> freq = new HashMap<>((int) (n / 0.75) + 1);
        Map<Integer, Integer> need = new HashMap<>((int) (n / 2.25) + 1);
        for (int v : nums) {
            freq.merge(v, 1, Integer::sum);
        }

        for (int v : nums) {
            // 已被用到其他子序列中
            if (freq.get(v) == 0)
                continue;

            // 是否可以被接到其他子序列中
            if (need.getOrDefault(v, 0) > 0) {
                // 把 v 接到某个子序列中
                freq.merge(v, -1, Integer::sum);
                // 对 v 的需求减 1
                need.merge(v, -1, Integer::sum);
                // 对 v + 1 的需求加 1
                need.merge(v + 1, 1, Integer::sum);
            }
            // 然后看看 v 是否能成为子序列的开头
            else if (freq.getOrDefault(v + 1, 0) > 0
                    && freq.getOrDefault(v + 2, 0) > 0) {
                // 把 v、v + 1、v + 2 作为新的子序列
                freq.merge(v, -1, Integer::sum);
                freq.merge(v + 1, -1, Integer::sum);
                freq.merge(v + 2, -1, Integer::sum);
                // 对 v + 3 的需求加 1
                need.merge(v + 3, 1, Integer::sum);
            }
            // 两种情况都不符合，返回 false
            else {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testIsPossible() {
        test(this::isPossible);
    }
}
