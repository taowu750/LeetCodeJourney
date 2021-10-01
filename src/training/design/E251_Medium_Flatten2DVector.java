package training.design;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 251. 展开二维向量: https://leetcode-cn.com/problems/flatten-2d-vector/
 *
 * 请设计并实现一个能够展开二维向量的迭代器。该迭代器需要支持 next 和 hasNext 两种操作。
 *
 * 例 1：
 * IVector iterator = new IVector([[1,2],[3],[4]]);
 * iterator.next(); // 返回 1
 * iterator.next(); // 返回 2
 * iterator.next(); // 返回 3
 * iterator.hasNext(); // 返回 true
 * iterator.hasNext(); // 返回 true
 * iterator.next(); // 返回 4
 * iterator.hasNext(); // 返回 false
 *
 * 约束：
 * - 请记得「重置」在 IVector 中声明的类变量（静态变量），因为类变量会在多个测试用例中保持不变，影响判题准确。
 * - 你可以假定 next() 的调用总是合法的，即当 next() 被调用时，二维向量总是存在至少一个后续元素。
 */
public class E251_Medium_Flatten2DVector {

    public static void test(Function<int[][], IVector> factory) {
        IVector iterator = factory.apply(new int[][]{{1,2}, {3}, {4}});
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = factory.apply(new int[][]{{}});
        assertFalse(iterator.hasNext());

        iterator = factory.apply(new int[][]{});
        assertFalse(iterator.hasNext());

        iterator = factory.apply(new int[][]{{1}, {}});
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = factory.apply(new int[][]{{}, {}, {}, {}});
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testVector2DImpl() {
        test(Vector2D::new);
    }
}

interface IVector {

    int next();

    boolean hasNext();
}

/**
 * 考察细心。
 *
 * LeetCode 耗时：10 ms - 93.05%
 *          内存消耗：43.1 MB - 11.76%
 */
class Vector2D implements IVector {

    private int[][] vec;
    private int i, j;

    public Vector2D(int[][] vec) {
        this.vec = vec;
    }

    @Override
    public int next() {
        while (i < vec.length && j == vec[i].length) {
            i++;
            j = 0;
        }
        int result = vec[i][j++];
        while (i < vec.length && j == vec[i].length) {
            i++;
            j = 0;
        }

        return result;
    }

    @Override
    public boolean hasNext() {
        while (i < vec.length && j == vec[i].length) {
            i++;
            j = 0;
        }

        return i != vec.length;
    }
}