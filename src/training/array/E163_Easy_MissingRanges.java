package training.array;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 163. 缺失的区间: https://leetcode-cn.com/problems/missing-ranges/
 *
 * 给定一个排序的整数数组 nums ，其中元素的范围在闭区间 [lower, upper] 当中，返回不包含在数组中的缺失区间。
 *
 * 例 1：
 * 输入: nums = [0, 1, 3, 50, 75], lower = 0 和 upper = 99,
 * 输出: ["2", "4->49", "51->74", "76->99"]
 */
public class E163_Easy_MissingRanges {

    static void test(TriFunction<int[], Integer, Integer, List<String>> method) {
        assertEquals(asList("2", "4->49", "51->74", "76->99"), method.apply(new int[]{0, 1, 3, 50, 75}, 0, 99));
        assertEquals(Collections.singletonList("1"), method.apply(new int[]{}, 1, 1));
        assertEquals(asList("0", "2", "6->8", "10"), method.apply(new int[]{1, 3, 4, 5, 9}, 0, 10));
        assertEquals(asList("0->1", "6->7", "9->10"), method.apply(new int[]{2, 3, 4, 5, 8}, 0, 10));
    }

    /**
     * LeetCode 耗时：6 ms - 76.31%
     *          内存消耗：36.9 MB - 39.24%
     */
    public List<String> findMissingRanges(int[] nums, int lower, int upper) {
        if (nums.length == 0) {
            return lower == upper ? Collections.singletonList(Integer.toString(lower))
                    : Collections.singletonList(lower + "->" + upper);
        }

        List<String> result = new ArrayList<>();

        // 端点要特殊处理
        int num = nums[0];
        if (num - lower > 1) {
            result.add(lower + "->" + (num - 1));
        } else if (num - lower == 1) {
            result.add(Integer.toString(lower));
        }

        int last = nums[0];
        for (int i = 1; i < nums.length; i++) {
            num = nums[i];
            if (num - last > 2) {
                result.add((last + 1) + "->" + (num - 1));
            } else if (num - last == 2) {
                result.add(Integer.toString(num - 1));
            }
            last = num;
        }

        if (upper - last > 1) {
            result.add((last + 1) + "->" + upper);
        } else if (upper - last == 1) {
            result.add(Integer.toString(upper));
        }

        return result;
    }

    @Test
    public void testFindMissingRanges() {
        test(this::findMissingRanges);
    }
}
