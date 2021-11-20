package training.string;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 71. 简化路径: https://leetcode-cn.com/problems/simplify-path/
 *
 * 给你一个字符串 path ，表示指向某一文件或目录的 Unix 风格「绝对路径（以 '/' 开头）」，请你将其转化为更加简洁的规范路径。
 *
 * 在 Unix 风格的文件系统中，一个点（.）表示当前目录本身；此外，两个点 （..）表示将目录切换到上一级（指向父目录）；
 * 两者都可以是复杂相对路径的组成部分。任意多个连续的斜杠（即，'//'）都被视为单个斜杠 '/' 。
 * 对于此问题，任何其他格式的点（例如，'...'）均被视为文件/目录名称。
 *
 * 请注意，返回的规范路径必须遵循下述格式：
 * - 始终以斜杠 '/' 开头。
 * - 两个目录名之间必须只有一个斜杠 '/' 。
 * - 最后一个目录名（如果存在）不能 以 '/' 结尾。
 * - 此外，路径仅包含从根目录到目标文件或目录的路径上的目录（即，不含 '.' 或 '..'）。
 *
 * 例 1：
 * 输入：path = "/home/"
 * 输出："/home"
 * 解释：注意，最后一个目录名后面没有斜杠。
 *
 * 例 2：
 * 输入：path = "/../"
 * 输出："/"
 * 解释：从根目录向上一级是不可行的，因为根目录是你可以到达的最高级。
 *
 * 例 3：
 * 输入：path = "/home//foo/"
 * 输出："/home/foo"
 * 解释：在规范路径中，多个连续斜杠需要用一个斜杠替换。
 *
 * 例 4：
 * 输入：path = "/a/./b/../../c/"
 * 输出："/c"
 *
 * 说明：
 * - 1 <= path.length <= 3000
 * - path 由英文字母，数字，'.'，'/' 或 '_' 组成。
 * - path 是一个有效的 Unix 风格绝对路径。
 */
public class E71_Medium_SimplifyPath {

    public static void test(UnaryOperator<String> method) {
        assertEquals("/home", method.apply("/home/"));
        assertEquals("/", method.apply("/../"));
        assertEquals("/home/foo", method.apply("/home//foo/"));
        assertEquals("/c", method.apply("/a/./b/../../c/"));
        assertEquals("/", method.apply("/"));
    }

    /**
     * LeetCode 耗时：2 ms - 99.94%
     *          内存消耗：38.4 MB - 61.98%
     */
    public String simplifyPath(String path) {
        List<String> segments = new ArrayList<>();
        segments.add("/");
        for (int i = 0; i < path.length();) {
            // 接下来每次解析一个文件名

            // 首先跳过开头的 /
            while (i < path.length() && path.charAt(i) == '/') {
                i++;
            }
            if (i == path.length()) {
                break;
            }
            // 找到文件名结束下标
            int next = path.indexOf('/', i);
            if (next == -1) {
                next = path.length();
            }
            String segment = path.substring(i, next);
            // 如果文件名是 ..
            if (segment.equals("..")) {
                // 如果上一个目录不是根目录，则移除它
                if (segments.size() > 1) {
                    segments.remove(segments.size() - 1);
                }
            } else if (!segment.equals(".")) {  // 如果文件名不是当前目录，则添加它
                segments.add(segment);
            }
            i = next + 1;
        }

        if (segments.size() == 1) {
            return "/";
        } else {
            StringBuilder result = new StringBuilder();
            for (int i = 1; i < segments.size(); i++) {
                result.append('/').append(segments.get(i));
            }

            return result.toString();
        }
    }

    @Test
    public void testSimplifyPath() {
        test(this::simplifyPath);
    }
}
