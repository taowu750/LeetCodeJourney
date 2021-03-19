package company.tencent;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 在一组数的编码中，若任意两个相邻的代码只有一位二进制数不同，则称这种编码为格雷码(Gray Code)，
 * 请编写一个函数，用递归方法生成 N 位的格雷码。
 *
 * 给定一个整数 n，请返回 n 位的格雷码，顺序为从 0 开始。
 *
 * 例 1：
 * 输入：1
 * 输出：["0","1"]
 */
public class QQ1_Easy_GenerateGrayCode {

    static void test(IntFunction<String[]> method) {
        assertArrayEquals(method.apply(1), new String[]{"0", "1"});

        assertArrayEquals(method.apply(2), new String[]{"00", "01", "11", "10"});

        assertArrayEquals(method.apply(3), new String[]{"000","001","011","010","110","111","101","100"});
    }

    public String[] getGray(int n) {
        if (n == 0)
            return new String[0];

        List<String> result = new ArrayList<>((int) Math.pow(2, n));
        result.add("0");
        result.add("1");
        recur(n, result);
        return result.toArray(new String[0]);
    }

    private void recur(int n, List<String> result) {
        if (n == 1)
            return;
        int size = result.size();
        // 格雷码就是在上一次序列的前面加 0，然后在上一次序列的逆序序列前面加 1，
        // 这样就形成了下一次序列
        for (int i = size - 1; i >= 0; i--)
            result.add("1" + result.get(i));
        for (int i = 0; i < size; i++)
            result.set(i, "0" + result.get(i));
        recur(--n, result);
    }

    @Test
    public void testGetGray() {
        test(this::getGray);
    }
}
