package learn.linkedlist;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 设计一个链表，可以选择实现单链表或双链表。
 *
 * 例 1：
 * Input:
 * ["MyLinkedList", "addAtHead", "addAtTail", "addAtIndex", "get", "deleteAtIndex", "get"]
 * [[], [1], [3], [1, 2], [1], [1], [1]]
 * Output:
 * [null, null, null, null, 2, null, 3]
 * Explanation:
 * MyLinkedList myLinkedList = new MyLinkedList();
 * myLinkedList.addAtHead(1);
 * myLinkedList.addAtTail(3);
 * myLinkedList.addAtIndex(1, 2);    // linked list becomes 1->2->3
 * myLinkedList.get(1);              // return 2
 * myLinkedList.deleteAtIndex(1);    // now the linked list is 1->3
 * myLinkedList.get(1);              // return 3
 */
class MyLinkedList {

    public static void main(String[] args) {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.addAtHead(1);
        myLinkedList.addAtTail(3);
        myLinkedList.addAtIndex(1, 2);
        assertEquals(myLinkedList.get(1), 2);
        myLinkedList.deleteAtIndex(0);
        assertEquals(myLinkedList.get(0), 2);
    }

    private static class Node {
        int elem;
        Node prev;
        Node next;

        public Node() {
        }

        public Node(int elem) {
            this.elem = elem;
        }

        public Node(Node prev, Node next) {
            this.prev = prev;
            this.next = next;
        }

        public Node(int elem, Node prev, Node next) {
            this.elem = elem;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    void printList() {
        if (size == 0)
            return;

        Node p = head.next;
        System.out.print(p.elem);
        for (int i = 1; i < size; i++) {
            System.out.print("->");
            p = p.next;
            System.out.print(p.elem);
        }
        System.out.println();
    }

    /** 在此初始化您的数据结构。 */
    public MyLinkedList() {
        head = new Node();
        tail = new Node();
        head.prev = head.next = tail;
        tail.prev = tail.next = head;
    }

    /**
     * 获取链表中第index个节点的值。如果索引无效，则返回-1。
     */
    public int get(int index) {
        if (index < 0 || index >= size)
            return -1;

        return getNodeAt(index).elem;
    }

    /**
     * 在链接列表的第一个元素之前添加一个值为val的节点。插入后，新节点将成为链表的第一个节点。
     */
    public void addAtHead(int val) {
        Node next = head.next;
        Node newNode = new Node(val, head, next);
        head.next = newNode;
        next.prev = newNode;
        size++;
    }

    /**
     * 将值val的节点追加到链接列表的最后一个元素。
     */
    public void addAtTail(int val) {
        Node prev = tail.prev;
        Node newNode = new Node(val, tail.prev, tail);
        tail.prev = newNode;
        prev.next = newNode;
        size++;
    }

    /**
     * 在链接列表的第index个节点之前添加一个值为val的节点。如果index等于链表的长度，
     * 则该节点将附加到链表的末尾。如果index大于长度，则不会插入该节点。
     */
    public void addAtIndex(int index, int val) {
        if (index < 0 || index > size)
            return;
        if (index == size) {
            addAtTail(val);
        } else {
            Node p = getNodeAt(index);
            Node prev = p.prev;
            Node newNode = new Node(val, p.prev, p);
            p.prev = newNode;
            prev.next = newNode;
            size++;
        }
    }

    /**
     * 如果索引有效，请删除链接列表中的第index个节点。
     */
    public void deleteAtIndex(int index) {
        if (index < 0 || index >= size)
            return;

        Node p = getNodeAt(index);
        p.prev.next = p.next;
        p.next.prev = p.prev;
        size--;
    }


    private Node getNodeAt(int index) {
        Node p;
        if (index < size / 2) {
            p = head.next;
            for (int i = 0; i < index; i++) {
                p = p.next;
            }
        } else {
            p = tail.prev;
            int steps = size - index - 1;
            for (int i = 0; i < steps; i++) {
                p = p.prev;
            }
        }

        return p;
    }
}
