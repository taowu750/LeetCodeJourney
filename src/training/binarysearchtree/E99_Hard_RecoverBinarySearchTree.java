package training.binarysearchtree;

import org.junit.jupiter.api.Test;
import training.binarytree.TreeNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static training.binarytree.TreeNode.newTree;

/**
 * 99. 恢复二叉搜索树: https://leetcode-cn.com/problems/recover-binary-search-tree/
 *
 * 给你二叉搜索树的根节点 root ，该树中的两个节点被错误地交换。请在不改变其结构的情况下，恢复这棵树。
 *
 * 进阶：使用 O(n) 空间复杂度的解法很容易实现。你能想出一个只使用 O(1) 空间的解决方案吗？
 *
 * 例 1：
 * 输入：root = [1,3,null,null,2]
 * 输出：[3,1,null,null,2]
 * 解释：3 不能是 1 左孩子，因为 3 > 1 。交换 1 和 3 使二叉搜索树有效。
 *
 * 例 2：
 * 输入：root = [3,1,4,null,null,2]
 * 输出：[2,1,4,null,null,3]
 * 解释：2 不能在 3 的右子树中，因为 2 < 3 。交换 2 和 3 使二叉搜索树有效。
 *
 * 说明：
 * - 树上节点的数目在范围 [2, 1000] 内
 * - -2^31 <= Node.val <= 2^31 - 1
 */
public class E99_Hard_RecoverBinarySearchTree {

    public static void test(Consumer<TreeNode> method) {
        TreeNode root = newTree(1,3,null,null,2);
        method.accept(root);
        assertTrue(TreeNode.equals(newTree(3,1,null,null,2), root));

        root = newTree(3,1,4,null,null,2);
        method.accept(root);
        assertTrue(TreeNode.equals(newTree(2,1,4,null,null,3), root));
    }

    /**
     * 暴力法。
     *
     * LeetCode 耗时：2 ms - 93.17%
     *          内存消耗：38.8 MB - 47.06%
     */
    public void recoverTree(TreeNode root) {
        find = false;
        recover(root);
    }

    private boolean find;
    private TreeNode max, min;

    private void recover(TreeNode root) {
        if (root == null || find) {
            return;
        }
        // 找到左子树的最大值和右子树的最小值
        max = min = null;
        findMax(root.left);
        findMin(root.right);
        // 优先判断 max、min 是不是错位的，然后判断 max、root 和 min、root 是不是错位的
        if (max != null && max.val > root.val) {
            find = true;
            int temp = max.val;
            if (min != null && min.val < root.val) {
                max.val = min.val;
                min.val = temp;
            } else {
                max.val = root.val;
                root.val = temp;
            }
        } else if (min != null && min.val < root.val) {
            find = true;
            int temp = min.val;
            min.val = root.val;
            root.val = temp;
        }

        recover(root.left);
        recover(root.right);
    }

    private void findMax(TreeNode root) {
        if (root == null) {
            return;
        }
        if (max == null || max.val < root.val) {
            max = root;
        }
        findMax(root.left);
        findMax(root.right);
    }

    private void findMin(TreeNode root) {
        if (root == null) {
            return;
        }
        if (min == null || min.val > root.val) {
            min = root;
        }
        findMin(root.left);
        findMin(root.right);
    }

    @Test
    public void testRecoverTree() {
        test(this::recoverTree);
    }


    /**
     * 中序遍历解法，参见：
     * https://leetcode-cn.com/problems/recover-binary-search-tree/solution/hui-fu-er-cha-sou-suo-shu-by-leetcode-solution/
     *
     * 对于二叉搜索树，我们知道如果对其进行中序遍历，得到的值序列是递增有序的，而如果我们错误地交换了两个节点，
     * 等价于在这个值序列中交换了两个值，破坏了值序列的递增性。
     *
     * 我们来看下如果在一个递增的序列中交换两个值会造成什么影响。假设有一个递增序列 a=[1,2,3,4,5,6,7]。
     * 如果我们交换两个不相邻的数字，例如 2 和 6，原序列变成了 a=[1,6,3,4,5,2,7]，那么显然序列中有两个位置不满足
     * a_i < a_(i+1)，因此只要我们找到这两个位置，即可找到被错误交换的两个节点。
     *
     * 如果我们交换两个相邻的数字，例如 22 和 33，此时交换后的序列只有一个位置不满足 a_i < a_(i+1) 。
     * 因此整个值序列中不满足条件的位置或者有两个，或者有一个。
     *
     * 至此，解题方法已经呼之欲出了：
     * 1. 找到二叉搜索树中序遍历得到值序列的不满足条件的位置。
     * 2. 如果有两个，我们记为 i 和 j（i < j 且 a_i > a_(i+1)、a_j > a_(j+1)），那么对应被错误交换的节点即为
     *    a_i 和 a_(j+1)。
     * 3. 如果有一个，我们记为 i，那么对应被错误交换的节点即为 a_i 和 a_(i+1)
     *
     * 下面的遍历方法参见 {@link E98_Medium_ValidateBinarySearchTree}。
     *
     * 但是这种方法需要 O(树高) 的空间，不满足进阶要求。
     *
     * LeetCode 耗时：3 ms - 45.04%
     *          内存消耗：38.8 MB - 35.12%
     */
    public void inorderMethod(TreeNode root) {
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode prev = null, i = null, j = null;
        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (prev != null && prev.val > root.val) {
                j = root;
                if (i == null) {
                    i = prev;
                } else {
                    // 全部找到后可以停止
                    break;
                }
            }
            prev = root;
            root = root.right;
        }

        int temp = i.val;
        i.val = j.val;
        j.val = temp;
    }

    @Test
    public void testInorderMethod() {
        test(this::inorderMethod);
    }


    /**
     * 和上面的方法类似，只不过使用 Morris 中序遍历，只需要 O(1) 的空间。参见：
     * https://leetcode-cn.com/problems/recover-binary-search-tree/solution/hui-fu-er-cha-sou-suo-shu-by-leetcode-solution/
     *
     * Morris 遍历算法整体步骤如下（假设当前遍历到的节点为 x）：
     * 1. 如果 x 无左孩子，则访问 x 的右孩子，即 x=x.right。
     * 2. 如果 x 有左孩子，则找到 x 左子树上最右的节点（即左子树中序遍历的最后一个节点，x 在中序遍历中的前驱节点），
     *   我们记为 predecessor。根据 predecessor 的右孩子是否为空，进行如下操作。
     *   - 如果 predecessor 的右孩子为空，则将其右孩子指向 x，然后访问 x 的左孩子，即 x=x.left。
     *   - 如果 predecessor 的右孩子不为空，则此时其右孩子指向 x，说明我们已经遍历完 x 的左子树，
     *     我们将 predecessor 的右孩子置空，然后访问 x 的右孩子，即 x=x.right。
     * 3. 重复上述操作，直至访问完整棵树。
     *
     * 其实整个过程我们就多做一步：将当前节点左子树中最右边的节点指向它，这样在左子树遍历完成后我们通过这个指向走回了 x，
     * 且能再通过这个知晓我们已经遍历完成了左子树，而不用再通过栈来维护，省去了栈的空间复杂度。
     *
     * LeetCode 耗时：2 ms - 93.17%
     *          内存消耗：38.9 MB - 20.53%
     */
    public void morrisInorderMethod(TreeNode root) {
        TreeNode x = null, y = null, prev = null;
        while (root != null) {
            if (root.left != null) {
                // predecessor 节点就是中序序列中 root 的前序节点
                TreeNode predecessor = root.left;
                while (predecessor.right != null && predecessor.right != root) {
                    predecessor = predecessor.right;
                }
                // 第一次遍历左子树，让 predecessor 的右指针指向 root，继续遍历左子树
                if (predecessor.right == null) {
                    predecessor.right = root;
                    root = root.left;
                } else {  // 说明左子树已经访问完了，我们需要断开链接
                    if (prev != null && prev.val > root.val) {
                        if (x == null) {
                            x = prev;
                        }
                        y = root;
                    }
                    prev = root;
                    predecessor.right = null;
                    root = root.right;
                }
            } else {  // 如果没有左孩子，则直接访问右孩子
                if (prev != null && prev.val > root.val) {
                    if (x == null) {
                        x = prev;
                    }
                    y = root;
                }
                prev = root;
                root = root.right;
            }
        }

        int tmp = x.val;
        x.val = y.val;
        y.val = tmp;
    }

    @Test
    public void testMorrisInorderMethod() {
        test(this::morrisInorderMethod);
    }
}
