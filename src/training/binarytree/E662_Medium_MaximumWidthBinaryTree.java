package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 662. 二叉树最大宽度: https://leetcode-cn.com/problems/maximum-width-of-binary-tree/
 *
 * 给定一个二叉树，编写一个函数来获取这个树的最大宽度。树的宽度是所有层中的最大宽度。
 * 这个二叉树与满二叉树（full binary tree）结构相同，但一些节点为空。
 *
 * 每一层的宽度被定义为两个端点（该层最左和最右的非空节点，两端点间的null节点也计入长度）之间的长度。
 *
 * 例 1：
 * 输入:
 *
 *            1
 *          /   \
 *         3     2
 *        / \     \
 *       5   3     9
 *
 * 输出: 4
 * 解释: 最大值出现在树的第 3 层，宽度为 4 (5,3,null,9)。
 *
 * 例 2：
 * 输入:
 *
 *           1
 *          /
 *         3
 *        / \
 *       5   3
 *
 * 输出: 2
 * 解释: 最大值出现在树的第 3 层，宽度为 2 (5,3)。
 *
 * 例 3：
 * 输入:
 *
 *           1
 *          / \
 *         3   2
 *        /
 *       5
 *
 * 输出: 2
 * 解释: 最大值出现在树的第 2 层，宽度为 2 (3,2)。
 *
 * 例 4：
 * 输入:
 *
 *           1
 *          / \
 *         3   2
 *        /     \
 *       5       9
 *      /         \
 *     6           7
 * 输出: 8
 * 解释: 最大值出现在树的第 4 层，宽度为 8 (6,null,null,null,null,null,null,7)。
 *
 * 说明：
 * - 注意: 答案在32位有符号整数的表示范围内。
 */
public class E662_Medium_MaximumWidthBinaryTree {

    public static void test(ToIntFunction<TreeNode> method) {
        assertEquals(4, method.applyAsInt(newTree(1,3,2,5,3,null,9)));
        assertEquals(2, method.applyAsInt(newTree(1,3,null,5,3)));
        assertEquals(2, method.applyAsInt(newTree(1,3,2,5)));
        assertEquals(8, method.applyAsInt(newTree(1,3,2,5,null,null,9,6,null,null,7)));
    }

    static class Tuple {
        TreeNode node;
        int serial;

        public Tuple(TreeNode node, int serial) {
            this.node = node;
            this.serial = serial;
        }
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.2 MB - 52.24%
     */
    public int widthOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<Tuple> queue = new LinkedList<>();
        // 给每个节点编序号，根据完全二叉树的性质，父节点的序号为 i，则左子节点序号为 i * 2，右子节点序号为 i * 2 + 1
        queue.add(new Tuple(root, 1));
        int result = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            int minSerial = 0, maxSerial = 0;
            for (int i = 0; i < size; i++) {
                Tuple child = queue.remove();
                if (i == 0) {
                    minSerial = child.serial;
                }
                if (i == size - 1) {
                    maxSerial = child.serial;
                }
                if (child.node.left != null) {
                    queue.offer(new Tuple(child.node.left, child.serial * 2));
                }
                if (child.node.right != null) {
                    queue.offer(new Tuple(child.node.right, child.serial * 2 + 1));
                }
            }
            result = Math.max(result, maxSerial - minSerial + 1);
        }

        return result;
    }

    @Test
    public void testWidthOfBinaryTree() {
        test(this::widthOfBinaryTree);
    }
}
