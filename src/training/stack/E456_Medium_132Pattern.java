package training.stack;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 456. 132 模式: https://leetcode-cn.com/problems/132-pattern/
 *
 * 给你一个整数数组 nums ，数组中共有 n 个整数。132 模式的子序列由三个整数 nums[i]、nums[j] 和 nums[k] 组成，
 * 并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j] 。
 *
 * 如果 nums 中存在 132 模式的子序列 ，返回 true ；否则，返回 false 。
 *
 * 例 1：
 * 输入：nums = [1,2,3,4]
 * 输出：false
 * 解释：序列中不存在 132 模式的子序列。
 *
 * 例 2：
 * 输入：nums = [3,1,4,2]
 * 输出：true
 * 解释：序列中有 1 个 132 模式的子序列： [1, 4, 2] 。
 *
 * 例 3：
 * 输入：nums = [-1,3,2,0]
 * 输出：true
 * 解释：序列中有 3 个 132 模式的的子序列：[-1, 3, 2]、[-1, 3, 0] 和 [-1, 2, 0] 。
 *
 * 说明：
 * - n == nums.length
 * - 1 <= n <= 2 * 10^5
 * - -10^9 <= nums[i] <= 10^9
 */
public class E456_Medium_132Pattern {

    public static void test(Predicate<int[]> method) {
        assertFalse(method.test(new int[]{1,2,3,4}));
        assertTrue(method.test(new int[]{3,1,4,2}));
        assertTrue(method.test(new int[]{-1,3,2,0}));
        assertFalse(method.test(new int[]{-1,3,-3,-2}));
        assertTrue(method.test(new int[]{3,5,0,3,2}));
        assertTrue(method.test(new int[]{3,5,0,3,4}));
        assertFalse(method.test(new int[]{-2}));
    }

    /**
     * 枚举3，参见：
     * https://leetcode-cn.com/problems/132-pattern/solution/132mo-shi-by-leetcode-solution-ye89/
     *
     * 选取 i ∈ [1,n-1) 作为 3。维护 [0,i) 中的最小值，并维护 [i+1, n) 的有序集合，使用它们选择 1、2。
     *
     * LeetCode 耗时：102 ms - 10.56%
     *          内存消耗：64.6 MB - 5.09%
     */
    public boolean find132pattern(int[] nums) {
        if (nums.length < 3) {
            return false;
        }
        // 1: [0, i) 中的最小值
        int num1 = nums[0];
        // 2: [i+1, n) 的有序集合
        TreeMap<Integer, Integer> right = new TreeMap<>();
        for (int j = 2; j < nums.length; j++) {
            right.merge(nums[j], 1, Integer::sum);
        }
        for (int i = 1; i < nums.length - 1; i++) {
            int num3 = nums[i];
            if (num3 > num1) {
                // 找到右边刚好大于 num1 的 num2
                Integer num2 = right.higherKey(num1);
                // 如果 num2 存在并且小于 num3，则找到了 132 序列
                if (num2 != null && num2 < num3) {
                    return true;
                }
            }
            // 更新 num1
            num1 = Math.min(num1, num3);
            // 移除 i+1
            right.merge(nums[i + 1], -1, (old, neg) -> old + neg > 0 ? old + neg : null);
        }

        return false;
    }

    @Test
    public void testFind132pattern() {
        test(this::find132pattern);
    }


    /**
     * 枚举 1。参见：
     * https://leetcode-cn.com/problems/132-pattern/solution/132mo-shi-by-leetcode-solution-ye89/
     *
     * 如果我们从左到右枚举 1 的下标 i，那么 j,k 的下标范围都是减少的，这样就不利于对它们进行维护。
     * 因此我们可以考虑从右到左枚举 i。
     *
     * 那么我们应该如何维护 j,k 呢？在 132 模式中，如果 1<2 并且 2<3，那么根据传递性，1<3 也是成立的，
     * 所以我们可以「维护满足 32 模式的最大的 2」，只要 1 比这个 2 小就可以了。那么我们可以使用下面的方法进行维护：
     * - 我们使用一种数据结构维护所有遍历过的元素，它们作为 2 的候选元素。每当我们遍历到一个新的元素时，
     *   就将其加入数据结构中；
     * - 在遍历到一个新的元素的同时，我们可以考虑其是否可以作为 3。如果它作为 3，那么数据结构中所有严格
     *   小于它的元素都可以作为 2，我们将这些元素全部从数据结构中移除，并且使用一个变量维护「所有」被移除的元素的最大值。
     *   这些被移除的元素都是可以真正作为 2 的，并且元素的值越大，那么我们之后找到 1 的机会也就越大。
     * - 通过这种方式，我们可以找到符合 32 模式的最大的 2。
     *
     * 那么这个「数据结构」是什么样的数据结构呢？我们尝试提取出它进行的操作：
     * - 它需要支持添加一个元素；
     * - 它需要支持移除所有严格小于给定阈值的所有元素；
     * - 上面两步操作是「依次进行」的，即我们先用给定的阈值移除元素，再将该阈值加入数据结构中。
     *
     * 这就可以使用「单调栈」。在单调栈中，从栈底到栈顶的元素是严格单调递减的，记录从右到左的单减序列。
     * 当给定阈值 x 时，我们只需要不断地弹出栈顶的元素，直到栈为空或者 x 严格小于栈顶元素。此时我们再将 x 入栈，
     * 这样就维护了栈的单调性。
     *
     * LeetCode 耗时：10 ms - 91.44%
     *          内存消耗：62.9 MB - 21.40%
     */
    public boolean enum1(int[] nums) {
        int n = nums.length;
        if (n < 3) {
            return false;
        }

        Deque<Integer> stack = new LinkedList<>();
        stack.push(nums[n - 1]);
        int num2 = Integer.MIN_VALUE;

        for (int i = n - 2; i >= 0; i--) {
            // 尝试将 nums[i] 作为 num1
            if (nums[i] < num2) {
                return true;
            }

            /*
            判断 nums[i] 是否可以作为 3，以此找出哪些可以真正作为 2 的元素。
            我们不断将 nums[i] 与单调栈栈顶的元素进行比较，如果 nums[i] 较大，
            那么栈顶元素可以真正作为 2，将其弹出并更新 num2。

            通过这种方式，我们可以找到符合 32 模式的最大的 2，那么之后找到 1 的机会也就越大。
             */
            while (!stack.isEmpty() && nums[i] > stack.peek()) {
                num2 = stack.pop();
            }
            // 如果 nums[i] 可以作为 3
            if (nums[i] > num2) {
                // 将 nums[i] 放入栈中作为 3，num2 是小于它的最大数
                stack.push(nums[i]);
            }
        }

        return false;
    }

    @Test
    public void testEnum1() {
        test(this::enum1);
    }


    /**
     * 枚举 2。参见：
     * https://leetcode-cn.com/problems/132-pattern/solution/132mo-shi-by-leetcode-solution-ye89/
     *
     * 此方法的优点是可以处理在线问题（就是流式输入）。
     *
     * 和枚举 1 类似，不过这次我们从左到右枚举 k，那么 i,j 的下标范围都是增加的，利于维护。
     *
     * 我们可以分情况进行讨论，假设当前有一个小元素 xi 以及一个大元素 xj 表示一个二元组，
     * 而我们当前遍历到了一个新的元素 x=nums[k]，那么：
     * - 如果 x > xj，那么让 x 作为 3 显然是比 xj 作为 3 更优，因此我们可以用 x 替代 xj；
     * - 如果 x < xi，那么让 x 作为 1 显然是比 xi 作为 3 更优，然而我们必须要满足 132 模式中的顺序，
     *   因此我们需要为 x 找一个新的元素作为 3。由于我们还没有遍历到后面的元素，
     *   因此可以简单地将 x 同时看作一个二元组的 xi 和 xj。
     * - 如果 xi <= x <= xj，那么可以不用考虑它。
     *
     * 我们使用两个栈底到栈顶单调递减的单调栈维护一系列二元组 (xi, xj)（一个栈维护 xi，另一个维护 xj）。
     * 然而与方法二不同的是，如果我们想让 x 作为 2，那么我们并不知道到底应该选择单调栈中的哪个 1-3 区间，
     * 因此我们只能根据单调性进行二分查找。
     *
     * LeetCode 耗时：17 ms - 15.46%
     *          内存消耗：58.4 MB - 46.38%
     */
    public boolean enum2(int[] nums) {
        int n = nums.length;
        // stackI、stackJ 维护 (xi, xj) 元组，它们都是递减的
        List<Integer> stackI = new ArrayList<>(), stackJ = new ArrayList<>();
        stackI.add(nums[0]);
        stackJ.add(nums[0]);

        for (int k = 1; k < n; k++) {
            int idxI = binarySearchFirst(stackI, nums[k]);
            int idxJ = binarySearchLast(stackJ, nums[k]);
            // 找到 132 序列
            if (idxI >= 0 && idxJ >= 0 && idxI <= idxJ) {
                return true;
            }

            // x < xi 的情况
            if (nums[k] < stackI.get(stackI.size() - 1)) {
                stackI.add(nums[k]);
                stackJ.add(nums[k]);
            }
            // x > xj 的情况
            else if (nums[k] > stackJ.get(stackJ.size() - 1)) {
                int lastI = stackI.get(stackI.size() - 1);
                // 维护 stackJ 的单调性
                while (!stackJ.isEmpty() && stackJ.get(stackJ.size() - 1) <= nums[k]) {
                    stackJ.remove(stackJ.size() - 1);
                    stackI.remove(stackI.size() - 1);
                }
                // 注意，因为是将 (xi, xj) 中的 xj 替换成了 nums[k]，所以原来的 xi 不变
                stackJ.add(nums[k]);
                stackI.add(lastI);
            }
            // xi <= x <= xj 的情况，此时没有构成 132 的序列，且它也没有什么用
        }

        return false;
    }

    // 找到第一个小于 target 的下标，注意 candidate 是递减的
    public int binarySearchFirst(List<Integer> candidate, int target) {
        int low = 0, high = candidate.size() - 1;
        if (candidate.get(high) >= target) {
            return -1;
        }
        while (low < high) {
            int mid = (high - low) / 2 + low;
            int num = candidate.get(mid);
            if (target <= num) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // 找到最后一个大于 target 的下标，注意 candidate 是递减的
    public int binarySearchLast(List<Integer> candidate, int target) {
        int low = 0, high = candidate.size() - 1;
        if (candidate.get(low) <= target) {
            return -1;
        }
        while (low < high) {
            int mid = (high - low + 1) / 2 + low;
            int num = candidate.get(mid);
            if (target >= num) {
                high = mid - 1;
            } else {
                low = mid;
            }
        }
        return low;
    }

    @Test
    public void testEnum2() {
        test(this::enum2);
    }


    /**
     * 上一个方法的 O(n) 版本，使用前缀最小值数组避免二分查找。
     *
     * LeetCode 耗时：15 ms - 20.77%
     *          内存消耗：61.8 MB - 37.07%
     */
    public boolean betterEnum2(int[] nums) {
        int n = nums.length;
        // 单调栈，从栈底到栈顶单减，存的是下标。
        // 递减栈维护了 nums[i] 左边大于它的值；如果是递增栈，就维护的是 nums[i] 左边小于它的值
        Deque<Integer> stack = new LinkedList<>();
        // 当前遍历的元素是 nums[i]，数组对应的下标 i 存放的值是 [0,i-1] 的最小值
        List<Integer> minPrefix = new ArrayList<>(n);
        minPrefix.add(Integer.MAX_VALUE);

        // 枚举 nums[i] 作为 2
        for (int i = 0; i < n; i++) {
            // 维持单调栈，保证栈中的元素都大于 nums[i]
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
                stack.pop();
            }
            /*
            单调栈中还有比 nums[i] 更大的元素，即 [0,i-1] 中有比 nums[i] 更大的元素，则有 3 > 2；
            stack.peek() 是这个元素在 nums 的下标，而 minPrefix 存放的是 nums[i] 其左侧的最小值，
            于是 minPrefix[stack.peek()] 代表是单调栈中存在的元素的左侧的最小值，
            如果它小于 nums[i] 则满足了 1 < 2，则有 1 < 3 > 2
             */
            if (!stack.isEmpty() && minPrefix.get(stack.peek()) < nums[i]) {
                return true;
            }
            stack.push(i);
            // 更新 [0,i] 最小的元素
            minPrefix.add(Math.min(minPrefix.get(minPrefix.size() - 1), nums[i]));
        }

        return false;
    }

    @Test
    public void testBetterEnum2() {
        test(this::betterEnum2);
    }
}
