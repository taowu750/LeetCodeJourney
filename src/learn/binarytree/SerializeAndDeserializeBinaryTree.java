package learn.binarytree;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 序列化是将数据结构或对象转换为位序列的过程，以便可以将其存储在文件或内存缓冲区中，
 * 或者通过网络连接链接进行传输，以便稍后在相同或其他计算机环境中进行重构。
 *
 * 设计一种用于对二叉树进行序列化和反序列化的算法。序列化/反序列化算法的工作方式没有任何限制。
 * 您只需要确保可以将二叉树序列化为字符串，并且可以将该字符串反序列化为原始树结构。
 *
 * 二叉树这一节使用的 Input/Output 格式是 LeetCode 序列化二叉树的方式。您不一定需要遵循这种格式，
 * 因此请发挥创造力并自己提出不同的方法。
 *
 * 例 1：
 * Input: root = [1,2,3,null,null,4,5]
 * Output: [1,2,3,null,null,4,5]
 * Explanation:
 *   1
 *  / \
 * 2  3
 *   / \
 *  4  5
 *
 * 例 2：
 * Input: root = []
 * Output: []
 *
 * 例 3：
 * Input: root = [1]
 * Output: [1]
 *
 * 例 4：
 * Input: root = [1,2]
 * Output: [1,2]
 *
 * 约束：
 * - 结点数范围为 [0, 10**4]
 * - -1000 <= Node.val <= 1000
 */
public class SerializeAndDeserializeBinaryTree {

    static void test(Function<TreeNode, String> serialize,
                     Function<String, TreeNode> deserialize) {
        TreeNode root = newTree(1, 2, 3, null, null, 4, 5);
        assertTrue(TreeNode.equals(deserialize.apply(serialize.apply(root)),
                root));

        root = new TreeNode(1);
        assertEquals(deserialize.apply(serialize.apply(root)).val, root.val);

        assertNull(deserialize.apply(serialize.apply(null)));

        /*
                  1
              /      \
             2        3
            / \      / \
           4   5    6   7
          /     \  / \
         8      9 10 11
         */
        root = newTree(1, 2, 3, 4, 5, 6, 7, 8, null, null, 9, 10, 11);
        assertTrue(TreeNode.equals(deserialize.apply(serialize.apply(root)),
                root));

        /*
                 1
                / \
               2   3
              / \   \
             4   5   6
            /         \
           7           8
         */
        root = newTree(1, 2, 3, 4, 5, null, 6, 7, null, null, null, null, 8);
        assertTrue(TreeNode.equals(deserialize.apply(serialize.apply(root)),
                root));

        /*
                   -1
                  /  \
                 -7    9
                     /   \
                    -1    -7
                      \   /
                       8 -9
         */
        root = newTree(-1, -7, 9, null, null, -1, -7, null, 8, -9);
        assertTrue(TreeNode.equals(deserialize.apply(serialize.apply(root)),
                root));
    }

    /**
     * 此方法使用 LeetCode 的格式进行序列化。
     * 此方法最底层 null 也会被序列化，会造成一定的空间浪费。
     */
    public String serialize(TreeNode root) {
        if (root == null)
            return null;

        StringBuilder sb = new StringBuilder(32);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node != null) {
                sb.append(node.val).append(',');
                queue.offer(node.left);
                queue.offer(node.right);
            } else
                sb.append('n').append(',');
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public TreeNode deserialize(String data) {
        if (data == null)
            return null;

        String[] numbers = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(numbers[0]));
        if (numbers.length > 1) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            int len = numbers.length;
            for (int i = 1, childCnt = 0; i < len;) {
                if (childCnt == 0) {
                    if (!numbers[i].equals("n"))
                        queue.peek().left = new TreeNode(Integer.parseInt(numbers[i]));
                    childCnt++;
                    i++;
                } else if (childCnt == 1) {
                    if (!numbers[i].equals("n"))
                        queue.peek().right = new TreeNode(Integer.parseInt(numbers[i]));
                    childCnt++;
                    i++;
                } else {
                    childCnt = 0;
                    TreeNode lastParent = queue.poll();
                    if (lastParent.left != null)
                        queue.offer(lastParent.left);
                    if (lastParent.right != null)
                        queue.offer(lastParent.right);
                }
            }
        }

        return root;
    }

    @Test
    public void testSD() {
        test(this::serialize, this::deserialize);
    }
}
