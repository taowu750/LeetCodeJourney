package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个数组 arr，将该数组中的每个元素替换为其右侧元素中最大的元素，并将最后一个元素替换为 -1。
 * <p>
 * 例 1：
 * Input: arr = [17,18,5,4,6,1]
 * Output: [18,6,6,6,1,-1]
 * <p>
 * 例 2：
 * Input: arr = [400]
 * Output: [-1]
 * <p>
 * 约束：
 * - 1 <= arr.length <= 10**4
 * - 1 <= arr[i] <= 10**5
 */
public class E1299_Easy_ReplaceElementsWithGreatestElementOnRightSide {

    static void test(Function<int[], int[]> method) {
        int[] arr = {17, 18, 5, 4, 6, 1};
        assertArrayEquals(method.apply(arr), new int[]{18, 6, 6, 6, 1, -1});

        arr = new int[]{400};
        assertArrayEquals(method.apply(arr), new int[]{-1});
    }

    /**
     * 有更加高效的做法
     */
    public int[] replaceElements(int[] arr) {
        int len = arr.length;
        int max = -1, maxIdx = -1;
        for (int i = 0; i < len - 1; i++) {
            if (i >= maxIdx) {
                max = arr[i + 1];
                for (int j = i + 2; j < len; j++) {
                    if (max < arr[j]) {
                        max = arr[j];
                        maxIdx = j;
                    }
                }
                arr[i] = max;
            }
            arr[i] = max;
        }
        arr[len - 1] = -1;

        return arr;
    }

    @Test
    public void testReplaceElements() {
        test(this::replaceElements);
    }


    /**
     * 从右到左扫描数组
     */
    public int[] fasterMethod(int[] arr) {
        int len = arr.length;
        int max = arr[len - 1];

        for (int i = len - 2; i >= 0; i--) {
            int tmp = arr[i];
            arr[i] = max;
            if (tmp > max)
                max = tmp;
        }
        arr[len - 1] = -1;

        return arr;
    }

    @Test
    public void testFasterMethod() {
        test(this::fasterMethod);
    }


    /**
     * 与{@link #fasterMethod(int[])}相比更加简洁。
     */
    public int[] conciseMethod(int[] arr) {
        for (int i = arr.length - 1, max = -1; i >= 0; i--)
            max = Math.max(arr[i], arr[i] = max);
        return arr;
    }

    @Test
    public void testConciseMethod() {
        test(this::conciseMethod);
    }
}
