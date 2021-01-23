package learn.linkedlist;

import util.datastructure.SingleListNode;

/**
 * 单链表结点，和 LeetCode 的单链表名称保持一致
 */
public class ListNode extends SingleListNode<ListNode> {

    public ListNode() {}

    public ListNode(int val) {
        super(val);
    }

    public ListNode(int val, ListNode next) {
        super(val, next);
    }

    public static ListNode newList(int pos, int... vals) {
        return newList(ListNode.class, pos, vals);
    }

    public static ListNode intersectList(ListNode listA, ListNode intersect,
                                         int... listBElements) {
        return intersectList(ListNode.class, listA, intersect, listBElements);
    }
}
