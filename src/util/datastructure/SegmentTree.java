package util.datastructure;

/**
 * 参见：https://zhuanlan.zhihu.com/p/106118909
 *
 * 类似于堆的结构，支持 O(logN) 时间复杂度的区间查询和区间修改。
 *
 * 线段树是一棵平衡二叉树。母结点代表整个区间的和，越往下区间越小。注意，线段树的每个节点都对应一条线段（区间），
 * 但并不保证所有的线段（区间）都是线段树的节点，这两者应当区分开。
 *
 * 每个节点 p 的左右子节点的编号分别为 p * 2 和 p * 2 + 1。
 */
public class SegmentTree {

    private int[] nums;
    private int[] tree;
    // 标记数据，用于懒修改
    private int[] mark;

    public SegmentTree(int[] nums) {
        this.nums = nums;
        tree = new int[nums.length + 1];
        build(1, 1, nums.length);
    }

    private void build(int root, int left, int right) {
        // 到达叶子节点
        if (left == right) {
            // 用数组中的数据赋值
            tree[root] = nums[root - 1];
        } else {
            int mid = (left + right) >>> 1;
            // 先建立左右子节点
            build(root * 2, left, mid);
            build(root * 2 + 1, mid + 1, right);
            // 该节点的值等于左右子节点之和
            tree[root] = tree[root * 2] + tree[root * 2 + 1];
        }
    }

    public void add(int left, int right, int val) {
        // 保持参数下标和原数组一致，因此这里要加 1
        add(left + 1, right + 1, val, 1, 1, nums.length);
    }

    private void add(int targetLeft, int targetRight, int val, int root, int curLeft, int curRight) {
        // 目标区间和当前区间没有交集
        if (curLeft > targetRight || curRight < targetLeft) {
            return;
        } else if (curLeft >= targetLeft && curRight <= targetRight) {  // 目标区间包含当前区间
            // 更新当前区间的值
            tree[root] += (targetRight - targetLeft + 1) * val;
            // 不是叶子节点，更新标记。叶子节点可以不打标记，因为不会再向下传递了
            if (curLeft != curRight) {
                mark[root] += val;
            }
        } else {  // 当前区间包含目标区间，或当前区间和目标区间相交
            pushDown(root, curRight - curLeft + 1);
            int mid = (curLeft + curRight) >>> 1;
            // 递归地往下寻找
            add(targetLeft, targetRight, val, root * 2, curLeft, mid);
            add(targetLeft, targetRight, val, root * 2 + 1, mid + 1, curRight);
            // 根据子节点更新当前节点的值
            tree[root] = tree[root * 2] + tree[root * 2 + 1];
        }
    }

    public int query(int targetLeft, int targetRight) {
        return query(targetLeft + 1, targetRight + 1, 1, 1, nums.length);
    }

    private int query(int targetLeft, int targetRight, int root, int curLeft, int curRight) {
        // 目标区间和当前区间没有交集
        if (curLeft > targetRight || curRight < targetLeft) {
            return 0;
        } else if (curLeft >= targetLeft && curRight <= targetRight) {  // 目标区间包含当前区间
            return tree[root];
        } else {  // 当前区间包含目标区间，或当前区间和目标区间相交
            pushDown(root, curRight - curLeft + 1);
            int mid = (curLeft + curRight) >>> 1;
            return query(targetLeft, targetRight, root * 2, curLeft, mid)
                    + query(targetLeft, targetRight, root * 2 + 1, mid + 1, curRight);
        }
    }

    /**
     * 将 root 的标记下推给子节点
     */
    private void pushDown(int root, int len) {
        // 标记向下传递
        mark[root * 2] += mark[root];
        mark[root * 2 + 1] += mark[root];
        // 往下更新一层
        tree[root * 2] += mark[root] * (len - len / 2);
        tree[root * 2 + 1] += mark[root] * (len / 2);
        // 清除标记
        mark[root] = 0;
    }
}
