package training.binarytree;

import javafx.util.Pair;
import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;
import training.dynamicprogramming.E213_Medium_HouseRobberII;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为“根”。
 * 除了“根”之外，每栋房子有且只有一个“父“房子与之相连。一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
 * 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
 *
 * 计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
 *
 * 此题类似于 {@link E213_Medium_HouseRobberII}。
 *
 * 例 1：
 * 输入: [3,2,3,null,3,null,1]
 *
 *      3
 *     / \
 *    2   3
 *     \   \
 *      3   1
 *
 * 输出: 7
 * 解释: 小偷一晚能够盗取的最高金额 = 3 + 3 + 1 = 7.
 *
 * 例 2：
 * 输入: [3,4,5,1,3,null,1]
 *
 *      3
 *     / \
 *    4   5
 *   / \   \
 *  1   3   1
 *
 * 输出: 9
 * 解释: 小偷一晚能够盗取的最高金额 = 4 + 5 = 9.
 */
public class E337_Medium_HouseRobberIII {

    static void test(ToIntFunction<TreeNode> method) {
        assertEquals(method.applyAsInt(newTree(3,2,3,null,3,null,1)), 7);
        assertEquals(method.applyAsInt(newTree(3,4,5,1,3,null,1)), 9);
        assertEquals(method.applyAsInt(newTree(2,1,3,null,4)), 7);
        assertEquals(method.applyAsInt(newTree(5,3,6,1,4,null,null,null,2)), 12);
    }

    /**
     * 注意，此题和 {@link E213_Medium_HouseRobberII} 不同的是，并不是隔两个不打劫就亏了，
     * 看最后一个测试用例。
     *
     * LeetCode 耗时：6 ms - 26.94%
     *          内存消耗：39.6 MB - 5.08%
     */
    public int rob(TreeNode root) {
        // 注意加上记忆集，否则会超时。
        return rob(root, false, new HashMap<>());
    }

    // robbed 是之前有没有打劫。返回最高金额
    private int rob(TreeNode root, boolean robbed, Map<Pair<TreeNode, Boolean>, Integer> memory) {
        if (root == null)
            return 0;
        Pair<TreeNode, Boolean> cur = new Pair<>(root, robbed);
        int result = memory.getOrDefault(cur, -1);
        if (result != -1)
            return result;

        // 如果之前已经打劫了，这次就不能打劫了
        if (robbed)
            result = rob(root.left, false, memory) + rob(root.right, false, memory);
        // 如果没有打劫，那么从“这次打劫”和“这次也不打劫”中选最大的
        else
            result = Math.max(root.val + rob(root.left, true, memory) + rob(root.right, true, memory),
                    rob(root.left, false, memory) + rob(root.right, false, memory));
        memory.put(cur, result);

        return result;
    }

    @Test
    public void testRob() {
        test(this::rob);
    }


    /**
     * 在当前节点就把事情做了，这样就不需要依赖之前的状态。
     *
     * LeetCode 耗时：3 ms - 57.98%
     *          内存消耗：38.2 MB - 40.25%
     */
    public int noRobbedParam(TreeNode root) {
        return noRobbedParam(root, new HashMap<>());
    }

    public int noRobbedParam(TreeNode root, Map<TreeNode, Integer> memory) {
        if (root == null)
            return 0;
        int res = memory.getOrDefault(root, -1);
        if (res != -1)
            return res;

        // 抢，然后去下下家
        int rob = root.val +
                (root.left != null ? noRobbedParam(root.left.left, memory) + noRobbedParam(root.left.right, memory) : 0) +
                (root.right != null ? noRobbedParam(root.right.left, memory) + noRobbedParam(root.right.right, memory) : 0);
        // 不抢，然后去下家
        int noRob = noRobbedParam(root.left, memory) + noRobbedParam(root.right, memory);
        res = Math.max(rob, noRob);
        memory.put(root, res);

        return res;
    }

    @Test
    public void testNoRobbedParam() {
        test(this::noRobbedParam);
    }


    /**
     * 更好的方法，将选择的结果放在返回值中，这样就无需记忆集。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.1 MB - 61.41%
     */
    public int betterMethod(TreeNode root) {
        int[] result = dp(root);
        return Math.max(result[0], result[1]);
    }

    /*
    返回一个大小为 2 的数组 arr
    arr[0] 表示不抢当前 root 的话，得到的最大钱数
    arr[1] 表示抢当前 root 的话，得到的最大钱数
     */
    int[] dp(TreeNode root) {
        if (root == null)
            return new int[]{0, 0};

        int[] left = dp(root.left);
        int[] right = dp(root.right);

        // 抢，下家就不能抢了
        int rob = root.val + left[0] + right[0];
        // 不抢，可以选择是否抢下家
        int noRob = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);

        return new int[]{noRob, rob};
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
