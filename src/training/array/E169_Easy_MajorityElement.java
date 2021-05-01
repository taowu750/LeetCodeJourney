package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 169. 多数元素: https://leetcode-cn.com/problems/majority-element/
 *
 * 给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数「大于」 ⌊n/2⌋ 的元素。
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 *
 * 注意：⌊⌋ 是向下取整 floor 的符号。
 *
 * 尝试设计时间复杂度为 O(n)、空间复杂度为 O(1) 的算法解决此问题。
 *
 * 例 1：
 * 输入：[3,2,3]
 * 输出：3
 *
 * 例 2：
 * 输入：[2,2,1,1,1,2,2]
 * 输出：2
 */
public class E169_Easy_MajorityElement {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(3, method.applyAsInt(new int[]{3,2,3}));
        assertEquals(2, method.applyAsInt(new int[]{2,2,1,1,1,2,2}));
    }

    /**
     * 摩尔投票法。
     *
     * 候选人(cand_num)初始化为nums[0]，票数count初始化为1。
     * 当遇到与cand_num相同的数，则票数count = count + 1，否则票数count = count - 1。
     * 当票数count为0时，更换候选人，并将票数count重置为1。
     * 遍历完数组后，cand_num即为最终答案。
     *
     * 为何这行得通呢？
     * 投票法是遇到相同的则票数 + 1，遇到不同的则票数 - 1。
     * 且“多数元素”的个数> ⌊ n/2 ⌋，其余元素的个数总和<= ⌊ n/2 ⌋。
     * 因此“多数元素”的个数 - 其余元素的个数总和 的结果 肯定 >= 1。
     * 这就相当于每个“多数元素”和其他元素 两两相互抵消，抵消到最后肯定还剩余至少1个“多数元素”。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：41.8 MB - 51.93%
     */
    public int majorityElement(int[] nums) {
        int candidateNum = nums[0], candidateCount = 1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == candidateNum)
                candidateCount++;
            else {
                candidateCount--;
                if (candidateCount == 0) {
                    candidateNum = nums[i];
                    candidateCount = 1;
                }
            }
        }

        return candidateNum;
    }

    @Test
    public void testMajorityElement() {
        test(this::majorityElement);
    }
}
