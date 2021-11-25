package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import static util.ArrayUtil.equalsIgnoreOrder;

/**
 * 347. 前 K 个高频元素: https://leetcode-cn.com/problems/top-k-frequent-elements/
 *
 * 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按「任意顺序」返回答案。
 *
 * 例 1：
 * 输入: nums = [1,1,1,2,2,3], k = 2
 * 输出: [1,2]
 *
 * 例 2：
 * 输入: nums = [1], k = 1
 * 输出: [1]
 *
 * 约束：
 * - 1 <= nums.length <= 10^5
 * - k 的取值范围是 [1, 数组中不相同的元素的个数]
 * - 题目数据保证答案唯一，换句话说，数组中前 k 个高频元素的集合是唯一的。也就是考虑所有并列的情况，最后只有 K 个。
 */
public class E347_Medium_TopKFrequentElements {

    public static void test(BiFunction<int[], Integer, int[]> method) {
        equalsIgnoreOrder(new int[]{1,2}, method.apply(new int[]{1,1,1,2,2,3}, 2));
        equalsIgnoreOrder(new int[]{1}, method.apply(new int[]{1}, 1));
    }

    /**
     * LeetCode 耗时：11 ms - 95.48%
     *          内存消耗：41.5 MB - 5.22%
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        for (int num : nums) {
            map.merge(num, 1, Integer::sum);
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(k, Comparator.comparingInt(a -> a[1]));
        map.forEach((num, freq) -> {
            if (pq.size() < k) {
                pq.add(new int[]{num, freq});
            } else if (pq.element()[1] < freq) {
                pq.remove();
                pq.add(new int[]{num, freq});
            }
        });

        int i = 0;
        int[] result = new int[k];
        for (int[] numFreq : pq) {
            result[i++] = numFreq[0];
        }

        return result;
    }

    @Test
    public void testTopKFrequent() {
        test(this::topKFrequent);
    }


    /**
     * 利用快速排序，参见 {@link training.sort.E215_Medium_KthLargestElementInAnArray}。
     *
     * LeetCode 耗时：8 ms - 98.22%
     *          内存消耗：41.5 MB - 5.22%
     */
    public int[] quickSortMethod(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        for (int num : nums) {
            map.merge(num, 1, Integer::sum);
        }

        int[][] numFreqs = new int[map.size()][2];
        int l = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            numFreqs[l][0] = entry.getKey();
            numFreqs[l++][1] = entry.getValue();
        }

        int lo = 0, hi = numFreqs.length - 1, kIdx = 0;
        for (;;) {
            if (lo == hi) {
                kIdx = lo;
                break;
            }

            int mid = (lo + hi) >>> 1;
            if (numFreqs[lo][1] > numFreqs[mid][1]) {
                exch(numFreqs, lo, mid);
            }
            if (numFreqs[mid][1] > numFreqs[hi][1]) {
                exch(numFreqs, mid, hi);
            }
            if (numFreqs[lo][1] < numFreqs[mid][1]) {
                exch(numFreqs, lo, mid);
            }

            int pivot = numFreqs[lo][1];
            int i = lo, j = hi + 1;
            for (;;) {
                while (numFreqs[++i][1] < pivot);
                while (numFreqs[--j][1] > pivot);

                if (i >= j) {
                    break;
                }
                exch(numFreqs, i, j);
            }
            exch(numFreqs, lo, j);

            if (j == numFreqs.length - k) {
                kIdx = j;
                break;
            } else if (j < numFreqs.length - k) {
                lo = j + 1;
            } else {
                hi = j - 1;
            }
        }

        int[] result = new int[k];
        for (int i = kIdx; i < numFreqs.length; i++) {
            result[i - kIdx] = numFreqs[i][0];
        }

        return result;
    }

    private void exch(int[][] nums, int i, int j) {
        int[] tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    @Test
    public void testQuickSortMethod() {
        test(this::quickSortMethod);
    }
}
