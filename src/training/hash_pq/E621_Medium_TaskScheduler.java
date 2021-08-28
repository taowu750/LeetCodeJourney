package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 621. 任务调度器: https://leetcode-cn.com/problems/task-scheduler/
 *
 * 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。
 * 任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，
 * 或者处于待命状态。
 *
 * 然而，两个「相同种类」的任务「之间」必须有长度为整数 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，
 * 或者在待命状态。
 *
 * 你需要计算完成所有任务所需要的「最短时间」。
 *
 * 例 1：
 * 输入：tasks = ["A","A","A","B","B","B"], n = 2
 * 输出：8
 * 解释：A -> B -> (待命) -> A -> B -> (待命) -> A -> B
 *      在本示例中，两个相同类型任务之间必须间隔长度为 n = 2 的冷却时间，而执行一个任务只需要一个单位时间，
 *      所以中间出现了（待命）状态。
 *
 * 例 2：
 * 输入：tasks = ["A","A","A","B","B","B"], n = 0
 * 输出：6
 * 解释：在这种情况下，任何大小为 6 的排列都可以满足要求，因为 n = 0
 * ["A","A","A","B","B","B"]
 * ["A","B","A","B","A","B"]
 * ["B","B","B","A","A","A"]
 * ...
 * 诸如此类
 *
 * 例 3：
 * 输入：tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
 * 输出：16
 * 解释：一种可能的解决方案是：
 *      A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> (待命) -> (待命) -> A -> (待命) -> (待命) -> A
 *
 * 约束：
 * - 1 <= task.length <= 10^4
 * - tasks[i] 是大写英文字母
 * - n 的取值范围为 [0, 100]
 */
public class E621_Medium_TaskScheduler {

    static void test(ToIntBiFunction<char[], Integer> method) {
        assertEquals(8, method.applyAsInt(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2));
        assertEquals(6, method.applyAsInt(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 0));
        assertEquals(16, method.applyAsInt(new char[]{'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G'}, 2));
        assertEquals(12, method.applyAsInt(new char[]{'A', 'A', 'A', 'B', 'B', 'B', 'C', 'C', 'C', 'D', 'D', 'E'}, 2));
        assertEquals(52, method.applyAsInt(new char[]{'A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E', 'F', 'F',
            'G', 'G', 'H', 'H', 'I', 'I', 'J', 'J', 'K', 'K', 'L', 'L', 'M', 'M', 'N', 'N', 'O', 'O', 'P', 'P', 'Q', 'Q',
            'R', 'R', 'S', 'S', 'T', 'T', 'U', 'U', 'V', 'V', 'W', 'W', 'X', 'X', 'Y', 'Y', 'Z', 'Z'}, 2));
    }

    static class Pair implements Comparable<Pair> {
        char c;
        int cnt;

        public Pair(char c, int cnt) {
            this.c = c;
            this.cnt = cnt;
        }

        @Override
        public int compareTo(Pair o) {
            return Integer.compare(o.cnt, cnt);
        }

        @Override
        public String toString() {
            return "{" + c + ", " + cnt + '}';
        }
    }

    /**
     * LeetCode 耗时：20 ms - 24.64%
     *          内存消耗：38.6 MB - 93.09%
     */
    public int leastInterval(char[] tasks, int n) {
        if (n == 0) {
            return tasks.length;
        }

        Map<Character, Integer> t2c = new HashMap<>((int) (Math.min(tasks.length, 26) / 0.75) + 1);
        for (char task : tasks) {
            t2c.merge(task, 1, Integer::sum);
        }

        PriorityQueue<Pair> pq = new PriorityQueue<>(t2c.size());
        t2c.forEach((ch, cnt) -> pq.add(new Pair(ch, cnt)));

        int result = 0;
        Pair[] tmps = new Pair[n + 1];
        while (pq.size() > 1) {
            // 从优先队列中弹出 min(size, n + 1) 个任务，它们可以依次运行
            int needRemove = Math.min(pq.size(), n + 1);
            for (int i = 0; i < needRemove; i++) {
                tmps[i] = pq.remove();
                tmps[i].cnt--;
            }
            // 将还存在的任务继续放回去
            for (int i = 0; i < needRemove; i++) {
                if (tmps[i].cnt != 0) {
                    pq.add(tmps[i]);
                }
            }

            result += needRemove;
            // 如果不同的任务数不足 n + 1 个，并且还有其他任务需要运行
            // 那么就要加上等待的时间
            if (needRemove < n + 1 && pq.size() > 0) {
                result += n + 1 - needRemove;
            }
        }

        // 剩余任务加上运行时间和等待时间
        if (pq.size() > 0) {
            Pair p = pq.remove();
            result += p.cnt + (p.cnt - 1) * n;
        }

        return result;
    }

    @Test
    public void testLeastInterval() {
        test(this::leastInterval);
    }
}
