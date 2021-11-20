package training.math;

import java.util.Arrays;

/**
 * 528. 按权重随机选择: https://leetcode-cn.com/problems/random-pick-with-weight/
 *
 * 给你一个 下标从 0 开始 的正整数数组 w ，其中 w[i] 代表第 i 个下标的权重。
 *
 * 请你实现一个函数 pickIndex，它可以随机地从范围 [0, w.length - 1] 内（含 0 和 w.length - 1）选出并返回一个下标。
 * 选取下标 i 的概率为 w[i] / sum(w) 。
 *
 * 例如，对于 w = [1, 3]，挑选下标 0 的概率为 1 / (1 + 3) = 0.25 （即，25%），
 * 而选取下标 1 的概率为 3 / (1 + 3) = 0.75（即，75%）。
 *
 * 例 1：
 * 输入：
 * ["Solution","pickIndex"]
 * [[[1]],[]]
 * 输出：
 * [null,0]
 * 解释：
 * Solution solution = new Solution([1]);
 * solution.pickIndex(); // 返回 0，因为数组中只有一个元素，所以唯一的选择是返回下标 0。
 *
 * 例 2：
 * 输入：
 * ["Solution","pickIndex","pickIndex","pickIndex","pickIndex","pickIndex"]
 * [[[1,3]],[],[],[],[],[]]
 * 输出：
 * [null,1,1,1,1,0]
 * 解释：
 * Solution solution = new Solution([1, 3]);
 * solution.pickIndex(); // 返回 1，返回下标 1，返回该下标概率为 3/4 。
 * solution.pickIndex(); // 返回 1
 * solution.pickIndex(); // 返回 1
 * solution.pickIndex(); // 返回 1
 * solution.pickIndex(); // 返回 0，返回下标 0，返回该下标概率为 1/4 。
 *
 * 由于这是一个随机问题，允许多个答案，因此下列输出都可以被认为是正确的:
 * [null,1,1,1,1,0]
 * [null,1,1,1,1,1]
 * [null,1,1,1,0,0]
 * [null,1,1,1,0,1]
 * [null,1,0,1,0,0]
 * ......
 * 诸若此类。
 *
 * 说明：
 * - 1 <= w.length <= 10^4
 * - 1 <= w[i] <= 10^5
 * - pickIndex 将被调用不超过 10^4 次
 */
public class E528_Medium_RandomPickWithWeight {
}

interface IRandomPickWithWeight {

    int pickIndex();
}

/**
 * 前缀和+二分查找，参见：
 * https://leetcode-cn.com/problems/random-pick-with-weight/solution/an-quan-zhong-sui-ji-xuan-ze-by-leetcode-h13t/
 *
 * LeetCode 耗时：24 ms - 90.70%
 *          内存消耗：43.3 MB - 58.37%
 */
class RandomPickWithWeight implements IRandomPickWithWeight {

    private int[] prefix;
    private int total;

    public RandomPickWithWeight(int[] w) {
        prefix = new int[w.length];
        prefix[0] = w[0];
        total = w[0];
        for (int i = 1; i < w.length; i++) {
            prefix[i] = prefix[i - 1] + w[i];
            total += w[i];
        }
    }

    public int pickIndex() {
        int x = (int) (Math.random() * total) + 1;
        int i = Arrays.binarySearch(prefix, x);
        return i < 0 ? -i - 1 : i;
    }
}
