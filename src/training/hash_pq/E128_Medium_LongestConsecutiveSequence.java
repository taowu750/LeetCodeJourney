package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 128. 最长连续序列: https://leetcode-cn.com/problems/longest-consecutive-sequence/
 *
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 *
 * 例 1：
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 *
 * 例 2：
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出：9
 *
 * 约束：
 * - 0 <= nums.length <= 10^5
 * - -10^9 <= nums[i] <= 10^9
 */
public class E128_Medium_LongestConsecutiveSequence {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(4, method.applyAsInt(new int[]{100,4,200,1,3,2}));
        assertEquals(9, method.applyAsInt(new int[]{0,3,7,2,5,8,4,6,0,1}));
        assertEquals(4, method.applyAsInt(new int[]{-7,-1,3,-9,-4,7,-3,2,4,9,4,-9,8,-7,5,-1,-7}));
        assertEquals(4, method.applyAsInt(new int[]{7,-9,3,-6,3,5,3,6,-2,-5,8,6,-4,-6,-4,-4,5,-9,2,7,0,0}));
        assertEquals(1, method.applyAsInt(new int[]{0}));
        assertEquals(1, method.applyAsInt(new int[]{0, 0}));
    }

    /**
     * LeetCode 耗时：34 ms - 39.88%
     *          内存消耗：48.5 MB - 54.08%
     */
    public int longestConsecutive(int[] nums) {
        // 连续序列的头->连续序列的尾
        Map<Integer, Integer> head2tail = new HashMap<>((int) (nums.length / 0.75) + 1);
        // 连续序列的尾->连续序列的头
        Map<Integer, Integer> tail2head = new HashMap<>((int) (nums.length / 0.75) + 1);

        int max = 0;
        for (int num : nums) {
            boolean exists = head2tail.containsKey(num) || tail2head.containsKey(num);
            // num 能不能作为一个序列的头
            boolean canHead = !exists && head2tail.containsKey(num + 1);
            // num 能不能作为一个序列的尾
            boolean canTail = !exists && tail2head.containsKey(num - 1);

            // num 处于中间位置
            if (canHead && canTail) {
                int tail = head2tail.remove(num + 1);
                int head = tail2head.remove(num - 1);
                head2tail.put(head, tail);
                tail2head.put(tail, head);
                max = Math.max(max, tail - head + 1);
            } else if (canHead) {  // num 处于开头
                int tail = head2tail.remove(num + 1);
                head2tail.put(num, tail);
                tail2head.put(tail, num);
                max = Math.max(max, tail - num + 1);
            } else if (canTail) {  // num 处于结尾
                int head = tail2head.remove(num - 1);
                tail2head.put(num, head);
                head2tail.put(head, num);
                max = Math.max(max, num - head + 1);
            } else if (!exists) {  // num 不存在
                head2tail.put(num, num);
                tail2head.put(num, num);
                if (max == 0) {
                    max = 1;
                }
            }
        }

        return max;
    }

    @Test
    public void testLongestConsecutive() {
        test(this::longestConsecutive);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/longest-consecutive-sequence/solution/dong-tai-gui-hua-python-ti-jie-by-jalan/
     *
     * LeetCode 耗时：31 ms - 42.36%
     *          内存消耗：48.2 MB - 78.99%
     */
    public int betterMethod(int[] nums) {
        int max = 0;
        // 这个哈希表中存的实际上端点对应区间的长度值，只不过区间内部的元素长度我们也设为和端点一样，
        // 表示这个元素我们已经访问过了
        Map<Integer, Integer> intervals = new HashMap<>((int) (nums.length / 0.75) + 1);
        for (int num : nums) {
            if (!intervals.containsKey(num)) {
                int left = intervals.getOrDefault(num - 1, 0);
                int right = intervals.getOrDefault(num + 1, 0);
                int curLen = left + right + 1;
                if (curLen > max) {
                    max = curLen;
                }

                intervals.put(num, curLen);
                intervals.put(num - left, curLen);
                intervals.put(num + right, curLen);
            }
        }

        return max;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
