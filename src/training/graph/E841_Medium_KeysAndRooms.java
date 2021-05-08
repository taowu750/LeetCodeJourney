package training.graph;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 有 N 个房间，您从房间 0 开始。每个房间在 0、1、2，...，N-1 中都有不同的编号，
 * 每个房间可能都有一些用于访问下一房间的钥匙。
 * <p>
 * 正式地，每个房间 i 都有一个钥匙列表 rooms[i]，每个钥匙 rooms[i][j]
 * 是 [0，1，...，N-1] 中的整数，其中 N = rooms.length。
 * 钥匙 rooms[i][j] = v 打开编号为 v 的房间。
 * <p>
 * 最初，所有房间都锁定（房间 0 除外）。您可以在房间之间自由地来回走动。
 * 当且仅当您可以进入每个房间时才返回 true。
 * <p>
 * 例 1：
 * Input: [[1],[2],[3],[]]
 * Output: true
 * <p>
 * 例 2：
 * Input: [[1,3],[3,0,1],[2],[0]]
 * Output: false
 * Explanation: 不能打开 2 号房间
 * <p>
 * 约束：
 * - 1 <= rooms.length <= 1000
 * - 0 <= rooms[i].length <= 1000
 * - 所有房间的总钥匙数最多为 3000
 */
public class E841_Medium_KeysAndRooms {

    static void test(Predicate<List<List<Integer>>> method) {
        assertTrue(method.test(asList(singletonList(1), singletonList(2),
                singletonList(3), emptyList())));

        assertFalse(method.test(asList(asList(1, 3), asList(3, 0, 1), singletonList(2),
                singletonList(0))));
    }

    /**
     * BFS 方法。LeetCode 上 DFS 快一点。
     *
     * LeetCode 耗时：1ms - 85.85%
     */
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int size = rooms.size();
        boolean[] visited = new boolean[size];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0] = true;
        int count = 1;
        while (!queue.isEmpty()) {
            int room = queue.remove();
            for (Integer key : rooms.get(room)) {
                if (!visited[key]) {
                    queue.add(key);
                    visited[key] = true;
                    count++;
                }
            }
        }

        return count == size;
    }

    @Test
    public void testCanVisitAllRooms() {
        test(this::canVisitAllRooms);
    }
}
