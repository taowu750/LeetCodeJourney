package learn.binarytree;

import org.junit.jupiter.api.Test;
import util.datastructure.BinaryTreeNode;
import util.datastructure.function.TriFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定二叉树，找到树中两个给定节点的最近公共祖先（LCA）。
 * 根据 Wikipedia 上 LCA 的定义：“最近共同祖先定义为两个节点 p 和 q 之间，
 * 作为树中中同时具有 p 和 q 作为后代的最近节点（在这里我们允许节点成为其自身的后代）。 ”
 *
 * 例 1：
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * Output: 3
 * Explanation:
 *        3
 *      /   \
 *     5     1
 *    / \   / \
 *   6  2  0  8
 *     / \
 *    7  4
 *
 * 例 2：
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
 * Output: 5
 * Explanation:
 *        3
 *      /   \
 *     5     1
 *    / \   / \
 *   6  2  0  8
 *     / \
 *    7  4
 *
 * 例 3：
 * Input: root = [1,2], p = 1, q = 2
 * Output: 1
 *
 * 约束：
 * - 树中结点数量范围为 [2, 10**5]
 * - -10**9 <= Node.val <= 10**9
 * - 结点值没有重复
 * - p != q
 * - p 和 q 存在于树中
 */
public class LowestCommonAncestorOfABinaryTree {

    static void test(TriFunction<TreeNode, TreeNode, TreeNode, TreeNode> method) {
        TreeNode root = newTree(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
        TreeNode p = BinaryTreeNode.find(root, 5), q = BinaryTreeNode.find(root, 1);
        assertEquals(System.identityHashCode(method.apply(root, p, q)),
                System.identityHashCode(BinaryTreeNode.find(root, 3)));

        p = BinaryTreeNode.find(root, 5);
        q = BinaryTreeNode.find(root, 4);
        assertEquals(System.identityHashCode(method.apply(root, p, q)),
                System.identityHashCode(BinaryTreeNode.find(root, 5)));

        p = BinaryTreeNode.find(root, 6);
        q = BinaryTreeNode.find(root, 4);
        assertEquals(System.identityHashCode(method.apply(root, p, q)),
                System.identityHashCode(BinaryTreeNode.find(root, 5)));

        p = BinaryTreeNode.find(root, 7);
        q = BinaryTreeNode.find(root, 8);
        assertEquals(System.identityHashCode(method.apply(root, p, q)),
                System.identityHashCode(BinaryTreeNode.find(root, 3)));

        root = newTree(1, 2);
        p = BinaryTreeNode.find(root, 1);
        q = BinaryTreeNode.find(root, 2);
        assertEquals(System.identityHashCode(method.apply(root, p, q)),
                System.identityHashCode(BinaryTreeNode.find(root, 1)));
    }

    private Set<Integer> visited;
    private Map<Integer, TreeNode> uf;
    private TreeNode p, q;
    private TreeNode lca;

    /**
     * 使用 Tarjan 算法求最近公共祖先。
     * 算法思想见：https://www.cnblogs.com/JVxie/p/4854719.html
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (visited != null)
            visited.clear();
        else
            visited = new HashSet<>();
        if (uf != null)
            uf.clear();
        else
            uf = new HashMap<>();
        this.p = p;
        this.q = q;
        lca = null;
        tarjan(root);

        return lca;
    }

    private void tarjan(TreeNode root) {
        if (lca != null)
            return;

        // 访问 root 的所有子结点
        if (root.left != null) {
            // 继续向下遍历
            tarjan(root.left);
            // 在并查集中，将子结点合并到 root 上。
            // 这样在并查集中记录叶子结点往上的路径。
            merge(root, root.left);
            // 标记子结点已访问。如果某两个子结点都访问到了，
            // 则它们一直往上的路径必然会相聚在某个最近公共祖先那里。
            visited.add(root.left.val);
        }
        if (root.right != null) {
            tarjan(root.right);
            merge(root, root.right);
            visited.add(root.right.val);
        }
        // 如果 root 是查找结点之一
        if (root.val == p.val) {
            // 看看另一个查找结点(设为 e)有没有被访问，被访问则 p、q 的公共祖先为 find(e)
            if (visited.contains(q.val))
                lca = find(q);
        } else if (root.val == q.val) {
            if (visited.contains(p.val))
                lca = find(p);
        }
    }

    private TreeNode find(TreeNode node) {
        TreeNode u = node, v;
        while (u != null && u != (v = uf.get(u.val)))
            u = v;

        return u;
    }

    private void merge(TreeNode u, TreeNode v) {
        TreeNode uRoot = find(u), vRoot = find(v);
        if (uRoot == null) {
            uf.put(u.val, u);
            uRoot = u;
        }
        if (vRoot == null) {
            uf.put(v.val, u);
            vRoot = u;
        }
        if (uRoot != vRoot) {
            uf.put(vRoot.val, uRoot);
        }
    }

    @Test
    public void testLowestCommonAncestor() {
        test(this::lowestCommonAncestor);
    }
}
