package learn.queue_stack;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个由 0 和 1 组成的矩阵，找到每个单元最接近的 0 的距离，两个相邻单元之间的距离为 1。
 * <p>
 * 例 1：
 * Input:
 * [[0,0,0],
 *  [0,1,0],
 *  [0,0,0]]
 * Output:
 * [[0,0,0],
 *  [0,1,0],
 *  [0,0,0]]
 * <p>
 * 例 2：
 * Input:
 * [[0,0,0],
 *  [0,1,0],
 *  [1,1,1]]
 * Output:
 * [[0,0,0],
 *  [0,1,0],
 *  [1,2,1]]
 * <p>
 * 约束：
 * - 给定矩阵的元素数量将不超过 10000。
 * - 给定矩阵中至少有一个 0。
 * - 单元格仅在四个方向上相邻：上，下，左和右。
 */
public class Matrix01 {

    static void test(Function<int[][], int[][]> method) {
        assertArrayEquals(method.apply(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}),
                new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});

        assertArrayEquals(method.apply(new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 1}}),
                new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 2, 1}});

        assertArrayEquals(method.apply(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 0}}),
                new int[][]{{4, 3, 2}, {3, 2, 1}, {2, 1, 0}});

        assertArrayEquals(method.apply(new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}}),
                new int[][]{{2, 1, 2}, {1, 0, 1}, {2, 1, 2}});

        assertArrayEquals(method.apply(new int[][]{{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 0}}),
                new int[][]{{6, 5, 4, 3}, {5, 4, 3, 2}, {4, 3, 2, 1}, {3, 2, 1, 0}});

        assertArrayEquals(method.apply(new int[][]{{1, 1, 1, 1}, {1, 1, 0, 1}, {0, 1, 1, 0}}),
                new int[][]{{2, 2, 1, 2}, {1, 1, 0, 1}, {0, 1, 1, 0}});

        assertArrayEquals(method.apply(new int[][]{
                        {1, 1, 0, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {0, 1, 1, 1}}),
                new int[][]{
                        {2, 1, 0, 1},
                        {3, 2, 1, 2},
                        {2, 3, 2, 3},
                        {1, 2, 3, 4},
                        {0, 1, 2, 3}});

        assertArrayEquals(method.apply(new int[][]{
                        {1,0,1,1},
                        {1,1,1,1},
                        {0,0,1,1},
                        {1,0,1,1}}),
                new int[][]{
                        {1,0,1,2},
                        {1,1,2,3},
                        {0,0,1,2},
                        {1,0,1,2}});
    }

    private static class Pos implements Comparable<Pos> {
        int r, c, minDis;
        Pos parent;  // BFS 树上的父结点

        public Pos(int r, int c) {
            this.r = r;
            this.c = c;
            minDis = Integer.MAX_VALUE;
        }

        public Pos(int r, int c, Pos parent) {
            this.r = r;
            this.c = c;
            this.parent = parent;
            minDis = Integer.MAX_VALUE;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof Pos))
                return false;

            Pos that = (Pos) obj;
            return r == that.r && c == that.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, c);
        }

        @Override
        public int compareTo(Pos o) {
            int rc = Integer.compare(r, o.r);
            return rc != 0 ? rc : Integer.compare(c, o.c);
        }
    }

    private int[][] matrix;
    private int[][] distance;

    /**
     * LeetCode 耗时：19ms - 33.31%
     */
    public int[][] updateMatrix(int[][] matrix) {
        this.matrix = matrix;
        distance = new int[matrix.length][matrix[0].length];

        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                if (matrix[r][c] != 0 && distance[r][c] == 0)
                    bfs(r, c);
            }
        }

        return distance;
    }

    private void bfs(int r, int c) {
        Queue<Pos> queue = new LinkedList<>();
        Set<Pos> vis = new HashSet<>();
        Pos pos = new Pos(r, c);
        queue.add(pos);
        vis.add(pos);
        while (!queue.isEmpty()) {
            pos = queue.remove();
            r = pos.r;
            c = pos.c;
            Pos newPos;
            // 从下面相邻位置开始，逆时针遍历
            if (r + 1 < matrix.length) {
                // 如果遇到 0
                if (matrix[r + 1][c] == 0) {
                    // 则当前位置的最小距离是 1
                    pos.minDis = 1;
                    break;
                } else if (distance[r + 1][c] != 0) {
                    // 否则如果旁边的位置最小距离 d 已经确定，并且 d + 1 小于
                    // 当前位置的最小距离，则更新当前位置的最小距离
                    if (pos.minDis > distance[r + 1][c] + 1)
                        pos.minDis = distance[r + 1][c] + 1;
                } else if (!vis.contains((newPos = new Pos(r + 1, c, pos)))) {
                    // 否则如果旁边位置未被遍历，则将其加入队列中
                    queue.add(newPos);
                    vis.add(newPos);
                }
            }
            if (c + 1 < matrix[0].length) {
                if (matrix[r][c + 1] == 0) {
                    pos.minDis = 1;
                    break;
                } else if (distance[r][c + 1] != 0) {
                    if (pos.minDis > distance[r][c + 1] + 1)
                        pos.minDis = distance[r][c + 1] + 1;
                } else if (!vis.contains((newPos = new Pos(r, c + 1, pos)))) {
                    queue.add(newPos);
                    vis.add(newPos);
                }
            }
            if (r - 1 >= 0) {
                if (matrix[r - 1][c] == 0) {
                    pos.minDis = 1;
                    break;
                } else if (distance[r - 1][c] != 0) {
                    if (pos.minDis > distance[r - 1][c] + 1)
                        pos.minDis = distance[r - 1][c] + 1;
                } else if (!vis.contains((newPos = new Pos(r - 1, c, pos)))) {
                    queue.add(newPos);
                    vis.add(newPos);
                }
            }
            if (c - 1 >= 0) {
                if (matrix[r][c - 1] == 0) {
                    pos.minDis = 1;
                    break;
                } else if (distance[r][c - 1] != 0) {
                    if (pos.minDis > distance[r][c - 1] + 1)
                        pos.minDis = distance[r][c - 1] + 1;
                } else if (!vis.contains((newPos = new Pos(r, c - 1, pos)))) {
                    queue.add(newPos);
                    vis.add(newPos);
                }
            }
        }
        boolean uncertain = false;
        int dis = pos.minDis;
        Deque<Pos> stack = new LinkedList<>();
        // 从已经确定最小距离的叶子位置开始，从遍历树中一直往上，
        // 确定它的父结点的最小距离
        do {
            // 如果父位置最小距离要小于子位置最小距离+1，
            // 则表示可能从父位置到子位置有更小的距离
            if (pos.minDis < dis) {
                distance[pos.r][pos.c] = pos.minDis;
                dis = pos.minDis;
                uncertain = true;
            } else {
                distance[pos.r][pos.c] = dis;
                pos.minDis = dis;
            }
            dis += 1;
            stack.push(pos);
            pos = pos.parent;
        } while (pos != null);
        // 可能从父位置到子位置更短，因此需要进行检查
        if (uncertain) {
            Pos parent = stack.pop();
            while (!stack.isEmpty()) {
                pos = stack.pop();
                if (pos.minDis > parent.minDis + 1) {
                    pos.minDis = parent.minDis + 1;
                    distance[pos.r][pos.c] = parent.minDis + 1;
                }
                parent = pos;
            }
        }
    }

    @Test
    public void testUpdateMatrix() {
        test(this::updateMatrix);
    }
}
