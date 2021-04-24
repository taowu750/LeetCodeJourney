package training.math;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 384. 打乱数组: https://leetcode-cn.com/problems/shuffle-an-array/
 *
 * 给你一个整数数组 nums ，设计算法来打乱一个没有重复元素的数组。
 *
 * 实现 Solution class:
 * - Solution(int[] nums) 使用整数数组 nums 初始化对象
 * - int[] reset() 重设数组到它的初始状态并返回
 * - int[] shuffle() 返回数组随机打乱后的结果
 *
 * 例 1：
 * 输入：
 * ["Solution", "shuffle", "reset", "shuffle"]
 * [[[1, 2, 3]], [], [], []]
 * 输出：
 * [null, [3, 1, 2], [1, 2, 3], [1, 3, 2]]
 * 解释：
 * Solution solution = new Solution([1, 2, 3]);
 * solution.shuffle();    // 打乱数组 [1,2,3] 并返回结果。任何 [1,2,3]的排列返回的概率应该相同。例如，返回 [3, 1, 2]
 * solution.reset();      // 重设数组到它的初始状态 [1, 2, 3] 。返回 [1, 2, 3]
 * solution.shuffle();    // 随机返回数组 [1, 2, 3] 打乱后的结果。例如，返回 [1, 3, 2]
 *
 * 约束：
 * - 1 <= nums.length <= 200
 * - -10**6 <= nums[i] <= 10**6
 * - nums 中的所有元素都是「唯一的」
 * - 最多可以调用 5 * 10**4 次 reset 和 shuffle
 */
public class E384_Medium_ShuffleArray {

    static void test(Function<int[], IShuffleArray> factory) {
        int[] nums = {1,2,3};
        IShuffleArray solution = factory.apply(nums);
        solution.shuffle();
        assertArrayEquals(nums, solution.reset());
        solution.shuffle();
        assertArrayEquals(nums, solution.reset());
    }

    @Test
    public void testShuffleArray() {
        test(ShuffleArray::new);
    }
}

interface IShuffleArray {

    int[] reset();

    int[] shuffle();
}

/**
 * Fisher-Yates 洗牌算法。参见：
 * https://leetcode-cn.com/problems/shuffle-an-array/solution/da-luan-shu-zu-by-leetcode/
 *
 * LeetCode 耗时：93 ms - 92.55%
 *          内存消耗：46.4 MB - 93.60%
 */
class ShuffleArray implements IShuffleArray {

    private int[] nums;
    private int[] a;
    private Random random;

    public ShuffleArray(int[] nums) {
        this.nums = nums;
        a = nums.clone();
        random = new Random();
    }

    @Override
    public int[] reset() {
        return nums;
    }

    @Override
    public int[] shuffle() {
        for (int i = 0; i < a.length - 1; i++) {
            int randIdx = random.nextInt(a.length - i) + i;
            int tmp = a[i];
            a[i] = a[randIdx];
            a[randIdx] = tmp;
        }
        return a;
    }
}
