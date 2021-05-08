package training.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 485. 最大连续 1 的个数: https://leetcode-cn.com/problems/max-consecutive-ones/
 *
 * 给定一个内容只有 0 和 1 两个数字的 int 数组。找到连续的 1 的最大长度。
 * <p>
 * 例子:
 * Input: [1,1,0,1,1,1]
 * Output: 3
 * <p>
 * 注意：
 * - 输入数组长度大于 0 并且不会超过 10000
 */
public class E485_Easy_FindMaxConsecutiveOnes {

    /**
     * 有更短的解法
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        int cnt = 0, maxCount = 0;
        for (int binary : nums) {
            if (binary == 1) {
                cnt++;
            } else {
                if (maxCount < cnt)
                    maxCount = cnt;
                cnt = 0;
            }
        }
        if (maxCount < cnt)
            maxCount = cnt;

        return maxCount;
    }

    @Test
    public void testExampleInput() {
        int[] input = {1, 1, 0, 1, 1, 1};
        Assertions.assertEquals(findMaxConsecutiveOnes(input), 3);

        int[] input2 = {1, 0, 1, 1, 0, 1};
        Assertions.assertEquals(findMaxConsecutiveOnes(input2), 2);
    }



    public int shorterMethod(int[] nums) {
        int count = 0, maxCount = 0;
        for (int binary : nums)
            // 注意，复制运算符优先级低于三目运算符
            maxCount = Math.max(maxCount, count = binary == 0 ? 0 : count + 1);

        return maxCount;
    }
}
