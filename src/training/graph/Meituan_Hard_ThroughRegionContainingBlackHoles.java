package training.graph;

import java.util.*;

/**
 * 美团笔试题：穿过含有黑洞的区域。
 *
 * 小明坐标轴上，并且只能在纵坐标 0-H，横坐标 0-W 的矩形范围内移动，并且不能在边界上移动。
 * 刚开始小明在 (0, 0) 点，最终要去 (W, H) 点。
 *
 * 这个区间范围内分布着 n 个圆形黑洞，半径都是一样的。这些黑洞的坐标符合条件 0 <= x <= W, 0 <= y <= H。
 * 小明不能碰到这些黑洞，不然就完了。
 *
 * 小明可以缩小黑洞的半径，黑洞半径最小为 0。求能使得小明到达终点的最大黑洞半径。
 */
public class Meituan_Hard_ThroughRegionContainingBlackHoles {

    /**
     * 这一题的大致框架是使用二分查找确定最大半径。但要如何确定给定半径下能否到达终点呢？
     * 我们可以使用 BFS 遍历相连的黑洞，查找其中是否有与上边界/左边界有交集的和与下边界/右边界有交集黑洞，
     * 都有的话则路被封死。
     *
     * 参见：https://leetcode-cn.com/circle/discuss/LMqqdk/view/x9fEtR/
     */
    public int maxRadius(int W, int H, int[] X, int[] Y) {
        int n = X.length;
        // 预先计算每个黑洞之间的距离
        double[][] dists = new double[n][n - 1];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = Math.sqrt((X[i] - X[j]) * (X[i] - X[j]) + (Y[i] - Y[j]) * (Y[i] - Y[j])) / 2;
                dists[i][j] = dists[j][i] = dist;
            }
        }

        List<Integer> indices = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            indices.add(i);
        }
        int result = 0, lo = 0, hi = Math.min(W, H);
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            // up 表示是否有与上边界/左边界有交集的黑洞，down 表示是否有与下边界/右边界有交集的黑洞
            boolean up = false, down = false;
            LinkedList<Integer> cs = new LinkedList<>(indices);
            while (cs.size() > 0 && (!up || !down)) {
                Queue<Integer> queue = new LinkedList<>();
                queue.add(cs.removeFirst());
                while (!queue.isEmpty() && (!up || !down)) {
                    int i = queue.remove();
                    if (X[i] <= mid || Y[i] >= H - mid) {
                        up = true;
                    }
                    if (X[i] >= W - mid || Y[i] <= mid) {
                        down = true;
                    }
                    Iterator<Integer> iter = cs.iterator();
                    while (iter.hasNext()) {
                        int j = iter.next();
                        if (dists[i][j] <= mid) {
                            queue.add(j);
                            iter.remove();
                        }
                    }
                }
            }
            // up、down 没有同时满足，说明有通路
            if (!up || !down) {
                result = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return result;
    }
}
