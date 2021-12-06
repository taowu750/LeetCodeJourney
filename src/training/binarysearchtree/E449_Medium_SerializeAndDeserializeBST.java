package training.binarysearchtree;

import org.junit.jupiter.api.Test;
import training.binarytree.TreeNode;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static training.binarytree.TreeNode.newTree;

/**
 * 449. 序列化和反序列化二叉搜索树: https://leetcode-cn.com/problems/serialize-and-deserialize-bst/
 *
 * 序列化是将数据结构或对象转换为一系列位的过程，以便它可以存储在文件或内存缓冲区中，或通过网络连接链路传输，
 * 以便稍后在同一个或另一个计算机环境中重建。
 *
 * 设计一个算法来序列化和反序列化二叉搜索树。 对序列化/反序列化算法的工作方式没有限制。
 * 您只需确保二叉搜索树可以序列化为字符串，并且可以将该字符串反序列化为最初的二叉搜索树。
 *
 * 编码的字符串应尽可能紧凑。
 *
 * 例 1：
 * 输入：root = [2,1,3]
 * 输出：[2,1,3]
 *
 * 例 2：
 * 输入：root = []
 * 输出：[]
 *
 * 说明：
 * - 树中节点数范围是 [0, 10^4]
 * - 0 <= Node.val <= 10^4
 * - 题目数据保证输入的树是一棵二叉搜索树。
 */
public class E449_Medium_SerializeAndDeserializeBST {

    public static void test(Supplier<ICodec> factory) {
        ICodec codec = factory.get();
        TreeNode tree = newTree(2,1,3);
        assertTrue(TreeNode.equals(tree, codec.deserialize(codec.serialize(tree))));

        assertNull(codec.deserialize(codec.serialize(null)));
    }

    @Test
    public void testCodecImpl() {
        test(CodecImpl::new);
    }
}

interface ICodec {

    // Encodes a tree to a single string.
    String serialize(TreeNode root);

    // Decodes your encoded data to tree.
    TreeNode deserialize(String data);
}

/**
 * LeetCode 耗时：3 ms - 97.72%
 *          内存消耗：39.2 MB - 76.62%
 */
class CodecImpl implements ICodec {

    private static final StringBuilder NULL = new StringBuilder("n,");

    private int idx;

    @Override
    public String serialize(TreeNode root) {
        return se(root).toString();
    }

    private StringBuilder se(TreeNode root) {
        if (root == null) {
            return NULL;
        }

        StringBuilder left = se(root.left), right = se(root.right);
        String num = Integer.toString(root.val);
        StringBuilder result = new StringBuilder(num.length() + 1 + left.length() + right.length());
        result.append(num).append(',').append(left).append(right);

        return result;
    }

    @Override
    public TreeNode deserialize(String data) {
        idx = 0;
        return de(data);
    }

    private TreeNode de(String data) {
        if (idx >= data.length()) {
            return null;
        }

        String val = data.substring(idx, data.indexOf(',', idx));
        idx += val.length() + 1;
        if (val.equals("n")) {
            return null;
        } else {
            return new TreeNode(Integer.parseInt(val), de(data), de(data));
        }
    }
}
