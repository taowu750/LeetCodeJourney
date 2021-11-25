package training.sort;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 406. 根据身高重建队列: https://leetcode-cn.com/problems/queue-reconstruction-by-height/
 *
 * 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
 * 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面 正好 有 ki 个身高大于或等于 hi 的人。
 *
 * 请你重新构造并返回输入数组 people 所表示的队列。返回的队列应该格式化为数组 queue ，
 * 其中 queue[j] = [hj, kj] 是队列中第 j 个人的属性（queue[0] 是排在队列前面的人）。
 *
 * 例 1：
 * 输入：people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
 * 输出：[[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
 * 解释：
 * 编号为 0 的人身高为 5 ，没有身高更高或者相同的人排在他前面。
 * 编号为 1 的人身高为 7 ，没有身高更高或者相同的人排在他前面。
 * 编号为 2 的人身高为 5 ，有 2 个身高更高或者相同的人排在他前面，即编号为 0 和 1 的人。
 * 编号为 3 的人身高为 6 ，有 1 个身高更高或者相同的人排在他前面，即编号为 1 的人。
 * 编号为 4 的人身高为 4 ，有 4 个身高更高或者相同的人排在他前面，即编号为 0、1、2、3 的人。
 * 编号为 5 的人身高为 7 ，有 1 个身高更高或者相同的人排在他前面，即编号为 1 的人。
 * 因此 [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]] 是重新构造后的队列。
 *
 * 例 2：
 * 输入：people = [[6,0],[5,0],[4,0],[3,2],[2,2],[1,4]]
 * 输出：[[4,0],[5,0],[2,2],[3,2],[1,4],[6,0]]
 *
 * 约束：
 * - 1 <= people.length <= 2000
 * - 0 <= hi <= 10^6
 * - 0 <= ki < people.length
 * - 题目数据确保队列可以被重建。
 */
public class E406_Medium_QueueReconstructionByHeight {

    public static void test(UnaryOperator<int[][]> method) {
        assertArrayEquals(new int[][]{{5, 0}, {7, 0}, {5, 2}, {6, 1}, {4, 4}, {7, 1}},
                method.apply(new int[][]{{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}}));

        assertArrayEquals(new int[][]{{4, 0}, {5, 0}, {2, 2}, {3, 2}, {1, 4}, {6, 0}},
                method.apply(new int[][]{{6, 0}, {5, 0}, {4, 0}, {3, 2}, {2, 2}, {1, 4}}));
    }

    /**
     * LeetCode 耗时：14ms - 25%
     *          内存消耗：39.5MB - 30%
     */
    public int[][] reconstructQueue(int[][] people) {
        // 按照高度升序、k 升序进行排序
        Arrays.sort(people, (a, b) -> {
            int cmp = Integer.compare(a[0], b[0]);
            return cmp != 0 ? cmp : Integer.compare(a[1], b[1]);
        });

        int[][] result = new int[people.length][];
        int[] last = people[0];
        int lastIdx = last[1];
        /*
        排序后，从最矮到最高遍历。
        每个身高等于 p 且排在 p 前面的人，在 result 中也应该在 p 的前面。
        设 p 前面一个人是 o
        - 如果 p.h == o.h，p 一定在 o 后面，且 p、o 之间的空位数量就是 p.k - o.k - 1
        - 否则 p.h > o.h，p 需要在前面留下 p.k 个空位
        */
        // 先放置第一个人
        result[last[1]] = last;
        for (int i = 1; i < people.length; i++) {
            int[] p = people[i];
            int j;
            if (p[0] == last[0]) {
                int empty = p[1] - last[1];
                // 跳过 p.k - o.k - 1 个空位
                for (j = lastIdx + 1;; j++) {
                    if (result[j] == null) {
                        empty--;
                        if (empty == 0) {
                            break;
                        }
                    }
                }
            } else {
                int empty = p[1] + 1;
                // 跳过 p.k 个空位
                for (j = 0;; j++) {
                    if (result[j] == null) {
                        empty--;
                        if (empty == 0) {
                            break;
                        }
                    }
                }
            }
            // 跳过已放置的人
            while (result[j] != null) {
                j++;
            }
            result[j] = p;
            lastIdx = j;
            last = p;
        }

        return result;
    }

    @Test
    public void testReconstructQueue() {
        test(this::reconstructQueue);
    }


    /**
     * 逆向排序。
     *
     * LeetCode 耗时：7 ms - 84.78%
     *          内存消耗：39.3 MB - 67.52%
     */
    public int[][] reverseSortMethod(int[][] people) {
        // 按照高度降序、k 升序进行排序
        Arrays.sort(people, (a, b) -> {
            int cmp = -Integer.compare(a[0], b[0]);
            return cmp != 0 ? cmp : Integer.compare(a[1], b[1]);
        });

        /*
        当我们放入第 i 个人时：
        - 第 0,⋯,i−1 个人已经在队列中被安排了位置，他们只要站在第 i 个人的前面，就会对第 i 个人产生影响，
          因为他们都比第 i 个人高；
        - 而第 i+1,⋯,n−1 个人还没有被放入队列中，并且他们无论站在哪里，对第 i 个人都没有任何影响，
          因为他们都比第 i 个人矮。

        后面的人既然不会对第 i 个人造成影响，我们可以采用「插空」的方法，依次给每一个人在当前的队列中选择一个插入的位置。
        也就是说，当我们放入第 i 个人时，只需要将其插入队列中，使得他的前面恰好有 ki 个人即可。
         */
        List<int[]> ans = new ArrayList<>(people.length);
        for (int[] person : people) {
            ans.add(person[1], person);
        }

        return ans.toArray(new int[ans.size()][]);
    }

    @Test
    public void testReverseSortMethod() {
        test(this::reverseSortMethod);
    }
}
