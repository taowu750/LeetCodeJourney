package learn.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一棵树的中序和后序遍历，构造二叉树。树中不存在重复值。
 *
 * 例 1：
 * Input:
 * inorder = [9,3,15,20,7]
 * postorder = [9,15,7,20,3]
 * Output:
 *    3
 *   / \
 *  9  20
 *    /  \
 *   15  7
 */
public class ConstructBinaryTreeFromInorderAndPostorderTraversal {

    static void test(BiFunction<int[], int[], TreeNode> method) {
        int[] inorder = {9, 3, 15, 20, 7}, postorder = {9, 15, 7, 20, 3};
        TreeNode root = method.apply(inorder, postorder);
        assertTrue(TreeNode.equals(root, newTree(3, 9, 20, null, null, 15, 7)));

        inorder = new int[]{1, 2};
        postorder = new int[]{2, 1};
        root = method.apply(inorder, postorder);
        assertTrue(TreeNode.equals(root, newTree(1, null, 2)));

        inorder = new int[]{2, 1};
        postorder = new int[]{2, 1};
        root = method.apply(inorder, postorder);
        assertTrue(TreeNode.equals(root, newTree(1, 2)));

        inorder = new int[]{1, 3, 2};
        postorder = new int[]{3, 2, 1};
        root = method.apply(inorder, postorder);
        assertTrue(TreeNode.equals(root, newTree(1, null, 2, 3)));

        /*
             1
            / \
           2   3
          /     \
         4       5
         */
        inorder = new int[]{4, 2, 1, 3, 5};
        postorder = new int[]{4, 2, 5, 3, 1};
        root = method.apply(inorder, postorder);
        assertTrue(TreeNode.equals(root, newTree(1, 2, 3, 4, null, null, 5)));

        /*
                  1
              /      \
             2        3
            / \      / \
           4   5    6   7
          /     \  / \
         8      9 10 11
         */
        inorder = new int[]{8, 4, 2, 5, 9, 1, 10, 6, 11, 3, 7};
        postorder = new int[]{8, 4, 9, 5, 2, 10, 11, 6, 7, 3, 1};
        root = method.apply(inorder, postorder);
        assertTrue(TreeNode.equals(root, newTree(1, 2, 3, 4, 5, 6, 7, 8, null, null, 9,
                10, 11)));
    }

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        if (inorder.length == 0)
            return null;
        else if (inorder.length == 1)
            return new TreeNode(inorder[0]);

        return buildTree(inorder, 0, inorder.length - 1,
                postorder, 0, postorder.length - 1);
    }

    private TreeNode buildTree(int[] inorder, int inLo, int inHi,
                               int[] postorder, int postLo, int postHi) {
        // 取当前子树根节点值
        int rootVal = postorder[postHi];
        TreeNode root = new TreeNode(rootVal);
        if (inLo != inHi) {
            int riForIn = inLo - 1;
            // 在中序遍历中找到根节点值的下标
            while (inorder[++riForIn] != rootVal);

            if (riForIn < inHi) {
                // 如果没有左子树
                if (riForIn == inLo) {
                    // 递归地创建右子树
                    root.right = buildTree(inorder, riForIn + 1, inHi,
                            postorder, postLo, postHi - 1);
                } else {
                    // 中序遍历和后序遍历左右子树的长度相等
                    // 递归地创建左子和右子树
                    int ltLen = riForIn - inLo;
                    root.left = buildTree(inorder, inLo, riForIn - 1,
                            postorder, postLo, postLo + ltLen - 1);
                    int rtLen = inHi - riForIn;
                    root.right = buildTree(inorder, riForIn + 1, inHi,
                            postorder, postHi - rtLen, postHi - 1);
                }
            } else {
                // 如果没有右子树，那就递归地创建左子树
                root.left = buildTree(inorder, inLo, riForIn - 1,
                        postorder, postLo, postHi - 1);
            }
        }

        return root;
    }

    @Test
    public void testBuildTree() {
        test(this::buildTree);
    }
}
