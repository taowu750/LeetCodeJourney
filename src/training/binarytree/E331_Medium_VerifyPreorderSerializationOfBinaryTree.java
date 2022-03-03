package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 331. 验证二叉树的前序序列化: https://leetcode-cn.com/problems/verify-preorder-serialization-of-a-binary-tree/
 *
 * 序列化二叉树的一种方法是使用前序遍历。当我们遇到一个非空节点时，我们可以记录下这个节点的值。如果它是一个空节点，
 * 我们可以使用一个标记值记录，例如 #。
 *
 *      _9_
 *     /   \
 *    3     2
 *   / \   / \
 *  4   1  #  6
 * / \ / \   / \
 * # # # #   # #
 *
 * 例如，上面的二叉树可以被序列化为字符串 "9,3,4,#,#,1,#,#,2,#,6,#,#"，其中 # 代表一个空节点。
 *
 * 给定一串以逗号分隔的序列，验证它是否是正确的二叉树的前序序列化。编写一个在不重构树的条件下的可行算法。
 * 每个以逗号分隔的字符或为一个整数或为一个表示 null 指针的 '#' 。
 * 你可以认为输入格式总是有效的，例如它永远不会包含两个连续的逗号，比如 "1,,3" 。
 *
 * 例 1：
 * 输入: "9,3,4,#,#,1,#,#,2,#,6,#,#"
 * 输出: true
 *
 * 例 2：
 * 输入: "1,#"
 * 输出: false
 *
 * 例 3：
 * 输入: "9,#,#,1"
 * 输出: false
 */
public class E331_Medium_VerifyPreorderSerializationOfBinaryTree {

    public static void test(Predicate<String> method) {
        assertTrue(method.test("9,3,4,#,#,1,#,#,2,#,6,#,#"));
        assertFalse(method.test("1,#"));
        assertFalse(method.test("9,#,#,1"));
        assertTrue(method.test("#"));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：39.5 MB - 35.21%
     */
    public boolean isValidSerialization(String preorder) {
        i = 0;
        // 检测包含一颗合法二叉树，并且它等同于序列 preorder
        return check(preorder) && i == preorder.length();
    }

    private int i;

    private boolean check(String preorder) {
        if (i >= preorder.length()) {
            return false;
        }

        // 保存当前位置
        int cur = i;
        // 让 i 移动到下一个节点的位置
        i = preorder.indexOf(',', i) + 1;
        // 下一个节点不存在，令 i 等于序列长度
        if (i == 0) {
            i = preorder.length();
        }

        // 如果当前节点是空节点，则它不需要子节点，返回 true
        if (preorder.charAt(cur) == '#') {
            return true;
        }
        // 否则当前节点不是空节点，则检查左子树和右子树
        return check(preorder) && check(preorder);
    }

    @Test
    public void testIsValidSerialization() {
        test(this::isValidSerialization);
    }
}
