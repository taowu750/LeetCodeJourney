package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个按照非降序排列的整数数组，返回其中每个数字平方之后的组成的非降序排列数组。
 * <p>
 * 例子：
 * Input: nums = [-4,-1,0,3,10]
 * Output: [0,1,9,16,100]
 * Explanation: 在平方之后，数组变成 [16,1,0,9,100]。排序之后，数组变成 [0,1,9,16,100]。
 *
 * 约束：
 * - 1 <= nums.length <= 10**4
 * - -10**4 <= nums[i] <= 10**4
 * - nums 以非降序顺序排列.
 */
public class Review_E977_Easy_SortedSquares {

    static void test(Function<int[], int[]> func) {
        int[] inp = {-4, -1, 0, 3, 10};
        assertArrayEquals(func.apply(inp), new int[]{0, 1, 9, 16, 100});

        int[] inp2 = {-7, -3, 2, 3, 11};
        assertArrayEquals(func.apply(inp2), new int[]{4, 9, 9, 49, 121});

        assertArrayEquals(func.apply(new int[]{3, 4, 6, 7}), new int[]{9, 16, 36, 49});

        assertArrayEquals(func.apply(new int[]{-5, -3, -2, -1}), new int[]{1, 4, 9, 25});
    }

    /**
     * 有更简洁的做法。
     */
    public int[] sortedSquares(int[] nums) {
        int len = nums.length;
        int[] result = new int[len];
        int zeroPos = Arrays.binarySearch(nums, 0);
        int i = 1, negCursor = zeroPos - 1, posCursor = zeroPos + 1;
        if (zeroPos < 0) {
            i = 0;
            negCursor = -zeroPos - 2;
            posCursor = -zeroPos - 1;
        }

        int neg, pos;
        for (; negCursor >= 0 && posCursor < len; i++) {
            neg = Math.abs(nums[negCursor]);
            pos = nums[posCursor];

            if (neg <= pos) {
                result[i] = neg * neg;
                negCursor--;
            } else {
                result[i] = pos * pos;
                posCursor++;
            }
        }
        while (negCursor >= 0) {
            neg = Math.abs(nums[negCursor--]);
            result[i++] = neg * neg;
        }
        while (posCursor < len) {
            pos = nums[posCursor++];
            result[i++] = pos * pos;
        }

        return result;
    }

    @Test
    public void testSortedSquares() {
        test(this::sortedSquares);
    }



    /**
     * 从数组两端到中间遍历、从数组中间到两端遍历，适用于不同的情况。
     */
    public int[] shorterMethod(int[] nums) {
        int len = nums.length;
        int[] result = new int[len];
        int left = 0, right = len - 1;

        for (int i = len - 1; i >= 0; i--) {
            if (Math.abs(nums[left]) > Math.abs(nums[right])) {
                result[i] = nums[left] * nums[left];
                left++;
            } else {
                result[i] = nums[right] * nums[right];
                right--;
            }
        }

        return result;
    }

    @Test
    public void testShorterMethod() {
        test(this::shorterMethod);
    }
}
