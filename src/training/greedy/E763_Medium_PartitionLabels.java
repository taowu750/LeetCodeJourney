package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 763. 划分字母区间: https://leetcode-cn.com/problems/partition-labels/
 *
 * 字符串 S 由小写字母组成。我们要把这个字符串划分为「尽可能多」的片段，同一字母最多出现在一个片段中。
 * 返回一个表示每个字符串片段的长度的列表。
 *
 * 例 1：
 * 输入：S = "ababcbacadefegdehijhklij"
 * 输出：[9,7,8]
 * 解释：
 * 划分结果为 "ababcbaca", "defegde", "hijhklij"。
 * 每个字母最多出现在一个片段中。
 * 像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少。
 *
 * 说明：
 * - S的长度在[1, 500]之间。
 * - S只包含小写字母 'a' 到 'z' 。
 */
public class E763_Medium_PartitionLabels {

    public static void test(Function<String, List<Integer>> method) {
        assertEquals(Arrays.asList(9,7,8), method.apply("ababcbacadefegdehijhklij"));
    }

    /**
     * 类似于 {@link E56_Medium_MergeIntervals}。
     *
     * LeetCode 耗时：4 ms - 58.86%
     *          内存消耗：37 MB - 64.54%
     */
    public List<Integer> partitionLabels(String s) {
        // intervals 记录每个字符在 s 中的区间
        int[][] intervals = new int[26][2];
        for (int i = 0; i < 26; i++) {
            intervals[i][0] = intervals[i][1] = -1;
        }

        // 遍历 s，更新区间
        for (int i = 0; i < s.length(); i++) {
            int idx = s.charAt(i) - 'a';
            if (intervals[idx][0] == -1) {
                intervals[idx][0] = intervals[idx][1] = i;
            } else {
                intervals[idx][1] = i;
            }
        }
        // 按照区间开头排序区间
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        // 跳过不存在的字符
        int i = 0;
        while (intervals[i][0] == -1) {
            i++;
        }

        // 找出所有重叠的区间，加到结果中
        List<Integer> result = new ArrayList<>(26);
        int[] lastInterval = intervals[i];
        for (i++; i < intervals.length; i++) {
            if (intervals[i][0] < lastInterval[1]) {
                lastInterval[1] = Math.max(lastInterval[1], intervals[i][1]);
            } else {
                result.add(lastInterval[1] - lastInterval[0] + 1);
                lastInterval = intervals[i];
            }
        }
        result.add(lastInterval[1] - lastInterval[0] + 1);

        return result;
    }

    @Test
    public void testPartitionLabels() {
        test(this::partitionLabels);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/partition-labels/solution/hua-fen-zi-mu-qu-jian-by-leetcode-solution/
     *
     * LeetCode 耗时：4 ms - 58.86%
     *          内存消耗：37 MB - 64.54%
     */
    public List<Integer> greedyMethod(String s) {
        // 记录字符的结束下标
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
        }

        List<Integer> result = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            // 更新区间的结束下标为较大值
            end = Math.max(end, last[s.charAt(i) - 'a']);
            // 当便利到区间的结束下标时，可以添加结果并开始下一区间
            if (i == end) {
                result.add(end - start + 1);
                start = end + 1;
            }
        }

        return result;
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
