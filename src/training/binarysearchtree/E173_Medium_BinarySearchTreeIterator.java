package training.binarysearchtree;

import training.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 实现 BSTIterator 类，该类表示按中序顺序遍历二叉搜索树（BST）的迭代器。
 *
 * 例 1：
 * Input:
 * ["BSTIterator", "next", "next", "hasNext", "next", "hasNext", "next", "hasNext", "next", "hasNext"]
 * [[[7, 3, 15, null, null, 9, 20]], [], [], [], [], [], [], [], [], []]
 * Output:
 * [null, 3, 7, true, 9, true, 15, true, 20, false]
 * Explanation:
 *      7
 *    /  \
 *   3   15
 *     /   \
 *    9    20
 * BSTIterator bSTIterator = new BSTIterator([7, 3, 15, null, null, 9, 20]);
 * bSTIterator.next();    // return 3
 * bSTIterator.next();    // return 7
 * bSTIterator.hasNext(); // return True
 * bSTIterator.next();    // return 9
 * bSTIterator.hasNext(); // return True
 * bSTIterator.next();    // return 15
 * bSTIterator.hasNext(); // return True
 * bSTIterator.next();    // return 20
 * bSTIterator.hasNext(); // return False
 *
 * 约束：
 * - 结点数量范围为 [1, 10**5]
 * - 0 <= Node.val <= 10**6
 * - next() 调用都是合法的
 */
public class E173_Medium_BinarySearchTreeIterator {

    @Test
    public void testBSTIterator() {
        BSTIterator bSTIterator = new BSTIterator(newTree(7, 3, 15, null, null, 9, 20));
        assertEquals(bSTIterator.next(), 3);
        assertEquals(bSTIterator.next(), 7);
        assertTrue(bSTIterator.hasNext());
        assertEquals(bSTIterator.next(), 9);
        assertTrue(bSTIterator.hasNext());
        assertEquals(bSTIterator.next(), 15);
        assertTrue(bSTIterator.hasNext());
        assertEquals(bSTIterator.next(), 20);
        assertFalse(bSTIterator.hasNext());
    }
}

/**
 * 最快的方式是一开始就遍历并保存中序序列。但这种方式的迭代器不是惰性求值的迭代器。
 *
 * LeetCode 耗时：15ms - 78.35%
 */
class BSTIterator {

    private TreeNode root;
    private Deque<TreeNode> stack;

    public BSTIterator(TreeNode root) {
        this.root = root;
        stack = new LinkedList<>();
        while (this.root != null) {
            stack.push(this.root);
            this.root = this.root.left;
        }
    }

    public int next() {
        int result = -1;
        if (!stack.isEmpty()) {
            root = stack.pop();
            result = root.val;
            root = root.right;
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
        }

        return result;
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }
}
