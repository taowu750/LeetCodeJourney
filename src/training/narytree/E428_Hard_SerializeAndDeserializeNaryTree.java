package training.narytree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.narytree.Node.newTree;

/**
 * 428. 序列化和反序列化 N 叉树: https://leetcode-cn.com/problems/serialize-and-deserialize-n-ary-tree/
 *
 * 序列化是指将一个数据结构转化为位序列的过程，因此可以将其存储在文件中或内存缓冲区中，
 * 以便稍后在相同或不同的计算机环境中恢复结构。
 *
 * 设计一个序列化和反序列化 N 叉树的算法。一个 N 叉树是指每个节点都有不超过 N 个孩子节点的有根树。
 * 序列化 / 反序列化算法的算法实现没有限制。你只需要保证 N 叉树可以被序列化为一个字符串并且该字符串可以被反序列化成原树结构即可。
 *
 * 例 1：
 *       1
 *     / | \
 *    3  2  4
 *   / \
 *  5   6
 *
 * 例 2：
 *           1
 *      /  /  \    \
 *     2  3    4    5
 *       / \   |   / \
 *      6  7   8  9  10
 *         |   |  |
 *        11  12 13
 *         |
 *        14
 *
 * 说明：
 * - 树中节点数目的范围是 [0, 10^4].
 * - 0 <= Node.val <= 10^4
 * - N 叉树的高度小于等于 1000
 * - 不要使用类成员 / 全局变量 / 静态变量来存储状态。你的序列化和反序列化算法应是无状态的。
 */
public class E428_Hard_SerializeAndDeserializeNaryTree {

    public static void test(Supplier<ICodec> factory) {
        ICodec codec = factory.get();

        Node tree = newTree(1, null, 3, 2, 4, null, 5, 6);
        assertEquals(tree, codec.deserialize(codec.serialize(tree)));

        tree = newTree(1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14);
        assertEquals(tree, codec.deserialize(codec.serialize(tree)));
    }

    @Test
    public void testCodec() {
        test(Codec::new);
    }
}

interface ICodec {
    // Encodes a tree to a single string.
    String serialize(Node root);

    // Decodes your encoded data to tree.
    Node deserialize(String data);
}

/**
 * 参见 {@link training.binarytree.E297_Hard_SerializeAndDeserializeBinaryTree}。
 * 使用前序遍历序列化 N 叉树，只需要记录好每颗子树的结束标志即可。
 *
 * LeetCode 耗时：2 ms - 97.28%
 *          内存消耗：40.4 MB - 31.97%
 */
class Codec implements ICodec {

    public String serialize(Node root) {
        StringBuilder result = new StringBuilder();
        se(root, result);

        return result.toString();
    }

    private void se(Node root, StringBuilder result) {
        if (root == null) {
            return;
        }

        result.append(root.val).append(',');
        for (Node child : root.children) {
            se(child, result);
        }
        result.append(';');
    }

    private int idx;

    public Node deserialize(String data) {
        idx = 0;
        return de(data);
    }

    private Node de(String data) {
        if (idx >= data.length() || data.charAt(idx) == ';') {
            // 一定要注意，遇到 ; 后也要加 idx！！！
            idx++;
            return null;
        }

        int commaIdx = data.indexOf(',', idx);
        int val = Integer.parseInt(data.substring(idx, commaIdx));
        Node root = new Node(val, new ArrayList<>(4));

        idx = commaIdx + 1;
        Node child;
        while ((child = de(data)) != null) {
            root.children.add(child);
        }

        return root;
    }
}
