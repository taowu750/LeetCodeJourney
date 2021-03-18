package training.binarytree;

import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 如果两棵树具有相同的结构和相同的节点值，则它们是重复的。
 * 给定二叉树的根，返回所有重复的子树。
 * 对于每种重复的子树，您只需要返回其中任何一个子树的根节点。
 *
 * 例 1：
 * Input: root = [1,2,3,4,null,2,4,null,null,4]
 * Output: [[2,4],[4]]
 * Explanation:
 *         1
 *       /  \
 *    (2)    3
 *   /     /  \
 * (4)   (2)   4
 *       /
 *     (4)
 *
 * 例 2：
 * Input: root = [2,1,1]
 * Output: [[1]]
 * Explanation:
 *      2
 *    /  \
 *   1    1
 *
 * 例 3：
 * Input: root = [2,2,2,3,null,3,null]
 * Output: [[2,3],[3]]
 *         2
 *       /  \
 *     2     2
 *   /      /
 *  3      3
 *
 * 约束：
 * - 树的结点范围为 [1, 10**4]
 * - -200 <= Node.val <= 200
 */
public class E652_Medium_FindDuplicateSubtrees {

    static void test(Function<TreeNode, List<TreeNode>> method) {
        assertEquals(method.apply(newTree(1,2,3,4,null,2,4,null,null,4)),
                asList(newTree(2, 4), new TreeNode(4)));

        assertEquals(method.apply(newTree(2,1,1)),
                singletonList(new TreeNode(1)));

        assertEquals(method.apply(newTree(2,2,2,3,null,3,null)),
                asList(newTree(2, 3), new TreeNode(3)));

        assertEquals(method.apply(newTree(1,4,3,2,null,2,4,null,null,4)),
                singletonList(new TreeNode(4)));

        assertEquals(method.apply(newTree(0,0,0,0,null,null,0,null,null,null,0)),
                singletonList(new TreeNode(0)));

        assertEquals(method.apply(newTree(0,0,0,0,null,null,0,0,0,0,0)),
                asList(newTree(0, 0, 0), new TreeNode(0)));
    }

    /**
     * LeetCode 耗时：19ms - 72.78%
     *          内存消耗：43.7MB - 78.10%
     */
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        HashMap<String, Integer> memory = new HashMap<>();
        LinkedList<TreeNode> duplicates = new LinkedList<>();
        recur(root, memory, duplicates);

        return duplicates;
    }

    private String recur(TreeNode root, HashMap<String, Integer> memory, LinkedList<TreeNode> duplicates) {
        if (root == null)
            return "#";
        String leftSerial = recur(root.left, memory, duplicates);
        String rightSerial = recur(root.right, memory, duplicates);
        // 使用序列化记住结构
        // 将 root.val 放在前面使得时间消耗减少了 2ms
        String serial = root.val + "," + leftSerial + "," + rightSerial;
        if (memory.getOrDefault(serial, 0) == 1)
            duplicates.addFirst(root);
        memory.merge(serial, 1, Integer::sum);

        return serial;
    }

    @Test
    public void testFindDuplicateSubtrees() {
        test(this::findDuplicateSubtrees);
    }


    /**
     * 使用 StringBuilder。
     *
     * LeetCode 耗时：11ms - 95.83%
     *          内存消耗：49.7MB - 31.35%
     */
    public List<TreeNode> betterMethod(TreeNode root) {
        HashMap<String, Integer> memory = new HashMap<>();
        LinkedList<TreeNode> duplicates = new LinkedList<>();
        betterMethodRecur(root, memory, duplicates);

        return duplicates;
    }

    private String betterMethodRecur(TreeNode root, HashMap<String, Integer> memory, LinkedList<TreeNode> duplicates) {
        if (root == null)
            return "#";
        // 在递归或循环中使用 StringBuilder 效果更好
        StringBuilder sb = new StringBuilder();
        sb.append(root.val)
                .append(',')
                .append(betterMethodRecur(root.left, memory, duplicates))
                .append(',')
                .append(betterMethodRecur(root.right, memory, duplicates));
        String res = sb.toString();
        if (memory.getOrDefault(res, 0) == 1)
            duplicates.addFirst(root);
        memory.merge(res, 1, Integer::sum);

        return res;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
