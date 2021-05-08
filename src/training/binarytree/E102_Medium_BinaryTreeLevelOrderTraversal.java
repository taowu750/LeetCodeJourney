package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定二叉树的根，返回其节点值的层次遍历。
 *
 * 例 1：
 * Input: [3,9,20,null,null,15,7]
 * Output:
 * [
 *   [3],
 *   [9,20],
 *   [15,7]
 * ]
 * Explanation:
 *   3
 *  / \
 * 9  20
 *   /  \
 *  15   7
 *
 */
public class E102_Medium_BinaryTreeLevelOrderTraversal {

    static void test(Function<TreeNode, List<List<Integer>>> method) {
        TreeNode root = newTree(3, 9, 20, null, null, 15, 7);
        List<List<Integer>> result = method.apply(root);
        System.out.println(result);
        assertEquals(result, Arrays.asList(Collections.singletonList(3),
                Arrays.asList(9, 20), Arrays.asList(15, 7)));

        root = newTree(1, 2, 3);
        result = method.apply(root);
        System.out.println(result);
        assertEquals(result, Arrays.asList(Collections.singletonList(1),
                Arrays.asList(2, 3)));

        root = newTree(1, null, 2, 3, 4);
        result = method.apply(root);
        System.out.println(result);
        assertEquals(result, Arrays.asList(Collections.singletonList(1),
                Collections.singletonList(2), Arrays.asList(3, 4)));

        root = newTree(1, 2, 3, 4, 5);
        result = method.apply(root);
        System.out.println(result);
        assertEquals(result, Arrays.asList(Collections.singletonList(1),
                Arrays.asList(2, 3), Arrays.asList(4, 5)));
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        // 层与层之间的哨兵结点
        TreeNode end = new TreeNode();
        List<List<Integer>> allLevels = new ArrayList<>();
        List<Integer> levels = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(end);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur != end) {
                levels.add(cur.val);
                if (cur.left != null)
                    queue.offer(cur.left);
                if (cur.right != null)
                    queue.offer(cur.right);
                // 如果下个节点是哨兵结点并且还有其他结点（说明还有下一层），
                // 就再添加哨兵结点
                if (queue.peek() == end && queue.size() > 1)
                    queue.offer(end);
            } else {
                allLevels.add(levels);
                levels = new ArrayList<>();
            }
        }

        return allLevels;
    }

    @Test
    public void testLevelOrder() {
        test(this::levelOrder);
    }


    /**
     * 不需要哨兵结点
     */
    public List<List<Integer>> betterMethod(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        List<List<Integer>> allLevels = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int cnt = queue.size();
            List<Integer> levels = new ArrayList<>(cnt);
            for (int i = 0; i < cnt; i++) {
                TreeNode node = queue.poll();
                //noinspection ConstantConditions
                levels.add(node.val);
                if (node.left != null)
                    queue.offer(node.left);
                if (node.right != null)
                    queue.offer(node.right);
            }
            allLevels.add(levels);
        }

        return allLevels;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
