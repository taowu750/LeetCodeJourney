package util.datastructure;

/**
 * Union-Find 结构
 */
public class UF {

    private int count;
    private int[] parent;

    public UF(int count) {
        this.count = count;
        parent = new int[count];

        for (int i = 0; i < count; i++) {
            parent[i] = i;
        }
    }

    public void union(int p, int q) {
        int rootP = find(p), rootQ = find(q);
        if (rootP == rootQ) {
            return;
        }

        parent[rootP] = rootQ;
        count--;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public int count() {
        return count;
    }

    private int find(int id) {
        // 路径压缩
        if (parent[id] != id) {
            parent[id] = find(parent[id]);
        }
        return parent[id];
    }
}
