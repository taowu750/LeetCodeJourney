package learn.queue_stack;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 您前面有 4 个圆形轮盘的锁。每个轮盘有10个插槽：“0”，“1”，“2”，“3”，“4”，“5”，“6”，“7”，“8”，“9”。
 * 轮盘可以自由旋转：例如，我们可以将“9”转为“0”，或者将“0”转为“9”。每一步将一个轮盘转动一个槽。
 * <p>
 * 锁最初从'0000'开始，代表 4 个轮盘的状态的字符串。
 * <p>
 * 给定“deadend”列表，这意味着如果锁显示列表中的任何一个字符串，则轮盘将停止转动，
 * <p>
 * 给定一个解锁的目标字符串，返回打开锁所需的最小总转数，如果不可能则返回 -1。
 * <p>
 * 例 1：
 * Input: deadends = ["0201","0101","0102","1212","2002"], target = "0202"
 * Output: 6
 * Explanation:
 * "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202"。
 * 需要注意的是，"0000" -> "0001" -> "0002" -> "0102" -> "0202" 是不行的，
 * 因为 "0102" 是 deadend。
 * <p>
 * 例 2：
 * Input: deadends = ["8888"], target = "0009"
 * Output: 1
 * Explanation:
 * "0000" -> "0009"
 * <p>
 * 例 3：
 * Input: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
 * Output: -1
 * <p>
 * 例 4:
 * Input: deadends = ["0000"], target = "8888"
 * Output: -1
 * <p>
 * 约束：
 * - 1 <= deadends.length <= 500
 * - deadends[i].length == 4
 * - target.length == 4
 * - target 不会出现在 deadend 列表中
 * - target 和 deadends[i] 只由数字组成
 */
public class OpenTheLock {

    static void test(ToIntBiFunction<String[], String> method) {
        String[] deadends = {"0201", "0101", "0102", "1212", "2002"};
        String target = "0202";
        assertEquals(method.applyAsInt(deadends, target), 6);

        deadends = new String[]{"8888"};
        target = "0009";
        assertEquals(method.applyAsInt(deadends, target), 1);

        deadends = new String[]{"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"};
        target = "8888";
        assertEquals(method.applyAsInt(deadends, target), -1);

        deadends = new String[]{"0000"};
        target = "8888";
        assertEquals(method.applyAsInt(deadends, target), -1);

        deadends = new String[]{"5557", "5553", "5575", "5535", "5755", "5355", "7555", "3555",
                "6655", "6455", "4655", "4455", "5665", "5445", "5645", "5465", "5566", "5544",
                "5564", "5546", "6565", "4545", "6545", "4565", "5656", "5454", "5654", "5456",
                "6556", "4554", "4556", "6554"};
        target = "5555";
        assertEquals(method.applyAsInt(deadends, target), -1);
    }

    /**
     * 常规的 BFS 方法。从起始状态出发，遍历整个状态空间直到解锁或无法推进。
     */
    public int openLock(String[] deadends, String target) {
        Set<String> deadendSet = new HashSet<>(Arrays.asList(deadends));
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        visited.add("0000");
        queue.offer("0000");

        StringBuilder codeBuilder = new StringBuilder("0000");
        int level = 0;
        while (!queue.isEmpty()) {
            for (int size = queue.size(); size > 0; size--) {
                String s = queue.remove();
                // 死锁，则跳过
                if (deadendSet.contains(s))
                    continue;
                if (s.equals(target))
                    return level;
                // 使用 codeBuilder 构造下一个轮盘序列
                for (int i = 0; i < 4; i++) {
                    codeBuilder.setCharAt(i, s.charAt(i));
                }
                for (int i = 0; i < 4; i++) {
                    char ch = s.charAt(i);
                    // 添加加 1 的轮盘序列
                    codeBuilder.setCharAt(i, ch == '9' ? '0' : (char) (ch + 1));
                    String nextCode = codeBuilder.toString();
                    if (!visited.contains(nextCode) && !deadendSet.contains(nextCode)) {
                        queue.add(nextCode);
                        visited.add(nextCode);
                    }
                    // 添加减 1 的轮盘序列
                    codeBuilder.setCharAt(i, ch == '0' ? '9' : (char) (ch - 1));
                    nextCode = codeBuilder.toString();
                    if (!visited.contains(nextCode) && !deadendSet.contains(nextCode)) {
                        queue.add(nextCode);
                        visited.add(nextCode);
                    }
                    codeBuilder.setCharAt(i, ch);
                }
            }
            level++;
        }

        return -1;
    }

    @Test
    public void testOpenLock() {
        test(this::openLock);
    }


    int[] bases = {1, 10, 100, 1000};

    /**
     * 双向 BFS，从起始状态和结束状态往中间遍历，相遇后即解锁。
     * 和普通的 BFS 相比，不用保存所有状态，大大减少了空间和时间需求。
     *
     * 假设图的分支因子为 b，起始点到终点的最短距离为 d，则普通的 BFS 时间复杂度
     * 为 O(b**d)，而双向 BFS 的时间复杂度为 O(b**(d/2) + b**(d/2))。
     *
     * 详细说明参见：
     * https://www.geeksforgeeks.org/bidirectional-search/
     *
     * 又通过将字符串转为数字，避免了耗时的字符串处理。
     *
     * LeetCode 耗时：17ms - 98.48%
     */
    public int bidirectionalBFS(String[] deadends, String target) {
        Set<Integer> visited = new HashSet<>();
        for (String deadend : deadends) {
            visited.add(Integer.parseInt(deadend));
        }
        Set<Integer> begin = new HashSet<>();
        Set<Integer> end = new HashSet<>();
        begin.add(0);
        end.add(Integer.parseInt(target));

        Set<Integer> temp;
        int level = 0;
        while (!begin.isEmpty() && !end.isEmpty()) {
            // 始终使用包含较小状态数的进行遍历，进一步减少时间消耗
            if (begin.size() > end.size()) {
                temp = begin;
                begin = end;
                end = temp;
            }
            temp = new HashSet<>();
            for (int code : begin) {
                if (end.contains(code))
                    return level;
                if (visited.contains(code))
                    continue;
                // 防止已经遍历过的状态再次被遍历
                visited.add(code);
                // 添加下一轮状态
                for (int i = 0; i < 4; i++) {
                    // 添加加 1 的轮盘序列
                    int d = (code / bases[i]) % 10;
                    int nextCode = d == 9 ? code - 9 * bases[i] : code + bases[i];
                    if (!visited.contains(nextCode))
                        temp.add(nextCode);
                    // 添加减 1 的轮盘序列
                    nextCode = d == 0 ? code + 9 * bases[i] : code - bases[i];
                    if (!visited.contains(nextCode))
                        temp.add(nextCode);
                }
            }
            level++;
            begin = temp;
        }

        return -1;
    }

    @Test
    public void testBidirectionalBFS() {
        test(this::bidirectionalBFS);
    }
}
