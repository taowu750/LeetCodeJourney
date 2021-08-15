package training.pointer;

import org.junit.jupiter.api.Test;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0？
 * 请你找出所有和为 0 且不重复的三元组。
 *
 * 注意：答案中不可以包含重复的三元组。
 *
 * 例 1：
 * Input：nums = [-1,0,1,2,-1,-4]
 * Output：[[-1,-1,2],[-1,0,1]]
 *
 * 例 2：
 * Input：nums = []
 * Output：[]
 *
 * 例 3：
 * Input：nums = [0]
 * Output：[]
 *
 * 约束：
 * - 0 <= nums.length <= 3000
 * - -10**5 <= nums[i] <= 10**5
 */
public class E15_Medium_ThreeSum {

    static void test(Function<int[], List<List<Integer>>> method) {
        List<List<Integer>> result = method.apply(new int[]{-1,0,1,2,-1,-4});
        System.out.println(result);
        assertTrue(CollectionUtil.deepEqualsIgnoreOrder(result,
                new ArrayList<>(Arrays.asList(Arrays.asList(-1,-1,2), Arrays.asList(-1,0,1)))));

        assertTrue(method.apply(new int[]{}).isEmpty());
        assertTrue(method.apply(new int[]{0}).isEmpty());

        result = method.apply(new int[]{-1,0,1,2,-1,-4,-2,-3,3,0,4});
        System.out.println(result);
        assertTrue(CollectionUtil.deepEqualsIgnoreOrder(result,
                new ArrayList<>(Arrays.asList(Arrays.asList(-4,0,4), Arrays.asList(-4,1,3)
                        , Arrays.asList(-3,-1,4), Arrays.asList(-3,0,3), Arrays.asList(-3,1,2)
                        , Arrays.asList(-2,-1,3), Arrays.asList(-2,0,2), Arrays.asList(-1,-1,2)
                        , Arrays.asList(-1,0,1)))));

        result = method.apply(new int[]{-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6});
        System.out.println(result);
        assertTrue(CollectionUtil.deepEqualsIgnoreOrder(result,
                new ArrayList<>(Arrays.asList(Arrays.asList(-4,-2,6), Arrays.asList(-4,0,4)
                        , Arrays.asList(-4,1,3), Arrays.asList(-4,2,2), Arrays.asList(-2,-2,4)
                        , Arrays.asList(-2,0,2)))));
    }

    /**
     * 双指针法。
     *
     * LeetCode 耗时：23 ms - 80.50%
     *          内存消耗：42.2 MB - 80.27%
     */
    public List<List<Integer>> threeSum(int[] nums) {
        // 先排序
        Arrays.sort(nums);
        if (nums.length < 3 || nums[0] > 0 || nums[nums.length - 1] < 0)
            return Collections.emptyList();

        List<List<Integer>> result = new ArrayList<>();
        // 双指针法
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0)
                return result;
            if (i > 0 && nums[i] == nums[i - 1])
                continue;
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1])
                        left++;
                    while (left < right && nums[right] == nums[right - 1])
                        right--;
                    left++;
                    right--;
                } else if (sum < 0)
                    left++;
                else
                    right--;
            }
        }

        return result;
    }

    @Test
    public void test() {
        test(this::threeSum);
    }
}
