package training.math;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定整数 rowIndex，返回帕斯卡三角形（杨辉三角）的第 rowIndex（以 0 开头）行。
 * 你能找到仅使用 O(rowIndex) 额外空间的实现吗？
 *
 * 例 1：
 * Input: rowIndex = 3
 * Output: [1,3,3,1]
 * Explanation:
 *      1
 *     1 1
 *    1 2 1
 * → 1 3 3 1
 *
 * 例 2：
 * Input: rowIndex = 0
 * Output: [1]
 *
 * 例 3：
 * Input: rowIndex = 1
 * Output: [1,1]
 *
 * 约束：
 * - 0 <= rowIndex <= 33
 */
public class E119_Easy_PascalTriangleII {

    static void test(IntFunction<List<Integer>> method) {
        assertEquals(method.apply(3), asList(1, 3, 3, 1));

        assertEquals(method.apply(0), singletonList(1));

        assertEquals(method.apply(1), asList(1, 1));

        assertEquals(method.apply(4), asList(1, 4, 6, 4, 1));
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public List<Integer> getRow(int rowIndex) {
        Integer[] row = new Integer[rowIndex + 1];
        row[0] = 1;
        for (int i = 1; i <= rowIndex; i++) {
            for (int j = i; j >= 0; j--)
                row[j] = (row[j] != null ? row[j] : 0) + (j - 1 >= 0 ? row[j - 1] : 0);
        }

        return Arrays.asList(row);
    }

    @Test
    public void testGetRow() {
        test(this::getRow);
    }
}
