package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 480. 滑动窗口中位数: https://leetcode-cn.com/problems/sliding-window-median/
 *
 * 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
 * 例如：
 * - [2,3,4]，中位数是 3
 * - [2,3]，中位数是 (2 + 3) / 2 = 2.5
 *
 * 给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。
 * 你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
 *
 * 例 1：
 * 给出 nums = [1,3,-1,-3,5,3,6,7]，以及 k = 3。
 *
 * 窗口位置                      中位数
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       1
 *  1 [3  -1  -3] 5  3  6  7      -1
 *  1  3 [-1  -3  5] 3  6  7      -1
 *  1  3  -1 [-3  5  3] 6  7       3
 *  1  3  -1  -3 [5  3  6] 7       5
 *  1  3  -1  -3  5 [3  6  7]      6
 *
 * 因此，返回该滑动窗口的中位数数组 [1,-1,-1,3,5,6]。
 *
 * 说明：
 * - 你可以假设 k 始终有效，即：k 始终小于等于输入的非空数组的元素个数。
 * - 与真实值误差在 10 ^ -5 以内的答案将被视作正确答案。
 */
public class E480_Hard_SlidingWindowMedian {

    public static void test(BiFunction<int[], Integer, double[]> method) {
        assertArrayEquals(new double[]{1,-1,-1,3,5,6}, method.apply(new int[]{1,3,-1,-3,5,3,6,7}, 3), 1e-5);
        assertArrayEquals(new double[]{2147483647.0}, method.apply(new int[]{2147483647, 2147483647}, 2), 1e-5);
    }

    /**
     * 参见 {@link training.design.E295_Hard_FindMedianFromDataStream}。
     *
     * LeetCode 耗时：56 ms - 46.03%
     *          内存消耗：40.9 MB - 14.79%
     */
    public double[] medianSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) {
            return new double[0];
        }

        double[] result = new double[nums.length - k + 1];
        if (k == 1) {
            for (int i = 0; i < nums.length; i++) {
                result[i] = nums[i];
            }
            return result;
        }

        Median median = new Median(k);
        for (int j = 0; j < k; j++) {
            median.add(nums[j]);
        }
        result[0] = median.median();
        for (int i = 1; i < result.length; i++) {
            median.remove(nums[i - 1]);
            median.add(nums[i + k - 1]);
            result[i] = median.median();
        }

        return result;
    }

    public static class Median {

        private PriorityQueue<Integer> left;
        private PriorityQueue<Integer> right;

        public Median(int k) {
            left = new PriorityQueue<>(k / 2 + 1, Comparator.reverseOrder());
            right = new PriorityQueue<>(k / 2);
        }

        public void add(int val) {
            if (left.isEmpty()) {
                left.add(val);
            } else if (left.size() > right.size()) {
                if (val < left.element()) {
                    right.add(left.remove());
                    left.add(val);
                } else {
                    right.add(val);
                }
            } else {
                if (val <= right.element()) {
                    left.add(val);
                } else {
                    left.add(right.remove());
                    right.add(val);
                }
            }
        }

        public double median() {
            if (left.size() == right.size()) {
                return ((double) left.element() + right.element()) / 2.;
            } else {
                return left.element();
            }
        }

        public void remove(int val) {
            if (val <= left.element()) {
                left.remove(val);
            } else {
                right.remove(val);
            }
            if (left.size() < right.size()) {
                left.add(right.remove());
            } else if (left.size() - right.size() > 1) {
                right.add(left.remove());
            }
        }
    }

    @Test
    public void testMedianSlidingWindow() {
        test(this::medianSlidingWindow);
    }


    /**
     * 优先队列的延迟删除技巧，参见：
     * https://leetcode-cn.com/problems/sliding-window-median/solution/hua-dong-chuang-kou-zhong-wei-shu-by-lee-7ai6/
     *
     * 在前面的算法中，添加数字很容易，但是删除数字则是一个 O(k) 的操作。我们可以考虑使用「延迟删除」的技巧，即：
     * - 当我们需要移出优先队列中的某个元素时，我们只将这个删除操作「记录」下来，而不去真的删除这个元素。
     *   当这个元素出现在 small 或者 large 的堆顶时，我们再去将其移出对应的优先队列。
     *
     * 「延迟删除」使用到的辅助数据结构一般为哈希表 delayed，其中的每个键值对 (num,freq)，表示元素 num 还需要被删除 freq 次。
     *
     * 我们首先设计一个辅助函数 prune(heap)，它的作用很简单，就是对 heap 这个优先队列（left 或者 right 之一），
     * 不断地弹出其需要被删除的堆顶元素，并且减少 delayed 中对应项的值。在 prune(heap) 完成之后，
     * 我们就可以保证 heap 的堆顶元素是不需要被「延迟删除」的。
     *
     * 这样我们就可以在 prune(heap) 的基础上设计另一个辅助函数 makeBalance()，它的作用即为调整 left 和 right 中的元素个数，
     * 使得二者的元素个数满足要求。由于有了 remove(num) 以及「延迟删除」，我们在将一个优先队列的堆顶元素放入另一个优先队列时，
     * 第一个优先队列的堆顶元素可能是需要删除的。因此我们就可以用 makeBalance() 将 prune(heap) 封装起来，它的逻辑如下：
     * - 如果 left 和 right 中的元素个数满足要求，则不进行任何操作；
     * - 如果 left 比 right 的元素个数多了 2 个，那么我们我们将 left 的堆顶元素放入 right。
     *   此时 left 的对应元素可能是需要删除的，因此我们调用 prune(left)；
     * - 如果 left 比 right 的元素个数少了 1 个，那么我们将 right 的堆顶元素放入 left。
     *   此时 right 的对应的元素可能是需要删除的，因此我们调用 prune(right)。
     * 此时，我们只需要在原先 add(num) 的设计的最后加上一步 makeBalance() 即可。
     *
     * 然而对于 remove(num)，我们还是需要进行一些思考的：
     * - 如果 num 与 left 和 right 的堆顶元素都不相同，那么 num 是需要被「延迟删除」的，我们将其在哈希表中的值增加 1；
     * - 否则，例如 num 与 left 的堆顶元素相同，那么该元素是可以理解被删除的。虽然我们没有实现「立即删除」这个辅助函数，
     *   但只要我们将 num 在哈希表中的值增加 1，并且调用「延迟删除」的辅助函数 prune(left)，
     *   那么就相当于实现了「立即删除」的功能。
     *
     * LeetCode 耗时：21 ms - 73.57%
     *          内存消耗：40.3 MB - 72.83%
     */
    public double[] delayRemoveMethod(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) {
            return new double[0];
        }

        double[] result = new double[nums.length - k + 1];
        if (k == 1) {
            for (int i = 0; i < nums.length; i++) {
                result[i] = nums[i];
            }
            return result;
        }

        DelayRemoveMedian median = new DelayRemoveMedian(k);
        for (int j = 0; j < k; j++) {
            median.add(nums[j]);
        }
        result[0] = median.median();
        for (int i = 1; i < result.length; i++) {
            median.remove(nums[i - 1]);
            median.add(nums[i + k - 1]);
            result[i] = median.median();
        }

        return result;
    }

    public static class DelayRemoveMedian {

        private PriorityQueue<Integer> left;
        private PriorityQueue<Integer> right;
        private Map<Integer, Integer> delay;
        // left 和 right 当前包含的元素个数，需要扣除被「延迟删除」的元素
        private int leftSize, rightSize;

        public DelayRemoveMedian(int k) {
            left = new PriorityQueue<>(k / 2 + 1, Comparator.reverseOrder());
            right = new PriorityQueue<>(k / 2);
            delay = new HashMap<>(k);
        }

        public void add(int num) {
            if (left.isEmpty() || num <= left.peek()) {
                left.offer(num);
                ++leftSize;
            } else {
                right.offer(num);
                ++rightSize;
            }
            makeBalance();
        }

        public double median() {
            if (leftSize == rightSize) {
                return ((double) left.element() + right.element()) / 2.;
            } else {
                return left.element();
            }
        }

        public void remove(int num) {
            delay.merge(num, 1, Integer::sum);
            if (!left.isEmpty() && num <= left.element()) {
                leftSize--;
                if (num == left.element()) {
                    prune(left);
                }
            } else if (!right.isEmpty()) {
                rightSize--;
                if (num == right.element()) {
                    prune(right);
                }
            }
            makeBalance();
        }

        private void makeBalance() {
            if (leftSize < rightSize) {
                left.add(right.remove());
                leftSize++;
                rightSize--;
                prune(right);
            } else if (leftSize - rightSize > 1) {
                right.add(left.remove());
                leftSize--;
                rightSize++;
                prune(left);
            }
        }

        private void prune(PriorityQueue<Integer> heap) {
            while (!heap.isEmpty()) {
                if (delay.containsKey(heap.element())) {
                    delay.merge(heap.remove(), -1, (older, delta) -> older + delta > 0 ? older + delta : null);
                } else {
                    break;
                }
            }
        }
    }

    @Test
    public void testDelayRemoveMethod() {
        test(this::delayRemoveMethod);
    }
}
