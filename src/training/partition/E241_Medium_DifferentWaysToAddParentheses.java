package training.partition;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 241. 为运算表达式设计优先级: https://leetcode-cn.com/problems/different-ways-to-add-parentheses/
 *
 * 给定一个含有数字和运算符的字符串，为表达式添加括号，改变其运算优先级以求出不同的结果。
 * 你需要给出所有可能的组合的结果。有效的运算符号包含 +, -以及 *。
 *
 * 例 1：
 * 输入: "2-1-1"
 * 输出: [0, 2]
 * 解释:
 * ((2-1)-1) = 0
 * (2-(1-1)) = 2
 *
 * 例 2：
 * 输入: "2*3-4*5"
 * 输出: [-34, -14, -10, -10, 10]
 * 解释:
 * (2*(3-(4*5))) = -34
 * ((2*3)-(4*5)) = -14
 * ((2*(3-4))*5) = -10
 * (2*((3-4)*5)) = -10
 * (((2*3)-4)*5) = 10
 */
public class E241_Medium_DifferentWaysToAddParentheses {

    static void test(Function<String, List<Integer>> method) {
        equalsIgnoreOrder(Arrays.asList(0, 2), method.apply("2-1-1"));
        equalsIgnoreOrder(Arrays.asList(-34, -14, -10, -10, 10), method.apply("2*3-4*5"));
    }

    /**
     * 分治算法。解决这个问题之前，我们需要明确三点：
     * 1. 不要思考整体，而是把目光聚焦局部，只看一个运算符。
     *    解决递归相关的算法问题，就是一个化整为零的过程，你必须瞄准一个小的突破口，
     *    然后把问题拆解，大而化小，利用递归函数来解决。
     * 2. 明确递归函数的定义是什么，相信并且利用好函数的定义。
     *    因为递归函数要自己调用自己，你必须搞清楚函数到底能干嘛，才能正确进行递归调用。
     *
     * 参见：https://labuladong.gitee.io/algo/算法思维系列/分治算法/
     *
     * LeetCode 耗时：2 ms - 76.48%
     *          内存消耗：37.3 MB - 79.47%
     */
    public List<Integer> diffWaysToCompute(String expression) {
        int n = expression.length();
        List<Integer> nums = new ArrayList<>(n >>> 1 + 1);
        List<Character> operators = new ArrayList<>(n >>> 1);

        int num = 0;
        for (int i = 0; i < n; i++) {
            char c = expression.charAt(i);
            if (c >= '0' && c <= '9') {
                num = num * 10 + c - '0';
            } else {
                nums.add(num);
                num = 0;
                operators.add(c);
            }
        }
        nums.add(num);

        return partition(nums, operators, 0, nums.size() - 1);
    }

    /**
     * 把递归算法看作是分层的过程，每一层做一个局部的事情，「嵌套」交给递归来解决。
     *
     * 例如：1 + 2 * 3 - 4 * 5
     *
     * 因为括号可以嵌套，要穷举出来肯定得费点功夫。不过呢，嵌套这个事情吧，我们人类来看是很头疼的，
     * 但对于算法来说嵌套括号不要太简单，一次递归就可以嵌套一层，一次搞不定大不了多递归几次。
     * 作为写算法的人类，我们只需要思考，如果「不让括号嵌套」（即只加一层括号），有几种加括号的方式？
     *
     * 还是上面的例子，显然我们有四种加括号方式：
     * (1) + (2 * 3 - 4 * 5)
     * (1 + 2) * (3 - 4 * 5)
     * (1 + 2 * 3) - (4 * 5)
     * (1 + 2 * 3 - 4) * (5)
     *
     * 发现规律了么？其实就是按照运算符进行分割，给每个运算符的左右两部分加括号，
     * 这就是之前说的第一个关键点，不要考虑整体，而是聚焦每个运算符。
     *
     * 分治分治，分而治之，这一步就是把原问题进行了「分」，我们现在要开始「治」了。现在单独说上面的第三种情况：
     * (1 + 2 * 3) - (4 * 5)
     *
     * 1 + 2 * 3可以有两种加括号的方式，分别是：
     * (1) + (2 * 3) = 7
     * (1 + 2) * (3) = 9
     * 或者我们可以写成这种形式：
     * 1 + 2 * 3 = [9, 7]
     *
     * 而4 * 5当然只有一种加括号方式，就是4 * 5 = [20]。
     *
     * 显然，可以推导出来(1 + 2 * 3) - (4 * 5)有两种结果，分别是：
     * 9 - 20 = -11
     * 7 - 20 = -13
     */
    private List<Integer> partition(List<Integer> nums, List<Character> operators, int lo, int hi) {
        if (lo == hi)
            return Collections.singletonList(nums.get(lo));
        if (lo == hi - 1)
            return Collections.singletonList(calc(nums.get(lo), nums.get(hi), operators.get(lo)));

        int partitionCount = hi - lo;
        List<Integer> result = new ArrayList<>(hi - lo + 1);
        for (int i = 0; i < partitionCount; i++) {
            List<Integer> leftResult = partition(nums, operators, lo, lo + i);
            List<Integer> rightResult = partition(nums, operators, lo + i + 1, hi);
            for (Integer left : leftResult) {
                for (Integer right : rightResult) {
                    result.add(calc(left, right, operators.get(lo + i)));
                }
            }
        }

        return result;
    }

    private int calc(int left, int right, char operator) {
        switch (operator) {
            case '+':
                return left + right;

            case '-':
                return left - right;

            case '*':
                return left * right;
        }
        return 0;
    }

    @Test
    public void testDiffWaysToCompute() {
        test(this::diffWaysToCompute);
    }
}
