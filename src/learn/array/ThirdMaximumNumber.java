package learn.array;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定整数数组 nums，返回此数组中的第三个最大值。如果第三个最大值不存在，则返回最大值。
 * 要求时间复杂度为 O(n)
 * <p>
 * 例 1：
 * Input: nums = [3,2,1]
 * Output: 1
 * <p>
 * 例 2：
 * Input: nums = [1,2]
 * Output: 2
 * <p>
 * 例 3：
 * Input: nums = [2,2,3,1]
 * Output: 1
 * <p>
 * 约束：
 * - 1 <= nums.length <= 10**4
 * - -2**31 <= nums[i] <= 2**31 - 1
 */
public class ThirdMaximumNumber {

    static void test(ToIntFunction<int[]> method) {
        int[] nums = {3, 2, 1};
        assertEquals(method.applyAsInt(nums), 1);

        nums = new int[]{1, 2};
        assertEquals(method.applyAsInt(nums), 2);

        nums = new int[]{2, 2, 3, 1};
        assertEquals(method.applyAsInt(nums), 1);

        nums = new int[]{2, 2, 2, 2};
        assertEquals(method.applyAsInt(nums), 2);

        nums = new int[]{-10};
        assertEquals(method.applyAsInt(nums), -10);
    }

    public int thirdMax(int[] nums) {
        // 分别存放第一大、第二大和第三大的元素
        Integer[] threeMaximum = new Integer[3];

        for (int num : nums) {
            if (threeMaximum[0] == null)
                threeMaximum[0] = num;
            else if (threeMaximum[0] <= num) {
                if (threeMaximum[0] == num)
                    continue;
                System.arraycopy(threeMaximum, 0, threeMaximum, 1, 2);
                threeMaximum[0] = num;
            } else if (threeMaximum[1] == null)
                threeMaximum[1] = num;
            else if (threeMaximum[1] <= num) {
                if (threeMaximum[1] == num)
                    continue;
                threeMaximum[2] = threeMaximum[1];
                threeMaximum[1] = num;
            } else if (threeMaximum[2] == null || threeMaximum[2] < num)
                threeMaximum[2] = num;
        }

        return threeMaximum[2] != null ? threeMaximum[2] : threeMaximum[0];
    }

    @Test
    public void testThirdMax() {
        test(this::thirdMax);
    }


    /**
     * 使用了优先队列的方法。虽然简洁，但在速度上不如{@link #thirdMax(int[])}。
     */
    public int usePriorityQueueMethod(int[] nums) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(4);
        for (int num : nums) {
            if (!pq.contains(num)) {
                if (pq.size() < 3) {
                    pq.add(num);
                } else {
                    if (pq.peek() < num) {
                        pq.poll();
                        pq.add(num);
                    }
                }
            }
        }
        if (pq.size() == 2)
            pq.poll();

        //noinspection ConstantConditions
        return pq.peek();
    }

    @Test
    public void testUsePriorityQueueMethod() {
        test(this::usePriorityQueueMethod);
    }

    public int conciseMethod(int[] nums) {
        Integer max1 = null, max2 = null, max3 = null;

        for (Integer num : nums) {
            if (num.equals(max1) || num.equals(max2) || num.equals(max3))
                continue;
            if (max1 == null || max1 < num) {
                max3 = max2;
                max2 = max1;
                max1 = num;
            } else if (max2 == null || max2 < num) {
                max3 = max2;
                max2 = num;
            } else if (max3 == null || max3 < num)
                max3 = num;
        }

        //noinspection ConstantConditions
        return max3 != null ? max3 : max1;
    }

    @Test
    public void testConciseMethod() {
        test(this::conciseMethod);
    }
}
