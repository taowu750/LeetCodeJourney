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
     *    - 常规逻辑下，递归函数返回了 false，那么我们需要尝试将工作 i 分配给其他工人，假设分配给了工人 j'，
     *      那么此时工人 j' 的工作量必定不多于工人 j 的工作量；
     *    - 如果存在一个方案使得分配给工人 j' 能够成功完成分配任务，那么此时必然有一个或一组工作 i' 取代了
     *      工作 i 被分配给工人 j，否则我们可以直接将工作 i 移交给工人 j，仍然能成功完成分配任务。
     *      而我们知道工作 i' 的总工作量不会超过工作 i，因此我们可以直接交换工作 i 与工作 i'，
     *      仍然能成功完成分配任务。这与假设不符，可知不存在这样一个满足条件的工人 j'
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
}
