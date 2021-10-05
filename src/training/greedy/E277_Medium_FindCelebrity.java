package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 277. 搜寻名人: https://leetcode-cn.com/problems/find-the-celebrity/
 *
 * 假设你是一个专业的狗仔，参加了一个 n 人派对，其中每个人被从 0 到 n - 1 标号。
 * 在这个派对人群当中可能存在一位 “名人”。所谓 “名人” 的定义是：其他所有 n - 1 个人都认识他/她，
 * 而他/她并不认识其他任何人。
 *
 * 现在你想要确认这个 “名人” 是谁，或者确定这里没有 “名人”。而你唯一能做的就是问诸如 “A 你好呀，
 * 请问你认不认识 B呀？”的问题，以确定 A 是否认识 B。你需要在（渐近意义上）尽可能少的问题内来确定这位 “名人” 是谁
 * （或者确定这里没有 “名人”）。
 *
 * 在本题中，你可以使用辅助函数 bool knows(a, b) 获取到 A 是否认识 B。请你来实现一个函数 int findCelebrity(n)。
 *
 * 派对最多只会有一个 “名人” 参加。若 “名人” 存在，请返回他/她的编号；若 “名人” 不存在，请返回 -1。
 *
 * 例 1：
 * 输入: graph = [
 *  [1,1,0],
 *  [0,1,0],
 *  [1,1,1]
 * ]
 * 输出: 1
 * 解释: 有编号分别为 0、1 和 2 的三个人。graph[i][j] = 1 代表编号为 i 的人认识编号为 j 的人，而 graph[i][j] = 0 则代表编号为 i 的人不认识编号为 j 的人。“名人” 是编号 1 的人，因为 0 和 2 均认识他/她，但 1 不认识任何人。
 *
 * 例 2：
 * 输入: graph = [
 *   [1,0,1],
 *   [1,1,0],
 *   [0,1,1]
 * ]
 * 输出: -1
 * 解释: 没有 “名人”
 *
 * 约束：
 * - n == graph.length
 * - n == graph[i].length
 * - 2 <= n <= 100
 * - graph[i][j] 是 0 或 1.
 * - graph[i][i] == 1
 */
public class E277_Medium_FindCelebrity {

    private static int[][] graph;

    private static boolean knows(int a, int b) {
        return graph[a][b] == 1;
    }

    public static void test(IntUnaryOperator method) {
        graph = new int[][]{
                {1,1,0},
                {0,1,0},
                {1,1,1}};
        assertEquals(1, method.applyAsInt(3));

        graph = new int[][]{
                {1,0,1},
                {1,1,0},
                {0,1,1}};
        assertEquals(-1, method.applyAsInt(3));
    }

    /**
     * 活用任何人都认识名人，但名人不认识任何人这一特性
     * - 如果 knows(i,j) 为 true，说明 i 不可能是名人
     * - 如果 knows(i,j) 为 false， 说明 j 不可能是名人
     * 也就说说任意两人相互比较总能淘汰一个人。
     *
     * n 轮淘汰下来，淘汰 n-1 个，剩下 1 个，而题目给出名人最多 1 个，再检查这个剩下的候选名人即可
     *
     * LeetCode 耗时：19 ms - 95.70%
     *          内存消耗：38.9 MB - 10.75%
     */
    public int findCelebrity(int n) {
        int result = 0;
        for (int i = 1; i < n; i++) {
            if (knows(result, i)) {
                result = i;
            }
        }

        for (int i = 0; i < n; i++) {
            if (i != result) {
                if (!knows(i, result)) {
                    return -1;
                }
                if (knows(result, i)) {
                    return -1;
                }
            }
        }

        return result;
    }

    @Test
    public void testFindCelebrity() {
        test(this::findCelebrity);
    }
}
