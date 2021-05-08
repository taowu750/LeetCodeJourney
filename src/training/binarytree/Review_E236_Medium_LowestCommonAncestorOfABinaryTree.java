package training.binarytree;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import util.datastructure.BinaryTreeNode;
import util.datastructure.function.TriFunction;

import java.util.*;

import static training.binarytree.TreeNode.newTree;
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
public class Review_E236_Medium_LowestCommonAncestorOfABinaryTree {

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


    public TreeNode postRecursiveMethod(TreeNode root, TreeNode p, TreeNode q) {
        recuseTree(root, p, q);
        return lca;
    }

    private boolean recuseTree(TreeNode curNode, TreeNode p, TreeNode q) {
        if (curNode == null)
            return false;

        // 如果当前结点等于查找的值，则 mid 设为 1
        int mid = curNode.val == p.val || curNode.val == q.val ? 1 : 0;
        // 搜索左子树包不包含查找的值
        int left = recuseTree(curNode.left, p, q) ? 1 : 0;
        // 搜索右子树包不包含查找的值
        int right = recuseTree(curNode.right, p, q) ? 1 : 0;
        int sum = left + mid + right;

        // 如果 sum >= 2，说明两个查找的值都已找到，
        // 则最近公共祖先为 curNode
        if (sum >= 2)
            lca = curNode;

        return sum > 0;
    }

    @Test
    public void testPostRecursiveMethod() {
        test(this::postRecursiveMethod);
    }


    public TreeNode betterRecursiveMethod(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return null;

        if (root.val == p.val || root.val == q.val)
            return root;

        TreeNode left = betterRecursiveMethod(root.left, p, q);
        TreeNode right = betterRecursiveMethod(root.right, p, q);

        if (left != null && right != null)
            return root;
        else if (left != null)
            return left;
        else
            return right;
    }

    @Test
    public void testBetterRecursiveMethod() {
        test(this::betterRecursiveMethod);
    }


    public TreeNode iterateMethod(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        // 使用 parent 记录结点的父结点
        parent.put(root, null);
        queue.add(root);
        // 使用层序遍历找出 p 和 q 结点
        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = queue.remove();
            if (node.left != null) {
                parent.put(node.left, node);
                queue.add(node.left);
            }
            if (node.right != null) {
                parent.put(node.right, node);
                queue.add(node.right);
            }
        }
        Set<TreeNode> ancestor = new HashSet<>();
        // 保存 p 的所有祖先结点
        while (p != null) {
            ancestor.add(p);
            p = parent.get(p);
        }
        // 从 q 结点向上查找最近的公共祖先
        while (!ancestor.contains(q))
            q = parent.get(q);

        return q;
    }

    @Test
    public void testIterateMethod() {
        test(this::iterateMethod);
    }


    /*
    下面是三个静态变量，表示栈中结点的不同状态。
    只有处于 BOTH_DONE 的结点才能被弹出。
     */
    // 当前结点没有第一次被访问
    public static final int BOTH_PENDING = 2;
    // 当前结点左子树已被遍历
    public static final int LEFT_DONE = 1;
    // 当前结点已遍历完成
    public static final int BOTH_DONE = 0;

    public TreeNode withStateIterate(TreeNode root, TreeNode p, TreeNode q) {
        Deque<Pair<TreeNode, Integer>> stack = new LinkedList<>();
        // 是不是已经找到其中一个结点了
        boolean oneNodeFound = false;
        TreeNode lca = null;
        TreeNode childNode;

        stack.push(new Pair<>(root, BOTH_PENDING));
        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> top = stack.peek();
            TreeNode node = top.getKey();
            int state = top.getValue();

            // 如果 state 不等于 BOTH_DONE，这意味着还不能弹出 node。
            if (state != BOTH_DONE) {
                // 如果两个子结点都还没遍历
                if (state == BOTH_PENDING) {
                    // 检查当前结点是不是要查找的结点
                    if (node.val == p.val || node.val == q.val) {
                        // 如果之前已经找到一个节点，则意味着我们已经找到了两个节点。
                        // 则直接返回 lca
                        if (oneNodeFound)
                            return lca;
                        else {
                            // 否则标记已找到一个结点，并将 lca 设为这个结点
                            oneNodeFound = true;
                            lca = node;
                        }
                    }
                    // 两个子结点都未遍历，则先遍历左子节点
                    childNode = node.left;
                } else {
                    // 已遍历左子节点，则再遍历右子结点
                    childNode = node.right;
                }
                stack.pop();
                // 转换栈顶结点状态
                stack.push(new Pair<>(node, state - 1));
                // 子结点存在，将其压入栈中
                if (childNode != null)
                    stack.push(new Pair<>(childNode, BOTH_PENDING));
            } else {
                // 如果当前结点遍历完成，则弹出该结点。
                // 并且查看它是不是等于 lca，是的话，让 lca 在树中向上移动。
                if (lca == stack.pop().getKey() && oneNodeFound)
                    lca = stack.peek().getKey();
            }
        }

        return null;
    }

    @Test
    public void testWithStateIterate() {
        test(this::withStateIterate);
    }
}
