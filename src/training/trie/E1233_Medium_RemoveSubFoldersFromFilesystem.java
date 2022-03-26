package training.trie;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 1233. 删除子文件夹: https://leetcode-cn.com/problems/remove-sub-folders-from-the-filesystem/
 *
 * 你是一位系统管理员，手里有一份文件夹列表 folder，你的任务是要删除该列表中的所有「子文件夹」，
 * 并以「任意顺序」返回剩下的文件夹。
 *
 * 如果文件夹 folder[i] 位于另一个文件夹 folder[j] 下，那么 folder[i] 就是 folder[j] 的「子文件夹」。
 *
 * 文件夹的「路径」是由一个或多个按以下格式串联形成的字符串：'/' 后跟一个或者多个小写英文字母。
 * - 例如，"/leetcode" 和 "/leetcode/problems" 都是有效的路径，而空字符串和 "/" 不是。
 *
 * 例 1：
 * 输入：folder = ["/a","/a/b","/c/d","/c/d/e","/c/f"]
 * 输出：["/a","/c/d","/c/f"]
 * 解释："/a/b/" 是 "/a" 的子文件夹，而 "/c/d/e" 是 "/c/d" 的子文件夹。
 *
 * 例 2：
 * 输入：folder = ["/a","/a/b/c","/a/b/d"]
 * 输出：["/a"]
 * 解释：文件夹 "/a/b/c" 和 "/a/b/d/" 都会被删除，因为它们都是 "/a" 的子文件夹。
 *
 * 例 3：
 * 输入: folder = ["/a/b/c","/a/b/ca","/a/b/d"]
 * 输出: ["/a/b/c","/a/b/ca","/a/b/d"]
 *
 * 说明：
 * - 1 <= folder.length <= 4 * 10^4
 * - 2 <= folder[i].length <= 100
 * - folder[i] 只包含小写字母和 '/'
 * - folder[i] 总是以字符 '/' 起始
 * - 每个文件夹名都是「唯一」的
 */
public class E1233_Medium_RemoveSubFoldersFromFilesystem {

    public static void test(Function<String[], List<String>> method) {
        equalsIgnoreOrder(asList("/a","/c/d","/c/f"), method.apply(new String[]{"/a","/a/b","/c/d","/c/d/e","/c/f"}));
        equalsIgnoreOrder(asList("/a"), method.apply(new String[]{"/a","/a/b/c","/a/b/d"}));
        equalsIgnoreOrder(asList("/a/b/c","/a/b/ca","/a/b/d"), method.apply(new String[]{"/a/b/c","/a/b/ca","/a/b/d"}));
    }

    /**
     * LeetCode 耗时：42 ms - 87.32%
     *          内存消耗：52.3 MB - 29.35%
     */
    public List<String> removeSubfolders(String[] folder) {
        Trie root = new Trie();
        for (String f : folder) {
            root.insert(f);
        }

        return root.allPaths();
    }

    public static class Trie {
        private boolean isPath;
        private Map<String, Trie> subFolders;

        public Trie() {
            subFolders = new HashMap<>(4);
        }

        public void insert(String path) {
            Trie cur = this;
            String[] folders = path.split("/");
            for (int i = 1; i < folders.length; i++) {
                if (cur.subFolders.containsKey(folders[i])) {
                    if (cur.isPath) {
                        return;
                    }
                    cur = cur.subFolders.get(folders[i]);
                } else {
                    if (cur.isPath) {
                        return;
                    }
                    Trie sub = new Trie();
                    cur.subFolders.put(folders[i], sub);
                    cur = sub;
                }
            }
            cur.isPath = true;
            cur.subFolders.clear();
        }

        public List<String> allPaths() {
            StringBuilder sb = new StringBuilder();
            List<String> result = new ArrayList<>();
            dfs(result, sb, this);

            return result;
        }

        private void dfs(List<String> result, StringBuilder sb, Trie cur) {
            if (cur.isPath) {
                result.add(sb.toString());
                return;
            }
            for (Map.Entry<String, Trie> en : cur.subFolders.entrySet()) {
                int len = sb.length();
                sb.append('/').append(en.getKey());
                dfs(result, sb, en.getValue());
                sb.setLength(len);
            }
        }
    }

    @Test
    public void testRemoveSubfolders() {
        test(this::removeSubfolders);
    }
}
