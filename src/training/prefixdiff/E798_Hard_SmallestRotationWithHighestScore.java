package training.prefixdiff;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 798. 得分最高的最小轮调: https://leetcode-cn.com/problems/smallest-rotation-with-highest-score/
 *
 * 给你一个数组 nums，我们可以将它按一个非负整数 k 进行轮调，这样可以使数组变为
 * [nums[k], nums[k + 1], ... nums[nums.length - 1], nums[0], nums[1], ..., nums[k-1]] 的形式。
 * 此后，任何值小于或等于其索引的项都可以记作一分。
 *
 * 例如，数组为 nums = [2,4,1,3,0]，我们按 k = 2 进行轮调后，它将变成 [1,3,0,2,4]。
 * 这将记为 3 分，因为 2 > 0 [不计分]、3 > 1 [不计分]、0 <= 2 [计 1 分]、2 <= 3 [计 1 分]，4 <= 4 [计 1 分]。
 *
 * 在所有可能的轮调中，返回我们所能得到的最高分数对应的轮调 k 。如果有多个答案，返回满足条件的最小的 k 。
 *
 * 例 1：
 * 输入：nums = [2,3,1,4,0]
 * 输出：3
 * 解释：
 * 下面列出了每个 k 的得分：
 * k = 0,  nums = [2,3,1,4,0],    score 2
 * k = 1,  nums = [3,1,4,0,2],    score 3
 * k = 2,  nums = [1,4,0,2,3],    score 3
 * k = 3,  nums = [4,0,2,3,1],    score 4
 * k = 4,  nums = [0,2,3,1,4],    score 3
 * 所以我们应当选择 k = 3，得分最高。
 *
 * 例 2：
 * 输入：nums = [1,3,0,2,4]
 * 输出：0
 * 解释：
 * nums 无论怎么变化总是有 3 分。
 * 所以我们将选择最小的 k，即 0。
 *
 * 说明：
 * - 1 <= nums.length <= 10^5
 * - 0 <= nums[i] < nums.length
 */
public class E798_Hard_SmallestRotationWithHighestScore {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(3, method.applyAsInt(new int[]{2,3,1,4,0}));
        assertEquals(0, method.applyAsInt(new int[]{1,3,0,2,4}));
    }

    /**
     * 差分数组解法，参见：
     * https://leetcode-cn.com/problems/smallest-rotation-with-highest-score/solution/de-fen-zui-gao-de-zui-xiao-lun-diao-by-l-hbtd/
     *
     * 对于数组 nums 中的元素 x，当 x 所在下标大于或等于 x 时，元素 x 会记 1 分。因此元素 x 记 1 分的下标范围是 [x,n−1]，
     * 有 n−x 个下标，元素 x 不计分的下标范围是 [0,x−1]，有 x 个下标。
     *
     * 假设元素 x 的初始下标为 i，则当轮调下标为 k 时，元素 x 位于下标 (i−k+n)%n。如果元素 x 记 1 分，
     * 则有 x≤(i−k+n)%n，等价于 k≤(i−x+n)%n，将这一值记为 k 对于 x 的上界。
     * 又由于元素 x 记 1 分的下标有 n−x 个，那么 k 的下界即可由区间长度和上界求得，下界为 k≥(i+1)%n。
     * 上界 - 下界 + 1 = 区间长度
     * 下界 = 上界 - 区间长度 + 1 = (i + n - nums[i]) % n - (n - nums[i]) + 1 = (i + 1) % n
     *
     * 将取模运算去掉之后，可以得到 k 的实际取值范围：
     * - 当 i < x 时，i+1 ≤ k ≤ i−x+n；
     * - 当 i ≥ x 时，k ≥ i+1 或 k ≤ i−x。
     *
     * 对于数组 nums 中的每个元素，都可以根据元素值与元素所在下标计算该元素记 1 分的 k 范围。
     * 遍历所有元素之后，即可得到每个 k 对应的计 1 分的元素个数，计 1 分的元素个数最多的 k 即为得分最高的 k。
     * 如果存在多个得分最高的 k，则取其中最小的 k。
     *
     * （逆向思维，找出元素记 1 分的下标范围）
     *
     * 创建长度为 n 的数组 diff，其中 diff[k] 表示轮调下标为 k 时的得分。对于数组 nums 中的每个元素，
     * 得到该元素记 1 分的 k 范围，然后将数组 diff 的该下标范围内的所有元素加 1。
     * 当数组 diff 中的元素值确定后，找到最大元素的最小下标。该做法的时间复杂度仍然是 O(n^2)，为了降低时间复杂度，
     * 需要利用差分数组，参见 {@link util.datastructure.DiffArray}。
     *
     * LeetCode 耗时：6 ms - 83.66%
     *          内存消耗：53.1 MB - 55.82%
     */
    public int bestRotation(int[] nums) {
        final int n = nums.length;
        int[] diff = new int[n];
        // 对于数组 nums 中的每个元素，得到该元素记 1 分的 k 范围
        for (int i = 0; i < n; i++) {
            int x = nums[i];
            if (i < x) {  // i+1 ≤ k ≤ i−x+n 范围内加 1
                diff[i + 1] += 1;
                if (i - x + n + 1 < n) {
                    diff[i - x + n + 1] -= 1;
                }
            } else {  // k ≥ i+1 或 k ≤ i−x 范围内加 1
                diff[0] += 1;
                if (i - x + 1 < n) {
                    diff[i - x + 1] -= 1;
                }
                if (i + 1 < n) {
                    diff[i + 1] += 1;
                }
            }
        }
        // 由差分数组推出原始数组
        for (int k = 1; k < n; k++) {
            diff[k] += diff[k - 1];
        }
        // 找到最大的 k
        int ans = 0, max = 0;
        for (int k = 0; k < n; k++) {
            if (diff[k] > max) {
                max = diff[k];
                ans = k;
            }
        }

        return ans;
    }

    @Test
    public void testBestRotation() {
        test(this::bestRotation);
    }


    /**
     * LeetCode 耗时：6 ms - 83.66%
     *          内存消耗：53.1 MB - 55.82%
     */
    public int conciseMethod(int[] nums) {
        int n = nums.length;
        int[] diffs = new int[n];
        for (int i = 0; i < n; i++) {
            int low = (i + 1) % n;
            int high = (i - nums[i] + n + 1) % n;
            diffs[low]++;
            diffs[high]--;
            if (low >= high) {
                diffs[0]++;
            }
        }

        int bestIndex = 0, maxScore = 0, score = 0;
        for (int i = 0; i < n; i++) {
            score += diffs[i];
            if (score > maxScore) {
                bestIndex = i;
                maxScore = score;
            }
        }

        return bestIndex;
    }

    @Test
    public void testConciseMethod() {
        test(this::conciseMethod);
    }


    /**
     * 动态规划，渐进分析。
     *
     * 发现每次移动一位的时候（k=1 在 k=0 的基础上移动一位，k=2 在 k=1 的基础上移动一位，以此类推），
     * nums[1..] 内的值下标-1，0转移到n-1。本来比下标小的依旧得分，本来等于下标的失分，本来大于下标的依旧不得分，
     * 本来在下标0的移到n-1处得分。
     *
     * 状态转移为 dp[k] = dp[k-1] - (k-1步时值与下标相等的元素个数) + 1。要得到每个步数时值与下标相等的元素个数，
     * 可实现由O(N)的预处理得到：根据每个元素移到与值相等的下标的步数计算
     *
     * LeetCode 耗时：5 ms - 91.21%
     *          内存消耗：52.9 MB - 83.13%
     */
    public int dpMethod(int[] nums) {
        final int n = nums.length;
        int score = 0;
        // 计算最开始的得分
        for (int i = 0; i < n; i++) {
            if (nums[i] <= i) {
                score++;
            }
        }

        /*
        step[k] 表示每个 k 对应多少下标相等的，它的最终的结果就是当前数移动多少步，跟下标相同了。
        举个例子 {2,3,1,4,0} 这个是初始数组，第一个2走3步，第二个3走3步，第三个1走1步，第四个4走4步，第五个0走4步,
        此时step {0,1,0,2,2} 这个就是每一步时值与下标相等的元素个数
         */
        int[] steps = new int[n];
        for (int i = 0; i < n; i++) {
            // 计算 nums[i] 和 i 之间的距离，注意每一步都是左旋
            if (nums[i] <= i) {
                steps[i - nums[i]]++;
            } else {
                steps[i + n - nums[i]]++;
            }
        }

        int ans = 0, maxScore = score;
        for (int k = 1; k < n; k++) {
            score += -steps[k - 1] + 1;
            if (score > maxScore) {
                maxScore = score;
                ans = k;
            }
        }

        return ans;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
