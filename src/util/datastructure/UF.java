package util.datastructure;

/**
 * Union-Find 结构
 */
public class UF {

    int count;
    int[] parent;
    int[] weight;

    public UF(int count) {
        this.count = count;
        parent = new int[count];
        weight = new int[count];

        for (int i = 0; i < count; i++) {
            parent[i] = i;
            weight[i] = 1;
        }
    }

    public void union(int p, int q) {
        int pr = find(p), qr = find(q);
        if (pr == qr)
            return;

        if (weight[pr] > weight[qr]) {
            parent[qr] = pr;
            weight[pr] += weight[qr];
        } else {
            parent[pr] = qr;
            weight[qr] += weight[pr];
        }
        count--;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public int count() {
        return count;
    }

    /**
     * 找到 id 的根 id。
     */
    private int find(int id) {
        while (id != parent[id]) {
            // 路径压缩
            parent[id] = parent[parent[id]];
            id = parent[id];
        }

        return id;
    }
}
