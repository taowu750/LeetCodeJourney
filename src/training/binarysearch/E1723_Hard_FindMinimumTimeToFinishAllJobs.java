package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1723. 完成所有工作的最短时间: https://leetcode-cn.com/problems/find-minimum-time-to-finish-all-jobs/
 *
 * 给你一个整数数组 jobs ，其中 jobs[i] 是完成第 i 项工作要花费的时间。
 *
 * 请你将这些工作分配给 k 位工人。所有工作都应该分配给工人，且每项工作只能分配给一位工人。
 * 工人的「工作时间」是完成分配给他们的所有工作花费时间的总和。请你设计一套最佳的工作分配方案，
 * 使工人的「最大工作时间」得以「最小化」。
 *
 * 返回分配方案中尽可能「最小」的「最大工作时间」。
 *
 * 例 1：
 * 输入：jobs = [3,2,3], k = 3
 * 输出：3
 * 解释：给每位工人分配一项工作，最大工作时间是 3 。
 *
 * 例 2：
 * 输入：jobs = [1,2,4,7,8], k = 2
 * 输出：11
 * 解释：按下述方式分配工作：
 * 1 号工人：1、2、8（工作时间 = 1 + 2 + 8 = 11）
 * 2 号工人：4、7（工作时间 = 4 + 7 = 11）
 * 最大工作时间是 11 。
 *
 * 约束：
 * - 1 <= k <= jobs.length <= 12
 * - 1 <= jobs[i] <= 10**7
 */
public class E1723_Hard_FindMinimumTimeToFinishAllJobs {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(3, method.applyAsInt(new int[]{3,2,3}, 3));
        assertEquals(11, method.applyAsInt(new int[]{1,2,4,7,8}, 2));
    }

    /**
     * 二分查找结合回溯法。
     *
     * 思路来源于：
     * https://leetcode-cn.com/problems/find-minimum-time-to-finish-all-jobs/solution/wan-cheng-suo-you-gong-zuo-de-zui-duan-s-hrhu/
     *
     * LeetCode 耗时：1 ms - 99.65%
     *          内存消耗：35.6 MB - 86.97%
     */
    public int minimumTimeRequired(int[] jobs, int k) {
        int max = Integer.MIN_VALUE, sum = 0;
        for (int job : jobs) {
            if (max < job)
                max = job;
            sum += job;
        }
        if (k == 1)
            return sum;
        if (k == jobs.length)
            return max;

        Arrays.sort(jobs);

        int[] workers = new int[k];
        int lo = max, hi = sum;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            // 如果 mid 可以满足，那么收缩最大边界（因为我们要找满足条件的最小值）
            if (dfs(jobs, jobs.length - 1, workers, mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
            Arrays.fill(workers, 0);
        }

        return lo;
    }

    /**
     * 回溯法分配任务，并保证每个工人的最大任务量不超过 limit。
     * 无需确保每个工人都被分配了任务，因为任务数量大于工人数量，只要不超过 limit，
     * 随时可以从其他工人那里拿任务给未分配任务的工人。
     *
     * 每次尝试将任务 i 分配给工人。我们有以下三个优化：
     * 1. 优先分配工作量大的工作
     *    感性地理解，如果要求将小石子和大石块放入玻璃瓶中，优先放入大石块更容易使得工作变得简单，
     *    并且更容易触发剪枝条件。在搜索过程中，优先分配工作量小的工作会使得工作量大的工作更有可能最后无法被分配。
     * 2. 当工人 j 还没被分配工作时，我们不给工人 j+1 分配工作。
     *    如果当前工人 j 和 j+1 都没有被分配工作，那么我们将工作先分配给任何一个人都没有区别;
     *    如果分配给工人 j 不能成功完成分配任务，那么分配给工人 j+1 也一样无法完成。
     * 3. 当我们将工作 i 分配给工人 j，使得工人 j 的工作量恰好达到 limit，且计算分配下一个工作的递归函数返回了
     *    false，此时即无需尝试将工作 i 分配给其他工人，直接返回 false 即可。
     *    - 常规逻辑下，递归函数返回了 false，那么我们需要尝试将工作 i 分配给其他工人，假设分配给了工人 j'；
     *    - 如果存在一个方案使得分配给工人 j' 能够成功完成分配任务，那么此时必然有一个或一组工作 i' 取代了
     *      工作 i 被分配给工人 j，且工作 i' 的总工作量不会超过工作 i，即此时工人 j 的总工时必然 < limit
     *    - 反推之，既然把比 i 还小的 i' 分配给工人 j 是能 DFS 成功的，就意味着原来把 i 分配给工人 j 更应该能成功
     *    （因为这种情况下工人 j 分担了最大的 limit），这表明之前的 DFS 不应该失败。所以反证不成立。
     *    - 因为 jobs 是排序的，若 i 给工人 j 后，工人 j 的工作量刚好等于 limit，此时剩余的工作分配失败，
     *      说明 i 之后的小工作量不能被剩余的工人在 limit 期限内完成；若此时将 i 之后的小工作量（可能是多个，没影响）
     *      给到工人 j，也就是 i 分给工人 j 之后的工人，相当于分给工人 j 之后工人的工作量增大了，自然更无法完成。
     */
    private boolean dfs(int[] jobs, int i, int[] workers, int limit) {
        // 任务分配完了，返回 true
        if (i < 0)
            return true;

        int cur = jobs[i];
        // 注意要遍历所有工人，搜寻所有可能性。而不是先给一个工人分配完，再分配下一个工人。
        for (int j = 0; j < workers.length; j++) {
            if (workers[j] + cur <= limit) {
                workers[j] += cur;
                if (dfs(jobs, i - 1, workers, limit))
                    return true;
                workers[j] -= cur;
            }
            /*
            如果当前工人未被分配工作，那么下一个工人也必然未被分配工作。
            或者当前工作恰能使该工人的工作量达到了上限，这两种情况下我们无需尝试继续分配工作。
             */
            if (workers[j] == 0 || workers[j] + cur == limit)
                break;
        }

        return false;
    }

    @Test
    public void testMinimumTimeRequired() {
        test(this::minimumTimeRequired);
    }


    /**
     * 动态规划解法，参见：
     * https://leetcode-cn.com/problems/find-minimum-time-to-finish-all-jobs/solution/zhuang-ya-dp-jing-dian-tao-lu-xin-shou-j-3w7r/
     *
     * LeetCode 耗时：87 ms - 24.20%
     *          内存消耗：37.7 MB - 22.80%
     */
    public int dpMethod(int[] jobs, int k) {
        int n = jobs.length;

        // 用 [0, 2^N] 之间的整数表示 jobs 的子集，tot[i] 代表子集 i 的工作总时间
        int[] tot = new int[1 << n];
        for (int i = 1; i < tot.length; i++) {
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) == 0) {
                    continue;
                }
                // i - (1 << j) 表示「子集 i 中去掉了元素 j 后剩下的那部分」
                tot[i] = tot[i - (1 << j)] + jobs[j];
                break;
            }
        }

        // dp[i][j] 表示前 i 个工人为了完成作业子集 j，需要花费的最大工作时间的最小值；
        // dp[i][j] = min(max(dp[i-1][i-s], tot[s]))   s ∈ i（s 是 i 的子集）
        int[][] dp = new int[k][1 << n];
        // 初始化只有一个工人的情况
        System.arraycopy(tot, 0, dp[0], 0, tot.length);

        for (int i = 1; i < k; i++) {
            for (int j = 0; j < tot.length; j++) {
                int minVal = Integer.MAX_VALUE;
                // 枚举 j 的全部子集，参见：https://blog.csdn.net/wat1r/article/details/114298873#_329
                for (int s = j; s != 0 ; s = (s - 1) & j) {
                    minVal = Math.min(minVal, Math.max(dp[i - 1][j - s], tot[s]));
                }
                dp[i][j] = minVal;
            }
        }

        return dp[k - 1][tot.length - 1];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * 二分查找结合动态规划，在 n 比较大时会比单纯的动态规划更好。参见：
     * https://leetcode-cn.com/problems/find-minimum-time-to-finish-all-jobs/solution/zhuang-ya-dp-jing-dian-tao-lu-xin-shou-j-3w7r/
     *
     * LeetCode 耗时：282 ms - 10.20%
     *          内存消耗：36.1 MB - 26.20%
     */
    public int dpAndBinarySearchMethod(int[] jobs, int k) {
        int n = jobs.length;

        int max = Integer.MIN_VALUE, sum = 0;
        for (int job : jobs) {
            if (max < job)
                max = job;
            sum += job;
        }

        int[] tot = new int[1 << n];
        for (int i = 1; i < tot.length; i++) {
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) == 0) {
                    continue;
                }
                tot[i] = tot[i - (1 << j)] + jobs[j];
                break;
            }
        }

        int[] dp = new int[1 << n];
        Arrays.fill(dp, Integer.MAX_VALUE >>> 1);
        dp[0] = 0;

        int lo = max, hi = sum;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            for (int i = 0; i < tot.length; i++) {
                for (int s = i; s != 0; s = (s - 1) & i) {
                    if (tot[s] <= mid) {
                        dp[i] = Math.min(dp[i], dp[i - s] + 1);
                    }
                }
            }

            if (dp[(1 << n) - 1] <= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }

            Arrays.fill(dp, Integer.MAX_VALUE >>> 1);
            dp[0] = 0;
        }

        return lo;
    }

    @Test
    public void testDpAndBinarySearchMethod() {
        test(this::dpAndBinarySearchMethod);
    }
}
