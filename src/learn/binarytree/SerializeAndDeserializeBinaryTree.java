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
     */
    public String serialize(TreeNode root) {
        if (root == null)
            return null;

        StringBuilder sb = new StringBuilder(32);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        // 记录后导 null 的个数
        int trailingNullCnt = 0;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node != null) {
                sb.append(node.val).append(',');
                queue.offer(node.left);
                queue.offer(node.right);
                trailingNullCnt = 0;
            } else {
                sb.append('n').append(',');
                trailingNullCnt++;
            }
        }
        // 删除后导 null 值
        sb.delete(sb.length() - (trailingNullCnt << 1), sb.length());

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
            for (int i = 1; i < len;) {
                TreeNode node = queue.poll();
                if (!numbers[i].equals("n")) {
                    node.left = new TreeNode(Integer.parseInt(numbers[i]));
                    queue.offer(node.left);
                }
                i++;
                if (i >= len)
                    break;
                if (!numbers[i].equals("n")) {
                    node.right = new TreeNode(Integer.parseInt(numbers[i]));
                    queue.offer(node.right);
                }
                i++;
            }
        }

        return root;
    }

    @Test
    public void testSD() {
        test(this::serialize, this::deserialize);
    }


    /**
     * 使用先序遍历递归地进行序列化和反序列化。
     */
    public String preorderSerialize(TreeNode root) {
        StringBuilder sb = new StringBuilder(32);
        preorderSerialize(root, sb);
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private void preorderSerialize(TreeNode root, StringBuilder sb) {
        if (root == null)
            sb.append('n').append(',');
        else {
            sb.append(root.val).append(',');
            preorderSerialize(root.left, sb);
            preorderSerialize(root.right, sb);
        }
    }

    private int idx;

    public TreeNode preorderDeserialize(String data) {
        idx = 0;
        String[] numbers = data.split(",");
        return preorderDeserialize(numbers);
    }

    private TreeNode preorderDeserialize(String[] numbers) {
        if (idx >= numbers.length)
            return null;
        String number = numbers[idx++];
        if (!number.equals("n")) {
            TreeNode node = new TreeNode(Integer.parseInt(number));
            node.left = preorderDeserialize(numbers);
            node.right = preorderDeserialize(numbers);

            return node;
        } else
            return null;
    }

    @Test
    public void testPreorderSD() {
        test(this::preorderSerialize, this::preorderDeserialize);
    }


    /**
     * 改进的版本，避免在反序列化开头切分字符串。
     *
     * LeetCode 时间消耗：3ms
     */
    public String improvedPreorderSerialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        improvedPreorderSerialize(root, sb);

        return sb.toString();
    }

    private void improvedPreorderSerialize(TreeNode root, StringBuilder sb) {
        if (root == null) {
            sb.append(';');
            return;
        }
        sb.append(root.val).append(',');
        improvedPreorderSerialize(root.left, sb);
        improvedPreorderSerialize(root.right, sb);
    }

    public TreeNode improvedPreorderDeserialize(String data) {
        idx = 0;
        return improvedPreorderDe(data);
    }

    public TreeNode improvedPreorderDe(String data) {
        if (idx >= data.length() || data.charAt(idx) == ';') {
            idx++;
            return null;
        }
        int spliteratorIdx = data.indexOf(',', idx);
        TreeNode node = new TreeNode(Integer.parseInt(data.substring(idx, spliteratorIdx)));
        idx = spliteratorIdx + 1;
        node.left = improvedPreorderDe(data);
        node.right = improvedPreorderDe(data);

        return node;
    }

    @Test
    public void testImprovedPreorderSD() {
        test(this::improvedPreorderSerialize, this::improvedPreorderDeserialize);
    }
}
