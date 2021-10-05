package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个已经按升序排序的整数数组 nums，请找到两个数字，使它们加起来成为一个特定的数字 target。
 * 返回两个数字的索引(以1开始)数组 answer，其中 1 <= answer[0] < answer[1] <= numbers.length。
 * <p>
 * 假设每个输入都只有一个解决方案，并且不会两次使用同一元素。
 * <p>
 * 例 1：
 * Input: numbers = [2,7,11,15], target = 9
 * Output: [1,2]
 * <p>
 * 例 2：
 * Input: numbers = [2,3,4], target = 6
 * Output: [1,3]
 * <p>
 * 例 3：
 * Input: numbers = [-1,0], target = -1
 * Output: [1,2]
 * <p>
 * 约束：
 * - 2 <= numbers.length <= 3 * 10**4
 * - -1000 <= numbers[i] <= 1000
 * - -1000 <= target <= 1000
 * - 只有一个有效答案
 */
public class Review_E167_Easy_TwoSumII {

    static void test(BiFunction<int[], Integer, int[]> method) {
        assertArrayEquals(method.apply(new int[]{2, 7, 11, 15}, 9),
                new int[]{1, 2});

        assertArrayEquals(method.apply(new int[]{2, 3, 4}, 6),
                new int[]{1, 3});

        assertArrayEquals(method.apply(new int[]{-1, 0}, -1),
                new int[]{1, 2});

        assertArrayEquals(method.apply(new int[]{1, 2, 3, 4, 4, 9, 56, 90}, 8),
                new int[]{4, 5});
    }

    /**
     * 使用二分查找。
     *
     * LeetCode 耗时：1ms - 49.74%
     */
    public int[] twoSum(int[] numbers, int target) {
        int first = 0, last;
        while ((last = Arrays.binarySearch(numbers, first + 1, numbers.length,
                target - numbers[first])) < 0)
            first++;
        return new int[]{first + 1, last + 1};
    }

    @Test
    public void testTwoSum() {
        test(this::twoSum);
    }


    /**
     * 双指针方法。
     *
     * LeetCode 耗时：0ms - 100%
     */
    public int[] twoPoint(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target)
                break;
            else if (sum < target)
                left++;
            else
                right--;
        }
        return new int[]{left + 1, right + 1};
    }

    @Test
    public void testTwoPoint() {
        test(this::twoPoint);
    }
}
