package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 941. 有效的山脉数组：https://leetcode-cn.com/problems/valid-mountain-array/
 *
 * 给定一个整数数组 arr，如果它是“mountain”数组返回 true。
 * 当且仅当 arr 满足下列条件时，它是一个“mountain”数组：
 * 1. arr.length >= 3
 * 2. 存在 i，0 < i < arr.length -1 且有
 * - arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
 * - arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
 * 也就是说 arr 是一个先“严格”递增，然后“严格”递减的数组。
 * <p>
 * 例 1：
 * Input: arr = [2,1]
 * Output: false
 * <p>
 * 例 2：
 * Input: arr = [3,5,5]
 * Output: false
 * <p>
 * 例 3：
 * Input: arr = [0,3,2,1]
 * Output: true
 * <p>
 * 约束：
 * 1 <= arr.length <= 10**4
 * 0 <= arr[i] <= 10**4
 */
public class E941_Easy_ValidMountainArray {

    static void test(Predicate<int[]> method) {
        int[] arr = {2, 1};
        assertFalse(method.test(arr));

        arr = new int[]{3, 5, 5};
        assertFalse(method.test(arr));

        arr = new int[]{0, 3, 2, 1};
        assertTrue(method.test(arr));

        arr = new int[]{0, 1, 2, 3, 4, 8, 9, 10, 11, 12, 11};
        assertTrue(method.test(arr));

        arr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertFalse(method.test(arr));

        arr = new int[]{9, 8, 7, 6, 5};
        assertFalse(method.test(arr));

        arr = new int[]{1, 4, 3, 2, 0};
        assertTrue(method.test(arr));

        arr = new int[]{2, 1, 2, 3, 5, 7, 9, 10, 12, 14, 15, 16, 18, 14, 13};
        assertFalse(method.test(arr));
    }

    /**
     * 这个问题主要考你的逻辑思考是否缜密，能否考虑到每一种情况。
     * 应该先把可能的情况列出来再求解题目。
     *
     * 此题还有更简洁的做法。
     */
    public boolean validMountainArray(int[] arr) {
        if (arr.length < 3)
            return false;

        int len = arr.length;
        byte state = 0;
        for (int i = 0; i < len - 1; i++) {
            int cur = arr[i], next = arr[i + 1];
            if (cur == next)
                return false;
            else if (cur < next) {
                if (state == 0)
                    state = 1;
                else if (state == 2)
                    return false;
            } else {
                if (state == 1)
                    state = 2;
                else if (state == 0)
                    return false;
            }
        }

        return state == 2;
    }

    @Test
    public void testValidMountainArray() {
        test(this::validMountainArray);
    }


    /**
     * 使用两个指针不需要一起移动，满足条件移动其中一个也是解决问题的方法。
     */
    public boolean conciseMethod(int[] arr) {
        if (arr.length < 3)
            return false;

        int start = 0, end = arr.length - 1;
        while (start < end) {
            if (arr[start] - arr[start + 1] < 0)
                start++;
            else if (arr[end] - arr[end - 1] < 0)
                end--;
            else
                break;
        }

        return start != 0 && end != arr.length - 1 && start == end;
    }

    @Test
    public void testConciseMethod() {
        test(this::conciseMethod);
    }
}
