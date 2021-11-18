package training.uf;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 547. 省份数量: https://leetcode-cn.com/problems/number-of-provinces/
 *
 * 有 n 个城市，其中一些彼此相连，另一些没有相连。如果城市 a 与城市 b 直接相连，且城市 b 与城市 c 直接相连，
 * 那么城市 a 与城市 c 间接相连。
 *
 * 「省份」是一组直接或间接相连的城市，组内不含其他没有相连的城市。
 *
 * 给你一个 n x n 的矩阵 isConnected ，其中 isConnected[i][j] = 1 表示第 i 个城市和第 j 个城市直接相连，
 * 而 isConnected[i][j] = 0 表示二者不直接相连。
 *
 * 返回矩阵中「省份」的数量。
 *
 * 例 1：
 * 输入：isConnected = [[1,1,0],[1,1,0],[0,0,1]]
 * 输出：2
 *
 * 例 2：
 * 输入：isConnected = [[1,0,0],[0,1,0],[0,0,1]]
 * 输出：3
 *
 * 说明：
 * - 1 <= n <= 200
 * - n == isConnected.length
 * - n == isConnected[i].length
 * - isConnected[i][j] 为 1 或 0
 * - isConnected[i][i] == 1
 * - isConnected[i][j] == isConnected[j][i]
 */
public class E547_Medium_NumberOfProvinces {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(2, method.applyAsInt(new int[][]{{1,1,0}, {1,1,0}, {0,0,1}}));
        assertEquals(3, method.applyAsInt(new int[][]{{1,0,0}, {0,1,0}, {0,0,1}}));
    }

    public static class UF {
        private int[] parent;
        private int count;

        public UF(int count) {
            parent = new int[count];
            for (int i = 0; i < parent.length; i++) {
                parent[i] = i;
            }
            this.count = count;
        }

        public void union(int p, int q) {
            int pRoot = find(p), qRoot = find(q);
            if (pRoot == qRoot) {
                return;
            }

            parent[pRoot] = qRoot;
            count--;
        }

        public int count() {
            return count;
        }

        private int find(int p) {
            if (p != parent[p]) {
                parent[p] = find(parent[p]);
            }

            return parent[p];
        }
    }

    /**
     * LeetCode 耗时：1 ms - 95.93%
     *          内存消耗 ：39.4 MB - 33.51%
     */
    public int findCircleNum(int[][] isConnected) {
        UF uf = new UF(isConnected.length);
        for (int i = 0; i < isConnected.length - 1; i++) {
            for (int j = i + 1; j < isConnected.length; j++) {
                if (isConnected[i][j] == 1) {
                    uf.union(i, j);
                }
            }
        }

        return uf.count();
    }

    @Test
    public void test() {
        test(this::findCircleNum);
    }
}
