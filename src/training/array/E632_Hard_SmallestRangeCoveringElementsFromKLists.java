package training.array;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 632. 最小区间: https://leetcode-cn.com/problems/smallest-range-covering-elements-from-k-lists/
 *
 * 你有 k 个「非递减排列」的整数列表。找到一个「最小」区间，使得 k 个列表中的每个列表至少有一个数包含在其中。
 *
 * 我们定义如果 b-a < d-c 或者在 b-a == d-c 时 a < c，则区间 [a,b] 比 [c,d] 小。
 *
 * 例 1：
 * 输入：nums = [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]
 * 输出：[20,24]
 * 解释：
 * 列表 1：[4, 10, 15, 24, 26]，24 在区间 [20,24] 中。
 * 列表 2：[0, 9, 12, 20]，20 在区间 [20,24] 中。
 * 列表 3：[5, 18, 22, 30]，22 在区间 [20,24] 中。
 *
 * 例 2：
 * 输入：nums = [[1,2,3],[1,2,3],[1,2,3]]
 * 输出：[1,1]
 *
 * 例 3：
 * 输入：nums = [[10,10],[11,11]]
 * 输出：[10,11]
 *
 * 例 4：
 * 输入：nums = [[10],[11]]
 * 输出：[10,11]
 *
 * 例 5：
 * 输入：nums = [[1],[2],[3],[4],[5],[6],[7]]
 * 输出：[1,7]
 *
 * 约束：
 * - nums.length == k
 * - 1 <= k <= 3500
 * - 1 <= nums[i].length <= 50
 * - -10**5 <= nums[i][j] <= 10**5
 * - nums[i] 按非递减顺序排列
 */
public class E632_Hard_SmallestRangeCoveringElementsFromKLists {

    static void test(Function<List<List<Integer>>, int[]> method) {
        assertArrayEquals(new int[]{20,24},
                method.apply(asList(asList(4,10,15,24,26), asList(0,9,12,20), asList(5,18,22,30))));

        assertArrayEquals(new int[]{1,1},
                method.apply(asList(asList(1,2,3), asList(1,2,3), asList(1,2,3))));

        assertArrayEquals(new int[]{10,11},
                method.apply(asList(asList(10,10), asList(11,11))));

        assertArrayEquals(new int[]{10,11},
                method.apply(asList(singletonList(10), singletonList(11))));

        assertArrayEquals(new int[]{1,7},
                method.apply(asList(singletonList(1), singletonList(2), singletonList(3),
                        singletonList(4), singletonList(5), singletonList(6), singletonList(7))));
    }

    static class State implements Comparable<State> {
        int val, idx;

        State(int val, int idx) {
            this.val = val;
            this.idx = idx;
        }

        @Override
        public int compareTo(State o) {
            int cmp = Integer.compare(val, o.val);
            return cmp != 0 ? cmp : Integer.compare(idx, o.idx);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            State state = (State) o;
            return val == state.val && idx == state.idx;
        }

        @Override
        public int hashCode() {
            return Objects.hash(val, idx);
        }

        @Override
        public String toString() {
            return "{" + val + ", " + idx + "}";
        }
    }

    /**
     * LeetCode 耗时：50 ms - 52.99%
     *          内存消耗：45 MB - 47.76%
     */
    public int[] smallestRange(List<List<Integer>> nums) {
        int k = nums.size();
        if (k == 1) {
            int b = nums.get(0).get(0);
            return new int[]{b,b};
        }

        TreeSet<State> treeSet = new TreeSet<>();
        int[] indices = new int[k];
        for (int i = 0; i < k; i++) {
            treeSet.add(new State(nums.get(i).get(0), i));
        }

        int lo = 0, hi = Integer.MAX_VALUE;
        outer: for(;;) {
            State min = treeSet.first();
            State max = treeSet.last();
            if (max.val - min.val < hi - lo) {
                lo = min.val;
                hi = max.val;
            }

            do {
                min = treeSet.pollFirst();
                // 跳过相等的值
                while (++indices[min.idx] < nums.get(min.idx).size()
                        && nums.get(min.idx).get(indices[min.idx]) == min.val);
                // 如果最小值序列的值全部遍历完了，则可以确定范围
                if (indices[min.idx] >= nums.get(min.idx).size())
                    break outer;
                // 添加最小值序列的下一个值
                treeSet.add(new State(nums.get(min.idx).get(indices[min.idx]), min.idx));

                // 如果还有别的序列的值等于 min，则也推进它们
            } while (!treeSet.isEmpty() && treeSet.first().val == min.val);
        }

        return new int[]{lo, hi};
    }

    @Test
    public void testSmallestRange() {
        test(this::smallestRange);
    }


    /**
     * 滑动窗口方法。
     *
     * LeetCode 耗时：90ms - 25%
     *          内存消耗：46.4MB - 26%
     */
    public int[] slidingWindowMethod(List<List<Integer>> nums) {
        int k = nums.size();
        if (k == 1) {
            int b = nums.get(0).get(0);
            return new int[]{b,b};
        }

        // 遍历所有数字。键是 nums 中的数字，值是包含此数字的列表的下标。滑动窗口将在 indices 上移动
        Map<Integer, List<Integer>> indices = new HashMap<>();
        Function<Integer, List<Integer>> compute = new Function<Integer, List<Integer>>() {
            @Override
            public List<Integer> apply(Integer integer) {
                return new LinkedList<>();
            }
        };
        // 记录最大最小值
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int i = 0; i < k; i++) {
            for (int num: nums.get(i)) {
                if (num < min)
                    min = num;
                if (num > max)
                    max = num;
                indices.computeIfAbsent(num, compute).add(i);
            }
        }

        int[] freq = new int[k];
        // 滑动窗口指针
        int left = min, right = min - 1, count = 0;
        int bestLeft = min, bestRight = max;
        outer: while (right < max) {
            right++;
            if (indices.containsKey(right)) {
                // right 存在于 nums 中。将对应列表添加到滑动窗口计数中
                for (int idx: indices.get(right)) {
                    if (++freq[idx] == 1)
                        count++;
                }
                // count 等于 k，说明滑动窗口已经包含了所有列表
                while (count == k) {
                    if (right - left < bestRight - bestLeft) {
                        bestLeft = left;
                        bestRight = right;
                    }
                    // 如果 left == right，则直接跳出，因为不会有比 0 更小的范围了
                    if (left == right)
                        break outer;
                    // 开始收缩滑动窗口，缩减 left
                    for (int idx: indices.get(left)) {
                        if (--freq[idx] == 0)
                            count--;
                    }
                    // 找到下一个存在于 indices 中的 left
                    while (++left < right && !indices.containsKey(left));
                }
            }
        }

        return new int[]{bestLeft, bestRight};
    }

    @Test
    public void testSlidingWindowMethod() {
        test(this::slidingWindowMethod);
    }
}
