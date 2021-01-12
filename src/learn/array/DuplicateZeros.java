package learn.array;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个整数数组，重复其中的每个 0，将 0 之后的元素往右移。
 * 请注意，超出原始数组长度的元素将被舍弃。
 * 对输入数组执行上述修改，不要从函数中返回任何内容。
 * <p>
 * 例子：
 * Input: [1,0,2,3,0,4,5,0]
 * Output: null
 * Explanation: 在函数调用完后，输入数组被修改为：[1,0,0,2,3,0,0,4]
 * <p>
 * 约束：
 * - 1 <= arr.length <= 10000
 * - 0 <= arr[i] <= 9
 */
public class DuplicateZeros {

    static void test(Consumer<int[]> func) {
        int[] inp = {1, 0, 2, 3, 0, 4, 5, 0};
        func.accept(inp);
        assertArrayEquals(inp, new int[]{1, 0, 0, 2, 3, 0, 0, 4});

        int[] inp2 = {1, 2, 3};
        func.accept(inp2);
        assertArrayEquals(inp2, new int[]{1, 2, 3});
    }

    /**
     * 存在空间复杂度为 O(1) 的方法
     */
    public void duplicateZeros(int[] arr) {
        int len = arr.length;
        int[] old = Arrays.copyOf(arr, len);

        for (int op = 0, np = 0; np < len; op++, np++) {
            arr[np] = old[op];
            if (old[op] == 0) {
                np++;
                if (np == len)
                    break;
                arr[np] = 0;
            }
        }
    }

    @Test
    public void testDuplicateZeros() {
        test(this::duplicateZeros);
    }



    /**
     * 从右往左遍历数组、从左往右遍历数组
     */
    public void o1space(int[] arr) {
        // 计算 0 的数量
        int cntZero = 0;
        for (int n : arr) {
            if (n == 0)
                cntZero++;
        }
        int len = arr.length, newLen = len + cntZero;

        // 使用 i 表示原数组的指针，j 表示复制了 0 的数组的指针。它们操作的都是同一个数组。
        // 从右往左
        for (int i = len - 1, j = newLen - 1; i < j; i--, j--) {
            if (arr[i] != 0) {
                if (j < len)
                    arr[j] = arr[i];
            } else {
                // 复制两次 0
                if (j < len)
                    arr[j] = 0;
                j--;
                if (j < len)
                    arr[j] = 0;
            }
        }
    }

    @Test
    public void testO1space() {
        test(this::o1space);
    }
}
