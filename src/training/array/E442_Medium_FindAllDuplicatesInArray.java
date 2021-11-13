package training.array;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static util.CollectionUtil.setEquals;

/**
 * 442. 数组中重复的数据: https://leetcode-cn.com/problems/find-all-duplicates-in-an-array/
 *
 * 给定一个整数数组 a，其中1 ≤ a[i] ≤ n （n为数组长度）, 其中有些元素出现两次而其他元素出现一次。
 * 找到所有出现两次的元素。
 *
 * 你可以不用到任何额外空间并在O(n)时间复杂度内解决这个问题吗？
 *
 * 例 1：
 * 输入:
 * [4,3,2,7,8,2,3,1]
 * 输出:
 * [2,3]
 */
public class E442_Medium_FindAllDuplicatesInArray {

    public static void test(Function<int[], List<Integer>> method) {
        setEquals(Arrays.asList(2, 3), method.apply(new int[]{4,3,2,7,8,2,3,1}));
    }

    /**
     * 交换法。
     *
     * LeetCode 耗时：6 ms - 57.89%
     *          内存消耗：47.3 MB - 47.36%
     */
    public List<Integer> findDuplicates(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != i + 1 && nums[i] != nums[nums[i] - 1]) {
                int tmp = nums[i];
                nums[i] = nums[tmp - 1];
                nums[tmp - 1] = tmp;
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                result.add(nums[i]);
            }
        }

        return result;
    }

    @Test
    public void testFindDuplicates() {
        test(this::findDuplicates);
    }


    /**
     * LeetCode 耗时：3 ms - 100.00%
     *          内存消耗：47.4 MB - 41.09%
     */
    public List<Integer> addMethod(int[] nums) {
        int n = nums.length;
        // 使用 num 作为下标，并对此下标位置+n，使用 % 使下标在合法范围内
        for (int num : nums) {
            nums[(num - 1) % n] = nums[(num - 1) % n] + n;
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // 重复的数会被加两次 n
            if (nums[i] > 2 * n) {
                result.add(i + 1);
            }
        }

        return result;
    }

    @Test
    public void testAddMethod() {
        test(this::addMethod);
    }
}
