package learn.binarysearchtree;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 设计一个类来查找流中第 k 个最大(注意是最大)的元素。请注意，它是排序顺序中第 k 个最大的元素，而不是第 k 个不同的元素。
 *
 * 实现 KthLargest 类：
 * - KthLargest(int k，int[] nums) 用整数 k 和整数流 nums 初始化对象。
 * - int add(int val) 返回表示流中第 k 个最大元素的元素。
 *
 * 例 1：
 * Input:
 * ["KthLargest", "add", "add", "add", "add", "add"]
 * [[3, [4, 5, 8, 2]], [3], [5], [10], [9], [4]]
 * Output:
 * [null, 4, 5, 5, 8, 8]
 * Explanation:
 * KthLargest kthLargest = new KthLargest(3, [4, 5, 8, 2]);
 * kthLargest.add(3);   // return 4
 * kthLargest.add(5);   // return 5
 * kthLargest.add(10);  // return 5
 * kthLargest.add(9);   // return 8
 * kthLargest.add(4);   // return 8
 *
 * 约束：
 * - 1 <= k <= 10**4
 * - 0 <= nums.length <= 10**4
 * - -10**4 <= nums[i] <= 10**4
 * - -10**4 <= val <= 10**4
 * - add 方法最多被调用 10**4 次
 * - 在搜索第 k 个元素时，保证数组中至少有 k 个元素。
 */
public class KthLargestElementInAStream {

    @Test
    public void testKthLargest() {
        KthLargest kthLargest = new KthLargest(3, new int[]{4, 5, 8, 2});
        assertEquals(kthLargest.add(3), 4);
        assertEquals(kthLargest.add(5), 5);
        assertEquals(kthLargest.add(10), 5);
        assertEquals(kthLargest.add(9), 8);
        assertEquals(kthLargest.add(4), 8);

        kthLargest = new KthLargest(2, new int[]{0});
        assertEquals(kthLargest.add(-1), -1);
        assertEquals(kthLargest.add(1), 0);
        assertEquals(kthLargest.add(-2), 0);
        assertEquals(kthLargest.add(-4), 0);
        assertEquals(kthLargest.add(3), 1);
    }
}

/**
 * LeetCode 耗时：12ms - 98.73%
 */
class KthLargest {

    private int k;
    private PriorityQueue<Integer> kPQ;

    public KthLargest(int k, int[] nums) {
        kPQ = new PriorityQueue<>(k);
        this.k = k;
        for (int num : nums)
            add(num);
    }

    @SuppressWarnings("ConstantConditions")
    public int add(int val) {
        if (kPQ.size() < k)
            kPQ.offer(val);
        else if (val > kPQ.peek()) {
            kPQ.poll();
            kPQ.offer(val);
        }
        return kPQ.peek();
    }
}
