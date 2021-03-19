package training.binarytree;

import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定二叉树的根，按任意顺序返回所有根到叶的路径。叶子是没有子节点的节点。
 *
 * 例 1：
 * Input: root = [1,2,3,null,5]
 * Output: ["1->2->5","1->3"]
 * Explanation:
 *      1
 *    /  \
 *   2   3
 *    \
 *     5
 *
 * 例 2：
 * Input: root = [1]
 * Output: ["1"]
 *
 * 约束：
 * - 树中结点数量范围为 [1, 100]
 * - -100 <= Node.val <= 100
 */
public class E257_Easy_BinaryTreePaths {

    static void test(Function<TreeNode, List<String>> method) {
        assertEquals(method.apply(newTree(1,2,3,null,5)), Arrays.asList("1->2->5", "1->3"));

        assertEquals(method.apply(new TreeNode(1)), singletonList("1"));
    }

    /**
     * LeetCode 耗时：1ms - 99.81%
     *          内存消耗：38.7MB - 98.07%
     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        recur(root, null, result);
        return result;
    }

    public void recur(TreeNode root, StringBuilder par, List<String> result) {
        if (root == null)
            return;
        StringBuilder sb;
        if (par == null)
            sb = new StringBuilder(6);
        else
            sb = new StringBuilder(par.length() + 6).append(par);
        sb.append(root.val);
        if (root.left == null && root.right == null)
            result.add(sb.toString());
        else {
            sb.append("->");
            recur(root.left, sb, result);
            recur(root.right, sb, result);
        }
    }

    @Test
    public void testBinaryTreePaths() {
        test(this::binaryTreePaths);
    }
}
