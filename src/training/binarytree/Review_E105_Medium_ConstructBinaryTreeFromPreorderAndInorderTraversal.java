package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一棵树的中序和先序遍历，构造二叉树。树中不存在重复值。
 *
 * 例 1：
 * Input:
 * inorder = [9,3,15,20,7]
 * preorder = [3,9,20,15,7]
 * Output:
 *    3
 *   / \
 *  9  20
 *    /  \
 *   15  7
 */
public class Review_E105_Medium_ConstructBinaryTreeFromPreorderAndInorderTraversal {

    static void test(BiFunction<int[], int[], TreeNode> method) {
        int[] inorder = {9, 3, 15, 20, 7}, preorder = {3, 9, 20, 15, 7};
        TreeNode root = method.apply(preorder, inorder);
        assertTrue(TreeNode.equals(root, newTree(3, 9, 20, null, null, 15, 7)));

        inorder = new int[]{1, 2};
        preorder = new int[]{1, 2};
        root = method.apply(preorder, inorder);
        assertTrue(TreeNode.equals(root, newTree(1, null, 2)));

        inorder = new int[]{2, 1};
        preorder = new int[]{1, 2};
        root = method.apply(preorder, inorder);
        assertTrue(TreeNode.equals(root, newTree(1, 2)));

        inorder = new int[]{1, 3, 2};
        preorder = new int[]{1, 2, 3};
        root = method.apply(preorder, inorder);
        assertTrue(TreeNode.equals(root, newTree(1, null, 2, 3)));

        /*
             1
            / \
           2   3
          /     \
         4       5
         */
        inorder = new int[]{4, 2, 1, 3, 5};
        preorder = new int[]{1, 2, 4, 3, 5};
        root = method.apply(preorder, inorder);
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
        preorder = new int[]{1, 2, 4, 8, 5, 9, 3, 6, 10, 11, 7};
        root = method.apply(preorder, inorder);
        assertTrue(TreeNode.equals(root, newTree(1, 2, 3, 4, 5, 6, 7, 8, null, null, 9,
                10, 11)));
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (inorder.length == 0)
            return null;
        else if (inorder.length == 1)
            return new TreeNode(inorder[0]);

        return buildTree(inorder, 0, inorder.length - 1,
                preorder, 0);
    }

    private TreeNode buildTree(int[] inorder, int inLo, int inHi,
                               int[] preorder, int preLo) {
        // 取当前子树根节点值
        int rootVal = preorder[preLo];
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
                            preorder, preLo + 1);
                } else {
                    // 中序遍历和先序遍历左右子树的长度相等
                    // 递归地创建左子和右子树
                    int ltLen = riForIn - inLo;
                    root.left = buildTree(inorder, inLo, riForIn - 1,
                            preorder, preLo + 1);
                    root.right = buildTree(inorder, riForIn + 1, inHi,
                            preorder, preLo + ltLen + 1);
                }
            } else {
                // 如果没有右子树，那就递归地创建左子树
                root.left = buildTree(inorder, inLo, riForIn - 1,
                        preorder, preLo + 1);
            }
        }

        return root;
    }

    @Test
    public void testBuildTree() {
        test(this::buildTree);
    }


    private int pInorder;
    private int nPreorder;
    private int len;

    public TreeNode betterMethod(int[] preorder, int[] inorder) {
        pInorder = 0;
        nPreorder = 0;
        len = inorder.length;

        return betterMethod(inorder, preorder, null);
    }

    // 参数 end 是子树的右边界
    private TreeNode betterMethod(int[] inorder, int[] preorder, TreeNode end) {
        if (nPreorder >= len)
            return null;

        // 将先序数组中的第一个元素作为根
        TreeNode root = new TreeNode(preorder[nPreorder++]);
        // 如果存在左子树，则构建左子树
        if (inorder[pInorder] != root.val)
            // 左子树的右边界是根节点
            root.left = betterMethod(inorder, preorder, root);
        // 左子树创建完成，pInorder 也指向了根，
        // 将其加 1，开始处理右子树
        pInorder++;
        // 如果存在左子树，则构建左子树。
        if ((end == null) || (inorder[pInorder] != end.val))
            // 右子树的右边界和父亲相同
            root.right = betterMethod(inorder, preorder, end);

        return root;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
