package training.graph;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 773. 滑动谜题: https://leetcode-cn.com/problems/sliding-puzzle/
 *
 * 在一个 2 x 3 的板上（board）有 5 块砖瓦，用数字 1~5 来表示, 以及一块空缺用 0 来表示.
 * 一次移动定义为选择 0 与一个相邻的数字（上下左右）进行交换.
 * 最终当板 board 的结果是 [[1,2,3],[4,5,0]] 谜板被解开。
 *
 * 给出一个谜板的初始状态，返回最少可以通过多少次移动解开谜板，如果不能解开谜板，则返回 -1 。
 *
 * 例 1：
 * 输入：board = [[1,2,3],[4,0,5]]
 * 输出：1
 * 解释：交换 0 和 5 ，1 步完成
 *
 * 例 2：
 * 输入：board = [[1,2,3],[5,4,0]]
 * 输出：-1
 * 解释：没有办法完成谜板
 *
 * 例 3：
 * 输入：board = [[4,1,2],[5,0,3]]
 * 输出：5
 * 解释：
 * 最少完成谜板的最少移动次数是 5 ，
 * 一种移动路径:
 * 尚未移动: [[4,1,2],[5,0,3]]
 * 移动 1 次: [[4,1,2],[0,5,3]]
 * 移动 2 次: [[0,1,2],[4,5,3]]
 * 移动 3 次: [[1,0,2],[4,5,3]]
 * 移动 4 次: [[1,2,0],[4,5,3]]
 * 移动 5 次: [[1,2,3],[4,5,0]]
 *
 * 例 4：
 * 输入：board = [[3,2,4],[1,5,0]]
 * 输出：14
 *
 * 约束：
 * - board 是一个如上所述的 2 x 3 的数组.
 * - board[i][j] 是一个 [0, 1, 2, 3, 4, 5] 的排列.
 */
public class E773_Hard_SlidingPuzzle {

    static void test(ToIntFunction<int[][]> method) {
        assertEquals(1, method.applyAsInt(new int[][]{
                {1,2,3},
                {4,0,5}}));
        assertEquals(-1, method.applyAsInt(new int[][]{
                {1,2,3},
                {5,4,0}}));
        assertEquals(5, method.applyAsInt(new int[][]{
                {4,1,2},
                {5,0,3}}));
        assertEquals(14, method.applyAsInt(new int[][]{
                {3,2,4},
                {1,5,0}}));
    }

    /**
     * LeetCode 耗时：7 ms - 76.15%
     *          内存消耗：37.9 MB - 70.43%
     */
    public int slidingPuzzle(int[][] board) {
        int dst = 123450, m = board.length, n = board[0].length;

        int cur = 0, zeroI = 0, zeroJ = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cur = cur * 10 + board[i][j];
                if (board[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }
        if (cur == dst)
            return 0;

        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int[] base = {100000, 10000, 1000, 100, 10, 1};
        int step = 0;

        Queue<int[]> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(new int[]{cur, zeroI, zeroJ});
        visited.add(cur);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] state = queue.remove();
                for (int[] dir : dirs) {
                    int r = state[1] + dir[0], c = state[2] + dir[1];
                    if (r < 0 || r >= m || c < 0 || c >= n)
                        continue;
                    int idx = r * n + c;
                    int digit = state[0] / base[idx] % 10;
                    int num = state[0] - digit * base[idx] + digit * base[state[1] * n + state[2]];
                    if (num == dst)
                        return step + 1;
                    if (!visited.contains(num)) {
                        queue.add(new int[]{num, r, c});
                        visited.add(num);
                    }
                }
            }
            step++;
        }

        return -1;
    }

    @Test
    public void testSlidingPuzzle() {
        test(this::slidingPuzzle);
    }


    /**
     * 双向 BFS。
     *
     * LeetCode 耗时：11 ms - 44.91%
     *          内存消耗：37.7 MB - 90.24%
     */
    public int bidirectionalMethod(int[][] board) {
        int dst = 123450, m = board.length, n = board[0].length;

        int cur = 0, zeroI = 0, zeroJ = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cur = cur * 10 + board[i][j];
                if (board[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }
        if (cur == dst)
            return 0;

        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int[] base = {100000, 10000, 1000, 100, 10, 1};
        int step = 0;

        Map<Integer, int[]> begin = new HashMap<>();
        Map<Integer, int[]> end = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        begin.put(cur, new int[]{zeroI, zeroJ});
        end.put(dst, new int[]{1, 2});
        visited.add(cur);
        visited.add(dst);

        Map<Integer, int[]> temp;
        while (!begin.isEmpty() && !end.isEmpty()) {
            if (begin.size() > end.size()) {
                temp = begin;
                begin = end;
                end = temp;
            }
            temp = new HashMap<>();
            for (Map.Entry<Integer, int[]> e : begin.entrySet()) {
                int oldNum = e.getKey();
                int[] pos = e.getValue();
                for (int[] dir : dirs) {
                    int r = pos[0] + dir[0], c = pos[1] + dir[1];
                    if (r < 0 || r >= m || c < 0 || c >= n)
                        continue;
                    int idx = r * n + c;
                    int digit = oldNum / base[idx] % 10;
                    int num = oldNum - digit * base[idx] + digit * base[pos[0] * n + pos[1]];
                    if (end.containsKey(num))
                        return step + 1;
                    if (!visited.contains(num)) {
                        temp.put(num, new int[]{r, c});
                        visited.add(num);
                    }
                }
            }
            begin = temp;
            step++;
        }

        return -1;
    }

    @Test
    public void testBidirectionalMethod() {
        test(this::bidirectionalMethod);
    }
}
